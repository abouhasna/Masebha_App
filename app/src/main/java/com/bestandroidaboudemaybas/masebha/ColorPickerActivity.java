package com.bestandroidaboudemaybas.masebha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.slider.LightnessSlider;
import com.flask.colorpicker.slider.OnValueChangedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ColorPickerActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private CardView cardLayout;
    private LinearLayout background;
    private FloatingActionButton fab;
    private LinearLayout totalLayout;
    private TextView majmu3;
    private int cardSelectedColor;
    private int backgroundSelectedColor;
    private int textSelectedColor;
    private int toolsSelectedColor;
    private int toolsAccentSelectedColor;
    private int toolBarSelectedColor;


    private TextView textView;
    private TextView textView2;
    private TextView majmu3Static;
    private ImageView imageView;

    private GradientDrawable drawable;
    private RadioButton radioCard;
    private RadioButton radioBackground;
    private RadioButton radioText;
    private RadioButton radioTools;
    private RadioButton radioToolsAccent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        setupToolbar();
        drawable = new GradientDrawable();
        sharedPreferences = getSharedPreferences("ColorsPrefs", MODE_PRIVATE);

        cardLayout = findViewById(R.id.card_layout);
        textView = cardLayout.findViewById(R.id.titleTextView);
        textView2 = cardLayout.findViewById(R.id.descriptionTextView);
        imageView = cardLayout.findViewById(R.id.more_option);

        background = findViewById(R.id.background);

        totalLayout = findViewById(R.id.total_layout);
        fab = findViewById(R.id.fab);
        majmu3 = findViewById(R.id.majmu3);
        majmu3Static = findViewById(R.id.majmu3_static);

        radioCard = findViewById(R.id.radioCard);
        radioBackground = findViewById(R.id.radioBackground);
        radioText = findViewById(R.id.radioText);
        radioTools = findViewById(R.id.radioTools);
        radioToolsAccent = findViewById(R.id.radioToolsAccent);
        Button saveButton = findViewById(R.id.saveButton);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("cardSelectedColor", cardSelectedColor);
                editor.putInt("backgroundSelectedColor", backgroundSelectedColor);
                editor.putInt("textSelectedColor", textSelectedColor);
                editor.putInt("toolsSelectedColor", toolsSelectedColor);
                editor.putInt("toolsAccentSelectedColor", toolsAccentSelectedColor);
                editor.apply();
                Toast.makeText(ColorPickerActivity.this, "تم الحفظ", Toast.LENGTH_LONG).show();
//                finish();
            }
        });


        final ColorPickerView colorPickerView = findViewById(R.id.color_picker_view);
        OnColorSelectedListener onColorSelectedListener = new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                if(radioCard.isChecked()){
                    cardSelectedColor = color;
                    cardLayout.setCardBackgroundColor(cardSelectedColor);
                } else if (radioBackground.isChecked()) {
                    backgroundSelectedColor = color;
                    background.setBackgroundColor(backgroundSelectedColor);
                } else if (radioText.isChecked()) {
                    textSelectedColor = color;
                    TextView textView = cardLayout.findViewById(R.id.titleTextView);
                    TextView textView2 = cardLayout.findViewById(R.id.descriptionTextView);
                    ImageView imageView = cardLayout.findViewById(R.id.more_option);
                    textView.setTextColor(textSelectedColor);
                    textView2.setTextColor(textSelectedColor);
                    imageView.setColorFilter(textSelectedColor);
                } else if (radioTools.isChecked()) {
                    toolsSelectedColor = color;
                    drawable.setColor(toolsSelectedColor);
                    totalLayout.setBackground(drawable);
                    fab.setBackgroundTintList(ColorStateList.valueOf(toolsSelectedColor));
                } else if (radioToolsAccent.isChecked()) {
                    toolsAccentSelectedColor = color;
                    majmu3.setTextColor(toolsAccentSelectedColor);
                    majmu3Static.setTextColor(toolsAccentSelectedColor);
                    fab.setColorFilter(toolsAccentSelectedColor);
                }

            }
        };
        colorPickerView.addOnColorSelectedListener(onColorSelectedListener);

        LightnessSlider lightnessSlider = findViewById(R.id.v_lightness_slider);
        lightnessSlider.setOnValueChangedListener(new OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                int currentColor = colorPickerView.getSelectedColor();

                float[] hsv = new float[3];
                Color.colorToHSV(currentColor, hsv);

                hsv[2] = value;

                int newColor = Color.HSVToColor(hsv);

                onColorSelectedListener.onColorSelected(newColor);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();


        cardSelectedColor = sharedPreferences.getInt("cardSelectedColor", Color.BLACK);
        backgroundSelectedColor = sharedPreferences.getInt("backgroundSelectedColor", Color.parseColor("#202020"));
        textSelectedColor = sharedPreferences.getInt("textSelectedColor", Color.WHITE);
        toolsSelectedColor = sharedPreferences.getInt("toolsSelectedColor", Color.parseColor("#E6DFDF"));
        toolsAccentSelectedColor = sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK);
//        toolBarSelectedColor = sharedPreferences.getInt("toolBarSelectedColor", Color.BLACK);

        cardLayout.setCardBackgroundColor(cardSelectedColor);
        background.setBackgroundColor(backgroundSelectedColor);
        textView.setTextColor(textSelectedColor);
        textView2.setTextColor(textSelectedColor);
        imageView.setColorFilter(textSelectedColor);


        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(sharedPreferences.getInt("toolsSelectedColor", Color.parseColor("#E6DFDF")));
        drawable.setCornerRadius(10);

        totalLayout.setBackground(drawable);
        fab.setBackgroundTintList(ColorStateList.valueOf(sharedPreferences.getInt("toolsSelectedColor", Color.parseColor("#E6DFDF"))));

        fab.setColorFilter(sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK));




        majmu3.setTextColor(sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK));
        majmu3Static.setTextColor(sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK));
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("");
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