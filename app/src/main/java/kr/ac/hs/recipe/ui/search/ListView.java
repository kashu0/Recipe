package kr.ac.hs.recipe.ui.search;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ListView {
    private Drawable mainImg ;
    /*private Drawable replaceImg;*/
    private String nameStr;
    private String aboutStr ;

    public void setImg(Drawable img) {
        mainImg = img ;
    }
    /*public void setReplaceImg(Drawable reimg) {
        replaceImg = reimg ;
    }*/
    public void setName(String name) {
        nameStr = name ;
    }
    public void setAbout(String about) {
        aboutStr = about ;
    }

    public Drawable getImg() {
        return this.mainImg ;
    }
    /*public Drawable getReplacingImg() {
        return this.replaceImg ;
    }*/
    public String getName() {
        return this.nameStr ;
    }
    public String getAbout() {
        return this.aboutStr ;
    }
}
