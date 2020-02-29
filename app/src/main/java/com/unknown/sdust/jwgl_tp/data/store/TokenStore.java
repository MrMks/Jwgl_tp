package com.unknown.sdust.jwgl_tp.data.store;

public class TokenStore implements IFileStore<TokenStore>{

    public static final String NAME = "JSESSIONID";
    private String token = "";

    public String getToken(){
        return token;
    }

    public boolean isTokenAccessible(){
        return !(token == null) && !token.isEmpty() && isTokenInTime();
    }

    /*
    this method will access internet so this will be a time costing method
    please call this method in work thread
     */
    private boolean isTokenInTime(){
        return false;
    }

    @Override
    public void fromFile(TokenStore file) {}

    @Override
    public TokenStore toFile() {
        return this;
    }
}
