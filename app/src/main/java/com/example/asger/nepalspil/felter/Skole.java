package com.example.asger.nepalspil.felter;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.asger.nepalspil.R;
import com.example.asger.nepalspil.activities.SpillePlade;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static com.example.asger.nepalspil.activities.MainActivity.spiller;


public class Skole extends AppCompatActivity {

    private Animation animation;
    private Animation animationfood;
    static TextView textpenge;
    static TextView textviden;
    static TextView textmad;
    static TextView playerInfo;

    @Override
    public void onBackPressed() {
        SpillePlade.updateEntireBoard();
        finish();
    }

    TextView schoolText;

    AlertDialog.Builder dialog;

    @Override
    public void onResume() {
        super.onResume();
       // updateInfo();

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skole);


        final MediaPlayer mp = MediaPlayer.create(this, R.raw.cash);
        final TextView schoolText = (TextView) findViewById(R.id.schoolText);
        playerInfo = (TextView) findViewById(R.id.schoolPlayerInfo);
        final TextView klassetrin = (TextView) findViewById(R.id.klassetrin);
        Button bSpis = (Button) findViewById(R.id.spis);
        Button bStuder = (Button) findViewById(R.id.Studer);
        Button bEksamen = (Button) findViewById(R.id.eksamen);
        ImageView back = (ImageView) findViewById(R.id.skoleBack);
        ImageView helpField = (ImageView) findViewById(R.id.skoleHelp);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plusknowledge);
        animationfood = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plusfood);
        final TextView scroll = (TextView) findViewById(R.id.plusknowledge);
        final TextView mad = (TextView) findViewById(R.id.scrollfood);
        textpenge = (TextView) findViewById(R.id.textpenge);
        textviden = (TextView) findViewById(R.id.textviden);
        textmad = (TextView) findViewById(R.id.textmad);
        updateText();

        dialog = new AlertDialog.Builder(Skole.this);
        schoolText.setText("Velkommen til Skolen, her kan du spise, studere og tage din eksamen når tiden er.");
        playerInfo.setText("Tid: " + spiller.getTid());
        klassetrin.setText("Du går i " + spiller.getKlassetrin() + ". klasse.");
        if (spiller.getKlassetrin()>=12){
            bEksamen.setVisibility(View.INVISIBLE);
        }
        onResume();
        {

        }


        helpField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Skolen");
                dialog.setMessage("I skolen kan du studere og få en smule mad. Tryk på knapperne for at studere eller spise.\n Når du har gået nok i skole kan du tage din eksamen for at komme op i næste klasse. Alt undervisning er svær, så husk derfor at benyt lektiehjælpen, hvis du ikke forstår det hele. .");
                dialog.show();
            }
        });
        bSpis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spiller.getTid() > 0) {
                    mad.setText("+5 mad");
                    mad.startAnimation(animationfood);
                    spis();
                    schoolText.setText("Mmm! Du har spist skolemad.");
                    playerInfo.setText(updateInfo());
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    try {
                        mp.reset();
                        AssetFileDescriptor afd;
                        afd = getAssets().openFd("bitesound.mp3");
                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        mp.prepare();
                        mp.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Skole.this);
                    dialog.setTitle("Ikke nok tid!");
                    dialog.setMessage("Du har ikke nok tid til at spise");
                    dialog.show();
                }
            }
        });

        bStuder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int thisStudy = studer();
                if (spiller.getTid() > 0) {
                    if (spiller.getTid() > 0 && thisStudy == 1) {
                        scroll.setText("+1 viden");
                        scroll.startAnimation(animation);
                        spiller.study(1, 1);
                        updateText();
                        if (mp.isPlaying()) {
                            mp.stop();
                        }
                        try {
                            mp.reset();
                            AssetFileDescriptor afd;
                            afd = getAssets().openFd("study.mp3");
                            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                            mp.prepare();
                            mp.start();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(spiller.getViden());
                    } else if (thisStudy == 2) {
                        Toast.makeText(Skole.this, "Du forstod ikke alt undervisningen, tag i lektiehjælpen for at forstå det", Toast.LENGTH_SHORT).show();
                        spiller.study(1, 0);
                        updateText();
                    } else if (thisStudy == 3) {
                        Toast.makeText(Skole.this, "Du forstod ingenting af denne lektion", Toast.LENGTH_SHORT).show();
                        spiller.study(1, 0);
                        updateText();
                    }
                } else {

                    dialog.setTitle("Ikke nok tid!");
                    dialog.setMessage("Du har ikke nok tid til at studere.");
                    dialog.show();
                }
            //    playerInfo.setText(updateInfo());
            }
        });

        bEksamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kanStartEksamen()) {
                    schoolText.setText("Held og Lykke!");
                    finish();
                    Intent myIntent = new Intent(Skole.this, Eksamen.class);
                    startActivity(myIntent);

                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Skole.this);
                    dialog.setTitle("Ikke nok viden!");
                    dialog.setMessage("Du har ikke nok viden til at starte eksamenen! Du skal have mindst " + vidensKrav() + " for at starte eksamenen.");
                    dialog.show();

                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SpillePlade.updateEntireBoard();
                v.startAnimation(AnimationUtils.loadAnimation(Skole.this, R.anim.image_click));
                finish();


            }
        });



    }

    //public static int vidensKrav = 10 * spiller.getKlassetrin();


    public int studer() {
        int result = 0;
        switch (spiller.getLearningAmp()) {

            case 0:
                result = tryToStudy(0.5, 0.25, 0);
                Log.d("Spil", "Spiller studied with 0 learning Amp");
                break;
            case 1:
                result = tryToStudy(0.45, 0.2, 0);
                Log.d("Spil", "Spiller studied with 1 learning Amp");
                break;
            case 2:
                result = tryToStudy(0.40, 0.15, 0);
                Log.d("Spil", "Spiller studied with 2 learning Amp");
                break;
            case 3:
                result = tryToStudy(0.30, 0, 0);
                Log.d("Spil", "Spiller studied with 3 learning Amp");
                break;


        }
        return result;
    }


    public void spis() {
        if (spiller.getTid() > 0) {
            spiller.eat(1, 0, 5);
            SpillePlade.updateInfobox();
            updateText();
        } else {
            schoolText.setText("");
        }

    }

    public boolean kanStartEksamen() {
        if ((spiller.getViden() >= vidensKrav())) {
            return true;
        } else
            return false;
    }

    public static String updateInfo() {
        SpillePlade.updateInfobox();
        return "Tid: " + spiller.getTid();

    }


    public static int vidensKrav() {
        switch(spiller.getKlassetrin()){
            case 1:
                return 10;
            case 2:
               return 40;
            case 3:
                return 90;
            case 4:
                return 110;
            case 5:
                return 160;
            case 6:
                return 220;
            case 7:
                return 300;
            case 8:
                return 400;
            case 9:
                return 500;
            case 10:
                return 700;
            case 11:
                return 800;


        }
        return 10 * spiller.getKlassetrin();
    }

    public int tryToStudy(double success, double homework, double fail) {
        double rand = Math.random();
        Log.d("Spil", "rand = " + rand + " Success = " + success + " homework = " + homework + "fail = " + fail);
        if (success <= rand && rand < 1) return 1;
        else if (rand < success && rand >= homework) return 2;
        else if (rand < homework && rand >= fail) return 3;
        else return 0;
    }
    public static void updateText() {
        textpenge.setText(String.valueOf(spiller.getPenge()));
        textviden.setText(String.valueOf(spiller.getViden()));
        textmad.setText(String.valueOf(spiller.getHp()));
        playerInfo.setText("Tid: " + spiller.getTid());
    }



}
