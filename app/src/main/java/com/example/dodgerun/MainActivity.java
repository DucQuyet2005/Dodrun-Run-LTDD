package com.example.dodgerun;

import android.content.Intent;
import android.graphics.Paint;
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
    private final int DIFFICULTY_MEDIUM =20 ;
    private final int DIFFICULTY_HARD = 60;

    private final int REQUEST_CODE_CHOOSE_CAR = 100;
    
    public static String SELECTED_CARID="Key_xe";
    public static String SELECTED_DIFFICULT="Key_diff";
    private static int Key_Xe=R.drawable.f1_gold_tran;//Mac dinh la xe mau vang
    private static int Key_Kho=R.id.rdoEasy;//Mac dinh la de

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

        tvHighScore = findViewById(R.id.tvHighScore);
        btnPlay = findViewById(R.id.btnPlay);
        imgBtnChooseCar = findViewById(R.id.imgBtnChooseCar);
        rdoGroup=findViewById(R.id.rdoGroup);


        imgBtnChooseCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChooseCarActivity.class);
                startActivityForResult(intent,REQUEST_CODE_CHOOSE_CAR);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GameActivity.class);//Can tao them man hinh choi
                Bundle bundle = new Bundle();
                bundle.putInt(SELECTED_CARID,Key_Xe);
                int levelOfDiff=rdoGroup.getCheckedRadioButtonId();
                if(levelOfDiff==R.id.rdoMedi)
                {
                    Key_Kho=DIFFICULTY_MEDIUM;
                }
                else if(levelOfDiff==R.id.rdoDiff)
                {
                    Key_Kho=DIFFICULTY_HARD;
                }
                else if(levelOfDiff==R.id.rdoEasy)
                {
                    Key_Kho=DIFFICULTY_EASY;
                }
                   Log.d("MainActivity","Level of difficult:"+Key_Kho);
                bundle.putInt(SELECTED_DIFFICULT,Key_Kho);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CHOOSE_CAR && resultCode==RESULT_OK && data != null)
        {
            Bundle bundle = data.getExtras();
            int selectedCarID=bundle.getInt(SELECTED_CARID);
            Key_Xe=selectedCarID;
            imgBtnChooseCar.setImageResource(Key_Xe);
            Log.d("MainActivity","ID Xe:"+selectedCarID);
        }

    }
}