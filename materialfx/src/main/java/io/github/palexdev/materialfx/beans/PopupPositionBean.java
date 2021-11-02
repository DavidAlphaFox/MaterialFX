package io.github.palexdev.materialfx.beans;

import io.github.palexdev.materialfx.controls.MFXPopup;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;

/**
 * A useful bean which gives info about a {@link MFXPopup}'s position and owner.
 * <p></p>
 * The purpose of this bean is to provide a way to communicate between the popup and its skin.
 * The precise location of a popup cannot be computed when the show methods are called because the content
 * has not been laid out yet, thus its sizes/bounds are 0. This changes when the skin is created, at that moment
 * all info about the content are available so this bean is necessary to properly reposition and animate the popup.
 */
public class PopupPositionBean {
    //================================================================================
    // Properties
    //================================================================================
    private final Node owner;
    private final Bounds ownerBounds;
    private final PositionBean positionBean;
    private final HPos hPos;
    private final VPos vPos;
    private final double xOffset;
    private final double yOffset;

    //================================================================================
    // Constructors
    //================================================================================
    public PopupPositionBean(Node owner, PositionBean positionBean, HPos hPos, VPos vPos, double xOffset, double yOffset) {
        this.owner = owner;
        this.ownerBounds = owner.getLayoutBounds();
        this.positionBean = positionBean;
        this.hPos = hPos;
        this.vPos = vPos;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * @return the popup's owner
     */
    public Node getOwner() {
        return owner;
    }

    /**
     * @return the popup owner's bounds
     */
    public Bounds getOwnerBounds() {
        return ownerBounds;
    }

    /**
     * @return the popup owner's width
     */
    public double getOwnerWidth() {
        return ownerBounds.getWidth();
    }

    /**
     * @return the popup owner's height
     */
    public double getOwnerHeight() {
        return ownerBounds.getHeight();
    }

    /**
     * You should NOT rely on these coordinates since as of now
     * they do not take into account the translations made by the skin.
     *
     * @return the initial computed coordinates of the popup
     */
    public PositionBean getPositionBean() {
        return positionBean;
    }

    /**
     * Delegate for {@link #getPositionBean()}.getX().
     */
    public double getX() {
        return positionBean.getX();
    }

    /**
     * Delegate for {@link #getPositionBean()}.getY().
     */
    public double getY() {
        return positionBean.getY();
    }

    /**
     * @return the specified {@link HPos}
     */
    public HPos getHPos() {
        return hPos;
    }

    /**
     * @return the specified {@link VPos}
     */
    public VPos getVPos() {
        return vPos;
    }

    /**
     * @return the specified x offset
     */
    public double getXOffset() {
        return xOffset;
    }

    /**
     * @return the specified y offset
     */
    public double getYOffset() {
        return yOffset;
    }
}