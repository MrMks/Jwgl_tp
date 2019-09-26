package com.unknown.sdust.jwgl_tp.info;

import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.data.LoginData;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

public class BaseInfo implements IInfo<String[]> {
    private IInfo<LoginData> info;

    public BaseInfo(IInfo<LoginData> _info){
        info = _info;
    }

    @Override
    public ResultPack<String[]> getInfo() {
        /*
        LoginData data = info.getInfo().getResult();
        if (data == null) return null;
        return new ResultPack<>(true,new String[]{data.Account,data.Name};
         */
        LoginData data = info.getInfo().getResultOrDefault(new LoginData());
        return new ResultPack<>(info.getInfo().isPresent(),new String[]{data.Account,data.Name},info.getInfo().getMsg());
    }
}
