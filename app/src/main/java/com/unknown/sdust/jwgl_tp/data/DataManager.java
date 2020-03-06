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
                Log.i(TAG,"Token file doesn't exist");
            }
        }
        if (token != null && token.selfCheck()) return token;
        else return null;
    }

    /**
     * account read from local file or from LoginActivity
     * there will always a local file before manual logout. and always save LoginActivity data
     */
    private AccountStore account;
    public AccountStore getAccount() {
        if (account != null && account.selfCheck()) return account;
        else return null;
    }

    /**
     * read from local file first;
     * if null then from net;
     * this method depends on an accessible token
     */
    private TableStore table;
    public TableStore getTable() {
        if (table != null && table.selfCheck()) return table;
        else return null;
    }

    /**
     * same to table
     */
    private CalendarStore calendar;
    public CalendarStore getCalendar() {
        if (calendar != null && calendar.selfCheck()) return calendar;
        else return null;
    }

    public boolean isWebsiteAccessible(){
        return qz.isWebAccessible();
    }

    public boolean login(AccountStore account, String code){
        boolean flag = qz.login(token,account,code);
        if (flag) {
            this.account = account;
            account.setName(qz.getAccountName(token));
            token.CheckIn();
            try {
                local.saveToken(token);
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
        if (token == null) return false;
        if (!token.isCheckIn()) return false;
        else {
            if (token.isCheckOut()) return false;
            else {
                boolean tokenValid = qz.testToken(token);
                if (!tokenValid) {
                    token.CheckOut();
                    try {local.saveToken(token);} catch (IOException e){Log.w(TAG,e);}
                }
                return tokenValid;
            }
        }
    }

    public boolean isLocalAvailable(){
        return account != null && account.selfCheck()
                && table != null && table.selfCheck()
                && calendar != null && calendar.selfCheck();
    }

    public void loadNew() {
        if (account == null) account = qz.getAccount(token); else account.setName(qz.getAccountName(token));
        calendar = qz.getCalendar(token);
        table = qz.getTable(token);
        try {local.saveAccount(account);} catch (IOException e) {Log.w(TAG,e);}
        try {local.saveCalendar(calendar);} catch (IOException e) {Log.w(TAG,e);}
        try {local.saveTable(table);} catch (IOException e) {Log.w(TAG,e);}
    }

    public void loadLocal(){
        try {
            account = local.getAccount();
            calendar = local.getCalendar();
            table = local.getTable();
        } catch (FileNotFoundException e) {
            Log.w(TAG,e);
        }
    }

    public void getNewToken(){
        if (isWebsiteAccessible()) token = qz.getToken();
        try {
            local.saveToken(token);
        } catch (IOException e) {
            Log.w(TAG,e);
        }
    }

}
