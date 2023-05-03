/*
 * Copyright (C) 2022 Parisi Alessandro - alessandro.parisi406@gmail.com
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

package io.github.palexdev.mfxcomponents.behaviors;

import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcore.behavior.BehaviorBase;
import io.github.palexdev.mfxeffects.ripple.MFXRippleGenerator;
import javafx.geometry.Bounds;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * This is the default behavior used by all {@link MFXButton}s.
 * <p>
 * Defines the actions to:
 * <p> - generate ripples
 * <p> - handle mouse press
 * <p> - handle mouse click
 * <p> - handle key press
 */
public class MFXButtonBehavior extends BehaviorBase<MFXButton> {
    //================================================================================
    // Properties
    //================================================================================
    private MFXRippleGenerator rg;

    //================================================================================
    // Constructors
    //================================================================================
    public MFXButtonBehavior(MFXButton node) {
        super(node);
    }

    //================================================================================
    // Methods
    //================================================================================

    /**
     * Instructs the button's {@link MFXRippleGenerator} to generate a ripple for the given
     * {@link MouseEvent}.
     */
    public void generateRipple(MouseEvent me) {
        if (me.getButton() != MouseButton.PRIMARY) return;
        getRippleGenerator().ifPresent(rg -> rg.generate(me));
    }

    /**
     * Requests focus on mouse pressed.
     */
    public void mousePressed(MouseEvent me) {
        getNode().requestFocus();
    }

    /**
     * Calls {@link MFXButton#fire()} on mouse clicked.
     */
    public void mouseClicked(MouseEvent me) {
        if (me.getButton() != MouseButton.PRIMARY) return;
        getNode().fire();
    }

    /**
     * Handles {@link KeyEvent}s.
     * <p></p>
     * By default, if the pressed key is {@link KeyCode#ENTER}, first triggers the generation of a ripple effect at the
     * center of the button, then triggers {@link MFXButton#fire()}.
     */
    public void keyPressed(KeyEvent ke) {
        MFXButton node = getNode();
        if (ke.getCode() == KeyCode.ENTER) {
            Bounds b = node.getLayoutBounds();
            getRippleGenerator().ifPresent(rg -> rg.generate(b.getCenterX(), b.getCenterY()));
            node.fire();
        }
    }

    /**
     * Attempts at getting the {@link MFXRippleGenerator} from the icon' skin.
     * <p>
     * This is null and exception safe as it uses {@link Stream} and {@link Optional}.
     * Once found, it is also cached to avoid useless computations.
     */
    public Optional<MFXRippleGenerator> getRippleGenerator() {
        if (rg == null) {
            MFXButton btn = getNode();
            Skin<?> skin = btn.getSkin();
            if (skin == null) return Optional.empty();
            Optional<MFXRippleGenerator> opt = btn.getChildrenUnmodifiable().stream()
                    .filter(n -> n instanceof MFXRippleGenerator)
                    .map(n -> ((MFXRippleGenerator) n))
                    .findFirst();
            rg = opt.orElse(null);
            return opt;
        }
        return Optional.of(rg);
    }

    //================================================================================
    // Overridden Methods
    //================================================================================

    /**
     * {@inheritDoc}
     * <p></p>
     * Also clears the cached instance of the {@link MFXRippleGenerator} if found.
     */
    @Override
    public void dispose() {
        rg = null;
        super.dispose();
    }
}
