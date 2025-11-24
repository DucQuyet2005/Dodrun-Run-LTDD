package com.example.dodgerun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//import androidx.annotation.NonNull;
//
//import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private GameActivity activity;
    private StringBuilder scoreBuilder;

    public static final String SCORE="score";

    private final int[] obstacleImages = {
            R.drawable.obstac1_tran ,
            R.drawable.obstac2_tran,
            R.drawable.obstac3_tran,
            R.drawable.obstac4_tran,
            R.drawable.obstac5
    };

    private long lastSpawnTime = 0;       // thời điểm tạo obstacle trước đó
    private long spawnInterval = 1500;    // thời gian giữa 2 lần spawn (1.5 giây)
    int screenWidth,screenHeight;
    private int score=0;
    private int lastLevelUpScore = 0;
    private int speed;
    private PlayerCar car;
//    private Obstacle obstacle;

    private boolean gameOver=false;

    private List<Obstacle> obstacleList=new CopyOnWriteArrayList<>();
    private Paint scorePaint;

    private boolean Paused=false;
    private ScoreManager scoreManager;

    private int SelectedCarID=R.drawable.f1_gold_tran;

    private Bitmap bgBitmap;
    private int bgY1 = 0, bgY2 = 0;
    private int bgSpeed = 10; // tốc độ nền

    private Random random=new Random();

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
//        paint = new Paint();
        // Cài đặt Paint MỘT LẦN
                scorePaint = new Paint();
                scorePaint.setColor(getResources().getColor(R.color.high_score));
                scorePaint.setTextSize(60);
                scorePaint.setAntiAlias(true);
        scoreBuilder=new StringBuilder();
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    public void setSelectedCar(int id) {
        this.SelectedCarID = id;
    }

    public void setActivity(GameActivity activity)
    {
        this.activity=activity;
    }

    public void setDifficultLevel(int difficultLevel)
    {
        this.speed=difficultLevel;
    }


    public void pauseGame()
    {
        Paused=true;
    }

    public void resumeGame()
    {
        Paused=false;
    }
    public boolean isPaused()
    {
        return Paused;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
         screenWidth = getWidth();
         screenHeight = getHeight();

        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.road);

        bgBitmap = Bitmap.createScaledBitmap(bgBitmap, screenWidth, screenHeight, true);

        bgY1 = 0;
        bgY2 = -screenHeight;


        // Tạo xe
        car = new PlayerCar(getContext(), screenWidth, screenHeight, SelectedCarID);
        scoreManager=new ScoreManager(activity);

        gameThread = new GameThread(getHolder(), this);
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    public void update()
    {
        if (Paused) return; // Nếu game đang tạm dừng thì bỏ qua

        long now = System.currentTimeMillis();
//        List<Obstacle> obstaclesToRemove = new ArrayList<>();

        //  Cập nhật vật cản hiện có
        for (Obstacle obs : obstacleList) {
            obs.update();

            // Kiểm tra va chạm với xe người chơi
            if (Rect.intersects(obs.getRect(), car.getRect()) && !gameOver) {
                gameOver = true;
                Paused = true;

                scoreManager.saveHighScore(score);
                scoreManager.saveRecentScores(score);

                post(() -> {
                    if (activity != null) {
                        Intent intent = new Intent(activity, AccidentActivity.class);
                        intent.putExtra(SCORE, score);
                        activity.startActivity(intent);
                    }
                });

                return;
            }

            // Xóa vật cản ra khỏi màn hình, cộng điểm
            if (obs.isOffScreen(getHeight())) {
//                obstaclesToRemove.add(obs);
                obstacleList.remove(obs);
                score += 10;
            }
        }
//        synchronized (obstacleList)
//        {
//            obstacleList.removeAll(obstaclesToRemove);
//        }

        if (now - lastSpawnTime >= spawnInterval) {
//             screenWidth = getWidth();
            int carWidth = car.getRect().width();

            int leftLaneX = screenWidth / 4 - carWidth / 2;
            int rightLaneX = (int) (screenWidth * 0.63f - carWidth / 2);

            // --- Random chọn 1 làn ---
            boolean spawnRightLane = random.nextBoolean();
            int obstacleX = spawnRightLane ? rightLaneX : leftLaneX;

            // --- Kiểm tra khoảng cách an toàn với tất cả obstacle hiện có ---
            boolean tooClose = false;
            int safeDistance = (int) (getHeight() * 0.4f); // cách nhau tối thiểu 60% chiều cao màn hình

            for (Obstacle obs : obstacleList) {
                // Nếu obstacle hiện có nằm trong phạm vi dọc quá gần obstacle sắp spawn
                if (Math.abs(obs.getRect().top - (-200)) < safeDistance) {
                    tooClose = true;
                    break;
                }
            }

            // --- Chỉ spawn nếu khoảng cách đủ xa ---
            if (!tooClose) {
                int obstacleResId = obstacleImages[random.nextInt(obstacleImages.length)];
                int obstacleSpeed = speed;
                int laneWidth = carWidth; // obstacle nhỏ bằng 1 làn

                obstacleList.add(new Obstacle(
                        getContext(),
                        obstacleResId,
                        obstacleX,
                        -200,
                        obstacleSpeed,
                        laneWidth
                ));
                lastSpawnTime = now;
            }
        }

        if (score - lastLevelUpScore >= 150) { // chỉ tăng khi vượt thêm 200 điểm
            if (spawnInterval > 600) spawnInterval -= 80;
            speed += 3;
            lastLevelUpScore = score; // cập nhật mốc lần tăng độ khó gần nhất
        }

        // Update background
        bgY1 += bgSpeed;
        bgY2 += bgSpeed;

        if (bgY1 >= screenHeight) {
            bgY1 = bgY2 - screenHeight;
        }

        if (bgY2 >= screenHeight) {
            bgY2 = bgY1 - screenHeight;
        }


    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if (car == null || Paused) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float touchX = event.getX();
//                int screenWidth = getWidth();

                // Nếu người chơi chạm/kéo ở nửa phải màn hình => sang phải
                if (touchX > screenWidth / 2f) {
                    car.switchLane(true);
                } else {
                    car.switchLane(false);
                }
                break;
        }

        return true;
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas == null) return;

        canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);

        canvas.drawBitmap(bgBitmap, 0, bgY1, null);
        canvas.drawBitmap(bgBitmap, 0, bgY2, null);

        // Vẽ xe
        if (car != null) car.draw(canvas);

        // Vẽ tất cả obstacle
        for (Obstacle obs : obstacleList) {
            obs.draw(canvas);
        }
        // TỐI ƯU HÓA HIỂN THỊ ĐIỂM
                scoreBuilder.setLength(0); // Xóa builder
                scoreBuilder.append("Score: ").append(score);

                        // Tính độ rộng chuỗi (để canh phải)
                                float textWidth = scorePaint.measureText(scoreBuilder, 0, scoreBuilder.length());
                // Lề cách mép phải 50px, cách đỉnh 80px
                        float x = getWidth() - textWidth - 50;
                float y = 80;
               // Vẽ text ở góc phải (Dùng scorePaint đã cài đặt)
                       canvas.drawText(scoreBuilder, 0, scoreBuilder.length(), x, y, scorePaint);

    }
}
