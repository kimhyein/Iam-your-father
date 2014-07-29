package com.example.at.accountbook;

import android.app.Activity;
import android.os.Bundle;
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


    }


    }

