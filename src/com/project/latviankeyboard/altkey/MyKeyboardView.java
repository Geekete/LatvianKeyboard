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

    private int backgroundColor = Color.GRAY;
    private int buttonColor = Color.WHITE;
    private int letterColor = Color.RED;
    private int borderColor = Color.LTGRAY;

    private boolean defaultStyle = false;

    private float roundness = 1;
    private int borderWidth = 2;

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

    public void setBackgroundColor(int alpha, int red, int green, int blue) {
        this.backgroundColor = Color.argb(alpha, red, green, blue);
    }

    public void setButtonColor(int alpha, int red, int green, int blue) {
        this.buttonColor = Color.argb(alpha, red, green, blue);
    }

    public void setLetterColor(int alpha, int red, int green, int blue) {
        this.letterColor = Color.argb(alpha, red, green, blue);
    }

    public void setBorderColor(int alpha, int red, int green, int blue) {
        this.borderColor = Color.argb(alpha, red, green, blue);
    }

    public void setRoundness(float roundness) {
        this.roundness = roundness;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setDefaultStyle(boolean defaultStyle) {
        this.defaultStyle = defaultStyle;
    }
}
