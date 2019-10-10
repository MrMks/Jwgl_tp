package com.unknown.sdust.jwgl_tp.info;

import com.unknown.sdust.jwgl_tp.data.LoginData;
import com.unknown.sdust.jwgl_tp.utils.JsonRes;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

public class LoginDataInfo extends JsonInfo<LoginData> {

    private ResultPack<LoginData> cache;
    @Override
    public ResultPack<LoginData> getInfo() {
        if (cache != null) return cache;
        ResultPack<LoginData> pack = super.getInfo();
        if (pack.isPresent()) cache = pack;
        else cache = new ResultPack<>(true,new LoginData(),"");

        return cache;
    }

    @Override
    String getChild() {
        return JsonRes.loginFile;
    }

    @Override
    Class<? extends LoginData> getKlass() {
        return LoginData.class;
    }
}
