package com.unknown.sdust.jwgl_tp.data.store;

import com.google.gson.annotations.Expose;
import com.unknown.sdust.jwgl_tp.data.IFileStore;
import com.unknown.sdust.jwgl_tp.data.ISelfCheck;

public class TokenStore implements IFileStore<TokenStore>, ISelfCheck {

    public static final String NAME = "JSESSIONID";
    @Expose private String token = "";
    @Expose private boolean checkOut = false;
    @Expose private boolean checkIn = false;

    public TokenStore(){}
    public TokenStore(String tk){
        this.token = tk;
    }

    public String getToken(){
        return token;
    }

    public void CheckIn(){
        checkIn = true;
    }

    public void CheckOut(){
        checkOut = true;
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public boolean isCheckOut() {
        return checkOut;
    }

    @Override
    public TokenStore toFile() {
        return this;
    }

    @Override
    public boolean selfCheck() {
        return token != null && !token.isEmpty();
    }
}
