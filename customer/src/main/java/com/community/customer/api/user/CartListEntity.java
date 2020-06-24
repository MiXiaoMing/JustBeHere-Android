package com.community.customer.api.user;

import com.community.support.common.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class CartListEntity extends BaseResult<CartListEntity> implements Serializable {
    private ArrayList<Cart> cartList;

    public ArrayList<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }
}
