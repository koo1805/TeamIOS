package com.example.teamios;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private Button btn_Speak;
    private EditText txtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this, this);
        btn_Speak = findViewById(R.id.btnSpeak);
        txtText = findViewById(R.id.txtText);

        btn_Speak.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) // LOLLIPOP버전 이상에서만 실행가능
            @Override
            public void onClick(View v){
                speakOut();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private  void speakOut(){
        CharSequence text = txtText.getText();
        tts.setPitch((float) 0.6); //음량조절
        tts.setSpeechRate((float) 0.1); // 재생속도 조절
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null,"id1");
        // tts.speak(음성출력할 텍스트, TextToSpeech.QUEUE_FLUSH : 실행중인 음성출력 중지후 현재 음성을 출력)
        //                            TextToSpeech.QUEUE_ADD   : 실행중인 음성출력이 끝난뒤에 현재 음성출력)
    }

    //사용된 TTS객체를 제거하는 함수
    @Override
    public void onDestroy() {
        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    //OnInitListener를 통해서 TTS를 초기화
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public  void onInit(int status){
        if(status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.KOREA); // TTS언어를 한국어로 설정

            if(result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","This Language is not supported");
            }else{
                btn_Speak.setEnabled(true);
                speakOut(); // onInit에 음성출력할 텍스트를 넣음
            }
        }else{
            Log.e("TTS","Initilization Failed!!!");
        }
    }
}
