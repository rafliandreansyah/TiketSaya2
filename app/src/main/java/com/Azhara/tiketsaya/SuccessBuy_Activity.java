package com.Azhara.tiketsaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class SuccessBuy_Activity extends AppCompatActivity {

    Animation tb_scale, bt;
    View img_success;
    TextView success_title, success_subtitle;
    Button btnViewTiket, btnDashboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_);

        tb_scale = AnimationUtils.loadAnimation(this, R.anim.top_bottom_scale);
        bt = AnimationUtils.loadAnimation(this, R.anim.bottom_top);

        img_success = findViewById(R.id.buy_success_img);
        success_title = findViewById(R.id.success_title);
        success_subtitle = findViewById(R.id.success_sub);
        btnViewTiket = findViewById(R.id.btn_viewticket);
        btnDashboard = findViewById(R.id.btn_dashboard);

        img_success.setAnimation(tb_scale);
        success_title.setAnimation(tb_scale);
        success_subtitle.setAnimation(tb_scale);
        btnViewTiket.setAnimation(bt);
        btnDashboard.setAnimation(bt);

        btnViewTiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewTicket = new Intent(SuccessBuy_Activity.this, My_Profile_Activity.class);
                startActivity(viewTicket);
                finish();
            }
        });

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(SuccessBuy_Activity.this, HomeActivity.class);
                startActivity(dashboard);
                finish();
            }
        });

    }
}
