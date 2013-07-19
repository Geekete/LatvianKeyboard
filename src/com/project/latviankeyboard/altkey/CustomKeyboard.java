package com.project.latviankeyboard.altkey;

import android.app.Activity;
import android.graphics.Point;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.project.latviankeyboard.R;

import java.util.HashMap;
import java.util.Map;

public class CustomKeyboard {

    private MyKeyboardView keyboardView;
    private Activity hostActivity;
    private static final int[] specialKeysQwerty = {11, 21, 29, 30, 31, 32, 33};
    private static final int[] specialKeysNumbers = {0, 29, 30, 31, 32, 33};
    private static Map<Integer, Character> alphabet = new HashMap<Integer, Character>();
    private static Map<Integer, Character> alterAlphabet = new HashMap<Integer, Character>();
    private static Map<Integer, Character> numberSymbolAlphabet = new HashMap<Integer, Character>();
    private static boolean upperCase = true;
    private static boolean alternative = false;
    private static boolean numbersAndSht = false;

    private Keyboard normal;
    private Keyboard alternate;
    private Keyboard normalLower;
    private Keyboard alternateLower;
    private Keyboard numbersSymbols;




    public CustomKeyboard(Activity hostActivity, MyKeyboardView keyboardView) {
        this.hostActivity = hostActivity;
        this.keyboardView = keyboardView;
        normal = new Keyboard(hostActivity, R.xml.alt_key_keyboard);
        normalLower = new Keyboard(hostActivity, R.xml.alt_key_keyboard_lower);
        alternate = new Keyboard(hostActivity, R.xml.alt_key_keyboard_alternative);
        alternateLower = new Keyboard(hostActivity, R.xml.alt_key_keyboard_alternative_lower);
        numbersSymbols = new Keyboard(hostActivity, R.xml.alt_key_keyboard_numbers);
        keyboardView.setKeyboard(normal);
        keyboardView.setPreviewEnabled(true);
        hostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void hideCustomKeyboard() {
        keyboardView.setVisibility(View.GONE);
        keyboardView.setEnabled(false);
    }

    public void showCustomKeyboard(View v) {
        keyboardView.setVisibility(View.VISIBLE);
        keyboardView.setEnabled(true);
        if (v != null) {
            ((InputMethodManager) hostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public boolean isCustomKeyboardVisible() {
        return keyboardView.getVisibility() == View.VISIBLE;
    }

    public void fillAlphabet() {
        alphabet.put(1, 'Q');
        alphabet.put(2, 'W');
        alphabet.put(3, 'E');
        alphabet.put(4, 'R');
        alphabet.put(5, 'T');
        alphabet.put(6, 'Y');
        alphabet.put(7, 'U');
        alphabet.put(8, 'I');
        alphabet.put(9, 'O');
        alphabet.put(10, 'P');
        alphabet.put(12, 'A');
        alphabet.put(13, 'S');
        alphabet.put(14, 'D');
        alphabet.put(15, 'F');
        alphabet.put(16, 'G');
        alphabet.put(17, 'H');
        alphabet.put(18, 'J');
        alphabet.put(19, 'K');
        alphabet.put(20, 'L');
        alphabet.put(22, 'Z');
        alphabet.put(23, 'X');
        alphabet.put(24, 'C');
        alphabet.put(25, 'V');
        alphabet.put(26, 'B');
        alphabet.put(27, 'N');
        alphabet.put(28, 'M');
    }

    public void fillAlterAlphabet() {
        alterAlphabet.put(1, 'Q');
        alterAlphabet.put(2, 'W');
        alterAlphabet.put(3, 'Ē');
        alterAlphabet.put(4, 'R');
        alterAlphabet.put(5, 'T');
        alterAlphabet.put(6, 'Y');
        alterAlphabet.put(7, 'Ū');
        alterAlphabet.put(8, 'Ī');
        alterAlphabet.put(9, 'O');
        alterAlphabet.put(10, 'P');
        alterAlphabet.put(12, 'Ā');
        alterAlphabet.put(13, 'Š');
        alterAlphabet.put(14, 'D');
        alterAlphabet.put(15, 'F');
        alterAlphabet.put(16, 'Ģ');
        alterAlphabet.put(17, 'H');
        alterAlphabet.put(18, 'J');
        alterAlphabet.put(19, 'Ķ');
        alterAlphabet.put(20, 'Ļ');
        alterAlphabet.put(22, 'Ž');
        alterAlphabet.put(23, 'X');
        alterAlphabet.put(24, 'Č');
        alterAlphabet.put(25, 'V');
        alterAlphabet.put(26, 'B');
        alterAlphabet.put(27, 'Ņ');
        alterAlphabet.put(28, 'M');
    }

    public void fillNumberSymbolAlphabet() {
        numberSymbolAlphabet.put(1, '1');
        numberSymbolAlphabet.put(2, '2');
        numberSymbolAlphabet.put(3, '3');
        numberSymbolAlphabet.put(4, '4');
        numberSymbolAlphabet.put(5, '5');
        numberSymbolAlphabet.put(6, '6');
        numberSymbolAlphabet.put(7, '7');
        numberSymbolAlphabet.put(8, '8');
        numberSymbolAlphabet.put(9, '9');
        numberSymbolAlphabet.put(10, '0');
        numberSymbolAlphabet.put(11, '+');
        numberSymbolAlphabet.put(12, '-');
        numberSymbolAlphabet.put(13, '/');
        numberSymbolAlphabet.put(14, '*');
        numberSymbolAlphabet.put(15, '%');
        numberSymbolAlphabet.put(16, '.');
        numberSymbolAlphabet.put(17, ',');
        numberSymbolAlphabet.put(18, ';');
        numberSymbolAlphabet.put(19, ':');
        numberSymbolAlphabet.put(20, '@');
        numberSymbolAlphabet.put(21, '!');
        numberSymbolAlphabet.put(22, '?');
        numberSymbolAlphabet.put(23, '\'');
        numberSymbolAlphabet.put(24, '"');
        numberSymbolAlphabet.put(25, '\\');
        numberSymbolAlphabet.put(26, '&');
        numberSymbolAlphabet.put(27, '=');
        numberSymbolAlphabet.put(28, '(');
        numberSymbolAlphabet.put(34, ')');
        numberSymbolAlphabet.put(35, '[');
        numberSymbolAlphabet.put(36, ']');
        numberSymbolAlphabet.put(37, '~');
        numberSymbolAlphabet.put(38, '{');
        numberSymbolAlphabet.put(39, '}');
        numberSymbolAlphabet.put(40, '|');
        numberSymbolAlphabet.put(41, '_');
        numberSymbolAlphabet.put(42, '^');
        numberSymbolAlphabet.put(43, '#');
        numberSymbolAlphabet.put(44, '$');
        numberSymbolAlphabet.put(45, '<');
        numberSymbolAlphabet.put(46, '>');

    }
}
