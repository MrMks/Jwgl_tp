package com.unknown.sdust.jwgl_tp.data.store;

import com.google.gson.annotations.Expose;
import com.unknown.sdust.jwgl_tp.data.IFileStore;
import com.unknown.sdust.jwgl_tp.data.ISelfCheck;

public class TokenStore implements IFileStore<TokenStore>, ISelfCheck {

    public static final String NAME = "JSESSIONID";
    @Expose private String token = "";

    public TokenStore(){}
    public TokenStore(String tk){
        this.token = tk;
    }

    public String getToken(){
        return token;
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
