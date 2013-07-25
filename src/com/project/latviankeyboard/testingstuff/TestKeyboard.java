package com.project.latviankeyboard.testingstuff;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.latviankeyboard.R;
import com.project.latviankeyboard.testingstuff.DancingKeyboardView;



public class TestKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

	long longSpeed = 0;
	int intNowScore = 0;
	int countLetters = 0;
	
	String textString = "";
	String textFromOutside = "";
	EditText textBox;
	TextView typeSpeed;
	TextView nowScore;
	
	DrawThread _thread;
	DancingMan dancingMan;
	
	//KeyboardView inputView;
	RelativeLayout rl;
	DancingKeyboardView inputView;

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
		// TODO possible stretching

		keyboardQWERTY = new Keyboard(this, R.xml.extra_row_qwerty);
		keyboardSymbols = new Keyboard(this, R.xml.extra_row_symbols);
		keyboardShiftedSymbols = new Keyboard(this, R.xml.extra_row_nums);
	}
	
	
	
	
	
	

	@Override
	public View onCreateInputView() {
		
		rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.test_ime, null);
		inputView = (DancingKeyboardView) rl.findViewById(R.id.keyboard_test_ime);
		//TextBox initialization
		textBox =(EditText) rl.findViewById(R.id.text_box);
		typeSpeed = (TextView) rl.findViewById(R.id.type_speed);
		nowScore = (TextView) rl.findViewById(R.id.now_score);
		textBox.setText(textString);
		nowScore.setText(String.valueOf(intNowScore));
		dancingMan = (DancingMan) rl.findViewById(R.id.dancing_man);
		
		//inputView = (DancingKeyboardView) getLayoutInflater().inflate(R.layout.extra_row_input, null);
		inputView.setOnKeyboardActionListener(this);
		Log.d("!","set qwerty");
		inputView.setKeyboard(keyboardQWERTY);
		_thread = new DrawThread(dancingMan.getHolder(), dancingMan); //Start the thread that
		_thread.setRunning(true); //will make calls to 
		_thread.start();   //onDraw()
		
		return rl;
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
				// TODO if view is auto completing itself
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
		textFromOutside = getCurrentInputConnection().getExtractedText(new ExtractedTextRequest(), 0).text.toString();
		textBox.setText(textFromOutside);
		textBox.setSelection(textFromOutside.length());

        
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
		setCandidatesViewShown(false);

		keyboardCur = keyboardQWERTY;
		if (inputView != null) {
			inputView.closing();
		}
	}


	
	
	// onkeyboard action listener

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		Log.d("onKey", "spam: "+String.valueOf((char) primaryCode));
		//adding char to dance queue
		_thread.charList.add(primaryCode);
		//Scoring
		char inp = (char) primaryCode;
		inp = String.valueOf(inp).toLowerCase().toCharArray()[0];
		if(intNowScore < 500){
			nowScore.setTextColor(0xFF009900);
		}else if(intNowScore > 500 && intNowScore < 3000){
			nowScore.setTextColor(0xFF999900);
		}else{
			nowScore.setTextColor(0xFFFF0000);
		}
		if(countLetters < 3){
			countLetters += 1;
		}else if(
				primaryCode != Keyboard.KEYCODE_DELETE && 
				primaryCode != Keyboard.KEYCODE_SHIFT &&
				primaryCode != Keyboard.KEYCODE_ALT &&
				primaryCode != Keyboard.KEYCODE_CANCEL){
			countLetters = 0;
			long tempVal = 1000/((SystemClock.currentThreadTimeMillis() - longSpeed)/5);
			if(tempVal <= 50){
				typeSpeed.setText(String.valueOf(tempVal) + " Fair");
				typeSpeed.setTextColor(0xFF0000FF);
			}else if(tempVal > 50 && tempVal <= 60){
				typeSpeed.setText(String.valueOf(tempVal) + " Good");
				typeSpeed.setTextColor(0xFF00FF00);
			}else if(tempVal > 60 && tempVal <= 70){
				typeSpeed.setText(String.valueOf(tempVal) + " Amazing");
				typeSpeed.setTextColor(0xFFFFFF00);
			}else{
				typeSpeed.setText(String.valueOf(tempVal) + " Insane");
				typeSpeed.setTextColor(0xFFFF0000);
			}
			
			
			intNowScore += tempVal;
			longSpeed = SystemClock.currentThreadTimeMillis();
			intNowScore += tempVal;
			nowScore.setText(String.valueOf(intNowScore));
			
		}
		
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
			//getCurrentInputConnection().deleteSurroundingText(1, 0);
			String tempString = textBox.getText().toString();
			int tempPos = textBox.getSelectionStart();
			Log.d("Fatal","tempPos = " + String.valueOf(tempPos));
			if(tempPos == tempString.length() && tempPos != 0){
				tempString = tempString.substring(0,tempString.length() - 1);
				textBox.setText(tempString);
				textBox.setSelection(tempPos-1,tempPos-1);
			}else if(tempPos != 0 && tempPos != -1){
				tempString = tempString.substring(0, tempPos - 1) + tempString.substring(tempPos, tempString.length());
				textBox.setText(tempString);
				textBox.setSelection(tempPos-1,tempPos-1);
			}
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
			//getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
	        //getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
			textBox.append(String.valueOf((char)primaryCode));
			break;
		default:
				if(this.inputView.isShifted()){
					this.inputView.setShifted(false);
					if(textBox.length() == textBox.getSelectionEnd()){
						textBox.append(String.valueOf((char) primaryCode).toUpperCase());
					}else{
						int tempPos1 = textBox.getSelectionStart();
						String tempStr = textBox.getText().toString();
						tempStr = tempStr.substring(0, tempPos1) + String.valueOf((char) primaryCode).toUpperCase() + tempStr.substring(tempPos1, tempStr.length());
						textBox.setText(tempStr);
						textBox.setSelection(tempPos1+1, tempPos1+1);
					}
					//getCurrentInputConnection().commitText(String.valueOf((char) primaryCode).toUpperCase(), 1);
				}else{
					if(textBox.length() == textBox.getSelectionEnd()){
						textBox.append(String.valueOf((char) primaryCode));
					}else{
						int tempPos1 = textBox.getSelectionStart();
						String tempStr = textBox.getText().toString();
						tempStr = tempStr.substring(0, tempPos1) + String.valueOf((char) primaryCode) + tempStr.substring(tempPos1, tempStr.length());
						textBox.setText(tempStr);
						textBox.setSelection(tempPos1+1, tempPos1+1);
						
					}
					//getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
				}
		}
		textString = textBox.getText().toString();
	}
	
	public void hideKeyboard(View v){
		intNowScore = 0;
		getCurrentInputConnection().setSelection(textFromOutside.length(), textFromOutside.length());
		getCurrentInputConnection().deleteSurroundingText(textFromOutside.length(), 0);
		getCurrentInputConnection().commitText(textBox.getText().toString(), 1);
		textBox.setText("");
		this.hideWindow();
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
