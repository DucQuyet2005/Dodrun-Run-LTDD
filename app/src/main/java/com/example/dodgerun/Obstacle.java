package com.example.dodgerun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Obstacle {
    private Bitmap bitmap;
    private int x, y;
    private int speed;
    private int width, height;

    // scaleRatio: cho phép điều chỉnh tỉ lệ nhỏ bao nhiêu so với làn
    public Obstacle(Context context, int resID, int x, int y, int speed, int laneWidth) {
        Bitmap original = BitmapFactory.decodeResource(context.getResources(), resID);

        //  Thay đổi tỉ lệ ở đây — ví dụ: 1/3 làn
        float scaleRatio = 1f / 2f;  // có thể đổi thành 1/4 nếu vẫn to

        // Tính kích thước theo tỉ lệ
        this.width = (int) (laneWidth * scaleRatio);
        float aspectRatio = (float) original.getHeight() / original.getWidth();
        this.height = (int) (width * aspectRatio);

        // Scale bitmap
        this.bitmap = Bitmap.createScaledBitmap(original, width, height, true);

        // Canh giữa obstacle trong làn (vì nhỏ hơn làn)
        this.x = x + (laneWidth - width) / 2;
        this.y = y;
        this.speed = speed;
    }

    public void update() { y += speed; }

    public void draw(Canvas canvas) { canvas.drawBitmap(bitmap, x, y, null); }

    public Rect getRect() {
        int marginX = bitmap.getWidth() / 8;
        int marginY = bitmap.getHeight() / 8;
        return new Rect(x + marginX, y + marginY,
                x + bitmap.getWidth() - marginX,
                y + bitmap.getHeight() - marginY);
    }


    public boolean isOffScreen(int screenHeight) { return y > screenHeight; }
}
