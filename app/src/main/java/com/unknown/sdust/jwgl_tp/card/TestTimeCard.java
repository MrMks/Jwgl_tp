package com.unknown.sdust.jwgl_tp.card;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.unknown.sdust.jwgl_tp.CardContent;
import com.unknown.sdust.jwgl_tp.IInfo;
import com.unknown.sdust.jwgl_tp.R;

public class TestTimeCard extends CardContent<String> {
    public TestTimeCard(AppCompatActivity activity, IInfo<String> _info) {
        super(activity, _info);
    }

    @Override
    public void run() {
        CardView card = generate(getContext());
        ConstraintLayout layout = new ConstraintLayout(getContext());
        layout.setId(R.id.testTimeLayout);
        card.addView(layout);

        TextView textView = new TextView(getContext());
        textView.setText(getInfo().getResultOrDefault("yyyy-MM-dd"));
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp2px(8),dp2px(8),dp2px(8),dp2px(8));
        params.startToStart = R.id.testTimeLayout;
        params.topToTop = R.id.testTimeLayout;
        params.bottomToBottom = R.id.testTimeLayout;

        layout.addView(textView,params);

        ConstraintLayout base = findViewById(R.id.constraintLayout);
        getHandler().post(()-> base.addView(card));
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
