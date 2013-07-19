package com.project.latviankeyboard.extrarow;


import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.method.MetaKeyKeyListener;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import com.project.latviankeyboard.R;



public class ExtraRowKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

	//KeyboardView inputView;
	MyKeyboardView inputView;

	/*
	LVKeyboardERow keyboardCur;
	LVKeyboardERow keyboardQWERTY;
	LVKeyboardERow keyboardSymbols;
	LVKeyboardERow keyboardShiftedSymbols;
	 */
	Keyboard keyboardCur;
	Keyboard keyboardQWERTY;
	Keyboard keyboardSymbols;
	Keyboard keyboardShiftedSymbols;
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	
	
	
	
	
	@Override
	public void onInitializeInterface() {

		keyboardQWERTY = new Keyboard(this, R.xml.extra_row_qwerty);
		keyboardSymbols = new Keyboard(this, R.xml.extra_row_symbols);
		keyboardShiftedSymbols = new Keyboard(this, R.xml.extra_row_symbols_shift);
	}
	
	
	
	
	
	

	@Override
	public View onCreateInputView() {
		inputView = (MyKeyboardView) getLayoutInflater().inflate(R.layout.extra_row_input, null);
		inputView.setOnKeyboardActionListener(this);
		Log.d("!","set qwerty");
		keyboardCur = keyboardQWERTY;
		inputView.setKeyboard(keyboardCur);
		return inputView;
	}

	
	
	
	
	
	
	@Override
	public void onStartInput(EditorInfo attribute, boolean restarting) {
		super.onStartInput(attribute, restarting);

		// if (!restarting) {
		// Clear shift states.
		// mMetaState = 0;
		// }

		// initialize our state based on the type of text being edited.
		switch (attribute.inputType & EditorInfo.TYPE_MASK_CLASS) {

		case EditorInfo.TYPE_CLASS_NUMBER:
		case EditorInfo.TYPE_CLASS_DATETIME:
			// Numbers and dates default to the symbols keyboard, with no extra
			// features.
			keyboardCur = keyboardSymbols;
			break;

		case EditorInfo.TYPE_CLASS_PHONE:
			// Phones will also default to the symbols keyboard, though
			// often you will want to have a dedicated phone keyboard.
			keyboardCur = keyboardSymbols;
			break;

		case EditorInfo.TYPE_CLASS_TEXT:
			// This is general text editing. We will default to the
			// normal alphabetic keyboard, and assume that we should
			// be doing predictive text (showing candidates as the
			// user types).
			keyboardCur = keyboardQWERTY;

			// looking for a few special variations of text that will modify our
			// behavior.
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

			// We also want to look at the current state of the editor
			// to decide whether our alphabetic keyboard should start out
			// shifted.
			// //////////////////////////////////////updateShiftKeyState(attribute);
			break;

		default:
			// For all unknown input types, default to the alphabetic
			// keyboard with no special features.
			keyboardCur = keyboardQWERTY;
			// ////////////////////updateShiftKeyState(attribute);
		}

		// Update the label on the enter key, depending on what the application says it will do.
		//keyboardCur.setImeOptions(getResources(), attribute.imeOptions);

	}
	
	
	
	
	
	
	
	

	@Override
	public void onStartInputView(EditorInfo info, boolean restarting) {
		super.onStartInputView(info, restarting);

		inputView.setKeyboard(keyboardCur);
		inputView.closing();
	}
	
	
	
	

	/**
	 * Deal with the editor reporting movement of its cursor.
	 */
	/*
	 * @Override public void onUpdateSelection(int oldSelStart, int oldSelEnd,
	 * int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
	 * super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
	 * candidatesStart, candidatesEnd);
	 * 
	 * // If the current selection in the text view changes, we should // clear
	 * whatever candidate text we have. if (mComposing.length() > 0 &&
	 * (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
	 * mComposing.setLength(0); //updateCandidates(); InputConnection ic =
	 * getCurrentInputConnection(); if (ic != null) { ic.finishComposingText();
	 * } } }
	 */

	
	
	
	
	
	/*
	 * //monitor [hard] key events being delivered to the application
	 * 
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { return
	 * super.onKeyDown(keyCode, event); }
	 * 
	 * 
	 * @Override public boolean onKeyUp(int keyCode, KeyEvent event) { return
	 * super.onKeyUp(keyCode, event); }
	 */

	
	
	
	
	
	
	// called when user is done editing the field
	@Override
	public void onFinishInput() {
		super.onFinishInput();

		// We only hide the candidates window when finishing input on
		// a particular editor, to avoid popping the underlying application
		// up and down if the user is entering text into the bottom of
		// its window.
		//setCandidatesViewShown(false);

		keyboardCur = keyboardQWERTY;
		if (inputView != null) {
			inputView.closing();
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// onkeyboard action listener

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		Log.d("onKey", "spam: "+String.valueOf((char) primaryCode));
		
		switch(primaryCode){
		case Keyboard.KEYCODE_MODE_CHANGE:
			if(keyboardCur == keyboardQWERTY){
				keyboardCur = keyboardSymbols;
			}else if(keyboardCur == keyboardSymbols || keyboardCur == keyboardShiftedSymbols){
				keyboardCur = keyboardQWERTY;
			}
			inputView.setKeyboard(keyboardCur);
			break;
		case Keyboard.KEYCODE_DELETE:
			getCurrentInputConnection().deleteSurroundingText(1, 0);
			//return;
			break;
		case Keyboard.KEYCODE_SHIFT:
			if(this.inputView.isShifted())
				this.inputView.setShifted(false);
			else
				this.inputView.setShifted(true);
			break;
		case '\n':
			//KeyEvent.KEYCODE_ENTER
			getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
	        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
			break;
		default:
				if(this.inputView.isShifted()){
					this.inputView.setShifted(false);
					getCurrentInputConnection().commitText(String.valueOf((char) primaryCode).toUpperCase(), 1);
				}else{
					getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
				}
		}
		
		
	}
	
	
	
	

	@Override
	public void onPress(int primaryCode) {
		Log.d("onPress", "spam: "+String.valueOf((char) primaryCode));
	}

	@Override
	public void onRelease(int primaryCode) {
		Log.d("onRelease", "spam: "+String.valueOf((char) primaryCode));

	}

	@Override
	public void onText(CharSequence text) {
		Log.d("onText", "spam: "+text);

	}

	@Override
	public void swipeDown() {

	}

	@Override
	public void swipeLeft() {

	}

	@Override
	public void swipeRight() {

	}

	@Override
	public void swipeUp() {

	}

	
	
	
	
	
	
    /////disables edittext from requesting full screen keyboard;
    @Override
	public void onUpdateExtractingVisibility(EditorInfo ei) {
    	ei.imeOptions |= EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        super.onUpdateExtractingVisibility(ei);
	}
	
	
}
