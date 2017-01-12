package com.example.asger.nepalspil.felter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import static com.example.asger.nepalspil.activities.MainActivity.spiller;
import com.example.asger.nepalspil.R;
import com.example.asger.nepalspil.activities.MainActivity;
import com.example.asger.nepalspil.models.SpillePlade;
import com.example.asger.nepalspil.models.Spiller;
import com.github.jinatonic.confetti.CommonConfetti;

import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Senad on 1/12/2017.
 */

public class Eksamen extends AppCompatActivity {
    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    FrameLayout container;



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Er du sikker på du vil forlade eksamen? Det koster dig " + Skole.vidensKrav() +  " viden.")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Eksamen.this.finish();
                        spiller.setViden(spiller.getViden()-10*spiller.getKlassetrin());
                        if(spiller.getViden()<0){
                            spiller.setViden(0);
                        }
                    }
                })
                .setNegativeButton("Nej", null)
                .show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eksamen);

        question = (TextView) findViewById(R.id.examQuestion);
        answer1 = (Button) findViewById(R.id.answer1Button);
        answer2 = (Button) findViewById(R.id.answer2Button);
        answer3 = (Button) findViewById(R.id.answer3Button);
        container = (FrameLayout) findViewById(R.id.container);



        spiller.getViden();
        switch(spiller.getKlassetrin()-1){
            case 0:
                setQuestion("Hvad spiser nepalesiske børn til morgenmad?","Ris og grøntsager","Havregryn med mælk","Grød");
                setSecondCorrect();
                break;
            case 1:
                setQuestion("Hvorfor er det godt, at børnene får skolemad?","Så kan de bedre koncentrere sig i timerne.","Så behøver de ikke selv tage mad med.","Så føles dagen længere.");
                setFirstCorrect();
                break;
            case 2:
                setQuestion("Hvilke ingredienser bruges blandt andet i nepalesisk skolemad?","Ris, løg, hvidløg og chili.","Brød og kød.","Nudler og chips.");
                setFirstCorrect();//?
                break;
            case 3:
                setQuestion("Hvad tid starter den offentlige skole i Nepal?","Kl. 10","Kl. 9","Kl. 8");
                setThirdCorrect(); //?
                break;
            case 4:
                setQuestion("Er der privatskoler i Nepal?","Ja, mange.","Nej, det har de ikke råd til.","Nogle få, men privatskoler er ikke almindeligt.");
                setThirdCorrect(); //?
                break;
            case 5:
                setQuestion("Hvem går i folkeskolen i Nepal?","De allerfattigste","De fleste","Alle er fattige og går i folkskolen.");
                setThirdCorrect(); //?
                break;
            case 6:
                setQuestion("Hvordan kan man bruge skolehaver i undervisningen?","Det er sjovere og lettere at lære noget, når man er udenfor.","Der er ikke lokaler nok til alle klasser.","Eleverne får mere mad, hvis de bliver undervist i haven.");
                setThirdCorrect(); //?
                break;
            case 7:
                setQuestion("Hvorfor er det vigtigt, at børn i Nepal lærer noget?","Fordi de skal have en uddannelse og et bedre liv.","Fordi de kommer til at kede sig derhjemme.","Fordi de ikke kan lide at være alene hjemme.");
                setThirdCorrect(); //?
                break;
            case 8:
                setQuestion("Hvorfor er der mange fattige børn i Nepal, som ikke laver lektier?","Deres forældre sætter dem ikke i gang","De behøver ikke lave lektier","Hindureligionen forbyder lektier");
                setThirdCorrect(); //?
                break;
            case 9:
                setQuestion("Hvor tit går børn i Nepal til eksamen?","Hvert år","Efter 9. klasse","Aldrig");
                setThirdCorrect(); //?
                break;
            case 10:
                setQuestion("Hvad skal børn i Nepal kunne til eksamen?","De skal kunne deres bøger udenad","De skal kunne tænke selvstændigt","De skal kunne forstå, hvad der står i bøgerne.");
                setThirdCorrect(); //?
                break;

        }
    }


    public void wrong(){

        spiller.setViden(spiller.getViden()-10*spiller.getKlassetrin());
        if(spiller.getViden()<0){
           spiller.setViden(0);
        }
        Skole.updateInfo();
        AlertDialog.Builder dialog = new AlertDialog.Builder(Eksamen.this);
        dialog.setMessage("Du har desværre svaret forkert på eksamen og er derfor dumpet. -"+ Skole.vidensKrav() +" viden" )
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SpillePlade.updateInfobox();
                        finish();
                    }
                });
        AlertDialog alert = dialog.create();
        alert.show();
    }

    public void setQuestion(String titel,String answerbox1, String answerbox2,String answerbox3)
    {
        question.setText(titel);
        answer1.setText(answerbox1);
        answer2.setText(answerbox2);
        answer3.setText(answerbox3);
    }
    public void setFirstCorrect(){
        answer1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(Eksamen.this, R.raw.tada);
                mp.start();
                CommonConfetti.rainingConfetti(container, new int[] { Color.BLACK })
                        .infinite();
                spiller.setKlassetrin(spiller.getKlassetrin()+1);
                AlertDialog.Builder dialog = new AlertDialog.Builder(Eksamen.this);
                dialog.setMessage("TILLYKKE!! Du bestod og går nu i "+spiller.getKlassetrin()+". klasse." )
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SpillePlade.updateInfobox();
                                finish();
                            }
                        });
                AlertDialog alert = dialog.create();
                alert.show();

            }
        });
        answer2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                wrong();
            }
        });
        answer3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                wrong();
            }
        });
    }


    public void setSecondCorrect(){
        answer1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                wrong();
            }
        });
        answer2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final MediaPlayer mp = MediaPlayer.create(Eksamen.this, R.raw.tada);
                mp.start();
                CommonConfetti.rainingConfetti(container, new int[] { Color.BLACK })
                        .infinite();
                spiller.setKlassetrin(spiller.getKlassetrin()+1);
                AlertDialog.Builder dialog = new AlertDialog.Builder(Eksamen.this);
                dialog.setMessage("TILLYKKE!! Du bestod og går nu i "+spiller.getKlassetrin()+". klasse." )
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SpillePlade.updateInfobox();
                                finish();
                            }
                        });
                AlertDialog alert = dialog.create();
                alert.show();

            }
        });
        answer3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                wrong();
            }
        });
    }


    public void setThirdCorrect(){
        answer1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                wrong();
            }
        });

        answer2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                wrong();
            }
        });
        answer3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final MediaPlayer mp = MediaPlayer.create(Eksamen.this, R.raw.tada);
                mp.start();
                CommonConfetti.rainingConfetti(container, new int[] { Color.BLACK })
                        .infinite();
                spiller.setKlassetrin(spiller.getKlassetrin()+1);
                AlertDialog.Builder dialog = new AlertDialog.Builder(Eksamen.this);
                dialog.setMessage("TILLYKKE!! Du bestod og går nu i "+spiller.getKlassetrin()+". klasse." )
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SpillePlade.updateInfobox();
                                finish();
                            }
                        });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }

}