package com.unknown.sdust.jwgl_tp.card;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.unknown.sdust.jwgl_tp.CardContent;
import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.data.CookieData;
import com.unknown.sdust.jwgl_tp.info.Infos;
import com.unknown.sdust.jwgl_tp.utils.JsonRes;

public class BaseInfoCard extends CardContent<String[]> {
    private AlertDialog logout_alertDialog;

    public BaseInfoCard(AppCompatActivity activity, IInfo<String[]> info){
        super(activity,info);
    }

    @Override
    public void run() {
        String[] info = getInfo().getResult();
        getHandler().post(()->{
            findViewById(R.id.cardView_baseInfo).setOnClickListener(v -> {
                logout_alertDialog = logout_alertDialog != null ? logout_alertDialog : new AlertDialog.Builder(getContext())
                        .setMessage(R.string.logout_alert_msg)
                        .setPositiveButton(R.string.yes, (dialog, which) -> new Thread(() -> {
                            try {

                                getHandler().post(() -> {
                                    getContext().setContentView(R.layout.activity_main_login_only);
                                    JsonRes.delete(JsonRes.tableFile);
                                    //LoginData.getInstance().logged = false;
                                    Infos.cookieInfoRead.getInfo().getResultOrDefault(new CookieData()).logged = false;
                                    //JsonRes.write(LoginData.getInstance(),JsonRes.loginFile);
                                });
                            } catch (final Exception e) {
                                getHandler().post(() -> Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show());
                                e.printStackTrace();
                            }
                        }).start())
                        .setNegativeButton(R.string.no, (dialog, which) -> {})
                        .create();
                logout_alertDialog.show();
            });
            TextView view = findViewById(R.id.text_usr_name);
            view.setText(R.string.logining);
            if(info != null){
                view.setText(info[1]);
                ((TextView)findViewById(R.id.text_usr_account)).setText(info[0]);
            } else {
                getHandler().post(()-> new AlertDialog.Builder(getContext())
                        .setMessage(R.string.unespected_error)
                        .setPositiveButton(R.string.ok, (dialog, which) -> getContext().finish())
                        .show());
            }
        });
    }

    @Override
    protected int getCardId() {
        return 0;
    }

    @Override
    protected int getTopToBottom() {
        return -1;
    }

    @Override
    protected int getTopToTop() {
        return -1;
    }
}