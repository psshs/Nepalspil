package com.example.asger.nepalspil.felter;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asger.nepalspil.R;
import com.example.asger.nepalspil.models.SpillePlade;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.example.asger.nepalspil.activities.MainActivity.spiller;

/**
 * Created by Bruger on 03-01-2017.
 */

public class butik extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onBackPressed() {
        SpillePlade.updateEntireBoard();
        finish();
    }

    AlertDialog.Builder dialog;
    ViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toejbutik);

        dialog = new AlertDialog.Builder(butik.this);
        final TextView fieldinfo = (TextView) findViewById(R.id.tbInfo);
        final TextView playerinfo = (TextView) findViewById(R.id.tbStats);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.cash);
        ImageView helpField = (ImageView) findViewById(R.id.tbHelp);
        final Button buy = (Button) findViewById(R.id.tbBuy);
        ImageView back = (ImageView) findViewById(R.id.tbBack);


        /*
        if (spiller.getLearningAmp()==0){
            buy.setText("Køb papir");
        } else if (spiller.getLearningAmp()==1){
            buy.setText("Køb blyanter");
        } else if (spiller.getLearningAmp()==2){
            buy.setText("Køb Lommeregner");
        }
        */


        fieldinfo.setText("Velkommen til Tøjbutikken! Her kan man købe nyt skoletøj");
        playerinfo.setText("Navn: " + spiller.getNavn() + "\n mad: " + spiller.getHp() + "\n Penge: " + spiller.getPenge() + "\n Viden: " + spiller.getViden() + "\n Klassetrin: " + spiller.getKlassetrin() + "\n Tid: " + spiller.getTid());

        helpField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Velkommen til tøjbutikken. Her kan du købe tøj og diverse skoleting til at gøre dit live bedre.");
                dialog.show();
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spiller.getLearningAmp() == 0) {
                    if (spiller.getPenge() >= 30) {
                        buy();
                        playerinfo.setText(updateInfo());
                        AlertDialog.Builder dialog = new AlertDialog.Builder(butik.this);
                        dialog.setTitle("Papir købt");
                        dialog.setMessage("Du har købt en blok papir for 30 penge.");
                        dialog.show();
                        buy.setText("Køb blyanter");
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(butik.this);
                        dialog.setTitle("Ikke nok penge!");
                        dialog.setMessage("Du har ikke penge nok. Tjen penge ved at arbejde.");
                        dialog.show();
                    }
                }
                if (spiller.getLearningAmp()==1) {
                    if (spiller.getPenge() >= 60) {
                        buy();
                        playerinfo.setText(updateInfo());
                        AlertDialog.Builder dialog = new AlertDialog.Builder(butik.this);
                        dialog.setTitle("Blyanter købt");
                        dialog.setMessage("Du har købt nye blyanter for 60 penge.");
                        dialog.show();
                        buy.setText("Køb Lommeregner");
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(butik.this);
                        dialog.setTitle("Ikke nok penge!");
                        dialog.setMessage("Du har ikke penge nok. Tjen penge ved at arbejde.");
                        dialog.show();
                    }
                }
                if (spiller.getLearningAmp()==2) {
                    if (spiller.getPenge() >= 100) {
                        buy();
                        playerinfo.setText(updateInfo());
                        AlertDialog.Builder dialog = new AlertDialog.Builder(butik.this);
                        dialog.setTitle("Lommeregner købt");
                        dialog.setMessage("Du har købt en ny lommeregner for 100 penge.");
                        dialog.show();
                        buy.setVisibility(View.INVISIBLE);
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(butik.this);
                        dialog.setTitle("Ikke nok penge!");
                        dialog.setMessage("Du har ikke penge nok. Tjen penge ved at arbejde.");
                        dialog.show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SpillePlade.updateEntireBoard();
                v.startAnimation(AnimationUtils.loadAnimation(butik.this, R.anim.image_click));
                finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void buy() {
        if (spiller.getLearningAmp()==0){
            spiller.setLearningAmp(1);
            spiller.setPenge(spiller.getPenge()-30);
        } else if (spiller.getLearningAmp()==1){
            spiller.setLearningAmp(2);
            spiller.setPenge(spiller.getPenge()-60);
        } else if (spiller.getLearningAmp()==1){
            spiller.setLearningAmp(3);
            spiller.setPenge(spiller.getPenge()-100);
        }
    }

    public String updateInfo() {
        SpillePlade.updateInfobox();
        return "Navn: " + spiller.getNavn() + "\n mad: " + spiller.getHp() + "\n Penge: " + spiller.getPenge() + "\n Viden: " + spiller.getViden() + "\n Klassetrin: " + spiller.getKlassetrin() + "\n Tid: " + spiller.getTid();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("butik Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}