package com.unknown.sdust.jwgl_tp.data;

import com.unknown.sdust.jwgl_tp.data.store.AccountStore;
import com.unknown.sdust.jwgl_tp.data.store.CalendarStore;
import com.unknown.sdust.jwgl_tp.data.store.TableStore;
import com.unknown.sdust.jwgl_tp.data.store.TokenStore;

import java.io.InputStream;

public interface QzConnect {

    boolean isWebAccessible();

    boolean testToken(TokenStore token);
    boolean login(TokenStore token, AccountStore account, String code);
    boolean logout(TokenStore token);

    TokenStore getToken();
    AccountStore getAccount(TokenStore token);
    String getAccountName(TokenStore token);
    CalendarStore getCalendar(TokenStore token);
    TableStore getTable(TokenStore token);

    InputStream getNewCodeImg(TokenStore token);
}
