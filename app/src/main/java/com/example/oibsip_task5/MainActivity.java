package com.example.oibsip_task5;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private boolean running;
    private long startTime;
    private long elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.start);
        Button stopButton = findViewById(R.id.stop);
        Button holdButton = findViewById(R.id.hold);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hold();
            }
        });
    }

    private void start() {
        if (!running) {
            startTime = System.currentTimeMillis();
            running = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running) {
                        elapsedTime = System.currentTimeMillis() - startTime;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTime();
                            }
                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
    private void stop() {
        running = false;
    }
    private void hold() {
        if (running) {
            running = false;
            elapsedTime = System.currentTimeMillis() - startTime;
        } else {
            startTime = System.currentTimeMillis() - elapsedTime;
            running = true;
        }
    }
    private void updateTime() {
        TextView time_text_update = findViewById(R.id.time_text);
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes);
        long milliseconds = elapsedTime - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds);
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", hours,minutes, seconds, milliseconds);
        time_text_update.setText(formattedTime);
    }
}