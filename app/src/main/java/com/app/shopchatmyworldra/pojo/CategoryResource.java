package com.app.shopchatmyworldra.pojo;

/**
 * Created by MMAD on 10-07-2017.
 */

public class CategoryResource {

    private String catId="";
    private String catName="";
    private String catImage="";

    private String categoryimg;
    private String categoryname;

    public String getCategoryimg() {
        return categoryimg;
    }

    public void setCategoryimg(String categoryimg) {
        this.categoryimg = categoryimg;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }
}
