package com.project.latviankeyboard.altkey;


import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.*;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import com.project.latviankeyboard.R;

import java.util.*;

public class AltKeyKeyboard extends InputMethodService {

    private static MyKeyboardView keyboardView;

    private static final int[] specialKeysQwerty = {11, 21, 29, 30, 32, 33};
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

    private HashMap<Integer, Integer> soundPoolMap;
    private HashMap<Integer, Integer> soundPoolForSpecialsMap;
    private SoundPool soundPool;
    private SoundPool soundPoolForSpecials;
    private int soundID = 1;
    int streamVolume;

    private OnKeyboardActionListener onKeyboardActionListener = new OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
            boolean specialKey = false;

            if (keyboardView.isSwitchSounds()) {
                if (primaryCode == 32) {
                    specialKey = !specialKey;
                    soundPoolForSpecials.play(1, streamVolume, streamVolume, 1, 0, 1);
                }
                if (primaryCode == 33) {
                    specialKey = !specialKey;
                    soundPoolForSpecials.play(2, streamVolume, streamVolume, 1, 0, 1);
                }
                if (primaryCode == 30) {
                    specialKey = !specialKey;
                    soundPoolForSpecials.play(3, streamVolume, streamVolume, 1, 0, 1);
                }
                if (primaryCode == 29) {
                    specialKey = !specialKey;
                    soundPoolForSpecials.play(4, streamVolume, streamVolume, 1, 0, 1);
                }
                if (!specialKey) {
                    soundPool.play(soundID++, streamVolume, streamVolume, 1, 0, 1);
                    if (soundID == 8) {
                        soundID = 1;
                    }
                }
            }

            boolean special = false;

            if (!numbersAndSht) {
                for (int i : specialKeysQwerty) {
                    if (primaryCode == i) {
                        special = !special;
                    }
                }
            } else {
                for (int i : specialKeysNumbers) {
                    if (primaryCode == i) {
                        special = !special;
                    }
                }
            }
            if (!special) {
                char c;
                if (!numbersAndSht) {
                    if (alternative) {
                        c = alterAlphabet.get(primaryCode);
                    } else {
                        c = alphabet.get(primaryCode);
                    }
                    if (!upperCase) {
                        c = Character.toLowerCase(c);
                    }
                } else {
                    c = numberSymbolAlphabet.get(primaryCode);
                }
                getCurrentInputConnection().commitText(Character.toString(c), 1);
            } else {
                if (!numbersAndSht) {
                    switch (primaryCode) {
                        case 11: {
                            if (!alternative) {
                                if (upperCase) {
                                    keyboardView.setKeyboard(alternate);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(alternate);
                                } else {
                                    keyboardView.setKeyboard(alternateLower);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(alternateLower);
                                }
                            } else {
                                if (upperCase) {
                                    keyboardView.setKeyboard(normal);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(normal);
                                } else {
                                    keyboardView.setKeyboard(normalLower);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(normalLower);
                                }
                            }
                            alternative = !alternative;
                            KeyboardCurrentState.getInstance().setAlternative(alternative);
                            break;
                        }
                        case 21: {

                            upperCase = !upperCase;
                            KeyboardCurrentState.getInstance().setUpperCase(upperCase);
                            if (upperCase) {
                                if (alternative) {
                                    keyboardView.setKeyboard(alternate);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(alternate);
                                } else {
                                    keyboardView.setKeyboard(normal);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(normal);
                                }
                            } else {
                                if (alternative) {
                                    keyboardView.setKeyboard(alternateLower);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(alternateLower);
                                } else {
                                    keyboardView.setKeyboard(normalLower);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(normalLower);
                                }
                            }
                            break;
                        }
                        case 29: {
                            getCurrentInputConnection().deleteSurroundingText(1, 0);
                            break;
                        }
                        case 30: {
                            numbersAndSht = !numbersAndSht;
                            KeyboardCurrentState.getInstance().setNumbersAndSht(numbersAndSht);
                            keyboardView.setKeyboard(numbersSymbols);
                            KeyboardCurrentState.getInstance().setCurrentKeyboard(numbersSymbols);
                            break;
                        }
                        case 32: {
                            getCurrentInputConnection().commitText(" ", 1);
                            break;
                        }
                        case 33: {
                            getCurrentInputConnection().commitText("\n", 1);
                            break;
                        }
                    }

                } else {
                    switch (primaryCode) {
                        case 0: {
                            break;
                        }
                        case 29: {
                            getCurrentInputConnection().deleteSurroundingText(1, 0);
                            break;
                        }
                        case 30: {
                            numbersAndSht = !numbersAndSht;
                            KeyboardCurrentState.getInstance().setNumbersAndSht(numbersAndSht);
                            if (upperCase) {
                                if (alternative) {
                                    keyboardView.setKeyboard(alternate);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(alternate);
                                } else {
                                    keyboardView.setKeyboard(normal);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(normal);
                                }
                            } else {
                                if (alternative) {
                                    keyboardView.setKeyboard(alternateLower);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(alternateLower);
                                } else {
                                    keyboardView.setKeyboard(normalLower);
                                    KeyboardCurrentState.getInstance().setCurrentKeyboard(normalLower);
                                }
                            }
                            break;
                        }
                        case 32: {
                            getCurrentInputConnection().commitText(" ", 1);
                            break;
                        }
                        case 33: {
                            getCurrentInputConnection().commitText("\n", 1);
                            break;
                        }
                    }
                }
            }


        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        fillAlphabet();
        fillAlterAlphabet();
        fillNumberSymbolAlphabet();
        soundPool = new SoundPool(4, AudioManager.STREAM_RING, 100);
        soundPoolForSpecials = new SoundPool(4, AudioManager.STREAM_RING, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolForSpecialsMap = new HashMap<Integer, Integer>();
    }

    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();

        normal = new Keyboard(this, R.xml.alt_key_keyboard);
        normalLower = new Keyboard(this, R.xml.alt_key_keyboard_lower);
        alternate = new Keyboard(this, R.xml.alt_key_keyboard_alternative);
        alternateLower = new Keyboard(this, R.xml.alt_key_keyboard_alternative_lower);
        numbersSymbols = new Keyboard(this, R.xml.alt_key_keyboard_numbers);
    }

    @Override
    public View onCreateInputView() {
        keyboardView = (MyKeyboardView) getLayoutInflater().inflate(R.layout.alt_key_input, null);

        soundPoolMap.put(1, soundPool.load(keyboardView.getContext(), R.raw.ba1, 1));
        soundPoolMap.put(2, soundPool.load(keyboardView.getContext(), R.raw.ba1, 1));
        soundPoolMap.put(3, soundPool.load(keyboardView.getContext(), R.raw.ba1, 1));
        soundPoolMap.put(4, soundPool.load(keyboardView.getContext(), R.raw.ba1, 1));
        soundPoolMap.put(5, soundPool.load(keyboardView.getContext(), R.raw.na1, 1));
        soundPoolMap.put(6, soundPool.load(keyboardView.getContext(), R.raw.na1, 1));
        soundPoolMap.put(7, soundPool.load(keyboardView.getContext(), R.raw.na1, 1));

        soundPoolForSpecialsMap.put(1, soundPoolForSpecials.load(keyboardView.getContext(), R.raw.bananaaa1, 1));
        soundPoolForSpecialsMap.put(2, soundPoolForSpecials.load(keyboardView.getContext(), R.raw.laugh, 1));
        soundPoolForSpecialsMap.put(3, soundPoolForSpecials.load(keyboardView.getContext(), R.raw.bee_do, 1));
        soundPoolForSpecialsMap.put(4, soundPoolForSpecials.load(keyboardView.getContext(), R.raw.noo, 1));
        soundPoolForSpecialsMap.put(5, soundPoolForSpecials.load(keyboardView.getContext(), R.raw.lullaby, 1));

        if (KeyboardCurrentState.getInstance().isNumbersAndSht()) {
            keyboardView.setKeyboard(numbersSymbols);
        } else {
            if (KeyboardCurrentState.getInstance().isAlternative()) {
                if (KeyboardCurrentState.getInstance().isUpperCase()) {
                    keyboardView.setKeyboard(alternate);
                } else {
                    keyboardView.setKeyboard(alternateLower);
                }
            } else {
                if (KeyboardCurrentState.getInstance().isUpperCase()) {
                    keyboardView.setKeyboard(normal);
                } else {
                    keyboardView.setKeyboard(normalLower);
                }
            }
        }
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);
        return keyboardView;
    }

    @Override
    public void onUpdateExtractingVisibility(EditorInfo ei) {
        ei.imeOptions |= EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        super.onUpdateExtractingVisibility(ei);
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        switch (attribute.inputType & EditorInfo.TYPE_MASK_CLASS) {

            case EditorInfo.TYPE_CLASS_NUMBER: {
                if (KeyboardCurrentState.getInstance().getCurrentKeyboard() == null) {
                    KeyboardCurrentState.getInstance().setCurrentKeyboard(numbersSymbols);
                }
                break;
            }
            case EditorInfo.TYPE_CLASS_DATETIME: {
                if (KeyboardCurrentState.getInstance().getCurrentKeyboard() == null) {
                    KeyboardCurrentState.getInstance().setCurrentKeyboard(numbersSymbols);
                }
                break;
            }

            case EditorInfo.TYPE_CLASS_PHONE: {
                if (KeyboardCurrentState.getInstance().getCurrentKeyboard() == null) {
                    KeyboardCurrentState.getInstance().setCurrentKeyboard(numbersSymbols);
                }
                break;
            }
            case EditorInfo.TYPE_CLASS_TEXT: {
                if (KeyboardCurrentState.getInstance().getCurrentKeyboard() == null) {
                    KeyboardCurrentState.getInstance().setCurrentKeyboard(normal);
                }
                int variation = attribute.inputType & EditorInfo.TYPE_MASK_VARIATION;

                if (variation == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD || variation == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // TODO if entering password
                }

                if (variation == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS || variation == EditorInfo.TYPE_TEXT_VARIATION_URI || variation == EditorInfo.TYPE_TEXT_VARIATION_FILTER) {
                    // TODO if entering email or URL
                }

                if ((attribute.inputType & EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    // TODO if view is autocompleteing itself
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);

        if (KeyboardCurrentState.getInstance().isNumbersAndSht()) {
            keyboardView.setKeyboard(numbersSymbols);
        } else {
            if (KeyboardCurrentState.getInstance().isAlternative()) {
                if (KeyboardCurrentState.getInstance().isUpperCase()) {
                    keyboardView.setKeyboard(alternate);
                } else {
                    keyboardView.setKeyboard(alternateLower);
                }
            } else {
                if (KeyboardCurrentState.getInstance().isUpperCase()) {
                    keyboardView.setKeyboard(normal);
                } else {
                    keyboardView.setKeyboard(normalLower);
                }
            }
        }
        keyboardView.closing();
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        setCandidatesViewShown(false);

        if (keyboardView != null) {
            keyboardView.closing();
        }
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
        alphabet.put(31, '.');
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
        alterAlphabet.put(31, '.');
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