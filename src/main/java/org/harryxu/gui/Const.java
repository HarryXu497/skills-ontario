package org.harryxu.gui;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;

public final class Const {
    public static final Color ACCENT_COLOR = new Color(126,81,185);

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;

    public static Border createRaisedBorder(Border padding) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedSoftBevelBorder(),
                padding
        );
    }

    public static Border createLoweredBorder(Border padding) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                padding
        );
    }
}
