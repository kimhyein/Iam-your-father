package com.example.at.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;





public class MyActivity extends Activity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, SplashActivity.class));

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my);

        initializeGood();

        ((Button)findViewById(R.id.btn_sms)).setOnClickListener(this);


        Button sms = (Button) findViewById(R.id.btn_sms);
        sms.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, SmsList.class);
                startActivity(intent);
            }
        });

        Button camera = (Button) findViewById(R.id.btn_camera);
        camera.setOnClickListener(new Button.OnClickListener()  {
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initializeGood()
    {
        InitializationRunnable init = new InitializationRunnable();
        new Thread(init).start();
    }



    class InitializationRunnable implements Runnable
    {
        public void run()
        {
            // 여기서부터 초기화 작업 처리
            for (int i=0; i<5;i++)
            {
                try
                {
                    Thread.sleep(1000);

                    // LogCat으로 로그를 보면 스플래시 화면 표시 중에 계속 이미 메시지가 표시됨을 확인할 수 있습니다.
                    Log.d("SPLASH", "------------- initialize..........");
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MyActivity.this, SmsList.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
