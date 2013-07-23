package com.project.latviankeyboard.altkey;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

public class MyKeyboardView extends KeyboardView {

    private int backgroundColor = Color.BLACK;
    private int buttonColor = Color.GRAY;
    private int letterColor = Color.WHITE;

    private boolean defaultStyle = false;

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

            List<Key> keys = getKeyboard().getKeys();
            int maxWidth = keys.get(0).width;
            for (Key key : keys) {

                canvas.drawRect(key.x + 1, key.y + 2, (key.x + key.width), (key.y + key.height), buttonPaint);
                if (key.icon != null) {
                    Drawable icon = key.icon;
                    int left;
                    int top = 0;

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        if (key.width > maxWidth) {
                            left = key.x + ((key.width - 50) / 2);
                        } else {
                            left = key.x + 2;
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
                    icon.draw(canvas);
                } else {
                    if (key.label != null) {
                        if (key.label.length() > 1) {
                            labelPaint.setTextSize(24);
                        } else {
                            labelPaint.setTextSize(35);
                        }
                        canvas.drawText(key.label.toString(), (key.x + 1) + key.width / 2, (key.y + 14) + key.height / 2, labelPaint);
                    }
                }
                int popups = key.popupResId;
                if (popups != 0) {
                    canvas.drawText("...", key.x + (key.width - 20), key.y + 18, hintPaint);
                }
            }
        }
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setButtonColor(int red, int green, int blue) {
        this.buttonColor = Color.rgb(red, green, blue);
    }

    public void setLetterColor(int red, int green, int blue) {
        this.letterColor = Color.rgb(red, green, blue);
    }

    public void setDefaultStyle(boolean defaultStyle) {
        this.defaultStyle = defaultStyle;
    }
}
