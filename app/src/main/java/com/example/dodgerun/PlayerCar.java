package com.example.dodgerun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class PlayerCar {
    private Bitmap bitmap;
    private int x, y;
    private int screenWidth, screenHeight;
    private int carWidth, carHeight;
    private int leftLaneX, rightLaneX;

    public PlayerCar(Context context, int screenWidth, int screenHeight, int resId) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Load ảnh gốc
        Bitmap original = BitmapFactory.decodeResource(context.getResources(), resId);

        // ✅ Tính chiều rộng 1 làn đường
        int laneWidth = screenWidth / 3; // bạn có thể thay đổi tỉ lệ nếu muốn 2 làn rõ hơn

        // ✅ Scale xe = 1 làn (hoặc nhỏ hơn chút, ví dụ 0.8f để xe nằm gọn)
        float scaleRatio = 0.8f; // 80% chiều rộng làn
        carWidth = (int) (laneWidth * scaleRatio);

        // Giữ tỉ lệ ảnh gốc
        float aspectRatio = (float) original.getHeight() / original.getWidth();
        carHeight = (int) (carWidth * aspectRatio);

        // Scale ảnh
        bitmap = Bitmap.createScaledBitmap(original, carWidth, carHeight, true);

        // ✅ Tính các vị trí làn dựa theo carWidth mới
        leftLaneX = screenWidth / 4 - carWidth / 2;
        rightLaneX = (int) (screenWidth * 0.63f - carWidth / 2);

        // Khởi tạo xe ở làn phải
        x = rightLaneX;
        y = screenHeight - carHeight - 50;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

// Sửa method switchLane trong PlayerCar.java

    public void switchLane(boolean moveRightLane) {
        int oldX = x;

        if (moveRightLane) {
            x = rightLaneX;
            Log.d("PlayerCar", "Switched to RIGHT lane: " + oldX + " → " + x);
        } else {
            x = leftLaneX;
            Log.d("PlayerCar", "Switched to LEFT lane: " + oldX + " → " + x);
        }

        Log.d("PlayerCar", "Current position: x=" + x + ", y=" + y);
    }

    public void resetPosition() {
        x = rightLaneX;
        y = screenHeight - carHeight - 50;
    }

    public Rect getRect() {
        int marginX = bitmap.getWidth() / 10;
        int marginY = bitmap.getHeight() / 10;
        return new Rect(x + marginX, y + marginY,
                x + bitmap.getWidth() - marginX,
                y + bitmap.getHeight() - marginY);
    }



}
