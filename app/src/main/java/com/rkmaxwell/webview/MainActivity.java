package com.rkmaxwell.webview;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;

import com.rkmaxwell.webview.interfaces.InternetCheckCallback;
import com.rkmaxwell.webview.utils.Functions;
import com.rkmaxwell.webview.utils.MyWebChromeClient;

public class MainActivity extends AppCompatActivity {

    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        apiCallHit();
        // callApiNotifications(this);
    }

    private void apiCallHit() {

        setTimer();
    }

    // show the splash for 3 sec
    public void setTimer() {
        countDownTimer = new CountDownTimer(2500, 500) {

            public void onTick(long millisUntilFinished) {
                // this will call on every 500 ms
            }

            public void onFinish() {

                if(Functions.isConnectedToInternet(MainActivity.this)) {

                    Intent intent = new Intent(MainActivity.this, MyWebChromeClient.class);
                    intent.putExtra("url", "https:// url here");
                    intent.putExtra("title", "Myapp");
                    startActivity(intent);
                    // overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                }else
                {

                    Intent intent = new Intent(MainActivity.this, MyWebChromeClient.class);
                    intent.putExtra("url", "file:///android_asset/error.html");
                    intent.putExtra("title", "Ole ERP");
                    startActivity(intent);
                    // overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                }

            }
        }.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(countDownTimer!=null)
            countDownTimer.cancel();
    }


    @Override
    protected void onResume() {
        super.onResume();


        Functions.RegisterConnectivity(this, new InternetCheckCallback() {
            @Override
            public void GetResponse(String requestType, String response) {

            }
        });
    }


    ActivityResultLauncher<Intent> connectionCallback = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getBooleanExtra("isShow",false))
                        {
                            apiCallHit();
                        }
                    }
                }
            });


    @Override
    protected void onPause() {
        super.onPause();
        Functions.unRegisterConnectivity(getApplicationContext());
    }


}
