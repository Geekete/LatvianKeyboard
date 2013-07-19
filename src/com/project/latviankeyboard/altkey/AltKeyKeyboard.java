package com.project.latviankeyboard.altkey;

import android.app.Activity;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.*;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.project.latviankeyboard.R;

import java.util.HashMap;
import java.util.Map;

public class AltKeyKeyboard extends InputMethodService {

    MyKeyboardView keyboardView;

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

    private Keyboard currentKeyboard;

    private OnKeyboardActionListener onKeyboardActionListener = new OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            View focusCurrent = getWindow().getCurrentFocus();
            if (focusCurrent == null || focusCurrent.getClass() != EditText.class) {
                return;
            }
            EditText edittext = (EditText) focusCurrent;
            Editable editable = edittext.getText();
            int start = edittext.getSelectionStart();

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
                editable.insert(start, Character.toString(c));
            } else {
                if (!numbersAndSht) {
                    switch (primaryCode) {
                        case 11: {
                            if (!alternative) {
                                if (upperCase) {
                                    keyboardView.setKeyboard(alternate);
                                    currentKeyboard = alternate;
                                } else {
                                    keyboardView.setKeyboard(alternateLower);
                                    currentKeyboard = alternateLower;
                                }
                            } else {
                                if (upperCase) {
                                    keyboardView.setKeyboard(normal);
                                    currentKeyboard = normal;
                                } else {
                                    keyboardView.setKeyboard(normalLower);
                                    currentKeyboard = normalLower;
                                }
                            }
                            alternative = !alternative;
                            break;
                        }
                        case 21: {

                            upperCase = !upperCase;
                            if (upperCase) {
                                if (alternative) {
                                    keyboardView.setKeyboard(alternate);
                                    currentKeyboard = alternate;
                                } else {
                                    keyboardView.setKeyboard(normal);
                                    currentKeyboard = normal;
                                }
                            } else {
                                if (alternative) {
                                    keyboardView.setKeyboard(alternateLower);
                                    currentKeyboard = alternateLower;
                                } else {
                                    keyboardView.setKeyboard(normalLower);
                                    currentKeyboard = normalLower;
                                }
                            }
                            break;
                        }
                        case 29: {
                            if (editable != null && start > 0) {
                                editable.delete(start - 1, start);
                            }
                            break;
                        }
                        case 30: {
                            numbersAndSht = !numbersAndSht;
                            keyboardView.setKeyboard(numbersSymbols);
                            currentKeyboard = numbersSymbols;
                            break;
                        }
                        case 32: {
                            editable.insert(start, " ");
                            break;
                        }
                        case 33: {
                            editable.insert(start, "\n");
                            break;
                        }
                    }

                } else {
                    switch (primaryCode) {
                        case 0: {
                            break;
                        }
                        case 29: {
                            if (editable != null && start > 0) {
                                editable.delete(start - 1, start);
                            }
                            break;
                        }
                        case 30: {
                            numbersAndSht = !numbersAndSht;
                            if (upperCase) {
                                if (alternative) {
                                    keyboardView.setKeyboard(alternate);
                                    currentKeyboard = alternate;
                                } else {
                                    keyboardView.setKeyboard(normal);
                                    currentKeyboard = normal;
                                }
                            } else {
                                if (alternative) {
                                    keyboardView.setKeyboard(alternateLower);
                                    currentKeyboard = alternateLower;
                                } else {
                                    keyboardView.setKeyboard(normalLower);
                                    currentKeyboard = normalLower;
                                }
                            }
                            break;
                        }
                        case 32: {
                            editable.insert(start, " ");
                            break;
                        }
                        case 33: {
                            editable.insert(start, "\n");
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
        keyboardView.setKeyboard(normal);
        currentKeyboard = normal;
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
                currentKeyboard = numbersSymbols;
                break;
            }
            case EditorInfo.TYPE_CLASS_DATETIME: {
                currentKeyboard = numbersSymbols;
                break;
            }

            case EditorInfo.TYPE_CLASS_PHONE: {
                currentKeyboard = numbersSymbols;
                break;
            }
            case EditorInfo.TYPE_CLASS_TEXT: {
                currentKeyboard = normal;
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
                currentKeyboard = normal;
                break;
            }
        }
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);

        keyboardView.setKeyboard(currentKeyboard);
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