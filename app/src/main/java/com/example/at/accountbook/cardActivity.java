package com.example.at.accountbook;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by user on 2014-07-23.
 */
public class cardActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //해당하는 xml파일 명으로 바꿔야함
        setContentView(R.layout.activity_card);
    }
}