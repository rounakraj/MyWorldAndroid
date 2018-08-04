package com.app.shopchatmyworldra.pojo;

import java.io.Serializable;

/**
 * Created by legacy on 04-Oct-17.
 */

public class SimilarList implements Serializable{
    private String productId="";
    private String  productName="";
    private String  productImage1="";
    private String  userName="";
    private String  productPrice="";
    private String  ProductsplPrice="";
    private String  Percent="";


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage1() {
        return productImage1;
    }

    public void setProductImage1(String productImage1) {
        this.productImage1 = productImage1;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductsplPrice() {
        return ProductsplPrice;
    }

    public void setProductsplPrice(String productsplPrice) {
        ProductsplPrice = productsplPrice;
    }
}
