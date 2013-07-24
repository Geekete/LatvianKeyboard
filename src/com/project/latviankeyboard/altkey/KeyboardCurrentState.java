package com.project.latviankeyboard.altkey;

import android.inputmethodservice.Keyboard;

public class KeyboardCurrentState {

    private static KeyboardCurrentState ourInstance = new KeyboardCurrentState();

    private boolean upperCase = true;
    private boolean alternative = false;
    private boolean numbersAndSht = false;
    private Keyboard currentKeyboard;

    public static KeyboardCurrentState getInstance() {
        return ourInstance;
    }

    private KeyboardCurrentState() {
    }

    public boolean isUpperCase() {
        return upperCase;
    }

    public void setUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
    }

    public boolean isAlternative() {
        return alternative;
    }

    public void setAlternative(boolean alternative) {
        this.alternative = alternative;
    }

    public boolean isNumbersAndSht() {
        return numbersAndSht;
    }

    public void setNumbersAndSht(boolean numbersAndSht) {
        this.numbersAndSht = numbersAndSht;
    }

    public Keyboard getCurrentKeyboard() {
        return currentKeyboard;
    }

    public void setCurrentKeyboard(Keyboard currentKeyboard) {
        this.currentKeyboard = currentKeyboard;
    }
}
