/*
 *     Copyright (C) 2021 Parisi Alessandro
 *     This file is part of MaterialFX (https://github.com/palexdev/MaterialFX).
 *
 *     MaterialFX is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     MaterialFX is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with MaterialFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.palexdev.materialfx.controls.legacy;

import io.github.palexdev.materialfx.MFXResourcesLoader;
import io.github.palexdev.materialfx.effects.RippleGenerator;
import io.github.palexdev.materialfx.utils.NodeUtils;
import javafx.css.*;
import javafx.geometry.Insets;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.util.List;

public class MFXLegacyTableRow<T> extends TableRow<T> {
    private static final StyleablePropertyFactory<MFXLegacyTableRow<?>> FACTORY = new StyleablePropertyFactory<>(TableRow.getClassCssMetaData());
    private final String STYLE_CLASS = "mfx-legacy-table-row";
    private final String STYLESHEET = MFXResourcesLoader.load("css/legacy/mfx-tablerow.css").toString();
    private final RippleGenerator rippleGenerator;

    public MFXLegacyTableRow() {
        rippleGenerator = new RippleGenerator(this);
        rippleGenerator.setRippleColor(Color.rgb(50, 150, 255));
        rippleGenerator.setInDuration(Duration.millis(400));

        initialize();
    }

    private void initialize() {
        getStyleClass().add(STYLE_CLASS);

        addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            rippleGenerator.setGeneratorCenterX(mouseEvent.getX());
            rippleGenerator.setGeneratorCenterY(mouseEvent.getY());
            rippleGenerator.createRipple();
        });

        addListeners();
    }

    private void addListeners() {
        tableViewProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                rippleGenerator.rippleRadiusProperty().bind(newValue.widthProperty().divide(2.0));
            }
        });

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                NodeUtils.updateBackground(MFXLegacyTableRow.this, getSelectedColor());
            } else {
                NodeUtils.updateBackground(MFXLegacyTableRow.this, Color.WHITE);
            }
        });

        hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (isSelected() || isEmpty()) {
                return;
            }

            if (newValue) {
                if (getIndex() == 0) {
                    setBackground(new Background(new BackgroundFill(getHoverColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    NodeUtils.updateBackground(MFXLegacyTableRow.this, getHoverColor());
                }
            } else {
                NodeUtils.updateBackground(MFXLegacyTableRow.this, Color.WHITE);
            }
        });

        selectedColor.addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && isSelected()) {
                NodeUtils.updateBackground(MFXLegacyTableRow.this, newValue);
            }
        });
    }

    //================================================================================
    // Styleable Properties
    //================================================================================

    /**
     * Specifies the background color of the cell when it is selected.
     */
    private final StyleableObjectProperty<Paint> selectedColor = new SimpleStyleableObjectProperty<>(
            StyleableProperties.SELECTED_COLOR,
            this,
            "selectedColor",
            Color.rgb(180, 180, 255)
    );

    /**
     * Specifies the background color of the cell when the mouse is hover.
     */
    private final StyleableObjectProperty<Paint> hoverColor = new SimpleStyleableObjectProperty<>(
            StyleableProperties.HOVER_COLOR,
            this,
            "hoverColor",
            Color.rgb(50, 150, 255, 0.15)
    );

    public Paint getSelectedColor() {
        return selectedColor.get();
    }

    public StyleableObjectProperty<Paint> selectedColorProperty() {
        return selectedColor;
    }

    public void setSelectedColor(Paint selectedColor) {
        this.selectedColor.set(selectedColor);
    }

    public Paint getHoverColor() {
        return hoverColor.get();
    }

    public StyleableObjectProperty<Paint> hoverColorProperty() {
        return hoverColor;
    }

    public void setHoverColor(Paint hoverColor) {
        this.hoverColor.set(hoverColor);
    }

    //================================================================================
    // CssMetaData
    //================================================================================
    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

        private static final CssMetaData<MFXLegacyTableRow<?>, Paint> SELECTED_COLOR =
                FACTORY.createPaintCssMetaData(
                        "-mfx-selected-color",
                        MFXLegacyTableRow::selectedColorProperty,
                        Color.rgb(180, 180, 255)
                );

        private static final CssMetaData<MFXLegacyTableRow<?>, Paint> HOVER_COLOR =
                FACTORY.createPaintCssMetaData(
                        "-mfx-hover-color",
                        MFXLegacyTableRow::hoverColorProperty,
                        Color.rgb(50, 150, 255, 0.15)
                );

        static {
            cssMetaDataList = List.of(SELECTED_COLOR, HOVER_COLOR);
        }

    }

    public static List<CssMetaData<? extends Styleable, ?>> getControlCssMetaDataList() {
        return StyleableProperties.cssMetaDataList;
    }

    //================================================================================
    // Override Methods
    //================================================================================
    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getControlCssMetaDataList();
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (!getChildren().contains(rippleGenerator)) {
            getChildren().add(0, rippleGenerator);
        }
    }
}
