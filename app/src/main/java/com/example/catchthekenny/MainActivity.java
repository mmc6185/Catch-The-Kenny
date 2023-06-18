package com.example.catchthekenny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.gridlayout.widget.GridLayout;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int score;
    GridLayout gridLayout;
    TextView scoreText, timeText;
    Handler handlerImagesVisibility, remainingTimeHandler;
    Runnable runnableImagesVisibility;
    ArrayList<ImageView> kennyImagesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueAssignment();

        hideImages();

        remainingTime();

    }

    public void increaseScore(View view) {
        score++;
        scoreText.setText("Score:" + score);
    }

    private void kennyImageViewAssignment(){
        kennyImagesArrayList = new ArrayList<ImageView>();
        for (int i = 1; i <= 25; i++){
            String kennyImagesId = "kenny" + i;
            int resourceId = getResources().getIdentifier(kennyImagesId, "id", getPackageName());
            kennyImagesArrayList.add(i - 1, findViewById(resourceId));
        }
    }

    private void remainingTime() {

        CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long milliSecondUntilFinished) {
                long secondsRemaining = (milliSecondUntilFinished / 1000);
                timeText.setText("Time : " + secondsRemaining);
            }

            @Override
            public void onFinish() {
                timeText.setText("Finished");
                handlerImagesVisibility.removeCallbacks(runnableImagesVisibility);

                for (ImageView kennyImage : kennyImagesArrayList){
                    kennyImage.setVisibility(View.INVISIBLE);
                }

                AlertDialog.Builder finishedAlertDialog = new AlertDialog.Builder(MainActivity.this);
                finishedAlertDialog.setTitle("Restart ?");
                finishedAlertDialog.setMessage("Are you sure to restart game ? ");

                finishedAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                });

                finishedAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Game over !", Toast.LENGTH_SHORT).show();
                    }
                });

                finishedAlertDialog.show();

            }
        };

        countDownTimer.start();

    }


    private void hideImages(){
        handlerImagesVisibility = new Handler();
        runnableImagesVisibility = new Runnable() {
            @Override
            public void run() {
                kennyImageViewAssignment();
                for (ImageView kennyImage : kennyImagesArrayList){
                    kennyImage.setVisibility(View.INVISIBLE);
                }
                Random random = new Random();
                int randomKennyImageId = random.nextInt(25);
                kennyImagesArrayList.get(randomKennyImageId).setVisibility(View.VISIBLE);
                handlerImagesVisibility.postDelayed(this, 1000);
            }
        };




        handlerImagesVisibility.post(runnableImagesVisibility);

    }

    private void valueAssignment(){
        gridLayout = findViewById(R.id.gridLayout);
        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
    }



}