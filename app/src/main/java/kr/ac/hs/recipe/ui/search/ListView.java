package kr.ac.hs.recipe.ui.search;

import android.graphics.drawable.Drawable;

public class ListView {
    private Drawable mainImg ;
    private String nameStr;
    private String aboutStr ;

    public void setImg(Drawable img) {
        mainImg = img ;
    }
    public void setName(String name) {
        nameStr = name ;
    }
    public void setAbout(String about) {
        aboutStr = about ;
    }

    public Drawable getImg() {
        return this.mainImg ;
    }
    public String getName() {
        return this.nameStr ;
    }
    public String getAbout() {
        return this.aboutStr ;
    }
}
