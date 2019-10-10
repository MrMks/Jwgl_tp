package com.unknown.sdust.jwgl_tp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.info.Infos;
import com.unknown.sdust.jwgl_tp.info.LoginActivityInfo;
import com.unknown.sdust.jwgl_tp.info.VerifyBitmapInfo;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    static Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.title_login);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLoginLayout();
    }

    private TempDataSaver temp = new TempDataSaver();
    private void loadLoginLayout() {
        Intent intent = getIntent();
        temp.xh = intent.getStringExtra("account");
        temp.code = "";
        temp.rem = intent.getBooleanExtra("rem_pass",false);
        temp.ps = temp.rem ? intent.getStringExtra("password") : "";
        temp.bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.randcode_img);

        setLoginLayout(temp);

        findViewById(R.id.imageButton_verifycode).setClickable(false);
        loadVerifyCodeBmp();
        findViewById(R.id.imageButton_verifycode).setClickable(true);
    }

    private void setLoginLayout(TempDataSaver temp){
        ((TextView)findViewById(R.id.textedit_account)).setText(temp.xh);
        ((TextView)findViewById(R.id.textedit_password)).setText(temp.ps);
        ((TextView)findViewById(R.id.textedit_code)).setText(temp.code);
        ((CheckBox)findViewById(R.id.checkbox_rem_pass)).setChecked(temp.rem);
        ((ImageButton)findViewById(R.id.imageButton_verifycode)).setImageBitmap(temp.bitmap);
    }

    public void onClickLogin(View view){
        final Runnable resume = () -> {
            setContentView(R.layout.activity_login);
            setLoginLayout(temp);
        };
        Runnable loading = () -> setContentView(R.layout.layout_loading);
        Runnable runnable = () -> {
            temp.xh = ((TextView)findViewById(R.id.textedit_account)).getText().toString();
            temp.ps = ((TextView)findViewById(R.id.textedit_password)).getText().toString();
            temp.code = ((TextView)findViewById(R.id.textedit_code)).getText().toString();
            temp.rem = ((CheckBox)findViewById(R.id.checkbox_rem_pass)).isChecked();

            mHandler.post(loading);

            try{
                if((temp.xh).isEmpty()) throw new Exception(getResources().getString(R.string.cant_empty_acc));
                if((temp.ps).isEmpty()) throw new Exception(getResources().getString(R.string.cant_empty_pass));
                if((temp.code).isEmpty()) throw new Exception(getResources().getString(R.string.cant_empty_code));

                IInfo info = new LoginActivityInfo(temp.xh, temp.ps, temp.rem, temp.code, Infos.cookieInfoRead);
                ResultPack pack = info.getInfo();

                if(pack.isPresent()) {
                    mHandler.post(toast(R.string.login_success));

                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    mHandler.post(toast(pack.getMsg()));
                    mHandler.post(resume);
                }
            } catch (Exception e){
                mHandler.post(toast(e.getMessage()));
                e.printStackTrace();
                mHandler.post(resume);
            }
        };
        new Thread(runnable).start();
    }

    public void onClickRandCode(View view){
        view.setClickable(false);
        loadVerifyCodeBmp();
        view.setClickable(true);
    }

    private IInfo<InputStream> bitmapInfo;
    private void loadVerifyCodeBmp(){
        if (bitmapInfo == null){
            bitmapInfo = new VerifyBitmapInfo(Infos.cookieInfoRead);
        }
        new Thread(() -> {
            Bitmap map = null;
            InputStream stream = bitmapInfo.getInfo().getResult();
            if(stream != null){
                map = BitmapFactory.decodeStream(stream);
                Matrix matrix = new Matrix();
                matrix.postScale(3,3);
                map = Bitmap.createBitmap(map,0,0,62,22,matrix,true);
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            map = map == null ? BitmapFactory.decodeResource(getResources(),R.drawable.randcode_img) : map;
            temp.bitmap = map;
            mHandler.post(() -> ((ImageButton)findViewById(R.id.imageButton_verifycode)).setImageBitmap(temp.bitmap));
            System.gc();
        }).start();
    }

    public Runnable toast(final CharSequence msg){
        return () -> Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    public Runnable toast(final int resourceId){
        return () -> Toast.makeText(LoginActivity.this,resourceId,Toast.LENGTH_SHORT).show();
    }

    private class TempDataSaver implements Cloneable{
        private String xh,ps,code;
        private boolean rem;
        private Bitmap bitmap;
    }
}
