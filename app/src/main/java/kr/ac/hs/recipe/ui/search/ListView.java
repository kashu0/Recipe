package kr.ac.hs.recipe.ui.search;

import android.graphics.Bitmap;

public class ListView {
    private Bitmap Img;
    private String nameStr;
    private String aboutStr ;

    public void setBImg(Bitmap img) { Img = img; }
    public void setName(String name) {
        nameStr = name ;
    }
    public void setAbout(String about) {
        aboutStr = about ;
    }

    public Bitmap getBImg() {
        return this.Img ;
    }
    public String getName() {
        return this.nameStr ;
    }
    public String getAbout() {
        return this.aboutStr ;
    }
}
