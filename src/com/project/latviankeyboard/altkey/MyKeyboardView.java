package com.project.latviankeyboard.altkey;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.List;

public class MyKeyboardView extends KeyboardView {

    private int backgroundColor;
    private int buttonColor;
    private int letterColor;
    private int borderColor;

    private boolean defaultStyle;

    private int roundness;
    private int borderWidth;
    private boolean switchSounds;

    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (defaultStyle) {
            super.onDraw(canvas);
        } else {

            Paint backgrPaint = new Paint();
            backgrPaint.setColor(backgroundColor);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgrPaint);

            Paint hintPaint = new Paint();
            hintPaint.setTextAlign(Paint.Align.CENTER);
            hintPaint.setTextSize(24);
            hintPaint.setColor(Color.LTGRAY);

            Paint buttonPaint = new Paint();
            buttonPaint.setColor(buttonColor);

            Paint labelPaint = new Paint();
            labelPaint.setColor(letterColor);
            labelPaint.setTextAlign(Paint.Align.CENTER);

            Paint borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStrokeWidth(borderWidth);
            borderPaint.setStyle(Paint.Style.STROKE);

            List<Key> keys = getKeyboard().getKeys();
            int maxWidth = keys.get(0).width;
            for (Key key : keys) {
                RectF rect = new RectF(key.x + 1, key.y + 2, (key.x + key.width), (key.y + key.height));
                RectF rectBorder = new RectF(key.x + 3, key.y + 4, (key.x + key.width - 2), (key.y + key.height - 2));
                canvas.drawRoundRect(rect, roundness, roundness, buttonPaint);
                canvas.drawRoundRect(rectBorder, roundness, roundness, borderPaint);
                if (key.icon != null) {
                    Drawable icon = key.icon;
                    int left;
                    int top = 0;

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        if (key.width > maxWidth) {
                            left = key.x + ((key.width - 50) / 2);
                        } else {
                            left = key.x - 2;
                        }
                        top = key.y + 15;
                        icon.setBounds(left, top, left + icon.getIntrinsicWidth(), top + icon.getIntrinsicHeight());
                    } else {
                        if (key.width > maxWidth) {
                            left = key.x + ((key.width - 35) / 2);
                        } else {
                            left = key.x + 10;
                        }
                        if ((top + icon.getIntrinsicWidth()) > top + key.height) {
                            top = key.y;
                        }
                        icon.setBounds(left, top, left + icon.getIntrinsicWidth(), top + key.height);
                    }
                    icon.setColorFilter(letterColor, PorterDuff.Mode.MULTIPLY);
                    icon.draw(canvas);
                    icon.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                } else {
                    if (key.label != null) {
                        if (key.label.length() > 1) {
                            labelPaint.setTextSize(new Button(this.getContext()).getTextSize() - 6);
                        } else {
                            labelPaint.setTextSize(new Button(this.getContext()).getTextSize() + 4);
                        }
                        canvas.drawText(key.label.toString(), (key.x + 1) + key.width / 2,
                                (key.y + 14) + key.height / 2, labelPaint);
                    }
                }
                int popups = key.popupResId;
                if (popups != 0) {
                    canvas.drawText("...", key.x + (key.width - 20), key.y + 18, hintPaint);
                }
            }
        }
    }

    public void setValues(Context context) {
        SharedPreferences prefs  = PreferenceManager.getDefaultSharedPreferences(context);
        this.backgroundColor = prefs.getInt("akBackgroundColor", Color.BLACK);
        this.buttonColor = prefs.getInt("akBtnColor", Color.WHITE);
        this.letterColor = prefs.getInt("akLetterColor", Color.RED);
        this.borderColor = prefs.getInt("akBorderColor", Color.LTGRAY);
        this.roundness = prefs.getInt("akBtnRoundness", 1);
        this.borderWidth = prefs.getInt("akBorderWidth", 2);
        this.defaultStyle = prefs.getBoolean("akDefaultStyle", true);
        this.switchSounds = prefs.getBoolean("akSwitchSounds", true);
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        super.setKeyboard(keyboard);
        setValues(getContext());
    }

    public boolean isSwitchSounds() {
        return switchSounds;
    }
}
