package com.app.shopchatmyworldra.pojo;

import java.io.Serializable;

/**
 * Created by legacy on 04-Oct-17.
 */

public class OrderListPlace implements Serializable {

    private String orderQuantity="";
    private String orderStatus="";
    private String orderDate="";
    private String paymentType="";
    private String productName="";
    private String productImage1="";
    private String productId="";
    private String productPrice="";
    private String ProductsplPrice="";

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
