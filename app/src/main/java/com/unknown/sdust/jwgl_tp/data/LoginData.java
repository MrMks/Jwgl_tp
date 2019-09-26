package com.unknown.sdust.jwgl_tp.data;

public class LoginData {
    /*
    public static void setInstance(LoginData i){
        instance = i;
    }
     */

    //public String Cookie = "";

    public String Name = "";
    public String Account = "";
    public String Password = "";
    public boolean savePassword = false;

    public boolean isEmpty(){
        return Name == null || Name.isEmpty() || Account == null || Account.isEmpty();
    }
}
