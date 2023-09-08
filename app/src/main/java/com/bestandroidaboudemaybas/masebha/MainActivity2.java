package com.bestandroidaboudemaybas.masebha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity2 extends AppCompatActivity {
    private SQLiteDatabase myDB;
    private CardData cardData;
    private TextView total;
    private ProgressBar progressBar;
    private TextView currentNumber;
    private TextView dawra;
    private Integer jalseNumber;
    private ImageButton btnToggleVibration;
    private boolean isVibrationEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            readFromDb(extras);
        }


        setupToolbar();

        TextView zekerName =  findViewById(R.id.zekerName);
        total = findViewById(R.id.total);
        dawra =  findViewById(R.id.dawra);
        progressBar = findViewById(R.id.progress_button);
        currentNumber =  findViewById(R.id.current_number);
        FloatingActionButton resetButton = findViewById(R.id.reset);


        zekerName.setText(cardData.getTitle());
        total.setText(cardData.getDescription());
        dawra.setText("0");
        currentNumber.setText("0");
        progressBar.setMax(10000);
        progressBar.setProgress(0);
        jalseNumber = 0;


        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jalseNumber++;
                Integer newCurrentNumber =Integer.parseInt(currentNumber.getText().toString())+1;
                currentNumber.setText(newCurrentNumber.toString());

                progressBar.setProgress( (newCurrentNumber * 10000) / cardData.getDawra() );


                Integer newTotal =Integer.parseInt(total.getText().toString())+1;
                total.setText(newTotal.toString());

                if(progressBar.getProgress()==10000){
                    progressBar.setProgress(0);
                    currentNumber.setText("0");
                    Integer newDawra = Integer.parseInt(dawra.getText().toString())+1;
                    dawra.setText(newDawra.toString());
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null && vibrator.hasVibrator()) {
                        if(isVibrationEnabled) {vibrator.vibrate(100);}
                    }
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog();
            }
        });



        isVibrationEnabled = true;
        btnToggleVibration = findViewById(R.id.color_picker_button);


        btnToggleVibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVibration();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        Cursor myCursor = myDB.rawQuery("select * from zeker where _id = "+ clickedCardId, null);
        myCursor.moveToFirst();
        String description = String.valueOf(myCursor.getInt(2));
        cardData = new CardData(myCursor.getInt(0),myCursor.getString(1),description,myCursor.getInt(3));
        myCursor.close();
        myDB.close();
    }


    private void toggleVibration() {
        isVibrationEnabled = !isVibrationEnabled;
        if (isVibrationEnabled) {
            btnToggleVibration.setImageResource(R.drawable.vibration_icon_on);
        } else {
            btnToggleVibration.setImageResource(R.drawable.vibration_icon_off);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("total", Integer.parseInt(total.getText().toString()));
        myDB.update("zeker", updatedValues, "_id=?", new String[] { String.valueOf(cardData.getId()) });
        myDB.close();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}