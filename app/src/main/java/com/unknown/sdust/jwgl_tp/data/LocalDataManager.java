package com.unknown.sdust.jwgl_tp.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unknown.sdust.jwgl_tp.data.store.AccountStore;
import com.unknown.sdust.jwgl_tp.data.store.CalendarStore;
import com.unknown.sdust.jwgl_tp.data.store.TableStore;
import com.unknown.sdust.jwgl_tp.data.store.TokenStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.unknown.sdust.jwgl_tp.Constant.TAG;

class LocalDataManager {

    private static final String FILE_TOKEN = "token.json";
    private static final String FILE_TABLE = "table.json";
    private static final String FILE_ACCOUNT = "account.json";
    private static final String FILE_CALENDAR = "calendar.json";

    private static LocalDataManager instance;
    static LocalDataManager getInstance(File dir){
        if (instance == null) instance = new LocalDataManager(dir);
        return instance;
    }

    private File dir;
    private LocalDataManager(File dir){
        this.dir = dir;
    }


    private TokenStore token;
    TokenStore getToken() throws FileNotFoundException {
        if (token == null) token = loadStore(FILE_TOKEN,TokenStore.class);
        return token;
    }
    void saveToken(TokenStore store) throws IOException {
        if (store != null) token = store;
        saveToLocal(token,FILE_TOKEN);
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void deleteToken(){
        new File(dir,FILE_TOKEN).delete();
    }


    private TableStore table;
    TableStore getTable() throws FileNotFoundException {
        if (table == null) table = loadStore(FILE_TABLE,TableStore.class);
        return table;
    }
    void saveTable(TableStore store) throws IOException {
        if (store != null) table = store;
        saveToLocal(table,FILE_TABLE);
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void deleteTable(){
        new File(dir,FILE_TABLE).delete();
    }


    private AccountStore account;
    AccountStore getAccount() throws FileNotFoundException {
        if (account == null) account = loadStore(FILE_ACCOUNT,AccountStore.class);
        return account;
    }
    void saveAccount(AccountStore store) throws IOException {
        if (store != null) account = store;
        saveToLocal(account,FILE_ACCOUNT);
    }
    void deleteAccount(){
        //noinspection ResultOfMethodCallIgnored
        new File(dir,FILE_ACCOUNT).delete();
    }


    private CalendarStore calendar;
    CalendarStore getCalendar() throws FileNotFoundException {
        if (calendar == null) calendar = loadStore(FILE_CALENDAR,CalendarStore.class);
        return calendar;
    }
    void saveCalendar(CalendarStore store) throws IOException {
        if (store != null) calendar = store;
        saveToLocal(calendar,FILE_CALENDAR);
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void deleteCalendar(){
        new File(dir,FILE_CALENDAR).delete();
    }


    private void saveToLocal(IFileStore store,String key) throws IOException {
        if (store == null) return;
        Log.d(TAG,"Writing file: " + key);
        File file = new File(dir,key);
        if (!file.exists() || (file.exists() && file.delete())){
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            FileWriter writer = new FileWriter(file);
            gson.toJson(store.toFile(),writer);
            writer.close();
        }
    }

    private synchronized <T extends IFileStore> T loadStore(String name, Class<T> k) throws FileNotFoundException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        File file = new File(dir,name);
        if (file.exists()){
            FileReader reader = new FileReader(file);
            T store = gson.fromJson(reader,k);
            try {
                reader.close();
            } catch (IOException e){
                Log.i(TAG,e.getMessage(),e);
            }
            return store;
        }
        return null;
    }
}
