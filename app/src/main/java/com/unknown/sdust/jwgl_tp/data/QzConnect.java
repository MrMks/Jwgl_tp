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

    String getAccountName(TokenStore token);
    AccountStore getAccount(TokenStore token);
    TokenStore getToken();
    CalendarStore getCalendar(TokenStore token);
    TableStore getTable(TokenStore token, String option);

    InputStream getNewCodeImg(TokenStore token);
}
