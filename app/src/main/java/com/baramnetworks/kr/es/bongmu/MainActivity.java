package com.baramnetworks.kr.es.bongmu;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import com.baramnetworks.kr.es.bongmu.R;


public class MainActivity extends AppCompatActivity {

    String strNickname, strProfile; //각각 닉네임, 프로필 사진 URL, 이메일, 연령대, 성별, 생일 받는 String

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //뷰 선언
        TextView tvNickname = findViewById(R.id.tvNickname);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnSignout = findViewById(R.id.btnSignout);
        Button btnLogin = findViewById(R.id.btnLogin);

        //LoginActivity에서 넘어온 정보 받아옴
        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");

        //유저 정보 표시
        //프로필 사진은 Glide 라이브러리 이용해서 표시함
        tvNickname.setText(strNickname);
        Glide.with(this).load(strProfile).into(ivProfile);

        btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BongmuLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //로그아웃 버튼
        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show(); //로그아웃 Toast 메세지

                //실제 로그아웃 처리
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        //로그아웃에 성공하면: LoginActivity로 이동
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        //회원탈퇴 버튼
        btnSignout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this) //탈퇴 의사를 묻는 팝업창 생성
                        .setMessage("탈퇴하시겠습니까?") //팝업창 메세지
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //"네" 버튼 클릭 시 -> 회원탈퇴 수행
                                //회원탈퇴 수행
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) { //회원탈퇴 실패 시
                                        int result = errorResult.getErrorCode(); //에러코드 받음

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) { //클라이언트 에러인 경우 -> 네트워크 오류
                                            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else { //클라이언트 에러가 아닌 경우 -> 기타 오류
                                            Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) { //처리 도중 세션이 닫힌 경우
                                        Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() { //가입된 적이 없는 계정에서 탈퇴를 요구하는 경우
                                        Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) { //회원탈퇴에 성공한 경우
                                        Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                dialog.dismiss(); //팝업창 제거
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //"아니요" 버튼 클릭 시 -> 팝업창 제거
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }
}
