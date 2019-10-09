package com.unknown.sdust.jwgl_tp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.unknown.sdust.jwgl_tp.card.BaseInfoCard;
import com.unknown.sdust.jwgl_tp.card.TimeCard;
import com.unknown.sdust.jwgl_tp.card.TimeTableCard_v3_lessonTable;
import com.unknown.sdust.jwgl_tp.data.CookieData;
import com.unknown.sdust.jwgl_tp.data.LoginData;
import com.unknown.sdust.jwgl_tp.data.VersionData;
import com.unknown.sdust.jwgl_tp.data.timeTable.LessonTable;
import com.unknown.sdust.jwgl_tp.info.TimeInfo;
import com.unknown.sdust.jwgl_tp.utils.JsonRes;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

public class MainActivity extends AppCompatActivity {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private static final long login_version = 20191009_00;
    private static final long table_version = 20191009_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        JsonRes.setPath(getFilesDir());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        createMainActivity();
    }

    private void createMainActivity(){
        setTitle(R.string.title_main);
        new Thread(() -> {
            try {
                new Updater().update(Infos.versionInfoRead.getInfo().getResult(),new VersionData(login_version,table_version));

                ResultPack<LoginData> data = Infos.loginDataInfo.getInfo();
                //ResultPack<TimeTable> table = Infos.tableInfoRead.getInfo();
                ResultPack<LessonTable> table = Infos.lessonInfoRead.getInfo();
                //ResultPack<CookieData> cookie = Infos.cookieInfoRead.getInfo();

                if(data.isPresent() && !data.getResult().isEmpty() && table.isPresent()){
                    handler.post(() -> {
                        setContentView(R.layout.activity_main);
                        new Thread(this::loadMainActivity).start();
                    });
                } else {
                    handler.post(() -> {
                        setContentView(R.layout.activity_main_login_only);
                        runLoginActivity(null);
                    });
                }
            } catch (final Exception e) {
                e.printStackTrace();
                handler.post(() -> new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage(e.getLocalizedMessage())
                        .setPositiveButton(R.string.yes, (dialog, which) -> finish())
                        .show());
            }
        }).start();
    }

    public void runLoginActivity(View view){
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
        Intent intent = new Intent(this,LoginActivity.class);
        new Thread(()->{
            intent.putExtra("cookie",cookie.cookie);
            intent.putExtra("account",data.Account);
            intent.putExtra("rem_pass",data.savePassword);
            intent.putExtra("password",data.Password);
            handler.post(()-> startActivityForResult(intent,0));
        }).start();
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
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
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
        //card.run();
        //card.generate(this);
        runCard(card);
    }

    private void loadTimeInfo(){
        runCard(new TimeCard(this,new TimeInfo()));
    }

    private void loadTimeTableInfo(){
        //runCard(new TimeTableCard_v2(this,Infos.tableInfoRead));
        runCard(new TimeTableCard_v3_lessonTable(this,Infos.lessonInfoRead));
    }

    private void runCard(ICard card){
        new Thread(card).start();
    }
}
