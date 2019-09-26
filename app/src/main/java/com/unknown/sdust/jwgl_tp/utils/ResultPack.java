package com.unknown.sdust.jwgl_tp.utils;

public class ResultPack<K> {
    private boolean present;
    private K result;
    private String msg;
    public ResultPack(boolean _present, K _result, String _msg){
        present = _present;
        result = _result;
        msg = _msg;
    }

    public boolean isPresent() {
        return present;
    }

    public K getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public K getResultOrDefault(K def){
        return present ? result : def;
    }
}
