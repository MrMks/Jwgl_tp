package com.unknown.sdust.jwgl_tp.data;

import com.unknown.sdust.jwgl_tp.data.store.AccountStore;
import com.unknown.sdust.jwgl_tp.data.store.CalenderStore;
import com.unknown.sdust.jwgl_tp.data.store.TableStore;
import com.unknown.sdust.jwgl_tp.data.store.TokenStore;

import java.io.InputStream;

public interface QzConnect {
    TokenStore getToken();
    boolean testToken(TokenStore token);
    boolean login(TokenStore token, AccountStore account, String code);
    CalenderStore getCalender(TokenStore token);
    TableStore getTable(TokenStore token, String option);
    boolean logout(TokenStore token);

    InputStream getNewCodeImg(TokenStore token);
}
