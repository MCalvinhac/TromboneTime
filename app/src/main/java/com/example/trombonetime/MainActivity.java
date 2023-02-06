package com.example.trombonetime;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Chronometer;
import android.view.View;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Calendar;

import android.content.ClipboardManager;


public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    private Chronometer chronometertrombone;
    private long pauseOffsettrombone;
    private boolean runningtrombone;





    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometertrombone = findViewById(R.id.chronometertrombone);
        chronometertrombone.setFormat("%s");
        chronometertrombone.setBase(SystemClock.elapsedRealtime());

        int[] state_start = {-1};
        final AtomicBoolean[] state_trombone = {new AtomicBoolean(false)};
        Button start = findViewById(R.id.start);
        Button reset = findViewById(R.id.reset);
        Button export = findViewById(R.id.export);
        ImageButton trombone = findViewById(R.id.trombone);
        TextView pourcent = findViewById(R.id.pourcentage);


        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int stoppedMillisecondp = 0;
                                int stoppedMillisecondtrbp = 0;
                                String chronotrbtxtp = chronometertrombone.getText().toString();
                                String chronotxtp = chronometer.getText().toString();
                                String arraytrbp[] = chronotrbtxtp.split(":");
                                String arrayp[] = chronotxtp.split(":");
                                String timepass = "";
                                String trbpass = "";
                                if(arrayp.length == 2) {
                                    stoppedMillisecondp = Integer.parseInt(arrayp[0]) * 60 * 1000 + Integer.parseInt(arrayp[1]) * 1000;
                                }else if(arrayp.length ==3) {
                                    stoppedMillisecondp = Integer.parseInt(arrayp[0]) * 60 * 60 * 1000 + Integer.parseInt(arrayp[1]) * 60 * 1000 + Integer.parseInt(arrayp[2]) * 1000;
                                }
                                if(arraytrbp.length == 2) {
                                    stoppedMillisecondtrbp = Integer.parseInt(arraytrbp[0]) * 60 * 1000 + Integer.parseInt(arraytrbp[1]) * 1000;
                                }else if(arraytrbp.length ==3) {
                                    stoppedMillisecondtrbp = Integer.parseInt(arraytrbp[0]) * 60 * 60 * 1000 + Integer.parseInt(arraytrbp[1]) * 60 * 1000 + Integer.parseInt(arraytrbp[2]) * 1000;
                                }
                                if(stoppedMillisecondp != 0){
                                    double ratio = ((double) stoppedMillisecondtrbp / (double) stoppedMillisecondp) * 100;
                                    DecimalFormat df = new DecimalFormat("00.00");
                                    String ratiotxtp = df.format(ratio);
                                    double percentage = ratio/100;
                                    if (percentage > 1) {
                                        percentage = 1;
                                    }
                                    else if (percentage < 0) {
                                        percentage = 0;
                                    }
                                    int red = (int)(255.0 * (1 - percentage));
                                    int green = (int)(255.0 * (percentage));
                                    pourcent.setTextColor(Color.rgb(red,green,0));
                                    pourcent.setText(ratiotxtp+"%");
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();

        export.setOnClickListener(view ->{
            int stoppedMillisecond = 0;
            int stoppedMillisecondtrb = 0;
            String chronotrbtxt = chronometertrombone.getText().toString();
            String chronotxt = chronometer.getText().toString();
            String arraytrb[] = chronotrbtxt.split(":");
            String array[] = chronotxt.split(":");
            String timepass = "";
            String trbpass = "";
            if(array.length == 2) {
                stoppedMillisecond = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
                timepass = array[0]+" minute(s) et "+array[1]+" seconde(s)";
            }else if(array.length ==3) {
                stoppedMillisecond = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
                timepass = array[0]+"heure(s) "+array[1]+" minute(s) et "+array[2]+" seconde(s)";
            }
            if(arraytrb.length == 2) {
                stoppedMillisecondtrb = Integer.parseInt(arraytrb[0]) * 60 * 1000 + Integer.parseInt(arraytrb[1]) * 1000;
                trbpass = arraytrb[0]+" minute(s) et "+arraytrb[1]+" seconde(s)";
            }else if(arraytrb.length ==3) {
                stoppedMillisecondtrb = Integer.parseInt(arraytrb[0]) * 60 * 60 * 1000 + Integer.parseInt(arraytrb[1]) * 60 * 1000 + Integer.parseInt(arraytrb[2]) * 1000;
                trbpass = arraytrb[0]+"heure(s) "+arraytrb[1]+" minute(s) et "+arraytrb[2]+" seconde(s)";
            }
            String retour = "Pour la répétition du "+new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())+" :\n";
            if(stoppedMillisecond == 0){
                retour = retour + "Vous n'avez pas joué.\n";
            }else {
                double ratio = ((double) stoppedMillisecondtrb / (double) stoppedMillisecond) * 100;
                DecimalFormat df = new DecimalFormat("00.00");
                String ratiotxt = df.format(ratio);
                retour = retour + "Vous avez joué " + ratiotxt + "% du temps\n";
                retour = retour + "Soit une durée de " + trbpass + ".\n";
                retour = retour + "Pour une répétition d'une durée de " + timepass + ".\n";
            }
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Résultat TrbTime",retour);
            clipboard.setPrimaryClip(clip);
        });

        start.setOnClickListener(view -> {
            if(state_start[0] < 0){
                state_start[0] = -state_start[0];
                startChronometer(view);
                start.setText("STOP");
                start.setBackgroundColor(Color.parseColor("#F44336"));
                trombone.setBackground(trombone.getResources().getDrawable(R.drawable.circle_button_red));

            } else if (state_start[0] > 0) {
                state_start[0] = -state_start[0];
                pauseChronometer(view);
                pauseChronometertrb(view);
                start.setText("REPRENDRE");
                start.setBackgroundColor(Color.parseColor("#00EBD3"));
                trombone.setBackground(trombone.getResources().getDrawable(R.drawable.circle_button_blue));
                if(state_trombone[0].get() == true){
                    pauseChronometertrb(view);
                    state_trombone[0].set(false);
                }
            }else{
                resetChronometer(view);
                resetChronometertrb(view);
                start.setText("démarrer");
                start.setBackgroundColor(Color.parseColor("#009688"));
            }
        });

        reset.setOnClickListener(view -> {
            resetChronometer(view);
            pauseChronometer(view);
            resetChronometertrb(view);
            pauseChronometertrb(view);
            state_start[0] = -1;
            start.setText("démarrer");
            pourcent.setText("00,00%");
            pourcent.setTextColor(Color.parseColor("#838383"));
            start.setBackgroundColor(Color.parseColor("#009688"));
            state_trombone[0].set(false);
            trombone.setBackground(trombone.getResources().getDrawable(R.drawable.circle_button_blue))
            ;

        });

        trombone.setOnClickListener(view -> {
            if(state_start[0] > 0){
                if(state_trombone[0].get() == false){
                    state_trombone[0].set(true);
                    startChronometertrb(view);
                    trombone.setBackground(trombone.getResources().getDrawable(R.drawable.circle_button_green))
                    ;
                }else{
                    state_trombone[0].set(false);
                    pauseChronometertrb(view);
                    trombone.setBackground(trombone.getResources().getDrawable(R.drawable.circle_button_red))
                    ;
                }
            }
        });


    }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void Chronodiff(View v){
        long chrono_passe = SystemClock.elapsedRealtime() - chronometer.getBase();
        long chronotrombone_passe = SystemClock.elapsedRealtime() - chronometertrombone.getBase();
        System.out.println("TIME: "+chrono_passe);
        System.out.println("TRBNE: "+chronotrombone_passe);
        double r = chronotrombone_passe/chrono_passe;
        System.out.println("RATIO: "+100*r+"%");
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void startChronometertrb(View v) {
        if (!runningtrombone) {
            chronometertrombone.setBase(SystemClock.elapsedRealtime() - pauseOffsettrombone);
            chronometertrombone.start();
            runningtrombone = true;
        }
    }

    public void pauseChronometertrb(View v) {
        if (runningtrombone) {
            chronometertrombone.stop();
            pauseOffsettrombone = SystemClock.elapsedRealtime() - chronometertrombone.getBase();
            runningtrombone = false;
        }
    }

    public void resetChronometertrb(View v) {
        chronometertrombone.setBase(SystemClock.elapsedRealtime());
        pauseOffsettrombone = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}