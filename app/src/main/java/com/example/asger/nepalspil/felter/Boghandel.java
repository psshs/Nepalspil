package com.example.asger.nepalspil.felter;

import android.content.res.AssetFileDescriptor;
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
import com.example.asger.nepalspil.activities.SpillePlade;

import java.io.IOException;

import static com.example.asger.nepalspil.models.Spiller.instans;

public class Boghandel extends AppCompatActivity {
    final int MONEY_PER_CLICK = 20;
    final int TIME_PER_CLICK = 1;

    @Override
    public void onBackPressed() {
        SpillePlade.updateEntireBoard();
        finish();
    }

    private Animation animation;
    private Topbar topbar;

    AlertDialog.Builder dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boghandel);

        topbar = new Topbar();
        topbar.init(this);

        dialog = new AlertDialog.Builder(Boghandel.this);
        final TextView bookstoreInfo = (TextView) findViewById(R.id.fieldinfo);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.cash);
        ImageView helpField = (ImageView) findViewById(R.id.vaerkstedHelp);
        Button work = (Button) findViewById(R.id.workButton);
        final Button buyBook = (Button) findViewById(R.id.buyBookButton);
        ImageView hjemBack = (ImageView) findViewById(R.id.hjemBack);
        final TextView money = (TextView) findViewById(R.id.scrollmoney);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plusknowledge);
        ImageView menu = (ImageView) findViewById(R.id.menuknap);
        menu.setVisibility(View.INVISIBLE);

        bookstoreInfo.setText("I boghandlen kan du købe skolebøger. Skolebøger giver mere viden. \n Du kan også få et job i boghandlen, hvis du har gået i skole længe nok.");
        updateText();
        work.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Boghandel.this, R.anim.image_click));
                if (instans.getTid() >= TIME_PER_CLICK && instans.getKlassetrin() >= 6) {
                    money.setText("+" + MONEY_PER_CLICK + " kr");
                    money.startAnimation(animation);
                    instans.work(TIME_PER_CLICK, MONEY_PER_CLICK);

                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    try {
                        mp.reset();
                        AssetFileDescriptor afd;
                        afd = getAssets().openFd("cash.mp3");
                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        mp.prepare();
                        mp.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    updateText();
                } else if (instans.getTid() < 2) {

                    dialog.setTitle("Intet tid!");
                    dialog.setMessage("Du har ikke nok tid til at arbejde");
                    dialog.show();

                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Boghandel.this);
                    dialog.setTitle("Ikke nok viden!");
                    dialog.setMessage("Du har ikke uddannelse nok til at arbejde her");
                    dialog.show();
                }
            }
        });

        helpField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Boghandel.this, R.anim.image_click));
                dialog.setMessage("I boghandlen kan man købe skolebøger, som hjælper en med at få viden, hvis man har penge nok. Du kan også arbejde i boghandlen fra 6. klasse.");
                dialog.show();
            }
        });

        buyBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Boghandel.this, R.anim.image_click));
                if (instans.getPenge() >= 30 && instans.getLastBookBought() + 5 <= instans.getRunde()) {

                    instans.setViden(instans.getViden() + 10);
                    instans.setPenge(instans.getPenge() - 30);
                    dialog.setTitle("Bog købt");
                    dialog.setMessage("Du har købt en ny bog for 30 penge. +10 viden");
                    dialog.show();
                    instans.setLastBookBought(instans.getRunde());
                    updateText();
                } else if (instans.getPenge() < 30) {
                    dialog.setTitle("Du mangler penge!");
                    dialog.setMessage("Bogen koster 30 kroner, men du har kun " + instans.getPenge());
                    dialog.show();
                } else if (!(instans.getLastBookBought() + 5 < instans.getRunde())) {
                    dialog.setTitle("Boghandlen har ikke den bog du vil have");
                    dialog.setMessage("Kig tilbage på et andet tidspunkt");
                    dialog.show();
                }
            }
        });

        hjemBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SpillePlade.updateEntireBoard();
                v.startAnimation(AnimationUtils.loadAnimation(Boghandel.this, R.anim.image_click));
                finish();
            }
        });

    }


    public void updateText() {
        topbar.opdaterGui(instans);
        SpillePlade.updateEntireBoard();
    }

}
