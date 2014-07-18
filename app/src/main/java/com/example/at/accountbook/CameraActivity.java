package com.example.at.accountbook; /**
 * Created by AT on 2014-07-18.
 */
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by AT on 2014-07-18.
 */
public class CameraActivity extends Activity{

    // 1. =================================================
    //	여기부터 시작. 시작하면 먼저 온간 Object 선언이 잴 먼저다. 아래쪽에 해도 되지만 통상 위에 한다.
    //	선언된 위치는 별로 상관이 없다.
    public final static int CAMERA_SHOOT = 100; //intent 에 사용될 요청코드
    public final static int GET_PICTURE = 200;

    private Bitmap bitmap; // preview 안에 들어갈 이미지.
    private ImageView preview; // preview
    private String[] photo_menu = {"찍겠어요", "찾을래요"}; // 다이얼로그 메뉴에 쓸 어레이.
    /*
     * 저장한 이미지 Uri 를 담은 변수다. 찍을때마다 갱신되겠지..
     * 이녀석을 전역변수로 선언한 이유는... Intent 에 putExtra 로 담아 보내면..
     * onActivityResult 로 돌려줘야 하는데 이게 단말기 돌려주는녀석 아닌녀석 각각이라고 한다.
     * 단말기마다 내장된 기본카메라 앱이 무엇이냐에따라 다른것일테지...
     * 내가 가지고 있는 T-Mobile G1/ NexusOne 의 기본 카메라 앱은 돌려주지 않는다.
     */
    Uri photo_uri;


    // 2. =================================================
    //	그다음 시작하는게 onCreate. Activity 생성시 잴 먼저 실행된다.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera); // layout/main.xml 정해둔것 전개.

        // 선언했던것들 초기화. 할당 등등...
        preview = (ImageView)findViewById(R.id.preview);
        preview.setOnClickListener(onPreview); // Click리스너(클릭하면 실행될 함수) 등록.
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

        preview.setImageBitmap(bitmap);
    }

    // 4. =================================================
    //	preview 이미지를 클릭하면 실행되는 함수.
    //	사진을찍을건지 이미지검색을 할건지 물어보자.
    View.OnClickListener onPreview = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.preview){ // 워 사실 이건 필요가 없겠지만.. 클릭되는게 하나뿐이니. 그래도 예의상 체크는 하자.
                new AlertDialog.Builder(CameraActivity.this)
                        .setTitle("뭐할래요???")
                        .setItems(photo_menu, onChoice) // photo_menu를 띄우고 선택시 onChoice로
                        .setCancelable(false) // back 버튼 못쓰게.
                        .setNegativeButton("닫기", null)
                        .show();
            }
        }
    };

    // 5. =================================================
    //	다이얼로그박스에서 뭔가 선택했을시 불려오는 리스너.
    DialogInterface.OnClickListener onChoice = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which){
                case 0: // 사진찍기 선택
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
                    break;
                case 1: // 사진찾기 선택
                    Intent intent_getPicture = new Intent(Intent.ACTION_GET_CONTENT);
                    intent_getPicture.setType("image/*");
                    startActivityForResult(intent_getPicture, GET_PICTURE);
                    break;
                default:
                    toAlert("Error", "onChoice\n여기는 default 입니다.");
                    break;
            }
        }
    };


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
                        preview.setImageBitmap(bitmap);
                    }catch (Exception e) {
                        // data가 없을경우. T-Mobile G1 과 Nexus One 의 경우 data가 항상 null 이다.
                        // 따라서 photo_uri 를 전역변수로 선언하고 사용하는데...
                        // 별로 좋지는 못한 방법이라고 생각한다.
                        // 비트맵 이미지를 업로드 또는 가공해야 할때 아래의 경로와 파일을 사용하면 된다.
                        String physical_path = photo_uri.toString().substring(7).replaceAll("%3B", ";").replaceAll("%20"," ");
                        if(bitmap != null && !bitmap.isRecycled()){ bitmap.recycle(); bitmap = null; }
                        bitmap = BitmapFactory.decodeFile(physical_path);

                        toLog("CAMERA_SHOOT/ physical_path : " + physical_path);

                        preview.setImageBitmap(bitmap);
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

                        preview.setImageBitmap(bitmap);
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

}
