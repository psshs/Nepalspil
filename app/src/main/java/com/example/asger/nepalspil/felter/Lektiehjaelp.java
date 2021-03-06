package com.example.asger.nepalspil.felter;

import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asger.nepalspil.R;
import com.example.asger.nepalspil.diverse.Topbar;
import com.example.asger.nepalspil.model.Spiller;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.asger.nepalspil.model.Spiller.instans;

public class Lektiehjaelp extends AppCompatActivity {
    //Studying
    final int VIDEN_PER_CLICK = 1;
    final int TIME_PER_CLICK = 1;

    Button homeworkHelp;
    AlertDialog.Builder dialog;
    private Animation animation;
    private Topbar topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Spiller.instans==null) { finish(); return; } // genstart i frisk JVM - vis hovedmenu
        setContentView(R.layout.lektiehjaelp);

        topbar = new Topbar();
        topbar.init(this);

        dialog = new AlertDialog.Builder(Lektiehjaelp.this);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.cash);
        final TextView lektiehjaelpInfo = (TextView) findViewById(R.id.lektiehjaelpTextField);
        ImageView hjemBack = (ImageView) findViewById(R.id.ikon_tilbage);
        homeworkHelp = (Button) findViewById(R.id.knap_lektiehjaelp);
        ImageView helpField = (ImageView) findViewById(R.id.vaerkstedHelp);
        final TextView scrollknowledge = (TextView) findViewById(R.id.scrollknowledge);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plusknowledge);
        ImageView menu = (ImageView) findViewById(R.id.menuknap);
        menu.setVisibility(View.INVISIBLE);

        Typeface face;
        face = Typeface.createFromAsset(getAssets(), "fonts/EraserDust.ttf");
        lektiehjaelpInfo.setTypeface(face);
        lektiehjaelpInfo.setText("Her kan du få lektiehjælp for at indhente det, du ikke forstod i timerne.");
        topbar.opdaterGui(instans);
        homeworkHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Lektiehjaelp.this, R.anim.image_click));
                if (instans.tid >= TIME_PER_CLICK && instans.glemtViden > 0) {
                    scrollknowledge.setText("+" + VIDEN_PER_CLICK + " viden");
                    scrollknowledge.startAnimation(animation);
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
                    learn();
                } else if (instans.glemtViden <= 0) {
                    new SweetAlertDialog(Lektiehjaelp.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Ingen glemt viden.")
                            .setContentText("Du har ikke behov for lektiehjælp, da du forstået al undervisning.")
                            .show();
                } else if (instans.tid <= TIME_PER_CLICK) {
                    new SweetAlertDialog(Lektiehjaelp.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Ikke tid nok!")
                            .setContentText("Du har ikke nok tid til at få lektiehjælp. Kom igen i morgen.")
                            .show();
                }

            }
        });

        helpField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Lektiehjaelp.this, R.anim.image_click));
                dialog.setMessage("Tit er det rigtig svært at forstå, hvad lærerne forklarer og dine forældre kan ikke hjælpe med lektierne. Så derfor har du brug for lektiehjælp.");
                dialog.show();
            }
        });
        hjemBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Lektiehjaelp.this, R.anim.image_click));
                finish();
            }
        });

    }

    private void learn() {
        instans.studér(TIME_PER_CLICK, VIDEN_PER_CLICK);
        instans.glemtViden = instans.glemtViden - 1;
        topbar.opdaterGui(instans);
    }
}