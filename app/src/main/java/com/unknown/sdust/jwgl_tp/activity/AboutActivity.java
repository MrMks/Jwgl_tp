package com.unknown.sdust.jwgl_tp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.unknown.sdust.jwgl_tp.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.action_bar_about);
        setContentView(R.layout.activity_about);
        TextView textView = findViewById(R.id.about_this_soft);
        Button gsonInfo = findViewById(R.id.gson_license);
        Button jsoupInfo = findViewById(R.id.jsoup_license);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            gsonInfo.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            jsoupInfo.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }

        textView.setText(R.string.app_name);
        textView.append("\nThis is a small lesson_table for private use");
        textView.append("\nCopyright (C) 2019 MrMks under GPLv3");

        gsonInfo.setText("Gson\nCopyright (C) 2008 Google Inc. under ALv2");
        jsoupInfo.setText("Jsoup\nCopyright (C) 2009-2019 Jonathan Hedley under GPLv3");

        findViewById(R.id.about_visit_github).setOnClickListener((v)->{
            Uri uri = Uri.parse("https://github.com/MrMks/Jwgl_tp");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        gsonInfo.setOnClickListener((v) -> {
            Uri uri = Uri.parse("http://www.apache.org/licenses/LICENSE-2.0");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        jsoupInfo.setOnClickListener((v) -> {
            Uri uri = Uri.parse("https://www.gnu.org/licenses/gpl-3.0.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}
