package com.unknown.sdust.jwgl_tp.data.store;

import androidx.annotation.Nullable;

import com.unknown.sdust.jwgl_tp.utils.EncodeInp;

public class AccountStore {
    private String account;
    private String password;

    private boolean save_pass;

    public AccountStore(){}

    public AccountStore(String acc, String pas, boolean rem){
        account = acc;
        password = pas;
        save_pass = rem;
    }

    public String getAccount(){
        return account;
    }

    public String getPassword(){
        return password;
    }

    public boolean isSaved(){
        return save_pass;
    }

    public String getEncoded(){
        return EncodeInp.e(account) + "%%%" + EncodeInp.e(password);
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
}
