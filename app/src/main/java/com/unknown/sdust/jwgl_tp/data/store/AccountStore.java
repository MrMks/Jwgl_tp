package com.unknown.sdust.jwgl_tp.data.store;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.unknown.sdust.jwgl_tp.data.IFileStore;
import com.unknown.sdust.jwgl_tp.data.ISelfCheck;
import com.unknown.sdust.jwgl_tp.utils.EncodeInp;

public class AccountStore implements IFileStore<AccountStore>, ISelfCheck {
    @Expose private String account;
    @Expose private String password;

    @Expose private boolean save_pass;

    @Expose private String name;

    private boolean fromLocal = true;

    public AccountStore(String acc, String pas, boolean rem){
        account = acc;
        password = pas;
        save_pass = rem;
    }

    public String getAccount(){
        return account;
    }

    public boolean isSaved(){
        return save_pass;
    }

    public String getEncoded(){
        return EncodeInp.e(account) + "%%%" + EncodeInp.e(password);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name == null ? "" : name;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setFromLocal(boolean f){
        this.fromLocal = f;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        else if (!(obj instanceof AccountStore)) return false;
        else {
            AccountStore n = (AccountStore) obj;
            return n.account.equals(account) && n.password.equals(password) && n.save_pass == save_pass;
        }
    }

    @Override
    public AccountStore toFile() {
        if (save_pass){
            return this;
        } else {
            AccountStore store = new AccountStore(account,"", false);
            store.name = name;
            return store;
        }
    }

    @Override
    public boolean selfCheck() {
        if (password == null) save_pass = false;
        return fromLocal ? account != null && name != null : account != null && password != null;
    }
}
