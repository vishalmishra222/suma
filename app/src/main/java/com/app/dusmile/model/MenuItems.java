package com.app.dusmile.model;

import android.graphics.drawable.Drawable;

/**
 * Created by sumasoft on 13/03/17.
 */

public class MenuItems {
    public MenuItems(String menuName, Drawable menuImage) {
        this.menuName = menuName;
        this.menuImage = menuImage;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Drawable getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(Drawable menuImage) {
        this.menuImage = menuImage;
    }

    public String menuName;
    public Drawable menuImage;
}
