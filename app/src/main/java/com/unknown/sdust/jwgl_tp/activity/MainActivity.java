package com.unknown.sdust.jwgl_tp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.data.DataManager;
import com.unknown.sdust.jwgl_tp.fragment.LoadingFragment;
import com.unknown.sdust.jwgl_tp.fragment.OfflineFragment;
import com.unknown.sdust.jwgl_tp.fragment.main.BaseInfoFragment;
import com.unknown.sdust.jwgl_tp.fragment.main.RefreshFragment;
import com.unknown.sdust.jwgl_tp.fragment.main.RequireLoginFragment;
import com.unknown.sdust.jwgl_tp.fragment.main.TableInfoFragment;
import com.unknown.sdust.jwgl_tp.fragment.main.TimeInfoFragment;

import static com.unknown.sdust.jwgl_tp.Constant.F_TAG_LOADING;
import static com.unknown.sdust.jwgl_tp.Constant.F_TAG_OFFLINE;
import static com.unknown.sdust.jwgl_tp.Constant.F_TAG_REQUIRE_LOGIN;
import static com.unknown.sdust.jwgl_tp.Constant.TAG;

public class MainActivity extends AppCompatActivity {

    private HandlerThread thread = new HandlerThread("Main Work Thread");
    private Handler mHandler;
    private DataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.title_main);

        manager = DataManager.getInstance(getFilesDir());
        thread.start();
        mHandler = new Handler(thread.getLooper());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.a_main_content,new LoadingFragment());
        transaction.commit();

        mHandler.post(this::createMainActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.quitSafely();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void createMainActivity(){
        try {
            if (isWebsiteAccessible()){
                if (isTokenAccessible()){
                    loadNew();
                    setupMainLayout();
                } else {
                    loadLocal();
                    if (isLocalAvailable()){
                        setupMainLayout();
                        addRefreshButton(R.string.main_refresh_cache);
                    } else {
                        getNewToken();
                        goRequestLoginFragment();
                    }
                }
            } else {
                loadLocal();
                if (isLocalAvailable()){
                    setupMainLayout();
                    addRefreshButton(R.string.main_refresh_offline);
                } else {
                    goOfflineFragment();
                }
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage(),e);
            runOnUiThread(() -> new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Error")
                    .setMessage(e.getLocalizedMessage())
                    .setPositiveButton(R.string.yes, (dialog, which) -> finish())
                    .show());
        }
    }

    private boolean isWebsiteAccessible(){
        return manager.isWebsiteAccessible();
    }

    private boolean isTokenAccessible(){
        return manager.getToken() != null && manager.testToken();
    }

    private boolean isLocalAvailable(){
        return manager.isLocalAvailable();
    }

    private void loadNew(){
        manager.loadNew();
    }

    private void loadLocal(){
        manager.loadLocal();
    }

    private void getNewToken(){
        manager.getNewToken();
    }

    private void setupMainLayout(){
        FragmentManager f_manager = getSupportFragmentManager();
        FragmentTransaction transaction = f_manager.beginTransaction();

        Fragment loading = f_manager.findFragmentByTag(F_TAG_LOADING);
        if (loading != null) transaction.remove(loading);
        Fragment require_login = f_manager.findFragmentByTag(F_TAG_REQUIRE_LOGIN);
        if (require_login != null) transaction.remove(require_login);

        transaction.replace(R.id.a_main_content,new BaseInfoFragment(mHandler))
                .add(R.id.a_main_content,new TimeInfoFragment())
                .add(R.id.a_main_content, new TableInfoFragment(mHandler))
                .commit();
    }

    private void addRefreshButton(@StringRes int strId){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.a_main_content,new RefreshFragment(strId,this::reload))
                .commit();
    }

    private void goOfflineFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.a_main_content,new OfflineFragment(),F_TAG_OFFLINE)
                .commit();
    }

    private void goRequestLoginFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.a_main_content,new RequireLoginFragment(),F_TAG_REQUIRE_LOGIN)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_without_login,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_about:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.main_menu_logout:
                mHandler.post(()->DataManager.getInstance().logout());
                mHandler.post(this::goRequestLoginFragment);
                return true;
            case R.id.main_menu_reload:
                reload();
                return true;
            default:
                return false;
        }
    }

    private void reload(){
        FragmentManager f_manager = getSupportFragmentManager();
        f_manager.beginTransaction().replace(R.id.a_main_content,new LoadingFragment()).commit();
        mHandler.post(()->{
            if (isWebsiteAccessible()){
                if (isTokenAccessible()){
                    manager.loadNew();
                    createMainActivity();
                } else {
                    manager.getNewToken();
                    getSupportFragmentManager().beginTransaction().replace(R.id.a_main_content,new RequireLoginFragment(),F_TAG_REQUIRE_LOGIN).commit();
                }
            } else {
                //f_manager.popBackStack();
                createMainActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setTitle(R.string.title_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.a_main_content,new LoadingFragment()).commit();
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            mHandler.post(()->{
                manager.loadNew();
                setupMainLayout();
            });
        }
    }
}
