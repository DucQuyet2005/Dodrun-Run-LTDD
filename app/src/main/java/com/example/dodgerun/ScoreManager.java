package com.example.dodgerun;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    private static final String PREF_NAME="GamePrefs";
    private static final String HIGH_SCORE_KEY = "HighScore";
    private static final String RECENT_SCORES_FILE = "recent_scores.json";
    private Context context;
    public ScoreManager(Context context){
        this.context=context;
    }

    public void saveHighScore(int score)
    {
        SharedPreferences preferences= context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int currentScore=preferences.getInt(GameView.SCORE,0);
        if(score >currentScore)
        {
            preferences.edit().putInt(GameView.SCORE,score).apply();
        }
    }

    public int getHightScore()
    {
        SharedPreferences preferences=context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getInt(GameView.SCORE,0);
    }

    public void saveRecentScores(int score)
    {
        List<Integer> scores = getRecentScores();
        scores.add(0, score); // thêm điểm mới lên đầu danh sách

        // Giới hạn 5 điểm gần nhất
        if (scores.size() > 5) {
            scores = scores.subList(0, 5);
        }

        // Ghi ra file JSON
        JSONArray jsonArray = new JSONArray(scores);
        try (FileOutputStream fos = context.openFileOutput(RECENT_SCORES_FILE, Context.MODE_PRIVATE)) {
            fos.write(jsonArray.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getRecentScores()
    {
        List<Integer> scores = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(RECENT_SCORES_FILE)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                scores.add(jsonArray.getInt(i));
            }
        } catch (Exception e) {
            // Nếu file chưa tồn tại, trả về danh sách rỗng
            e.printStackTrace();
        }
        return scores;
    }
}
