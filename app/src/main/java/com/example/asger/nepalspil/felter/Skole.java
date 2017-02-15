package com.example.asger.nepalspil.felter;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asger.nepalspil.R;
import com.example.asger.nepalspil.diverse.Topbar;
import com.example.asger.nepalspil.model.Spiller;

import cn.pedant.SweetAlert.SweetAlertDialog;



public class Skole extends AppCompatActivity {

    private Animation animation;
    private Animation animationfood;

    private Topbar topbar;


    //Studying
    final int VIDEN_PER_CLICK = 1;
    final int TIME_PER_CLICK = 1;

    //Eating
    final int FOOD_PER_CLICK = 10;
    final int COST_PER_FOOD_CLICK = 0;
    final int TIME_COST_EATING = 1;

    TextView taleboble_tekst;

    AlertDialog.Builder dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skole);

        topbar = new Topbar();
        topbar.init(this);
        ImageView figur = (ImageView) findViewById(R.id.figur);
        figur.setImageResource(Spiller.instans.figurdata.drawable_figur_hel_id);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.cash);
        taleboble_tekst = (TextView) findViewById(R.id.taleboble_tekst);
        final TextView klassetrin = (TextView) findViewById(R.id.klassetrin);
        Button bSpis = (Button) findViewById(R.id.knap_spis);
        Button bStuder = (Button) findViewById(R.id.knap_studer);
        Button bEksamen = (Button) findViewById(R.id.eksamen);
        ImageView hjemBack = (ImageView) findViewById(R.id.ikon_tilbage);
        ImageView helpField = (ImageView) findViewById(R.id.vaerkstedHelp);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plusknowledge);
        animationfood = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plusknowledge);
        final TextView flyvoptekst_studer = (TextView) findViewById(R.id.flyvoptekst_studer);
        final TextView flyvoptekst_spis = (TextView) findViewById(R.id.flyvoptekst_spis);
        flyvoptekst_spis.setText("");
        flyvoptekst_studer.setText("");
        ImageView menu = (ImageView) findViewById(R.id.menuknap);
        menu.setVisibility(View.INVISIBLE);

        topbar.opdaterGui(Spiller.instans);

        Typeface face;
        face = Typeface.createFromAsset(getAssets(), "fonts/EraserDust.ttf");
        klassetrin.setTypeface(face);


        dialog = new AlertDialog.Builder(Skole.this);
        klassetrin.setText("Du går i " + Spiller.instans.klassetrin + ". klasse.");
        if (Spiller.instans.klassetrin >= 10) {
            bEksamen.setVisibility(View.INVISIBLE);
        }

        helpField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Skole.this, R.anim.image_click));
                dialog.setTitle("Skolen");
                dialog.setMessage(R.string.skole_hjælp);
                dialog.show();
            }
        });
        bSpis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Skole.this, R.anim.image_click));
                if (Spiller.instans.tid >= TIME_PER_CLICK) {
                    flyvoptekst_spis.setText("+" + FOOD_PER_CLICK + " mad");
                    flyvoptekst_spis.startAnimation(animationfood);
                    spis();
                    taleboble_tekst.setText("Mmm! Du har spist skolemad.");
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    new SweetAlertDialog(Skole.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Du har ikke tid til at spise.")
                            .show();
                }
                topbar.opdaterGui(Spiller.instans);
            }
        });

        bStuder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Skole.this, R.anim.image_click));
                int thisStudy = studer();
                if (Spiller.instans.tid >= TIME_PER_CLICK) {
                    if (thisStudy == STUDER_VIDEN) {
                        taleboble_tekst.setText("Du blev lidt klogere");
                        flyvoptekst_studer.setText("+" + VIDEN_PER_CLICK + " viden");
                        flyvoptekst_studer.startAnimation(animation);
                        Spiller.instans.studér(TIME_PER_CLICK, VIDEN_PER_CLICK);
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(Spiller.instans.viden);
                    } else if (thisStudy == STUDER_LEKTIEHJÆLP) {
                        taleboble_tekst.setText("Du forstod det ikke, lektiehjælp kunne måske hjælpe");
                        flyvoptekst_studer.setText("+1 lektiehjælp");
                        flyvoptekst_studer.startAnimation(animation);
                        Spiller.instans.studér(TIME_PER_CLICK, 0);
                        Spiller.instans.glemtViden = Spiller.instans.glemtViden + 1;
                    } else if (thisStudy == STUDER_FORSTOD_IKKE) {
                        taleboble_tekst.setText("Du forstod ikke denne lektion");
                        Spiller.instans.studér(TIME_PER_CLICK, 0);
                    } else if (thisStudy == STUDER_FORSTOD_IKKE) {
                        taleboble_tekst.setText("Din XXX gik i stykker!");
                        try {
                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Skole.this, "Kunne ikke vibrere med telefonen:\n" + e, Toast.LENGTH_LONG).show();
                            Toast.makeText(Skole.this, "Har du husket:\n<uses-permission android:name=\"android.permission.VIBRATE\"/>\n i manifestet?", Toast.LENGTH_LONG).show();
                        }
                        Spiller.instans.studér(TIME_PER_CLICK, 0);
                    }
                } else {

                    new SweetAlertDialog(Skole.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Du har ikke tid til at studere.")
                            .show();
                }
                topbar.opdaterGui(Spiller.instans);
            }
        });

        bEksamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Skole.this, R.anim.image_click));
                if (kanStartEksamen()) {
                    taleboble_tekst.setText("Held og Lykke!");
                    finish();
                    Intent myIntent = new Intent(Skole.this, Eksamen.class);
                    startActivity(myIntent);

                } else {
                    new SweetAlertDialog(Skole.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Ikke nok viden!")
                            .setContentText("Du har ikke nok viden til at starte eksamen! Du skal have mindst " + vidensKrav() + " viden for at starte eksamen.")
                            .show();

                }

            }
        });

        hjemBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Skole.this, R.anim.image_click));
                finish();


            }
        });


    }

    public static int STUDER_VIDEN = 1;
    public static int STUDER_LEKTIEHJÆLP = 2;
    public static int STUDER_FORSTOD_IKKE = 3;
    public static int STUDER_REDSKAB_BRUGT_OP = 4;

    /**
     * Denne her metode er ret uforståelig - forklar hvad der sker.
     * @param studer_viden_sands
     * @param studer_lektiehjælp_sands
     * @return
     */
    public int tryToStudy(double studer_viden_sands, double studer_lektiehjælp_sands) {
        double rand = Math.random();
        Log.d("Spil", "rand = " + rand + " viden_sands = " + studer_viden_sands + " lektiehjælp_sands = " + studer_lektiehjælp_sands);
        if (rand < studer_viden_sands) return STUDER_VIDEN;
        else if (rand < studer_lektiehjælp_sands) return STUDER_LEKTIEHJÆLP;
        return STUDER_FORSTOD_IKKE;
    }


    // XXX
    public int studer() {
        int result = 0;
        switch (Spiller.instans.læringsfart) {

            case 0:
                result = tryToStudy(0.50, 0.75); // 50% chance for at lære noget, 25% for lektiehjælp, 25% for ikke af forstå noget
                Log.d("Spil", "Spiller studied with 0 learning Amp");
                break;
            case 1:
                result = tryToStudy(0.55, 0.80); // 55% chance for at lære noget, 25% for lektiehjælp, 20% for ikke af forstå noget
                Log.d("Spil", "Spiller studied with 1 learning Amp");
                break;
            case 2:
                result = tryToStudy(0.60, 0.85); // 60% chance for at lære noget, 25% for lektiehjælp, 15% for ikke af forstå noget
                Log.d("Spil", "Spiller studied with 2 learning Amp");
                break;
            case 3:
                result = tryToStudy(0.70, 1.00); // 70% chance for at lære noget, 30% for lektiehjælp
                Log.d("Spil", "Spiller studied with 3 learning Amp");
                break;


        }
        return result;
    }


    public void spis() {
        if (Spiller.instans.tid > 0) {
            Spiller.instans.spis(TIME_COST_EATING, COST_PER_FOOD_CLICK, FOOD_PER_CLICK);
            topbar.opdaterGui(Spiller.instans);
        } else {
            taleboble_tekst.setText("");
        }

    }

    // XXX
    public boolean kanStartEksamen() {
        if ((Spiller.instans.viden >= vidensKrav())) {
            return true;
        } else
            return false;
    }


    // XXX
    public static int vidensKrav() {
        switch (Spiller.instans.klassetrin) {
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
        return 10 * Spiller.instans.klassetrin;
    }
}
