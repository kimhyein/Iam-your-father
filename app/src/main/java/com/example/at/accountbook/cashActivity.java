package com.example.at.accountbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import android.app.Activity;

import android.content.Context;

import android.graphics.Color;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.view.Window;

import android.widget.ArrayAdapter;

import android.widget.ImageView;

import android.widget.ListView;

import android.widget.TextView;

public class cashActivity extends Activity {
    /** Called when the activity is first created. */

    private ListView listview; //리스트 뷰


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //해당하는 xml파일 명으로 바꿔야함
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cash);

        Button cash_in = (Button) findViewById(R.id.btn_input);
        cash_in.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent cash_intent = new Intent(cashActivity.this, inputActivity.class);
                startActivity(cash_intent);
            }
        });

    }


    }

