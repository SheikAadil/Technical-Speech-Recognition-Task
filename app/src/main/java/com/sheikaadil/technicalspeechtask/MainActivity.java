package com.sheikaadil.technicalspeechtask;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private TextView speechText;
    private RadioGroup radiobtnGroup;
    private RadioButton radiobtnEnglish, radiobtnArabic;
    private Button btnSpeakNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechText = findViewById(R.id.speechText);
        radiobtnGroup = findViewById(R.id.radiobtnGroup);
        radiobtnEnglish = findViewById(R.id.radiobtnEnglish);
        radiobtnArabic = findViewById(R.id.radiobtnArabic);
        btnSpeakNow = findViewById(R.id.btnSpeakNow);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        btnSpeakNow.setOnClickListener(v -> {
            int selectedId = radiobtnGroup.getCheckedRadioButtonId();
            if (selectedId == radiobtnEnglish.getId()) {
                startSpeechRecognition("en-UK");
            } else if (selectedId == radiobtnArabic.getId()) {
                startSpeechRecognition("ar-AE");
            }
        });
    }

    private void startSpeechRecognition(String language) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Speech recognition not supported on your device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String recognizedText = result.get(0);
                speechText.setText("Recognized Text: "+recognizedText);
                Toast.makeText(this, recognizedText, Toast.LENGTH_LONG).show();
                if (recognizedText.equalsIgnoreCase("open website") || recognizedText.equalsIgnoreCase("موقع مفتوح")) {
                    openWebsiteActivity();
                }
            } else {
                Toast.makeText(this, "No speech was recognized", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Speech recognition failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebsiteActivity() {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }
}