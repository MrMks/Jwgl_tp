package com.unknown.sdust.jwgl_tp.card;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.unknown.sdust.jwgl_tp.CardContent;
import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.R;
import com.unknown.sdust.jwgl_tp.utils.JsonRes;
import com.unknown.sdust.jwgl_tp.utils.ResultPack;

public class TestBaseInfoCard extends CardContent<String[]> {
    private AlertDialog logout_alertDialog;

    public TestBaseInfoCard(AppCompatActivity activity, IInfo<String[]> info) {
        super(activity, info);
    }

    @Override
    public void run() {
        //super.run();

        CardView card = generate(getContext());
        //card.setId(R.id.testCard);

        ConstraintLayout layout = new ConstraintLayout(getContext());
        layout.setId(R.id.testCardLayout);

        card.addView(layout);

        ResultPack<String[]> pack = getInfo();
        String[] strs = pack.getResultOrDefault(new String[]{"Error","Error"});

        // create the TextView and LayoutParams to show the name
        setNameTextView(layout,strs[1]);
        // to show the account
        setAccountTextView(layout,strs[0]);

        card.setOnClickListener(v -> {
            logout_alertDialog = logout_alertDialog != null ? logout_alertDialog : new AlertDialog.Builder(getContext())
                    .setMessage(R.string.logout_alert_msg)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        try {
                            JsonRes.delete(JsonRes.tableFile);
                            //LoginData.getInstance().logged = false;
                            //JsonRes.write(LoginData.getInstance(),JsonRes.loginFile);
                            getHandler().post(() -> getContext().setContentView(R.layout.activity_main_login_only));
                        } catch (final Exception e) {
                            getHandler().post(() -> Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show());
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton(R.string.no, (dialog, which) -> {})
                    .create();
            logout_alertDialog.show();
        });

        ConstraintLayout base = findViewById(R.id.constraintLayout);
        getHandler().post(()->
                base.addView(card)
        );
    }

    private void setNameTextView(ConstraintLayout layout, String name){
        TextView text = new TextView(getContext());
        text.setText(name);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.startToStart = R.id.testCardLayout;
        params.topToTop = R.id.testCardLayout;
        params.bottomToBottom = R.id.testCardLayout;
        params.setMargins(dp2px(8),dp2px(8),0,dp2px(8));
        layout.addView(text,params);
    }

    private void setAccountTextView(ConstraintLayout layout, String account){
        TextView text = new TextView(getContext());
        text.setText(account);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.endToEnd = R.id.testCardLayout;
        params.topToTop = R.id.testCardLayout;
        params.bottomToBottom = R.id.testCardLayout;
        params.setMargins(dp2px(8),dp2px(8),dp2px(8),dp2px(8));
        layout.addView(text,params);
    }

    @Override
    protected int getCardId() {
        return R.id.testCard;
    }

    @Override
    protected int getTopToBottom() {
        return R.id.cardView_classTable;
    }

    @Override
    protected int getTopToTop() {
        return -1;
    }
}
