package com.example.at.accountbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;





public class MyActivity extends Activity implements View.OnClickListener{


    public final static int CAMERA_SHOOT = 100; //intent 에 사용될 요청코드
    public final static int GET_PICTURE = 200;




    private Bitmap bitmap; // preview 안에 들어갈 이미지.

    Uri photo_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, SplashActivity.class));

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my);

        initializeGood();



        Button sms = (Button) findViewById(R.id.btn_sms);
        sms.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.putExtra("address", "15886700");
                intent.putExtra("sms_body", "");
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
            }
        });

        Button camera = (Button) findViewById(R.id.btn_camera);
        camera.setOnClickListener(new Button.OnClickListener()  {
            public void onClick(View v) {
                // sd 카드의 mount 여부 검사.
                if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
                    // sd 카드 경로 가져오기
                    String SD_path = Environment.getExternalStorageDirectory().getAbsolutePath(); //sd 카드 루트경로.
                    SD_path += "/testCamera"; // 사용할 폴더. 경로만 지정한것이지 실제로 폴더가 생기거나 한건 아니다.

                    // sd 카드에 사용할 폴더있는지 검사후 없으면 생성.
                    File photoFile = new File(SD_path);
                    if(!photoFile.exists()){
                        toLog("Image directory does not exist!!! new make directory");
                        photoFile.mkdir();
                    }else{
                        toLog("(File)photoFile : " + photoFile.getPath());
                    }
                    toLog("(String)SD_path : " + SD_path);
                    toLog("(File)photoFile : " + photoFile.getPath());
                    // 파일명 지정.파일명은 유니크 해야 하므로.. 가장 흔한 날짜 시간을 파일명으로 사용한다.
                    // "eastgem test shoot XXXX-XX-XX XX:XX:XX.jpg" 라는 이름이 될것이다.
                    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    //			    				"yyyy-MM-dd");
                    String filename = "/eastgem test shoot " + timeStampFormat.format(new Date()).toString() + ".jpg";

                    // 폴더 생성이나 점검을 완료했으니 파일을 직접 생성하자.
                    photoFile = new File(SD_path + filename); // 사진이 저장될 경로 및 파일명.
                    photo_uri = Uri.fromFile(photoFile);

                    toLog("new (File)photoFile : " + photoFile.getPath());
                    toLog("Uri photo_uri : " + photo_uri.toString());

                    // 카메라 Activity 부를때 사용할 인텐트
                    Intent intent_Camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent_Camera.putExtra(MediaStore.EXTRA_OUTPUT, photo_uri);
                    startActivityForResult(intent_Camera, CAMERA_SHOOT);
                }else{
                    // sd 카드가 없으면 내부 메모리에 저장해도 되긴 한다.
                    // 그러나 나는 사용하지 못하게 하겠다.
                    toAlert("SD카드가 장착되어 있지 않습니다.");
                }
            }
        });

        Button gallary = (Button) findViewById(R.id.btn_gal);
        gallary.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent_getPicture = new Intent(Intent.ACTION_GET_CONTENT);
                intent_getPicture.setType("image/*");
                startActivityForResult(intent_getPicture, GET_PICTURE);
            }
        });



        Button cash = (Button) findViewById(R.id.btn_cash);
        cash.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent_cash = new Intent(MyActivity.this, cashActivity.class);
                startActivity(intent_cash);
            }
        });

        Button card = (Button) findViewById(R.id.btn_card);
        card.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent_card = new Intent(MyActivity.this, cardActivity.class);
                startActivity(intent_card);
            }
        });

    }

    // 3. =================================================
    //	3번이라고 하지만 사실 정확하게 3번은 아니다.
    //	onStart 함수는 Activity의 라이프사이클중에서 Activity 가 뭐라고 해야하나..active 될때 불려진다.
    //	그러니 화면 셋팅은 여기서 하자.
    @Override
    protected void onStart() {
        super.onStart();
        if(bitmap == null){ // 비트맵이 저정되어 있으면 비트맵을 출력하고 없으면 기본이미지를 출력한다.
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        }

        //preview.setImageBitmap(bitmap);
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


    // 6. =================================================
    // 다른 엑티비티가 실행을 종료하면서 리턴값을 넘겨줬을 때 실행되는 함수.
    // 내가 액티비티를 실행하면서 startActivityForResult 함수로 실행하면 리턴받는것은 이곳에서 처리한다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_SHOOT: // 카메라 요청을 끝내고 리턴했을때.
                if(resultCode == Activity.RESULT_OK){ // 카메라로 사진을 찍고 저장했을때
                    try{
                        // data 가 있을경우. 이 경우가 기기마다 다르다고 한다.
                        if(bitmap != null && !bitmap.isRecycled()){ bitmap.recycle(); bitmap = null; }
                        bitmap = (Bitmap)data.getExtras().get(MediaStore.EXTRA_OUTPUT);
                        //preview.setImageBitmap(bitmap);
                    }catch (Exception e) {
                        // data가 없을경우. T-Mobile G1 과 Nexus One 의 경우 data가 항상 null 이다.
                        // 따라서 photo_uri 를 전역변수로 선언하고 사용하는데...
                        // 별로 좋지는 못한 방법이라고 생각한다.
                        // 비트맵 이미지를 업로드 또는 가공해야 할때 아래의 경로와 파일을 사용하면 된다.
                        String physical_path = photo_uri.toString().substring(7).replaceAll("%3B", ";").replaceAll("%20"," ");
                        if(bitmap != null && !bitmap.isRecycled()){ bitmap.recycle(); bitmap = null; }
                        bitmap = BitmapFactory.decodeFile(physical_path);

                        toLog("CAMERA_SHOOT/ physical_path : " + physical_path);

                        //preview.setImageBitmap(bitmap);
                        photo_uri = null;
                    }
                }else{ // 취소버튼을 눌렀을때.
                    toMessage("취소 하셨습니다.");
                }
                break;
            case GET_PICTURE: // 이미지 선택을 끝내고 리턴했을때.
                if(resultCode == Activity.RESULT_OK){ // 정상적으로 이미지를 선택했을경우.
                    try{ // 이미지 선택의 경우 data 가 null 이 들어오는 일은 없지 싶다...
                        Uri select_uri = data.getData();
                        toLog("GET_PICTURE/ select_uri : " + select_uri.toString());

                        Cursor c = getContentResolver().query(Uri.parse(select_uri.toString()), null,null,null,null);
                        c.moveToNext();

                        // 비트맵 이미지를 업로드 또는 가공할경우 아래의 변수를 사용하도록 한다.
                        String physical_path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA)).toString().substring(5).replaceAll("%3B", ";").replaceAll("%20"," ");
                        if(bitmap != null && !bitmap.isRecycled()){ bitmap.recycle(); bitmap = null; }
                        bitmap = BitmapFactory.decodeFile(physical_path);

                        toLog("GET_PICTURE/ physical_path : " + physical_path);

                        //preview.setImageBitmap(bitmap);
                    }catch (Exception e) {
                        toAlert("Exception\n" + e.getMessage());
                    }
                }else{ // 취소버튼을 눌렀을때.
                    toMessage("취소 하셨습니다.");
                }
                break;
            default:
                toAlert("Error", "onActivityResult\n여기는 default 입니다.");
                break;
        }
    };


    // 메세지 출력용 ================================
    Toast tempToast;
    public void toMessage(String str){
        if(tempToast==null){
            tempToast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        }else{
            tempToast.setText(str);
        }
        tempToast.show();
    }

    public void toAlert(String str){
        new AlertDialog.Builder(this)
                .setMessage(str)
                .setCancelable(false)
                .setNegativeButton("닫기", null)
                .show();
    }

    public void toAlert(String title, String str){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(str)
                .setCancelable(false)
                .setNegativeButton("닫기", null)
                .show();
    }

    public void toLog(String str){ //로그 출력용.
        Log.v("@#@===MY===@#@", str);
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
