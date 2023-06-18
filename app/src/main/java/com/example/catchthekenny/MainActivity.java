package com.example.catchthekenny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView timeText;
    TextView scoreText;
    int score;
    ArrayList<ImageView> kennyImageArray;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeText = (TextView) findViewById(R.id.timeText);
        scoreText = (TextView) findViewById(R.id.scoreText);
        score = 0;
        imageViewAssignment();
        hideImages();
        startCountdown(30);


    }

    public void increaseScore(View view) {
        score++;
        scoreText.setText("Score: " + score);

    }

    private void startCountdown(int Seconds) {
        CountDownTimer countDownTimer = new CountDownTimer(1000 * Seconds, 1000) {
            @Override
            public void onTick(long milliSecondUntilFinished) {
                long secondsRemaining = (milliSecondUntilFinished / 1000);
                timeText.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                timeText.setText("Time Off");
                handler.removeCallbacks(runnable);
                for (ImageView image : kennyImageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                AlertDialog.Builder gameFinishedAlert = new AlertDialog.Builder(MainActivity.this);
                gameFinishedAlert.setTitle("Restart ?");
                gameFinishedAlert.setMessage("Are you sure to restart game ? ");
                gameFinishedAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //Mevcut activity ve hedef activity aynı ise FLAG_ACTIVITY_CLEAR_TOP kullanmaya gerek yok.
                        startActivity(intent);
                    }
                });

                gameFinishedAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Game Over !", Toast.LENGTH_SHORT).show();
                    }
                });

                gameFinishedAlert.show();

            }
        };

        countDownTimer.start();

    }

    private void imageViewAssignment() {
        kennyImageArray = new ArrayList<ImageView>();
        for (int i = 1; i <= 9; i++) {
            String kenny = "kenny" + i;
            int resourceId = getResources().getIdentifier(kenny, "id", getPackageName());
            kennyImageArray.add(i - 1, findViewById(resourceId));


        }
    }

    public void hideImages() {

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                for (ImageView image : kennyImageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                Random random = new Random();
                int i = random.nextInt(9);
                kennyImageArray.get(i).setVisibility(View.VISIBLE);
                // Bu yöntem, belirli bir süre (milisaniye cinsinden)
                // geçtikten sonra bir Runnable nesnesini yürütmek için kullanılır.
                // this diyerek kendi üstündeki methoda referans veriyor.
                handler.postDelayed(this, 500);
                //500 saniyede bi runnable nesnesini çağırıyor.
            }
        };
            handler.post(runnable);

    }

}



