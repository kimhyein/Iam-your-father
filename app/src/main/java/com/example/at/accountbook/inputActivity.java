package com.example.at.accountbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by user on 2014-07-30.
 */
public class inputActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_input);
    }
}
