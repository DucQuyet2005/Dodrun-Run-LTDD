package com.example.dodgerun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class ChooseCarActivity extends AppCompatActivity {

    private TextView tvHighScore;
    private ImageButton imgBtnNext,imgBtnBack;
    private ImageView imgvCar;
    private Button btnRandom,btnChoose;

    private  int[] carList =new int[]{R.drawable.f1_gold_tran,R.drawable.f1_red_tran,R.drawable.blackcar_removebg};

    private int currentIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvHighScore=findViewById(R.id.tvHighScore);
        imgBtnNext=findViewById(R.id.imgBtnNext);
        imgBtnBack=findViewById(R.id.imgBtnBack);
        imgvCar=findViewById(R.id.imgVCar);
        btnRandom=findViewById(R.id.btnRandom);
        btnChoose=findViewById(R.id.btnChoose);

        imgvCar.setImageResource(carList[currentIndex]);

        imgBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if(currentIndex>=carList.length)
                {
                    currentIndex=0;
                }
                    imgvCar.setImageResource(carList[currentIndex]);
            }
        });

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               currentIndex--;
               if(currentIndex<0)
               {
                   currentIndex=carList.length-1;
               }
                imgvCar.setImageResource(carList[currentIndex]);
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseCarActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.SELECTED_CARID,carList[currentIndex]);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        //button random logical
        Random random = new Random();

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomIndex = random.nextInt(carList.length);
                currentIndex = randomIndex;

                imgvCar.setImageResource(carList[currentIndex]);
            }
        });

    }
}