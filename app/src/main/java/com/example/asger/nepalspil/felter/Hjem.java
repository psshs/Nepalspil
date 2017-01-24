package com.example.asger.nepalspil.felter;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asger.nepalspil.R;
import com.example.asger.nepalspil.activities.SpillePlade;

import static com.example.asger.nepalspil.models.Spiller.instans;


public class Hjem extends AppCompatActivity {
    TextView textpenge;
    TextView textviden;
    TextView textmad;
    TextView texttid;


    @Override
    public void onBackPressed() {
        SpillePlade.updateEntireBoard();
        finish();
    }

    AlertDialog.Builder dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hjem);

        ImageView im = (ImageView) findViewById(R.id.hjemprofile);
        dialog = new AlertDialog.Builder(Hjem.this);
        textpenge = (TextView) findViewById(R.id.textpenge);
        textviden = (TextView) findViewById(R.id.textviden);
        textmad = (TextView) findViewById(R.id.textmad);
        texttid = (TextView) findViewById(R.id.texttid);
        updateText();
        ImageView hjemBack = (ImageView) findViewById(R.id.hjemBack);
        // ImageView hjemhelp = (ImageView) findViewById(R.id.hjemhelp);
        TextView spillerintro = (TextView) findViewById(R.id.spillerintro);
        spillerintro.setText("Hej jeg hedder " + instans.getNavn() + ". Jeg bor med min familie i en landsby i Nepal. \nMin mor og far har aldrig gået i skole, så de tjener ikke så mange penge, så det er svært for dem at hjælpe mig med at få en uddannelse.");

        if (instans.getSex() == false) {
            im.setImageResource(R.drawable.pige2);
        } else {
            im.setImageResource(R.drawable.dreng1);
        }


        hjemBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SpillePlade.updateEntireBoard();
                v.startAnimation(AnimationUtils.loadAnimation(Hjem.this, R.anim.image_click));
                finish();
            }
        });

      /*  hjemhelp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SpillePlade.updateEntireBoard();
                v.startAnimation(AnimationUtils.loadAnimation(Hjem.this, R.anim.image_click));
                dialog.setMessage("Hej jeg hedder " + instans.getNavn() + ". Jeg bor med min familie i en landsby i Nepal. \n Min mor og far har aldrig gået i skole, så de tjener ikke så mange penge, så det er svært for dem at hjælpe mig med at få en uddannelse.");
                dialog.show();
            }
        });*/


    }

    public String updateInfo() {
        return "Navn: " + instans.getNavn() + "\n mad: " + instans.getHp() + "\n Penge: " + instans.getPenge() + "\n Viden: " + instans.getViden() + "\n Klassetrin: " + instans.getKlassetrin() + "\n Tid: " + instans.getTid();
    }

    public void updateText() {
        textpenge.setText(String.valueOf(instans.getPenge()));
        textviden.setText(String.valueOf(instans.getViden()));
        textmad.setText(String.valueOf(instans.getHp()));
        texttid.setText(String.valueOf(instans.getTid()));

    }
}
