package com.bestandroidaboudemaybas.masebha.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bestandroidaboudemaybas.masebha.R;
//import com.flask.colorpicker.ColorPickerView;
//import com.flask.colorpicker.OnColorSelectedListener;
//import com.flask.colorpicker.slider.LightnessSlider;
//import com.flask.colorpicker.slider.OnValueChangedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TesbihFragment extends Fragment {
//    private SharedPreferences sharedPreferences;
//
//
//
//    private int textSelectedColor;
//    private int backgroundSelectedColor;
//    private int progressButtonSelectedColor;
//    private int progressTextSelectedColor;
//    private int resetSelectedColor;
//    private int resetAccentSelectedColor;
//
//
//
//    private RelativeLayout background;
//    private TextView textView;
//    private ProgressBar progressBar;
//    private TextView progressText;
//
//
//    private FloatingActionButton fab;
//
//
//
//    private GradientDrawable strokDrawable;
//    private GradientDrawable ringDrawable;
//    private GradientDrawable ringAccentDrawable;
//    private LayerDrawable strokLayerDrawable;
//    private LayerDrawable ringLayerDrawable;
//    private RadioButton radioText;
//    private RadioButton radioBackground;
//    private RadioButton radioProgressButton;
//    private RadioButton radioProgressText;
//    private RadioButton radioReset;
//
//    private RadioButton radioRestAccent;
//    private RelativeLayout buttonsLayout;
//    private TextView enteredHexColor;
//    private Button selectedHexColor;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_tesbih, container, false);
//
//
//
//        sharedPreferences = getActivity().getSharedPreferences("ColorsPrefs", MODE_PRIVATE);
//
//
//        background = rootView.findViewById(R.id.rl);
//        textView = rootView.findViewById(R.id.zekerName);
//
//
//        progressBar = rootView.findViewById(R.id.progress_button);
//        progressText = rootView.findViewById(R.id.current_number);
//
//        fab = rootView.findViewById(R.id.reset);
//
//
//        strokLayerDrawable = (LayerDrawable) progressBar.getBackground();
//        strokDrawable = (GradientDrawable) strokLayerDrawable.getDrawable(0);
//        ringLayerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
//        ringDrawable = (GradientDrawable) ringLayerDrawable.getDrawable(1);
//        ringAccentDrawable = (GradientDrawable) ringLayerDrawable.getDrawable(0);
//
//
//        radioText = rootView.findViewById(R.id.radioText);
//        radioBackground = rootView.findViewById(R.id.radioBackground);
//        radioProgressButton = rootView.findViewById(R.id.radioProgressButton);
//        radioProgressText = rootView.findViewById(R.id.radioProgressText);
//        radioReset = rootView.findViewById(R.id.radioReset);
//        radioRestAccent = rootView.findViewById(R.id.radioResetAccent);
//
//        Button saveButton = rootView.findViewById(R.id.saveButton);
//        Button returnDefault = rootView.findViewById(R.id.returnDefault);
//        buttonsLayout = rootView.findViewById(R.id.buttonsLayout);
//        selectedHexColor = rootView.findViewById(R.id.selectedHexColor);
//        enteredHexColor = rootView.findViewById(R.id.enteredHexColor);
//
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("textSelectedColor1", textSelectedColor);
//                editor.putInt("backgroundSelectedColor1", backgroundSelectedColor);
//                editor.putInt("progressButtonSelectedColor", progressButtonSelectedColor);
//                editor.putInt("progressTextSelectedColor", progressTextSelectedColor);
//                editor.putInt("resetSelectedColor", resetSelectedColor);
//                editor.putInt("resetAccentSelectedColor", resetAccentSelectedColor);
//                editor.apply();
//                Toast.makeText(rootView.getContext(), "تم الحفظ", Toast.LENGTH_LONG).show();
////                finish();
//            }
//        });
//        returnDefault.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textSelectedColor = Color.WHITE;
//                backgroundSelectedColor = Color.parseColor("#202020");
//                progressButtonSelectedColor = Color.BLACK;
//                progressTextSelectedColor = Color.WHITE;
//                resetSelectedColor = Color.parseColor("#009736");
//                resetAccentSelectedColor = Color.parseColor("#DCDCDC");
//
//
//                textView.setTextColor(textSelectedColor);
//                background.setBackgroundColor(backgroundSelectedColor);
//                float strokeWidthDp = 52;
//                float strokeWidthPixels = strokeWidthDp * getResources().getDisplayMetrics().density;
//                strokDrawable.setStroke((int) strokeWidthPixels, backgroundSelectedColor);
//                strokDrawable.setColor(progressButtonSelectedColor);
//                progressText.setTextColor(progressTextSelectedColor);
//                fab.setBackgroundTintList(ColorStateList.valueOf(resetSelectedColor));
//                ringDrawable.setColor(resetSelectedColor);
//                fab.setColorFilter(resetAccentSelectedColor);
//                ringAccentDrawable.setColor(resetAccentSelectedColor);
//            }
//        });
//        selectedHexColor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String buttonText = selectedHexColor.getText().toString();
//
//                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//                android.content.ClipData clip = android.content.ClipData.newPlainText("Text", buttonText);
//                clipboard.setPrimaryClip(clip);
//
//                Toast.makeText(rootView.getContext(), "Copied", Toast.LENGTH_SHORT).show();
//            }
//        });
//        final ColorPickerView colorPickerView = rootView.findViewById(R.id.color_picker_view);
//        OnColorSelectedListener onColorSelectedListener = new OnColorSelectedListener() {
//            @Override
//            public void onColorSelected(int color) {
//                if(radioText.isChecked()){
//                    textSelectedColor = color;
//                    textView.setTextColor(textSelectedColor);
//                } else if (radioBackground.isChecked()) {
//                    backgroundSelectedColor = color;
//                    background.setBackgroundColor(backgroundSelectedColor);
//                    float strokeWidthDp = 52;
//                    float strokeWidthPixels = strokeWidthDp * getResources().getDisplayMetrics().density;
//                    strokDrawable.setStroke((int) strokeWidthPixels, color);
//                } else if (radioProgressButton.isChecked()) {
//                    progressButtonSelectedColor = color;
//                    strokDrawable.setColor(color);
////                    progressBar.setBackground(strokDrawable);
//
//                } else if (radioProgressText.isChecked()) {
//                    progressTextSelectedColor = color;
//                    progressText.setTextColor(progressTextSelectedColor);
//                } else if (radioReset.isChecked()) {
//                    resetSelectedColor = color;
//                    fab.setBackgroundTintList(ColorStateList.valueOf(color));
//                    ringDrawable.setColor(color);
////                    progressBar.setProgressDrawable(ringDrawable);
//                } else if (radioRestAccent.isChecked()) {
//                    resetAccentSelectedColor = color;
//                    fab.setColorFilter(resetAccentSelectedColor);
//                    ringAccentDrawable.setColor(color);
////                    progressBar.setProgressDrawable(ringDrawable);
//                }
//                selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & color)));
//            }
//        };
//        colorPickerView.addOnColorSelectedListener(onColorSelectedListener);
//
//        LightnessSlider lightnessSlider = rootView.findViewById(R.id.v_lightness_slider);
//        lightnessSlider.setOnValueChangedListener(new OnValueChangedListener() {
//            @Override
//            public void onValueChanged(float value) {
//                int currentColor = colorPickerView.getSelectedColor();
//
//                float[] hsv = new float[3];
//                Color.colorToHSV(currentColor, hsv);
//
//                hsv[2] = value;
//
//                int newColor = Color.HSVToColor(hsv);
//                colorPickerView.setLightness(value);
//                onColorSelectedListener.onColorSelected(newColor);
//            }
//        });
//        enteredHexColor.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(!TextUtils.isEmpty(enteredHexColor.getText().toString())) {
//                    String hexColor = String.valueOf(enteredHexColor.getText());
//                    try {
//                        int parsedColor = Color.parseColor(hexColor);
//                        colorPickerView.setColor(parsedColor, true);
//                        onColorSelectedListener.onColorSelected(parsedColor);
//                    } catch (IllegalArgumentException e) {
//
//                    }
//                }
//            }
//        });
//
//
//        radioText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    colorPickerView.setColor(textSelectedColor,true);
//                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & textSelectedColor)));
//
//                }
//            }
//        });
//        radioBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    colorPickerView.setColor(backgroundSelectedColor,true);
//                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & backgroundSelectedColor)));
//
//                }
//            }
//        });
//        radioProgressButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    colorPickerView.setColor(progressButtonSelectedColor,true);
//                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & progressButtonSelectedColor)));
//
//                }
//            }
//        });
//        radioProgressText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    colorPickerView.setColor(progressTextSelectedColor,true);
//                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & progressTextSelectedColor)));
//
//                }
//            }
//        });
//        radioReset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    colorPickerView.setColor(resetSelectedColor,true);
//                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & resetSelectedColor)));
//
//                }
//            }
//        });
//        radioRestAccent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    colorPickerView.setColor(resetAccentSelectedColor,true);
//                    selectedHexColor.setText(String.format("#%06X", (0xFFFFFF & resetAccentSelectedColor)));
//
//                }
//            }
//        });
//
//        return rootView;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        getAndSetColors();
//
//    }
//    private void getAndSetColors() {
//
//        textSelectedColor = sharedPreferences.getInt("textSelectedColor1", Color.WHITE);
//        backgroundSelectedColor = sharedPreferences.getInt("backgroundSelectedColor1", Color.parseColor("#202020"));
//        progressButtonSelectedColor = sharedPreferences.getInt("progressButtonSelectedColor", Color.BLACK);
//        progressTextSelectedColor = sharedPreferences.getInt("progressTextSelectedColor", Color.WHITE);
//        resetSelectedColor = sharedPreferences.getInt("resetSelectedColor", Color.parseColor("#009736"));
//        resetAccentSelectedColor = sharedPreferences.getInt("resetAccentSelectedColor",Color.parseColor("#DCDCDC"));
//
//
//        textView.setTextColor(textSelectedColor);
//        background.setBackgroundColor(backgroundSelectedColor);
//        float strokeWidthDp = 52;
//        float strokeWidthPixels = strokeWidthDp * getResources().getDisplayMetrics().density;
//        strokDrawable.setStroke((int) strokeWidthPixels, backgroundSelectedColor);
//        strokDrawable.setColor(progressButtonSelectedColor);
//        progressText.setTextColor(progressTextSelectedColor);
//        fab.setBackgroundTintList(ColorStateList.valueOf(resetSelectedColor));
//        ringDrawable.setColor(resetSelectedColor);
//        fab.setColorFilter(resetAccentSelectedColor);
//        ringAccentDrawable.setColor(resetAccentSelectedColor);
//
//    }
}
