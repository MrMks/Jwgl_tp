package com.unknown.sdust.jwgl_tp.info;

import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.utils.JsonRes;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import java.io.IOException;

public abstract class JsonInfo<K> implements IInfo<K>{
    private K cache;

    public JsonInfo(){}

    @Override
    public ResultPack<K> getInfo() {
        try {
            if(cache == null) {
                cache = JsonRes.read(getChild(),getKlass());
            }
            return new ResultPack<>(cache != null,cache,"");
        } catch (IOException e) {
            //e.printStackTrace();
            return new ResultPack<>(false,null,e.getLocalizedMessage());
        }
    }

    abstract String getChild();
    abstract Class<K> getKlass();
}
