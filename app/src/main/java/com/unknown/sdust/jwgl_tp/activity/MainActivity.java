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
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.unknown.sdust.jwgl_tp.ICard;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.card.BaseInfoCard;
import com.unknown.sdust.jwgl_tp.card.TimeCard;
import com.unknown.sdust.jwgl_tp.card.TimeTableCard_v3_lessonTable;
import com.unknown.sdust.jwgl_tp.data.DataManager;
import com.unknown.sdust.jwgl_tp.fragment.LoadingFragment;
import com.unknown.sdust.jwgl_tp.fragment.main.RequireLoginFragment;
import com.unknown.sdust.jwgl_tp.info.Infos;
import com.unknown.sdust.jwgl_tp.info.TimeInfo;

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

    private void createMainActivity(){
        try {
            if(isTokenAccessible()){
                setupMainLayout();
            } else {
                if (isTableAccessible()){
                    setupMainLayout();
                    addRefreshButton();
                } else {
                    setupRequestLoginLayout();
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

    private boolean isTokenAccessible(){
        //return manager.getToken() != null && manager.getToken().isTokenAccessible();
        return false;
    }

    private boolean isTableAccessible(){
        return false;
    }

    private void setupMainLayout(){

    }

    private void addRefreshButton(){

    }

    private void setupRequestLoginLayout(){
        runOnUiThread(()->{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.a_main_content,new RequireLoginFragment());
            transaction.commit();
        });
    }

    public void runLoginActivity(View view){
        /*
        ResultPack<LoginData> dataPack = Infos.loginDataInfo.getInfo();
        ResultPack<CookieData> cookiePack = Infos.cookieInfoRead.getInfo();
        if(!cookiePack.isPresent()){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(cookiePack.getMsg())
                    .setPositiveButton(R.string.yes,(a,b)->{})
                    .show();
            return;
        }
        LoginData data = dataPack.getResultOrDefault(new LoginData());
        CookieData cookie = cookiePack.getResult();
         */
        Intent intent = new Intent(this,LoginActivity.class);
        /*
        intent.putExtra("cookie",cookie.cookie);
        intent.putExtra("account",data.Account);
        intent.putExtra("rem_pass",data.savePassword);
        intent.putExtra("password",data.Password);

         */
        startActivityForResult(intent,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_bar_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setTitle(R.string.title_main);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            setContentView(R.layout.activity_main);
            loadMainActivity();
        }
    }

    private void loadMainActivity(){
        loadBaseInfo();
        loadTimeInfo();
        loadTimeTableInfo();
    }

    private void loadBaseInfo(){
        ICard card = new BaseInfoCard(this,Infos.baseInfoRead);
        runCard(card);
    }

    private void loadTimeInfo(){
        runCard(new TimeCard(this,new TimeInfo()));
    }

    private void loadTimeTableInfo(){
        runCard(new TimeTableCard_v3_lessonTable(this,Infos.lessonInfoRead));
    }

    private void runCard(ICard card){
        new Thread(card).start();
    }
}
