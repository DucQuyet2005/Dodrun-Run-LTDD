package com.example.dodgerun;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    private ImageButton btnPause;

    private GameView gameview;

    private int selectedCarID;
    private int speed;

    private LinearLayout pauseMenuLayout;
    private Button btnResume,btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnPause=findViewById(R.id.imgBtnPause);
        pauseMenuLayout=findViewById(R.id.pauseMenuLayout);
        btnResume=findViewById(R.id.buttonResume);
        btnBackToHome=findViewById(R.id.buttonBackToHome);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameview.pauseGame();
                pauseMenuLayout.setVisibility(View.VISIBLE);
            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMenuLayout.setVisibility(View.GONE);
                gameview.resumeGame();
            }
        });
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPause.bringToFront();
        gameview=findViewById(R.id.gameView);
        selectedCarID=getIntent().getExtras().getInt(MainActivity.SELECTED_CARID,R.drawable.f1_gold_tran);
        speed=getIntent().getExtras().getInt(MainActivity.SELECTED_DIFFICULT,5);
        gameview.setSelectedCar(selectedCarID);
        gameview.setDifficultLevel(speed);
        gameview.setActivity(this);
        Log.d("GameActivity","ID car:"+selectedCarID);
        Log.d("GameActivity","Speed:"+speed);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameview.pauseGame();
        pauseMenuLayout.setVisibility(View.VISIBLE);
    }
}