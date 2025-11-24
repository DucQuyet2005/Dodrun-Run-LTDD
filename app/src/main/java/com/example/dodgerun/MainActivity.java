package com.example.dodgerun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvHighScore;
    private Button btnPlay;
    private ImageButton imgBtnChooseCar;
    private RadioGroup rdoGroup;

    private final int DIFFICULTY_EASY = 10;
    private final int DIFFICULTY_MEDIUM = 20;
    private final int DIFFICULTY_HARD = 40;

    private final int REQUEST_CODE_CHOOSE_CAR = 100;

    public static String SELECTED_CARID = "Key_xe";
    public static String SELECTED_DIFFICULT = "Key_diff";
    private static int Key_Xe = R.drawable.f1_gold_tran; // Mặc định xe màu vàng
    private static int Key_Kho = R.id.rdoEasy; // Mặc định là dễ

    // SharedPreferences key
    private static final String PREFS_NAME = "GamePrefs";
    private static final String PREF_HIGH_SCORE = "HighScore";

    private ScoreManager scoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ----- INIT VIEW -----
        tvHighScore = findViewById(R.id.tvHighScore);
        scoreManager=new ScoreManager(MainActivity.this);
        tvHighScore.setText("High Score"+String.valueOf(scoreManager.getHightScore()));
        btnPlay = findViewById(R.id.btnPlay);
        imgBtnChooseCar = findViewById(R.id.imgBtnChooseCar);
        rdoGroup = findViewById(R.id.rdoGroup);

        // ----- LOAD HIGH SCORE từ SharedPreferences -----
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int highScore = prefs.getInt(PREF_HIGH_SCORE, 0);
        tvHighScore.setText("High Score: " + highScore);

        // ----- CHỌN XE -----
        imgBtnChooseCar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChooseCarActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_CAR);
        });

        // ----- BẮT ĐẦU GAME -----
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(SELECTED_CARID, Key_Xe);

            int levelOfDiff = rdoGroup.getCheckedRadioButtonId();
            if(levelOfDiff == R.id.rdoMedi) {
                Key_Kho = DIFFICULTY_MEDIUM;
            } else if(levelOfDiff == R.id.rdoDiff) {
                Key_Kho = DIFFICULTY_HARD;
            } else {
                Key_Kho = DIFFICULTY_EASY;
            }
            Log.d("MainActivity", "Level of difficulty: " + Key_Kho);

            bundle.putInt(SELECTED_DIFFICULT, Key_Kho);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_CHOOSE_CAR && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            int selectedCarID = bundle.getInt(SELECTED_CARID);
            Key_Xe = selectedCarID;
            imgBtnChooseCar.setImageResource(Key_Xe);
            Log.d("MainActivity", "ID Xe: " + selectedCarID);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load High Score từ SharedPreferences mỗi lần Activity hiển thị
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        int highScore = prefs.getInt("HighScore", 0);
        tvHighScore.setText("High Score: " + highScore);
    }

}
