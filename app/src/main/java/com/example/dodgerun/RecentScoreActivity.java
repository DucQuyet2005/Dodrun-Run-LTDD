package com.example.dodgerun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentScoreActivity extends AppCompatActivity {

    private RecyclerView rvRecentScores;
    private ScoreManager scoreManager;
    private Button btnBackHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_score);

        rvRecentScores = findViewById(R.id.rvRecentScores);

        scoreManager = new ScoreManager(this);


        // Lấy danh sách điểm gần nhất
        List<Integer> recentScores = scoreManager.getRecentScores();

        // Cấu hình RecyclerView
        rvRecentScores.setLayoutManager(new LinearLayoutManager(this));
        rvRecentScores.setAdapter(new ScoreAdapter(recentScores));

        btnBackHome = findViewById(R.id.btnBackHome);

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(RecentScoreActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


    }
}
