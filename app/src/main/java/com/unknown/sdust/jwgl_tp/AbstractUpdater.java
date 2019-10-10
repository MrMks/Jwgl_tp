package com.unknown.sdust.jwgl_tp;

public abstract class AbstractUpdater {
    public abstract void updateLogin();
    public abstract void updateTable();

    public static AbstractUpdater Empty = new AbstractUpdater() {
        @Override
        public void updateLogin() {}
        @Override
        public void updateTable() {}
    };
}
