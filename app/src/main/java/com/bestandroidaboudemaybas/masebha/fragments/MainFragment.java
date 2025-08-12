package com.bestandroidaboudemaybas.masebha.fragments;

import static android.content.Context.MODE_PRIVATE;



import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.bestandroidaboudemaybas.masebha.MainActivity;
import com.bestandroidaboudemaybas.masebha.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.slider.LightnessSlider;
import com.flask.colorpicker.slider.OnValueChangedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HexFormat;

public class MainFragment extends Fragment {
    private SharedPreferences sharedPreferences;

    private LinearLayout background;
    private CardView cardLayout;
    private TextView textView;
    private TextView textView2;
    private ImageView imageView;

    private FloatingActionButton fab;
    private FloatingActionButton resetAllButton;
    private FloatingActionButton colorPickerButton;


    private int cardSelectedColor;
    private int backgroundSelectedColor;
    private int textSelectedColor;
    private int toolsSelectedColor;
    private int toolsAccentSelectedColor;





    private RadioButton radioCard;
    private RadioButton radioBackground;
    private RadioButton radioText;
    private RadioButton radioTools;
    private RadioButton radioToolsAccent;
    private RelativeLayout buttonsLayout;
    private TextView enteredHexColor;
    private Button selectedHexColor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);



        sharedPreferences = getActivity().getSharedPreferences("ColorsPrefs", MODE_PRIVATE);
        cardLayout = rootView.findViewById(R.id.card_layout);
        textView = cardLayout.findViewById(R.id.titleTextView);
        textView2 = cardLayout.findViewById(R.id.descriptionTextView);
        imageView = cardLayout.findViewById(R.id.more_option);

        background = rootView.findViewById(R.id.background);

        fab = rootView.findViewById(R.id.fab);
        resetAllButton = rootView.findViewById(R.id.reset_all_button);
        colorPickerButton = rootView.findViewById(R.id.color_picker_button);

        radioCard = rootView.findViewById(R.id.radioCard);
        radioBackground = rootView.findViewById(R.id.radioBackground);
        radioText = rootView.findViewById(R.id.radioText);
        radioTools = rootView.findViewById(R.id.radioTools);
        radioToolsAccent = rootView.findViewById(R.id.radioToolsAccent);
        Button saveButton = rootView.findViewById(R.id.saveButton);
        Button returnDefault = rootView.findViewById(R.id.returnDefault);
        buttonsLayout = rootView.findViewById(R.id.buttonsLayout);
        selectedHexColor = rootView.findViewById(R.id.selectedHexColor);
        enteredHexColor = rootView.findViewById(R.id.enteredHexColor);

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
                Toast.makeText(rootView.getContext(), "تم الحفظ", Toast.LENGTH_LONG).show();
//                finish();
            }
        });

        returnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardSelectedColor = Color.BLACK;
                backgroundSelectedColor = Color.parseColor("#202020");
                textSelectedColor = Color.WHITE;
                toolsSelectedColor = Color.parseColor("#009736");
                toolsAccentSelectedColor = Color.WHITE;

                background.setBackgroundColor(backgroundSelectedColor);

                cardLayout.setCardBackgroundColor(cardSelectedColor);
                textView.setTextColor(textSelectedColor);
                textView2.setTextColor(textSelectedColor);
                imageView.setColorFilter(textSelectedColor);

                resetAllButton.setColorFilter(toolsAccentSelectedColor);
                colorPickerButton.setColorFilter(toolsAccentSelectedColor);
                resetAllButton.setBackgroundTintList(ColorStateList.valueOf(backgroundSelectedColor));
                colorPickerButton.setBackgroundTintList(ColorStateList.valueOf(backgroundSelectedColor));

                fab.setBackgroundTintList(ColorStateList.valueOf(toolsSelectedColor));
                fab.setColorFilter(textSelectedColor);

            }
        });

        selectedHexColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = selectedHexColor.getText().toString();

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Text", buttonText);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(rootView.getContext(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });
        final ColorPickerView colorPickerView = rootView.findViewById(R.id.color_picker_view);
        OnColorSelectedListener onColorSelectedListener = new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                if(radioCard.isChecked()){
                    cardSelectedColor = color;
                    cardLayout.setCardBackgroundColor(cardSelectedColor);
                } else if (radioBackground.isChecked()) {
                    backgroundSelectedColor = color;
                    background.setBackgroundColor(backgroundSelectedColor);
                    resetAllButton.setBackgroundTintList(ColorStateList.valueOf(backgroundSelectedColor));
                    colorPickerButton.setBackgroundTintList(ColorStateList.valueOf(backgroundSelectedColor));
                } else if (radioText.isChecked()) {
                    textSelectedColor = color;
                    TextView textView = cardLayout.findViewById(R.id.titleTextView);
                    TextView textView2 = cardLayout.findViewById(R.id.descriptionTextView);
                    ImageView imageView = cardLayout.findViewById(R.id.more_option);
                    textView.setTextColor(textSelectedColor);
                    textView2.setTextColor(textSelectedColor);
                    imageView.setColorFilter(textSelectedColor);
                    fab.setColorFilter(textSelectedColor);
                } else if (radioTools.isChecked()) {
                    toolsSelectedColor = color;
                    fab.setBackgroundTintList(ColorStateList.valueOf(toolsSelectedColor));
                } else if (radioToolsAccent.isChecked()) {
                    toolsAccentSelectedColor = color;
                    resetAllButton.setColorFilter(toolsAccentSelectedColor);
                    colorPickerButton.setColorFilter(toolsAccentSelectedColor);
                }
                selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & color)));
            }
        };

        colorPickerView.addOnColorSelectedListener(onColorSelectedListener);

        LightnessSlider lightnessSlider = rootView.findViewById(R.id.v_lightness_slider);
        lightnessSlider.setOnValueChangedListener(new OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                int currentColor = colorPickerView.getSelectedColor();

                float[] hsv = new float[3];
                Color.colorToHSV(currentColor, hsv);

                hsv[2] = value;

                int newColor = Color.HSVToColor(hsv);
                colorPickerView.setLightness(value);
                onColorSelectedListener.onColorSelected(newColor);
            }
        });
        enteredHexColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(enteredHexColor.getText().toString())) {
                    String hexColor = String.valueOf(enteredHexColor.getText());
                    try {
                        int parsedColor = Color.parseColor(hexColor);
                        colorPickerView.setColor(parsedColor, true);
                        onColorSelectedListener.onColorSelected(parsedColor);
                    } catch (IllegalArgumentException e) {

                    }
                }
            }
        });

        radioCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    colorPickerView.setColor(cardSelectedColor,true);
                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & cardSelectedColor)));
                }
            }
        });
        radioBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    colorPickerView.setColor(backgroundSelectedColor,true);
                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & backgroundSelectedColor)));

                }
            }
        });
        radioText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    colorPickerView.setColor(textSelectedColor,true);
                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & textSelectedColor)));

                }
            }
        });
        radioTools.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    colorPickerView.setColor(toolsSelectedColor,true);
                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & toolsSelectedColor)));

                }
            }
        });
        radioToolsAccent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    colorPickerView.setColor(toolsAccentSelectedColor,true);
                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & toolsAccentSelectedColor)));

                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getAndSetColors();

    }
    private void getAndSetColors() {
        cardSelectedColor = sharedPreferences.getInt("cardSelectedColor", Color.BLACK);
        backgroundSelectedColor = sharedPreferences.getInt("backgroundSelectedColor", Color.parseColor("#202020"));
        textSelectedColor = sharedPreferences.getInt("textSelectedColor", Color.WHITE);
        toolsSelectedColor = sharedPreferences.getInt("toolsSelectedColor", Color.parseColor("#009736"));
        toolsAccentSelectedColor = sharedPreferences.getInt("toolsAccentSelectedColor", Color.WHITE);

        background.setBackgroundColor(backgroundSelectedColor);

        cardLayout.setCardBackgroundColor(cardSelectedColor);
        textView.setTextColor(textSelectedColor);
        textView2.setTextColor(textSelectedColor);
        imageView.setColorFilter(textSelectedColor);

        resetAllButton.setColorFilter(toolsAccentSelectedColor);
        colorPickerButton.setColorFilter(toolsAccentSelectedColor);
        resetAllButton.setBackgroundTintList(ColorStateList.valueOf(backgroundSelectedColor));
        colorPickerButton.setBackgroundTintList(ColorStateList.valueOf(backgroundSelectedColor));

        fab.setBackgroundTintList(ColorStateList.valueOf(toolsSelectedColor));
        fab.setColorFilter(textSelectedColor);


        radioCard.setChecked(true);
    }
}