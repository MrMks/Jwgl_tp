package com.unknown.sdust.jwgl_tp.data;

import com.google.gson.Gson;
import com.unknown.sdust.jwgl_tp.data.store.AccountStore;
import com.unknown.sdust.jwgl_tp.data.store.CalenderStore;
import com.unknown.sdust.jwgl_tp.data.store.TableStore;
import com.unknown.sdust.jwgl_tp.data.store.TokenStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class LocalDataManager {

    private static LocalDataManager instance;
    public static LocalDataManager getInstance(File dir){
        if (instance == null) instance = new LocalDataManager(dir);
        return instance;
    }

    private File dir;
    private LocalDataManager(File dir){
        this.dir = dir;
    }

    private TokenStore token;
    public TokenStore getToken() throws FileNotFoundException {
        if (token == null) loadToken();
        return token;
    }
    public void deleteToken(){

    }

    private void loadToken() throws FileNotFoundException {
        synchronized (this){
            File token_f = new File(dir,"token.json");
            if (token_f.exists()){
                Gson gson = new Gson();
                token = gson.fromJson(new FileReader(token_f),TokenStore.class);
            }
        }
    }

    private TableStore table;
    public TableStore getTable() throws FileNotFoundException {
        if (table == null) loadTable();
        return table;
    }
    public void deleteTable(){}

    private void loadTable() throws FileNotFoundException {
        synchronized (this){
            File table_f = new File(dir,"table.json");
            if (table_f.exists()){
                Gson gson = new Gson();
                table = gson.fromJson(new FileReader(table_f),TableStore.class);
            }
        }
    }

    private AccountStore account;
    public AccountStore getAccount() throws FileNotFoundException {
        if (account == null) loadAccount();
        return account;
    }
    public void deleteAccount(){}

    private void loadAccount() throws FileNotFoundException {
        synchronized (this){
            File account_f = new File(dir,"account.json");
            if (account_f.exists()){
                Gson gson = new Gson();
                account = gson.fromJson(new FileReader(account_f),AccountStore.class);
            }
        }
    }

    private CalenderStore calender;
    public CalenderStore getCalender(){
        if (calender == null) loadCalender();
        return calender;
    }
    public void deleteCalender(){}

    private void loadCalender(){

    }
}
