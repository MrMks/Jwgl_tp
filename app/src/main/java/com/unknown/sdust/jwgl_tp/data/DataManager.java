package com.unknown.sdust.jwgl_tp.data;

import android.util.Log;

import com.unknown.sdust.jwgl_tp.data.store.AccountStore;
import com.unknown.sdust.jwgl_tp.data.store.CalendarStore;
import com.unknown.sdust.jwgl_tp.data.store.TableStore;
import com.unknown.sdust.jwgl_tp.data.store.TokenStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.unknown.sdust.jwgl_tp.Constant.TAG;

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
        qz = new QzConnectImpl();
    }

    private boolean tokenValid = false;

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
                Log.i(TAG,"Token file doesn't exist, getting new token");
            }
        }
        if (token == null || !token.selfCheck()){
            token = qz.getToken();
            if (token != null && token.selfCheck()){
                try {
                    local.saveToken(token);
                } catch (IOException e) {
                    Log.w(TAG,e.getMessage(),e);
                }
            }
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
                Log.i(TAG,"Account file doesn't exist, wait for login");
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
    public TableStore getTable(){
        return getTable(null);
    }

    //TODO add menu to support option and add menu to support week;
    @SuppressWarnings("WeakerAccess")
    public TableStore getTable(String option) {
        if (table == null){
            try {
                table = local.getTable();
            } catch (FileNotFoundException e){
                Log.i(TAG,"Table file doesn't exist, getting via internet");
            }
        }
        if (tokenValid && table == null){
            table = qz.getTable(token,null);
            if (table != null && table.selfCheck()){
                try {
                    local.saveTable(table);
                } catch (IOException e){
                    Log.w(TAG,e);
                }
            }
        }
        if (option != null && !option.isEmpty()){
            return qz.getTable(token,option);
        }
        return table;
    }

    /**
     * same to table
     */
    private CalendarStore calendar;
    public CalendarStore getCalendar() {
        if (calendar == null){
            try {
                calendar = local.getCalendar();
            } catch (FileNotFoundException e){
                Log.i(TAG, "Calender file doesn't exist, getting via internet");
            }
        }
        if (calendar == null || !calendar.selfCheck()){
            calendar = qz.getCalendar(token);
            if (calendar != null && calendar.selfCheck()){
                try {
                    local.saveCalendar(calendar);
                } catch (IOException e) {
                    Log.w(TAG,e.getMessage(),e);
                }
            }
        }
        return calendar;
    }

    public boolean isWebsiteAccessible(){
        return qz.isWebAccessible();
    }

    public boolean login(AccountStore account, String code){
        boolean flag = qz.login(token,account,code);
        if (flag) {
            this.account = account;
            account.setName(qz.getAccountName(token));
            try {
                local.saveAccount(account);
            } catch (IOException e) {
                Log.w(TAG,e.getMessage(),e);
            }
        }
        return flag;
    }

    public void logout(){
        boolean flag = qz.logout(token);
        if (flag) {
            this.account = null;
            local.deleteAccount();
            this.calendar = null;
            local.deleteCalendar();
            this.table = null;
            local.deleteTable();
            this.token = null;
            local.deleteToken();
        }
    }

    public InputStream getNewCodeImg(){
        return qz.getNewCodeImg(token);
    }

    public boolean testToken(){
        tokenValid = token != null && qz.testToken(token);
        return tokenValid;
    }

    public void reload() {
        if (account == null) account = qz.getAccount(token); else account.setName(qz.getAccountName(token));
        calendar = qz.getCalendar(token);
        table = qz.getTable(token,null);
        try {
            local.saveAccount(account);
            local.saveCalendar(calendar);
            local.saveTable(table);
        } catch (IOException e){
            Log.w(TAG,e);
        }
    }
}
