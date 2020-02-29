package com.unknown.sdust.jwgl_tp.data;

import android.util.Log;

import com.unknown.sdust.jwgl_tp.Constant;
import com.unknown.sdust.jwgl_tp.data.store.AccountStore;
import com.unknown.sdust.jwgl_tp.data.store.CalenderStore;
import com.unknown.sdust.jwgl_tp.data.store.TableStore;
import com.unknown.sdust.jwgl_tp.data.store.TokenStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DataManager {
    private static DataManager instance;
    public static DataManager getInstance(File dir){
        if (instance == null) instance = new DataManager(dir);
        return instance;
    }

    public static DataManager getInstance(){
        if (instance == null) throw new NullPointerException();
        return instance;
    }

    private LocalDataManager local;
    private QzConnect qz;
    private DataManager(File dir){
        local = LocalDataManager.getInstance(dir);
        //TODO qz = new QzConnectImpl();
    }

    /**
     * Token came from net or saved file
     * read in files first, then try from net if token still null
     * if still null, it may be offline? or the url has been changed?
     */
    private TokenStore token;
    public TokenStore getToken(){
        if (token == null){
            try {
                token = local.getToken();
            } catch (FileNotFoundException e){
                Log.i(Constant.TAG,"Token file doesn't exist, getting new token");
            }
        }
        if (token == null){
            token = qz.getToken();
        }
        return token;
    }

    /**
     * account read from local file or from LoginActivity
     * there will always a local file before manual logout. and always save LoginActivity data
     */
    private AccountStore account;
    public AccountStore getAccount() {
        if (account == null){
            try {
                account = local.getAccount();
            } catch (FileNotFoundException e){
                Log.i(Constant.TAG,"Account file doesn't exist, wait for login");
            }
        }
        return account;
    }

    /**
     * read from local file first;
     * if null then from net;
     * this method depends on an accessible token
     */
    private TableStore table;
    public TableStore getTable() {
        return table;
    }

    /**
     * same to table
     */
    private CalenderStore calender;
    public CalenderStore getCalender() {
        return calender;
    }

    public boolean isWebsiteAccessible(){
        return false;
    }

    public boolean login(AccountStore account, String code){
        boolean flag = qz.login(token,account,code);
        if (flag) this.account = account;
        return flag;
    }

    public boolean logout(){
        boolean flag = qz.logout(token);
        if (flag) {
            this.account = null;
            local.deleteAccount();
        }
        return flag;
    }

    public InputStream getNewCodeImg(){
        return qz.getNewCodeImg(token);
    }
}
