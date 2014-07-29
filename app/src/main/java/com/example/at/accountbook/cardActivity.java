package com.example.at.accountbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

/**
 * Created by user on 2014-07-23.
 */
public class cardActivity extends Activity {
    /** Called when the activity is first created. */
    private ListView listview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //해당하는 xml파일 명으로 바꿔야함
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card);
    }
}