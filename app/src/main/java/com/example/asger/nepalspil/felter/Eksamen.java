package com.example.asger.nepalspil.felter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.asger.nepalspil.R;
import com.example.asger.nepalspil.models.Spiller;
import com.github.jinatonic.confetti.CommonConfetti;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.asger.nepalspil.models.Spiller.instans;

public class Eksamen extends AppCompatActivity {
    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    FrameLayout container;
    AlertDialog.Builder dialog;
    SharedPreferences prefs;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Er du sikker på du vil forlade eksamen? Det koster dig " + Skole.vidensKrav() + " viden.")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Eksamen.this.finish();
                        instans.setViden(instans.getViden() - 10 * instans.getKlassetrin());
                        if (instans.getViden() < 0) {
                            instans.setViden(0);
                        }
                    }
                })
                .setNegativeButton("Nej", null)
                .show();

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Er du sikker?")
                .setContentText("Hvis du forlader eksamen vil det koste dig " + Skole.vidensKrav() + " viden.")
                .setConfirmText("Ja")
                .showCancelButton(true)
                .setCancelText("Nej, jeg kan godt!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        finish();
                    }

                }).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eksamen);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        question = (TextView) findViewById(R.id.examQuestion);
        answer1 = (Button) findViewById(R.id.answer1Button);
        answer2 = (Button) findViewById(R.id.answer2Button);
        answer3 = (Button) findViewById(R.id.answer3Button);
        container = (FrameLayout) findViewById(R.id.container);
        dialog = new AlertDialog.Builder(Eksamen.this);

        Typeface face;
        face = Typeface.createFromAsset(getAssets(), "fonts/EraserDust.ttf");
        question.setTypeface(face);

        instans.getViden();
        switch (instans.getKlassetrin() - 1) {
            case 0:
                setQuestion("Hvad spiser nepalesiske børn til morgenmad?", "Havregryn med mælk", "Grød", "Ris og grøntsager");
                setThirdCorrect();
                eller
                setQuestion("Går nepalesiske skolebørn med skoleuniform?", "Nej, det har de ikke råd til", "Ja næsten alle har en skoleuniform", "Nogle skoler bruger uniform andre gør ikke");
                setSecondCorrect();
                break;
            case 1:
                setQuestion("Hvorfor er det godt, at børnene får skolemad?", "Så kan de bedre koncentrere sig i timerne", "Så behøver de ikke selv tage mad med.", "Så føles dagen længere.");
                setFirstCorrect();
                eller
                setQuestion("Er det gratis at gå i skole i Nepal?", "De fleste skoler opkræver skolepenge", "Det er altid gratis", "Det koster altid penge");
                setFirstCorrect();
                break;
            case 2:
                setQuestion("Hvilke ingredienser bruges blandt andet i nepalesisk skolemad?", "Brød og kød", "Ris, løg, hvidløg og chili", "Nudler og chips");
                setSecondCorrect();
                eller
                setQuestion("Hvilke kornsorter dyrker man i Nepal?", "byg og rug", "ris og hvede", "havre og quinoa");
                setSecondCorrect();
                break;
            case 3:
                setQuestion("Hvad tid starter den offentlige skole i Nepal?", "Kl. 10", "Kl. 9", "Kl. 8");
                setFirstCorrect();
                eller
                setQuestion("Hvad tid slutter skolen i Nepal", "Det er forskelligt", "Kl. 14", "kl. 16");
                setThirdCorrect();
                break;
            case 4:
                setQuestion("Er der privatskoler i Nepal?", "Ja, mange.", "Nej, det har de ikke råd til.", "Nogle få");
                setFirstCorrect();
                eller
                setQuestion("Hvor tit går børnene til eksamen i Nepal?", "Efter 3. klasse", "Efter hvert skoleår", "Efter 10. klasse");
                setSecondCorrect();
                break;
            case 5:
                setQuestion("Hvem går i folkeskolen i Nepal?", "cirka halvdelen", "Næsten alle","De fattige");
                setThirdCorrect();
                eller
                setQuestion("Er undervisningen i den nepalesiske folkeskole god nok", "Nej desværre er den tit dårlig", "Ja da alle skolerne er vildt gode", "De fleste er gode");
                setFirstCorrect();
                break;
            case 6:
                setQuestion("Hvorfor er det vigtigt, at børn i Nepal lærer noget?", "Fordi de skal have en uddannelse og et bedre liv.", "Fordi de kommer til at kede sig derhjemme.", "Fordi de ikke kan lide at være alene hjemme");
                setFirstCorrect();
                break;
                eller
                setQuestion("Kan man tage en uddannelse i Nepal, hvis man ikke har bestået 10. klasse?", "Ja, det er let nok", "Kun hvis man spørger pænt", "Nej, man skal have 10. klasse for at få en uddannelse");
                setSecondCorrect();
            case 7:
                setQuestion("Hvorfor er der mange fattige børn i Nepal, som ikke laver lektier?", "De behøver ikke lave lektier", "Deres forældre sætter dem ikke i gang", "Hindureligionen forbyder lektier");
                setSecondCorrect();
                eller
                setQuestion("Hvilken usund snack er nepalesiske børn vilde med?", "vingummi", "lakrids", "nudler");
                setThirdCorrect();
                break;
            case 8:
                setQuestion("Hvad skal børn i Nepal kunne til eksamen?", "De skal kunne tænke selvstændigt", "De skal kunne deres bøger udenad", "De skal kunne forstå, hvad der står i bøgerne.");
                setSecondAsGameWinning();
                eller
                setQuestion("Bliver der snydt til eksamen i Nepal?", "Nej da! Hvordan kunne du tro det!", "Ja det er helt almindeligt", "En sjælden gang i mellem, men lærerne holder godt øje");
                setfirstCorrect();
                break;

        }
    }


    public void wrong() {

        instans.setViden(instans.getViden() - 10 * instans.getKlassetrin());
        if (instans.getViden() < 0) {
            instans.setViden(0);
        }


        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Øv...")
                .setContentText("Du har desværre svaret forkert på eksamen og er derfor dumpet. -" + 10 * instans.getKlassetrin() + " viden")
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        finish();
                    }
                })
                .show();

    }

    public void setQuestion(String titel, String answerbox1, String answerbox2, String answerbox3) {
        question.setText(titel);
        answer1.setText(answerbox1);
        answer2.setText(answerbox2);
        answer3.setText(answerbox3);
    }

    public void setFirstCorrect() {
        answer1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final MediaPlayer mp = MediaPlayer.create(Eksamen.this, R.raw.tada);
                mp.start();
                konfetti();
                instans.setKlassetrin(instans.getKlassetrin() + 1);

                new SweetAlertDialog(Eksamen.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("TILLYKKE!!!")
                        .setContentText("Du bestod og går nu i " + instans.getKlassetrin() + ". klasse.")
                        .setConfirmText("Fedt!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();

            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wrong();
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wrong();
            }
        });
    }

    private void konfetti() {
        CommonConfetti.rainingConfetti(container, new int[]{Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW})
                .infinite();
    }


    public void setSecondCorrect() {
        answer1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wrong();
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(Eksamen.this, R.raw.tada);
                mp.start();
                konfetti();
                instans.setKlassetrin(instans.getKlassetrin() + 1);

                new SweetAlertDialog(Eksamen.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("TILLYKKE!!!")
                        .setContentText("Du bestod og går nu i " + instans.getKlassetrin() + ". klasse.")
                        .setConfirmText("Fedt!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();

            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wrong();
            }
        });
    }


    public void setThirdCorrect() {
        answer1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wrong();
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wrong();
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(Eksamen.this, R.raw.tada);
                mp.start();
                konfetti();
                instans.setKlassetrin(instans.getKlassetrin() + 1);

                new SweetAlertDialog(Eksamen.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("TILLYKKE!!!")
                        .setContentText("Du bestod og går nu i " + instans.getKlassetrin() + ". klasse.")
                        .setConfirmText("Fedt!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    public void setSecondAsGameWinning() {
        answer1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wrong();
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(Eksamen.this, R.raw.tada);
                mp.start();
                konfetti();
                instans.setKlassetrin(instans.getKlassetrin() + 1);
                if (prefs.getInt(""+instans.getNavn(),-1)==-1){
                    prefs.edit().putInt(""+instans.getNavn(), instans.getRunde()).apply();
                }

                else if (prefs.getInt(""+instans.getNavn(),-1)>instans.getRunde()){
                prefs.edit().putInt(""+instans.getNavn(), instans.getRunde()).apply();
                }

                dialog.setMessage("Godt klaret, du har vundet spillet på " + instans.getRunde() + " uger! Nu kan jeg få en uddannelse. Prøv igen med en ny figur, eller <ahref"http://skolemadtilnepal.wordpress.com/boernene-paa-bhawanipurskole">klik her for at møde børnene i virkeligheden<a>")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                AlertDialog alert = dialog.create();
                alert.show();

            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wrong();
            }
        });
    }

}
