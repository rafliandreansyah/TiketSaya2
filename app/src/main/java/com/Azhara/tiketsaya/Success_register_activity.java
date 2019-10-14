package com.Azhara.tiketsaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Success_register_activity extends AppCompatActivity {


    Animation tb_scale, bt;
    TextView regis_success_title, regis_success_subtitle;
    View img_suc_reg;
    Button btn_explore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register_activity);

        img_suc_reg = findViewById(R.id.img_success_register);
        regis_success_title = findViewById(R.id.registersuccess_title);
        regis_success_subtitle = findViewById(R.id.registersuccess_subtitle);
        btn_explore = findViewById(R.id.btn_explore);

        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Success_register_activity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tb_scale = AnimationUtils.loadAnimation(this,R.anim.top_bottom_scale);
        bt = AnimationUtils.loadAnimation(this, R.anim.bottom_top);

        img_suc_reg.startAnimation(tb_scale);
        regis_success_title.startAnimation(tb_scale);
        regis_success_subtitle.startAnimation(bt);
        btn_explore.startAnimation(bt);


    }
}
