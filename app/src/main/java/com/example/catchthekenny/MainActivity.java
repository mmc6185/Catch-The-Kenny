package com.example.catchthekenny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView timeText, scoreText;
    int scoreValue;
    Handler gameHandler;
    Runnable runnableImagesVisibility;
    ArrayList<ImageView> kennyImagesArrayList;
    String kennyImagesId;
    int resourceId;
    int randomImageId;
    CountDownTimer countDownTimer;
    Intent restartIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ettiğimiz değişkenleri burada çağırıyoruz.
        valueAssignment();
        // Görselleri rastgele bir biçimde çağıran, geri sayımı başlatan metodumuzu burada çağırıyoruz.
        randomizeImageVisibilityCycle();

    }

    private void valueAssignment(){
        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
        kennyImagesArrayList = new ArrayList<ImageView>();
        gameHandler = new Handler();
        restartIntent = new Intent(MainActivity.this, MainActivity.class);
    }

    public void increaseScore(View view){
        // Kenny imageView'lara tanımlanmış onClick metodu.
        // ScoreText TextView'ın içerisindeki score değerini her onClick metodu çağırıldığında 1 arttıran bir metod.
        scoreValue++;
        scoreText.setText("Score : " + scoreValue);
    }

    private void kennyImageViewAssignment(){
        // Kenny image'ları bir ArrayList olarak tanımlıyor.
        // getResources().getIdentifier komuut sırası ile String, "id", ve paket ismi değerlerini alarak bize objenin id'sini döndürüyor.
        // id döndürülen ifadeyi findViewById komutu ile bularak ArrayList'e 0. index'ten başlayarak değer ataması yapıyoruz.
        for (int i = 1; i <= 25; i++){
            kennyImagesId = "kenny" + i;
            resourceId = getResources().getIdentifier(kennyImagesId, "id", getPackageName());
            kennyImagesArrayList.add(i -1, findViewById(resourceId));
        }
    }

    private void hideImages(){
        // kennyImageViewAssignment içerisinde tanımlanmış kennyImagesArrayList++++
        // ArrayList'inde bulunan tüm ImageView ifadelerini INVISIBLE yapıyoruz.
        for (ImageView kennyImage : kennyImagesArrayList){
            kennyImage.setVisibility(View.INVISIBLE);
        }
    }

    private void displayRandomImage(){
        // kennyImageViewAssignment içerisinde tanımlanmış kennyImagesArrayList ArrayList'inde ki ++
        // ImageView'lardan rastgele bir adetini VISIBLE yapıyoruz.
        randomImageId = new Random().nextInt(25);
        kennyImagesArrayList.get(randomImageId).setVisibility(View.VISIBLE);
    }
    private void randomizeImageVisibilityCycle() {

        // Her 0.5 saniyede kennyImagesArrayList ArrayList'te ki rastgele farklı bir ImageView'i düzenli
        // olarak VISIBLE yapan thread'i oluşturan metod.

        kennyImageViewAssignment();
        hideImages();

        runnableImagesVisibility = new Runnable() {

            @Override
            public void run() {

                hideImages();

                displayRandomImage();

                gameHandler.postDelayed(runnableImagesVisibility, 500);

            }

        };

        gameHandler.postDelayed(this::remainingTime, 2000);
        gameHandler.postDelayed(runnableImagesVisibility, 2000);


    }

    private void remainingTime(){

        // timeText içerisinde geri sayım yapmayı sağlayan bir metod.

        countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long milliSecondUntilFinished) {
                timeText.setText("Time : " + (milliSecondUntilFinished / 1000));
            }

            @Override
            public void onFinish() {

                // Oyun bitince timeText ile Game Over yazdırıyoruz ve runnableImagesVisibility
                // thread'ini iptal ediyoruz. Ardından Bir AlertDialog oluşturarak kullanıcıdan oyunu
                // tekrar başlatmak isteyip istemediğini soruyoruz.
                timeText.setText("Game Over !");
                gameHandler.removeCallbacks(runnableImagesVisibility);
                hideImages();

                AlertDialog.Builder finishedAlertDialog = new AlertDialog.Builder(MainActivity.this);
                finishedAlertDialog.setTitle("Restart ?");
                finishedAlertDialog.setMessage("Are you sure to restart game ? ");

                finishedAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Mevcut activity'i tekrar başlatmamızı sağlıyor.
                        startActivity(restartIntent);
                        finish();
                    }
                });

                finishedAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Game Over ! Score : " + scoreValue, Toast.LENGTH_SHORT).show();
                    }
                });

                finishedAlertDialog.show();

            }
        };

        countDownTimer.start();

    }



}