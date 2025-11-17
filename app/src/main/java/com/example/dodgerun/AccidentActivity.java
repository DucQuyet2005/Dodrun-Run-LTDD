package com.example.dodgerun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccidentActivity extends AppCompatActivity {

    private Button btnBackToHome, btnPlayAgain;
    private TextView tvScore, tvHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accident);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Views ---
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        tvScore = findViewById(R.id.tvScore);

        //luu highscore voi sharedpreferences
        // 🔹 Thêm TextView hiển thị high score
        tvHighScore = new TextView(this);
        tvHighScore.setTextSize(24);
        tvHighScore.setTextColor(getResources().getColor(R.color.high_score));
        // Bạn có thể set position bằng ConstraintLayout params nếu muốn cố định
        ConstraintLayout layout = findViewById(R.id.main);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.topToBottom = tvScore.getId(); // đặt dưới tvScore
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topMargin = 16;
        layout.addView(tvHighScore, params);

        // --- Lấy điểm hiện tại ---
        int currentScore = getIntent().getExtras().getInt(GameView.SCORE, 0);
        tvScore.setText("Score: " + currentScore);

        // --- Load và cập nhật high score ---
        SharedPreferences prefs = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        int highScore = prefs.getInt("HighScore", 0); // nếu chưa có -> 0

        if (currentScore > highScore) {
            // Lưu high score mới
            prefs.edit().putInt("HighScore", currentScore).apply();
            highScore = currentScore;
        }

        // Hiển thị high score
        tvHighScore.setText("High Score: " + highScore);

        // --- Nút ---
        btnBackToHome.setOnClickListener(v -> finish()); // quay lại MainActivity
        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(AccidentActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
