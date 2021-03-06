package com.example.asger.nepalspil.felter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asger.nepalspil.R;
import com.example.asger.nepalspil.diverse.Topbar;
import com.example.asger.nepalspil.model.Spiller;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Vaerksted extends AppCompatActivity {

    //Working
    final int MONEY_PER_CLICK = 5;
    final int TIME_PER_CLICK = 1;
    private Topbar topbar;

    private Animation animation;
    AlertDialog.Builder dialog;
    ViewPager viewPager;
    TextView viewPagerText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Spiller.instans==null) { finish(); return; } // genstart i frisk JVM - vis hovedmenu
        setContentView(R.layout.vaerksted);

        ImageView figur = (ImageView) findViewById(R.id.figur);
        figur.setImageResource(Spiller.instans.figurdata.drawable_figur_halv_id);
        dialog = new AlertDialog.Builder(Vaerksted.this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.cash);
        ImageView helpField = (ImageView) findViewById(R.id.vaerkstedHelp);
        Button work = (Button) findViewById(R.id.knap_arbejd);
        Button buy = (Button) findViewById(R.id.buyBikeButton);
        ImageView hjemBack = (ImageView) findViewById(R.id.ikon_tilbage);
        viewPagerText = (TextView) findViewById(R.id.taleboble_tekst);
        ImageView menu = (ImageView) findViewById(R.id.menuknap);
        menu.setVisibility(View.INVISIBLE);

        topbar = new Topbar();
        topbar.init(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plusknowledge);
        final TextView money = (TextView) findViewById(R.id.flyvoptekst_arbejd);
        topbar.opdaterGui(Spiller.instans);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        viewPagerText.setText("Brugt cykel 200 kr");
                        break;
                    case 1:
                        viewPagerText.setText("Pænt hurtig cykel 500 kr");
                        break;
                    case 2:
                        viewPagerText.setText("Racercykel 1000 kr");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        helpField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Vaerksted.this, R.anim.image_click));
                dialog.setMessage(R.string.værksted_hjælp);
                dialog.show();
            }
        });

        work.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Vaerksted.this, R.anim.image_click));
                if (Spiller.instans.tid >= TIME_PER_CLICK && Spiller.instans.klassetrin >= 3) {
                    Spiller.instans.arbejd(TIME_PER_CLICK, MONEY_PER_CLICK);
                    money.setText("+" + MONEY_PER_CLICK + " kr");
                    money.startAnimation(animation);

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

                    topbar.opdaterGui(Spiller.instans);
                } else if (Spiller.instans.tid < 2) {

                    new SweetAlertDialog(Vaerksted.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Du har ikke tid til at arbejde.")
                            .show();

                } else if (Spiller.instans.klassetrin < 3) {
                    new SweetAlertDialog(Vaerksted.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Du kan ikke arbejde her.")
                            .setContentText("Du skal gå i mindst 3. klasse for at arbejde i værkstedet.")
                            .show();
                }
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Vaerksted.this, R.anim.image_click));
                buy();
                topbar.opdaterGui(Spiller.instans);
            }
        });

        hjemBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(Vaerksted.this, R.anim.image_click));
                finish();
            }
        });

    }


    private class ImagePagerAdapter extends PagerAdapter {
        private int[] mImages = new int[]{
                R.drawable.cykel1,
                R.drawable.cykel2,
                R.drawable.cykel3,
        };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = Vaerksted.this;
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(mImages[position]);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);

        }

    }


    public void buy() {

        switch (viewPager.getCurrentItem()) {
            case 0:
                if (Spiller.instans.bevægelsesFart < 2) {
                    if (Spiller.instans.penge >= 200) {
                        Spiller.instans.penge = Spiller.instans.penge - 200;
                        Spiller.instans.bevægelsesFart = 2;
                        new SweetAlertDialog(Vaerksted.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Cykel købt!")
                                .setContentText("Du har købt en brugt cykel for 200kr.")
                                .show();
                    } else {
                        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Ikke nok penge!")
                                .setContentText("Du har ikke råd til en cykel, den koster 200kr")
                                .show();
                    }
                } else
                    Toast.makeText(Vaerksted.this, "Du har allerede en bedre cykel", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (Spiller.instans.bevægelsesFart < 3) {
                    if (Spiller.instans.penge >= 500) {
                        Spiller.instans.penge = Spiller.instans.penge - 500;
                        Spiller.instans.bevægelsesFart = 3;
                        new SweetAlertDialog(Vaerksted.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Pænt hurtig cykel købt!")
                                .setContentText("Du har købt en pænt hurtig cykel for 500kr.")
                                .show();
                    } else {
                        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Ikke nok penge!")
                                .setContentText("Du har ikke råd til den pænt hurtige cykel, den koster 500kr")
                                .show();
                    }
                } else
                    Toast.makeText(Vaerksted.this, "Du har allerede en bedre cykel", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if (Spiller.instans.bevægelsesFart < 4) {
                    if (Spiller.instans.penge >= 1000) {
                        Spiller.instans.penge = Spiller.instans.penge - 1000;
                        Spiller.instans.bevægelsesFart = 4;
                        new SweetAlertDialog(Vaerksted.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Racercykel købt!")
                                .setContentText("Du har købt en racercykel for 1000kr.")
                                .show();
                    } else {
                        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Ikke nok penge!")
                                .setContentText("Du har ikke råd til racercyklen, den koster 1000kr")
                                .show();
                    }
                } else
                    Toast.makeText(Vaerksted.this, "Du har allerede en bedre cykel", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}

