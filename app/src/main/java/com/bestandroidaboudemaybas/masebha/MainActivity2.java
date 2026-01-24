package com.bestandroidaboudemaybas.masebha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.TypedValue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity2 extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences appPreferences;

    private SQLiteDatabase myDB;
    private CardData cardData;


    private Integer jalseNumber;
    private TextView modeToggle;

    private boolean vibrationOnTap;
    private boolean vibrationOnLoop;
    private boolean isSoundEnabled;

    private MediaPlayer mediaPlayer;


    private Toolbar toolbar;
    private RelativeLayout background;
    private TextView zekerName;
    private TextView adadkulli1;
    private TextView total;
    private TextView dawratxt;
    private TextView dawra;
    private ProgressBar progressBar;
    private TextView currentNumber;

    private ImageButton resetButton;


    private GradientDrawable strokDrawable;
    private GradientDrawable ringDrawable;
    private GradientDrawable ringAccentDrawable;
    private LayerDrawable strokLayerDrawable;
    private LayerDrawable ringLayerDrawable;


    private FrameLayout overlayLayout;
    private ImageButton overlayProgress;
    private ImageButton closeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            readFromDb(extras);
        }


        setupToolbar();

        sharedPreferences = getSharedPreferences("ColorsPrefs", Context.MODE_PRIVATE);
        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String fontSize = appPreferences.getString("font_size", "30");
        int fontSizeValue = Integer.parseInt(fontSize);

//        // Optional: Scale other text elements proportionally
//        float scaleFactor = fontSizeValue / 30.0f; // 30 is your default size
//        adadkulli1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20 * scaleFactor);
//        total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20 * scaleFactor);
//        dawratxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20 * scaleFactor);
//        dawra.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20 * scaleFactor);
//        // Keep currentNumber large but scaled
//        currentNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100 * scaleFactor);

        background = findViewById(R.id.rl);
        zekerName = findViewById(R.id.zekerName);
        adadkulli1 = findViewById(R.id.adadkulli1);
        total = findViewById(R.id.total);
        dawratxt = findViewById(R.id.dawratxt);
        dawra = findViewById(R.id.dawra);
        progressBar = findViewById(R.id.progress_button);
        currentNumber = findViewById(R.id.current_number);
        resetButton = findViewById(R.id.reset);
        overlayProgress = findViewById(R.id.overlayProgress);
        closeButton = findViewById(R.id.close_button);
        modeToggle = findViewById(R.id.toggle_mode_button);
        overlayLayout = findViewById(R.id.overlayLayout);

        strokLayerDrawable = (LayerDrawable) progressBar.getBackground();
        strokDrawable = (GradientDrawable) strokLayerDrawable.getDrawable(0);
        ringLayerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
        ringDrawable = (GradientDrawable) ringLayerDrawable.getDrawable(1);
        ringAccentDrawable = (GradientDrawable) ringLayerDrawable.getDrawable(0);

        zekerName.setText(cardData.getTitle());
        zekerName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeValue);
        total.setText(cardData.getDescription());
        dawra.setText("0");
        if(appPreferences.getBoolean("save_counts", true))
            currentNumber.setText(String.valueOf(Integer.parseInt(cardData.getDescription()) % cardData.getDawra()));
        else
            currentNumber.setText("0");
        progressBar.setMax(10000);
        progressBar.setProgress((Integer.parseInt(currentNumber.getText().toString()) * 10000) / cardData.getDawra());
        jalseNumber = 0;


//        progressBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressUpdate();
//            }
//        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog();
            }
        });

        vibrationOnTap  = appPreferences.getBoolean("vibration_on_tap", false);
        vibrationOnLoop = appPreferences.getBoolean("vibration_on_loop", true);

        mediaPlayer = MediaPlayer.create(this, R.raw.tap);
        isSoundEnabled = appPreferences.getBoolean("default_sound", false);



        overlayProgress.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressUpdate();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayLayout.setVisibility(View.GONE);
            }
        });
        modeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void progressUpdate() {
        jalseNumber++;
        Integer newCurrentNumber = Integer.parseInt(currentNumber.getText().toString()) + 1;
        currentNumber.setText(newCurrentNumber.toString());

        progressBar.setProgress((newCurrentNumber * 10000) / cardData.getDawra());


        Integer newTotal = Integer.parseInt(total.getText().toString()) + 1;
        total.setText(newTotal.toString());
        if (isSoundEnabled) {
            mediaPlayer.start();
        }
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrationOnTap) {
            vibrator.vibrate(300);
        }
        if (progressBar.getProgress() == 10000) {
            progressBar.setProgress(0);
            currentNumber.setText("0");
            Integer newDawra = Integer.parseInt(dawra.getText().toString()) + 1;
            dawra.setText(newDawra.toString());
            if (vibrator != null && vibrator.hasVibrator()) {
                if (vibrationOnLoop) {
                    vibrator.vibrate(1000);
                }
            }
        }
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("");
    }

    private void displayDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reset_dialog, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();


        Button tesfir = dialogView.findViewById(R.id.tesfir);
        Button cancelReset = dialogView.findViewById(R.id.cancel_reset);
        TextView resetAll = dialogView.findViewById(R.id.reset_all);



        tesfir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                currentNumber.setText("0");
                dawra.setText("0");
                Integer newTotal =Integer.parseInt(total.getText().toString())-jalseNumber;
                total.setText(newTotal.toString());
                jalseNumber = 0;
                alertDialog.dismiss();
            }
        });
        cancelReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        resetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                currentNumber.setText("0");
                dawra.setText("0");
                total.setText("0");
                jalseNumber = 0;
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void readFromDb(Bundle extras) {
        int clickedCardId = extras.getInt("clickedCardId", -1);
        myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
        Cursor myCursor = myDB.rawQuery("select * from zeker where _id = " + clickedCardId, null);
        myCursor.moveToFirst();

        long lastOpenedAt = 0;
        int idx = myCursor.getColumnIndex("last_opened_at");
        if (idx != -1) lastOpenedAt = myCursor.getLong(idx);

        String description = String.valueOf(myCursor.getInt(2));
        cardData = new CardData(myCursor.getInt(0), myCursor.getString(1), description, myCursor.getInt(3), lastOpenedAt);
        myCursor.close();
        myDB.close();
    }



    @Override
    protected void onPause() {
        super.onPause();
        myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("total", Integer.parseInt(total.getText().toString()));
        updatedValues.put("last_opened_at", System.currentTimeMillis());
        myDB.update("zeker", updatedValues, "_id=?", new String[]{String.valueOf(cardData.getId())});
        myDB.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        zekerName.setTextColor(sharedPreferences.getInt("textSelectedColor1", Color.WHITE));
        adadkulli1.setTextColor(sharedPreferences.getInt("textSelectedColor1", Color.WHITE));
        total.setTextColor(sharedPreferences.getInt("textSelectedColor1", Color.WHITE));
        dawratxt.setTextColor(sharedPreferences.getInt("textSelectedColor1", Color.WHITE));
        dawra.setTextColor(sharedPreferences.getInt("textSelectedColor1", Color.WHITE));

        background.setBackgroundColor(sharedPreferences.getInt("backgroundSelectedColor1", Color.parseColor("#202020")));
        float strokeWidthDp = 52;
        float strokeWidthPixels = strokeWidthDp * getResources().getDisplayMetrics().density;
        strokDrawable.setStroke((int) strokeWidthPixels, sharedPreferences.getInt("backgroundSelectedColor1", Color.parseColor("#202020")));
        strokDrawable.setColor(sharedPreferences.getInt("progressButtonSelectedColor", Color.BLACK));
        toolbar.setBackgroundColor(sharedPreferences.getInt("resetSelectedColor", Color.parseColor("#009736")));
//        btnToggleVibration.setBackgroundColor(sharedPreferences.getInt("resetSelectedColor", Color.parseColor("#009736")));
        currentNumber.setTextColor(sharedPreferences.getInt("progressTextSelectedColor", Color.WHITE));
        ringDrawable.setColor(sharedPreferences.getInt("resetSelectedColor", Color.parseColor("#009736")));
        ringAccentDrawable.setColor(sharedPreferences.getInt("resetAccentSelectedColor", Color.parseColor("#F0F0F0")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}