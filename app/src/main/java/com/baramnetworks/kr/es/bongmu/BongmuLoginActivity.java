package com.baramnetworks.kr.es.bongmu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baramnetworks.kr.es.bongmu.R;

public class BongmuLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bongmu_login);
        Button btnCancel = findViewById(R.id.btn_bongmu_cancel);
        final TextView bongmu_id = findViewById(R.id.bongmu_id);
        final TextView bongmu_password = findViewById(R.id.bongmu_password);
        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnLogin = findViewById(R.id.btn_bongmu_login);
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(bongmu_id.getText());
                System.out.println(bongmu_password.getText());

            }
        });
    }
}
