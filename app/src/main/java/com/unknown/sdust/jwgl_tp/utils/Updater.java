package com.unknown.sdust.jwgl_tp.utils;

import android.util.SparseArray;

import com.unknown.sdust.jwgl_tp.AbstractUpdater;
import com.unknown.sdust.jwgl_tp.data.VersionData;
import com.unknown.sdust.jwgl_tp.updater.update_0;

import java.util.LinkedList;

public class Updater {

    private final static LinkedList<Integer> version_list_table = new LinkedList<>();
    private final static LinkedList<Integer> version_list_login = new LinkedList<>();
    private static SparseArray<AbstractUpdater> updaters = new SparseArray<>();
    static {
        //table file version list
        version_list_table.add(0);
        version_list_table.add(191009_00);
        version_list_table.add(191010_00);

        //login file version list
        version_list_login.add(0);
        version_list_login.add(191009_00);

        //updates
        updaters.put(0,new update_0());
    }

    public static void update(VersionData last){
        Updater u = new Updater();
        if (last == null) {
            u.updateLogin(version_list_login.getFirst(),version_list_login);
            u.updateTable(version_list_table.getFirst(),version_list_table);
            JsonRes.write(new VersionData(version_list_login.getLast(),version_list_table.getLast()),JsonRes.versionFile);
            return;
        }

        if (last.login_lastVersion != version_list_login.getLast()){
            u.updateLogin(last.login_lastVersion,version_list_login);
        }

        if (last.table_lastVersion != version_list_table.getLast()){
            u.updateTable(last.table_lastVersion,version_list_table);
        }
    }

    private void updateLogin(int last, LinkedList<Integer> list){
        if (!list.contains(last)) return;
        if (last == list.getLast()) return;
        updaters.get(last,AbstractUpdater.Empty).updateLogin();
        updateLogin(list.get(list.indexOf(last) + 1),list);
    }

    private void updateTable(int last, LinkedList<Integer> list){
        if (!list.contains(last)) return;
        if (last == list.getLast()) return;
        updaters.get(last,AbstractUpdater.Empty).updateTable();
        updateTable(list.get(list.indexOf(last) + 1),list);
    }
}
