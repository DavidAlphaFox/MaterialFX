/*
 * Copyright (C) 2023 Parisi Alessandro - alessandro.parisi406@gmail.com
 * This file is part of MaterialFX (https://github.com/palexdev/MaterialFX)
 *
 * MaterialFX is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * MaterialFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MaterialFX. If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.palexdev.mfxcomponents.skins;

import io.github.palexdev.mfxcomponents.behaviors.MFXFabBehavior;
import io.github.palexdev.mfxcomponents.controls.fab.MFXFabBase;
import io.github.palexdev.mfxcomponents.theming.enums.PseudoClasses;
import io.github.palexdev.mfxcore.base.beans.Position;
import io.github.palexdev.mfxcore.utils.fx.LayoutUtils;
import io.github.palexdev.mfxcore.utils.fx.TextUtils;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.Region;

import static io.github.palexdev.mfxcore.observables.When.onChanged;

/**
 * Default skin implementation for {@link MFXFabBase} components.
 * <p>
 * Extends {@link MFXButtonSkin}.
 * <p></p>
 * This is needed for the animations defined in {@link MFXFabBehavior} to work properly.
 * <p>
 * The min width computation is overridden to return {@link Region#USE_COMPUTED_SIZE}, while the
 * pref width property is overridden to adapt to the {@link MFXFabBase#extendedProperty()}.
 * <p></p>
 * The layout strategy is also overridden so that the label is never truncated, this is also needed for the animations
 * to look as expected. Also, FABs are not supposed to be truncated since they are important UI elements.
 */
public class MFXFabSkin extends MFXButtonSkin {

	//================================================================================
	// Constructors
	//================================================================================
	public MFXFabSkin(MFXFabBase fab) {
		super(fab);
	}

	//================================================================================
	// Overridden Methods
	//================================================================================
	@Override
	protected void addListeners() {
		MFXFabBase fab = getFab();
		onChanged(fab.contentDisplayProperty())
				.then((o, n) -> {
					if (!fab.isExtended()) {
						PseudoClasses.WITH_ICON_LEFT.setOn(fab, false);
						PseudoClasses.WITH_ICON_RIGHT.setOn(fab, false);
						return;
					}
					MFXFontIcon icon = fab.getIcon();
					boolean wil = (icon != null) && (n == ContentDisplay.LEFT);
					boolean wir = (icon != null) && (n == ContentDisplay.RIGHT);
					PseudoClasses.WITH_ICON_LEFT.setOn(fab, wil);
					PseudoClasses.WITH_ICON_RIGHT.setOn(fab, wir);
				})
				.executeNow()
				.invalidating(fab.iconProperty())
				.listen();
	}

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return Region.USE_COMPUTED_SIZE;
	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		MFXFabBase fab = getFab();
		MFXFontIcon icon = fab.getIcon();
		double iW = (icon != null) ? icon.getLayoutBounds().getWidth() : 0.0;
		double w = fab.isExtended() ?
				leftInset + iW + TextUtils.computeTextWidth(fab.getFont(), fab.getText()) + rightInset :
				leftInset + iW + rightInset;
		return Math.max(w, fab.getInitWidth());
	}

	@Override
	protected void layoutChildren(double x, double y, double w, double h) {
		MFXFabBase fab = getFab();
		double lW = Math.max(LayoutUtils.boundWidth(label), w);
		double lH = LayoutUtils.boundHeight(label);
		HPos hPos = fab.isExtended() ? fab.getAlignment().getHpos() : HPos.CENTER;
		VPos vPos = fab.getAlignment().getVpos();
		Position lPos = LayoutUtils.computePosition(
				fab, label,
				x, y, w, h, 0, Insets.EMPTY,
				hPos, vPos
		);
		label.resizeRelocate(lPos.getX(), lPos.getY(), lW, lH);
		rg.resizeRelocate(0, 0, fab.getWidth(), fab.getHeight());
	}

	//================================================================================
	// Getters
	//================================================================================

	/**
	 * @return {@link #getSkinnable()} cast to {@link MFXFabBase}
	 */
	protected MFXFabBase getFab() {
		return (MFXFabBase) getSkinnable();
	}
}