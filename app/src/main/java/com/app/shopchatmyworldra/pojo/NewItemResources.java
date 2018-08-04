package com.app.shopchatmyworldra.pojo;

/**
 * Created by MMAD on 29-08-2017.
 */

public class NewItemResources {
    String name="";
    String discounttext="";
    String productid="";
    String username="";
    String price="";
    String Percent="";

    public String getPercent() {
        return Percent;
    }

    public void setPercent(String percent) {
        Percent = percent;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscounttext() {
        return discounttext;
    }

    public void setDiscounttext(String discounttext) {
        this.discounttext = discounttext;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;
}
