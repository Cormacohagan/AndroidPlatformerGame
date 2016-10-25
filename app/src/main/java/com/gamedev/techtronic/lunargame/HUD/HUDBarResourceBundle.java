package com.gamedev.techtronic.lunargame.HUD;

import android.graphics.Bitmap;

public class HUDBarResourceBundle {
    public Bitmap background;
    public Bitmap firstSegment;
    public Bitmap lastSegment;
    public Bitmap middleSegment;

    public HUDBarResourceBundle(Bitmap background, Bitmap first, Bitmap segment, Bitmap last) {
        this.background = background;
        this.firstSegment = first;
        this.lastSegment = last;
        this.middleSegment = segment;
    }
}
