package com.project.latviankeyboard.extrarow;


import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.Keyboard.Row;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.PopupWindow;


public class MyKeyboardView extends View implements OnTouchListener{

	Context ctx;
	Keyboard keyboard;
	Key[] keys;

	OnKeyboardActionListener keyboardActionListener;

	//boolean isPreviewShown;
	Vibrator vibrator;

	private static final int NOT_A_KEY = -1;


	boolean kbdDrawPending;
	boolean kbdChanged;
	Bitmap kbdBuffer;
	Canvas kbdCanv;



	PopupWindow popupWin;
	//boolean isPopupShowing = false;
	//LinearLayout ll;
	//TextView popTv1;
	//TextView popTv2;
	FloatyView popupContent;

	//////
	boolean touchingKey = false;
	int touchingKeyIndex = NOT_A_KEY;
	long timeTouchedKey = 0;


	int displayWidth;
	
	boolean isHapticOn = true;
	int buttonHeight = 45;
	int waitTime = 200;
	int backgroundColor;
	int btnBackgroundColor;
	int btnBackgroundHoverColor;
	int btnBorderColor;
	int btnTextColor;
	int textSize;
	int btnPadding;
	int btnRoundedness;
	
	
	
	Key keyLongClicked;
	Runnable keyLongClick = new Runnable(){
		@Override
		public void run() {
			Log.d("!","..............runnable");
			if(MyKeyboardView.this.keyLongClicked.repeatable){
				vibrate();
				keyboardActionListener.onKey(keyLongClicked.codes[0], null);
				MyKeyboardView.this.postDelayed(MyKeyboardView.this.keyLongClick, waitTime);
			}else if(!MyKeyboardView.this.popupWin.isShowing() && keyLongClicked.popupCharacters != null){
				vibrate();
				
				int[] loc = new int[2];
				MyKeyboardView.this.getLocationOnScreen(loc);
				
				CharSequence cs = keyLongClicked.popupCharacters.subSequence(0, keyLongClicked.popupCharacters.length());
				//synchronized(MyKeyboardView.this.keyLongClicked.popupCharacters){
				MyKeyboardView.this.popupContent.setKeys(cs, keyLongClicked.popupResId);
				//}
				
				int posX = keyLongClicked.x - MyKeyboardView.this.popupWin.getWidth()/2 + keyLongClicked.width/2;
				int posY = keyLongClicked.y - MyKeyboardView.this.popupWin.getHeight()-5;
				if(posX <= 0)
					posX = 5;
				if(posX + MyKeyboardView.this.popupWin.getWidth() >= MyKeyboardView.this.displayWidth)
					posX = MyKeyboardView.this.displayWidth - MyKeyboardView.this.popupWin.getWidth() - 5;
				if(posY + MyKeyboardView.this.buttonHeight < 0)
					posY = - MyKeyboardView.this.buttonHeight;
				
				Log.d("!","key.x:"+keyLongClicked.x+" key.y:"+keyLongClicked.y+"\tpopupHeight:"+MyKeyboardView.this.popupWin.getHeight()+" popupWidth:"+MyKeyboardView.this.popupWin.getWidth()+"\tpopupX:"+posX+" popupY:"+posY);
				
				MyKeyboardView.this.popupWin.showAtLocation(MyKeyboardView.this, Gravity.NO_GRAVITY, posX, posY);
				//this.popupWin.showAtLocation(this, Gravity.NO_GRAVITY, k.x, k.y - this.popupWin.getHeight()-5);
				//Log.d("!","showing popup ... "+this.popupWin.isShowing());
				MyKeyboardView.this.invalidate();
			}
		}
	};
	/*
	public MyKeyboardView(Context context) {
		super(context);
		this.ctx = context;
	}*/

	public MyKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ctx = context;
		this.setOnTouchListener(this);
		vibrator = (Vibrator) ctx.getSystemService(Service.VIBRATOR_SERVICE);
		Log.d("!","ceil 4,3: "+Math.ceil(4.3));

		android.view.LayoutInflater inflater = (android.view.LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*
		ll = (LinearLayout) inflater.inflate(R.layout.popup_test, null);
		popTv1 = (TextView) ll.findViewById(R.id.tv1);
		popTv2 = (TextView) ll.findViewById(R.id.tv2);
		ll.setBackgroundColor(Color.GREEN);
		*/
		
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		popupContent = new FloatyView(context, this, display.getWidth()/10);
		//popupContent = new FloatyView(context, this, 30, 45);
		this.popupWin = new PopupWindow(context);
		//this.popupWin.setContentView(ll);
		this.popupWin.setContentView(popupContent);
		this.popupWin.setBackgroundDrawable(new BitmapDrawable(getResources()));
		this.popupWin.setTouchable(true);
		//this.popupWin.setHeight(100);
		//this.popupWin.setWidth(100);
		//this.popupWin.setFocusable(true);
		//ll.setOnTouchListener(this);
		//this.popupWin.setFocusable(true);
		//this.popupWin.setOutsideTouchable(true);
		//this.popupWin.setSplitTouchEnabled(true);
		
		/*
		this.popupContent.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent me) {
				//Log.d("!","YAUZA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				Log.d("!","YAUZA!!!!!!!!!!!!!!!!! touch x"+me.getX(0)+" y:"+me.getY(0)+" view_id: "+v.getId());
				if (me.getActionMasked() == MotionEvent.ACTION_UP){
					Log.d("!","ACTION_UP");
					
					MyKeyboardView.this.touchingKey = false;
					MyKeyboardView.this.touchingKeyIndex = NOT_A_KEY;
					MyKeyboardView.this.timeTouchedKey = 0;

					if (MyKeyboardView.this.popupWin.isShowing())
						MyKeyboardView.this.popupWin.dismiss();
					
					MyKeyboardView.this.invalidate();
				}
				return true;
			}
			
		});
		*/
		
		/*
		this.popupWin.setTouchInterceptor(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d("!","YAUZA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				return true;
			}
			
		});
		*/
		
		setDefaultValues();
	}


	public void setDefaultValues(){
		//this is done to make certain that if invalid values are passed, some settings are set
		//A. is going to change THIS HELLISHLY GREEN THINGY		
		this.isHapticOn = true;
		this.buttonHeight = 70;
		this.waitTime = 200;
		this.backgroundColor = new Color().argb(255,0,255,0);
		this.btnBackgroundColor = new Color().argb(255,255,255,0);
		this.btnBackgroundHoverColor = new Color().argb(255,200,200,0);
		this.btnBorderColor = new Color().argb(255, 29, 105, 143);
		this.btnTextColor = new Color().argb(255,0,0,255);
		this.textSize = 25;
		this.btnPadding = 5;
		this.btnRoundedness = 8;
	}
	
	
	
	/**
	 * this method assumes that all passed parameters are correct
	 * @param isHapticOn
	 * @param buttonHeight
	 * @param waitTime
	 * @param backgroundColor
	 * @param btnBackgroundColor
	 * @param btnBackgroundHoverColor
	 * @param btnBorderColor
	 * @param btnTextColor
	 * @param textSize
	 * @param btnPadding
	 * @param btnRoundedness
	 */
	public void setValues(boolean isHapticOn, int buttonHeight, int waitTime, int backgroundColor, int btnBackgroundColor, int btnBackgroundHoverColor, int btnBorderColor, int btnTextColor, int textSize, int btnPadding, int btnRoundedness){
		this.isHapticOn = isHapticOn; //vibracīja
		this.buttonHeight = buttonHeight; 
		this.waitTime = waitTime; //wait time for the pop up window and time for backspace
		this.backgroundColor = backgroundColor; //main background where the buttons are
		this.btnBackgroundColor = btnBackgroundColor; //button background color
		this.btnBackgroundHoverColor = btnBackgroundHoverColor; //color when holding button
		this.btnBorderColor = btnBorderColor; //when holding a button the border color
		this.btnTextColor = btnTextColor; //button text color
		this.textSize = textSize; //letter size
		this.btnPadding = btnPadding; //space between buttons
		this.btnRoundedness = btnRoundedness; //roundness of buttons
		
		
	}
	
	

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		kbdDrawPending = true;
		Log.d("!", "onmeasure");
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		this.displayWidth = display.getWidth();
		
		if (keyboard == null) {
			//setMeasuredDimension(mPaddingLeft + mPaddingRight, mPaddingTop + mPaddingBottom);
			setMeasuredDimension(display.getWidth(), 100);
			Log.d("!", "onmeasure ... null width:"+display.getWidth()+" height:"+100);
		} else {
			//int width = keyboard.getMinWidth();
			//if (MeasureSpec.getSize(widthMeasureSpec) < width + 10) {
			//    width = MeasureSpec.getSize(widthMeasureSpec);
			//}
			Key k = this.keys[this.keys.length-1];
			setMeasuredDimension(display.getWidth(), k.y+k.height);
			//setMeasuredDimension(display.getWidth(), keyboard.getHeight());
			
			//Log.d("!", "onmeasure ... not null width:"+display.getWidth()+" height:"+keyboard.getHeight());
		}

		
	}





	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(this.popupWin.isShowing())
			this.popupWin.dismiss();
	}


	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d("!", "onDraw");
		if (kbdDrawPending || kbdBuffer == null || kbdChanged) {
			onBufferDraw();
		}
		canvas.drawBitmap(kbdBuffer, 0, 0, null);

		if(this.touchingKey && !this.popupWin.isShowing()){
			/*
			Paint p = new Paint();
			p.setColor(Color.GREEN);
			p.setStrokeWidth(4);        
			p.setStyle(Paint.Style.STROKE);      
			Key k = keys[this.touchingKeyIndex];
			canvas.drawRect(k.x, k.y, k.x+k.width, k.y+k.height, p);
			*/
			drawButton(canvas, keys[this.touchingKeyIndex], true);
		}
		if(this.popupWin.isShowing()){
			Paint p = new Paint();
			p.setColor(new Color().argb(150,0,0,0));
            canvas.drawRect(0, 0, getWidth(), getHeight(), p);
		}
	}


	private void onBufferDraw() {
		Log.d("!","redrawing canvas");

		if (kbdBuffer == null || kbdChanged) {
			if (kbdBuffer == null || kbdChanged && (kbdBuffer.getWidth() != getWidth() || kbdBuffer.getHeight() != getHeight())) {
				// Make sure our bitmap is at least 1x1
				final int width = Math.max(1, getWidth());
				final int height = Math.max(1, getHeight());
				kbdBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
				kbdCanv = new Canvas(kbdBuffer);
			}
			invalidateAllKeys();
			kbdChanged = false;
		}



		kbdChanged = false;

		final Canvas canvas = kbdCanv;

		if (keyboard == null) return;

		//final Key[] keys = keyboard.getKeys().toArray(new Key[keyboard.getKeys().size()]);
		Paint labelPaint = new Paint();
		labelPaint.setColor(Color.WHITE);

		//Paint btnBgPaint = new Paint();
		//btnBgPaint.setColor(Color.GRAY);

		canvas.drawColor(backgroundColor);
		//canvas.drawColor(0x00000000, PorterDuff.Mode.CLEAR);

		//final int keyCount = keys.length;
		for (int i = 0; i < keys.length; i++) {
			final Key key = keys[i];
			drawButton(canvas, key, false);
		}
		//mInvalidatedKey = null;

		/*
        if (DEBUG && mShowTouchPoints) {
            paint.setAlpha(128);
            paint.setColor(0xFFFF0000);
            canvas.drawCircle(mStartX, mStartY, 3, paint);
            canvas.drawLine(mStartX, mStartY, mLastX, mLastY, paint);
            paint.setColor(0xFF0000FF);
            canvas.drawCircle(mLastX, mLastY, 3, paint);
            paint.setColor(0xFF00FF00);
            canvas.drawCircle((mStartX + mLastX) / 2, (mStartY + mLastY) / 2, 2, paint);
        }
		 */

		kbdDrawPending = false;
		//mDirtyRect.setEmpty();
	}



	public void drawButton(Canvas canv, Key k, boolean border){
		Paint labelPaint = new Paint();
		labelPaint.setColor(btnTextColor);
		labelPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

		Paint btnBgPaint = new Paint();
		if(border){
			btnBgPaint.setColor(btnBackgroundHoverColor);
			//btnBgPaint.setColor(new Color().argb(255,160,160,160));
		}else{
			btnBgPaint.setColor(btnBackgroundColor);
			//btnBgPaint.setColor(new Color().argb(255,120,120,120));
		}
		btnBgPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

		String label = k.label == null? null : adjustCase(k.label).toString();
		//String label = k.label+"";//TODO shift

		if(border){
			Paint btnBorderPaint = new Paint();
			btnBorderPaint.setColor(btnBorderColor);
			//btnBorderPaint.setColor(new Color().argb(255, 29, 205, 243));
			btnBorderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
			int borderPadding = btnPadding/2;
			if(borderPadding < 1)
				borderPadding = 1;
			canv.drawRoundRect(new RectF(k.x + borderPadding, k.y + borderPadding, k.x + k.width - borderPadding, k.y + k.height - borderPadding), btnRoundedness+2, btnRoundedness+2, btnBorderPaint);
		}
		
		if(!border)
			btnBgPaint.setShadowLayer(3, 0, 0, new Color().argb(170, 0, 0, 0));
		canv.drawRoundRect(new RectF(k.x + btnPadding, k.y + btnPadding, k.x + k.width - btnPadding, k.y + k.height - btnPadding), btnRoundedness, btnRoundedness, btnBgPaint);
		//canv.drawRect(k.x + 3, k.y + 3, k.x + k.width - 3, k.y + k.height - 3, btnBgPaint);
		btnBgPaint.setShadowLayer(0, 0, 0, 0);

		if (label != null) {
			// For characters, use large font. For labels like "Done", use small font.
			if (label.length() > 1 && k.codes.length < 2) {
				labelPaint.setTextSize(textSize);
				labelPaint.setTypeface(Typeface.DEFAULT_BOLD);
			} else {
				labelPaint.setTextSize(textSize);
				labelPaint.setTypeface(Typeface.DEFAULT);
			}
			// Draw a drop shadow for the text
			labelPaint.setShadowLayer(7, 0, 0, new Color().argb(135, 0, 0, 0));
			// Draw the text
			canv.drawText(label,(k.width) / 2 + k.x - (labelPaint.measureText(label)/2), (k.height) / 2 + (labelPaint.getTextSize() - labelPaint.descent()) / 2 + k.y, labelPaint);
			// Turn off drop shadow
			labelPaint.setShadowLayer(0, 0, 0, 0);
		} else if (k.icon != null) {
			/*
        	final int drawableX = (key.width - padding.left - padding.right 
                            - key.icon.getIntrinsicWidth()) / 2 + padding.left;
            final int drawableY = (key.height - padding.top - padding.bottom 
                    - key.icon.getIntrinsicHeight()) / 2 + padding.top;
            canvas.translate(drawableX, drawableY);
            key.icon.setBounds(0, 0, 
                    key.icon.getIntrinsicWidth(), key.icon.getIntrinsicHeight());
            key.icon.draw(canvas);
            canvas.translate(-drawableX, -drawableY);
			 */
		}
		Paint popCharPaint = new Paint();
		popCharPaint.setColor(btnTextColor);
		popCharPaint.setTextSize(15);
		popCharPaint.setTypeface(Typeface.DEFAULT);
		popCharPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		if(k.popupCharacters != null){
			canv.drawText("...", k.x + 1.25f*((k.width) / 2)  - (popCharPaint.measureText("...")/2) - btnPadding*0.5f , /*1.5f*((k.height) / 2) +*/k.y + (popCharPaint.getTextSize() - popCharPaint.descent()) + btnPadding*0.5f /* / 2*/, popCharPaint);
		}
	}



	private CharSequence adjustCase(CharSequence label) {
		if (keyboard.isShifted() && label != null && label.length() < 3 && Character.isLowerCase(label.charAt(0))) {
			label = label.toString().toUpperCase();
		}
		return label;
	}

	public void closing() {
		//if (mPreviewPopup.isShowing()) {
		//    mPreviewPopup.dismiss();
		//}
		//removeMessages();

		//dismissPopupKeyboard();
		kbdBuffer = null;
		kbdCanv = null;
		//mMiniKeyboardCache.clear();
	}

	public void invalidateAllKeys() {
		// mDirtyRect.union(0, 0, getWidth(), getHeight());
		kbdDrawPending = true;
		invalidate();
	}



	private int getKeysIndex(int x, int y) {
		int primaryIndex = NOT_A_KEY;
		
		for(int i=0; i<this.keys.length; i++){
			if (this.keys[i].isInside(x,y)) {
				primaryIndex = i;
				break;
			}
		}
		
		/*
		int[] nearestKeyIndices = keyboard.getNearestKeys(x, y);
		Log.d("!", "--------------------------------------------");
		for (int i = 0; i < nearestKeyIndices.length; i++) {
			final Key key = keys[nearestKeyIndices[i]];
			Log.d("!", "---- key.label: "+key.label);
			if (key.isInside(x,y)) {
				primaryIndex = nearestKeyIndices[i];
				break;
			}
		}
		*/
		return primaryIndex;
	}














	@Override
	public boolean onTouch(View v, MotionEvent me) {
		Log.d("!","touch x"+me.getX(0)+" y:"+me.getY(0)+" view_id: "+v.getId());
		
		if(this.popupWin.isShowing()){
			//this.popupContent.dispatchTouchEvent(me);
			handlePopUpWindowTouch(me);
			return true;
		}
		

		if (me.getActionMasked() == MotionEvent.ACTION_DOWN){
			Log.d("!","ACTION_DOWN");
			int index = getKeysIndex(Math.round(me.getX(0)), Math.round(me.getY(0)));
			if(index != NOT_A_KEY){
				vibrate();

				this.touchingKey = true;
				this.touchingKeyIndex = index;
				this.timeTouchedKey = me.getEventTime();
				
				//////////////////////////////////////////////////
				this.removeCallbacks(this.keyLongClick);
				this.keyLongClicked = this.keys[index];
				this.postDelayed(this.keyLongClick, waitTime);
				//////////////////////////////////////////////////
				
				
				this.invalidate();
			}
			return true;
		}

		if (me.getActionMasked() == MotionEvent.ACTION_MOVE){
			Log.d("!","ACTION_MOVE");
			int index = getKeysIndex(Math.round(me.getX(0)), Math.round(me.getY(0)));
			if(index != NOT_A_KEY){
				if(index != this.touchingKeyIndex && !this.popupWin.isShowing()){
					vibrate();
					
					this.touchingKeyIndex = index;
					this.timeTouchedKey = me.getEventTime();
					
					
					//////////////////////////////////////////////////
					this.removeCallbacks(this.keyLongClick);
					this.keyLongClicked = this.keys[index];
					this.postDelayed(this.keyLongClick, waitTime);
					//////////////////////////////////////////////////
					
					this.invalidate();
				}
				/*if(index != this.touchingKeyIndex && !this.popupWin.isShowing()){
					vibrate();
					
					this.touchingKeyIndex = index;
					this.timeTouchedKey = me.getEventTime();
					
					this.removeCallbacks(this.keyLongClick);
					this.keyLongClicked = this.keys[index];
					this.postDelayed(this.keyLongClick, 200);
					
					this.invalidate();
				}*/ //else if(me.getEventTime() > (this.timeTouchedKey + this.waitTime) && !this.popupWin.isShowing()){
					

					/*
					Key k = keys[this.touchingKeyIndex];
					if(k.popupCharacters != null){ //TODO show popup if there is one
						vibrate();
						
						int[] loc = new int[2];
						MyKeyboardView.this.getLocationOnScreen(loc);
						
						this.popupContent.setKeys(k.popupCharacters, k.popupResId);
						
						
						int posX = k.x - this.popupWin.getWidth()/2 + k.width/2;
						int posY = k.y - this.popupWin.getHeight()-5;
						if(posX <= 0)
							posX = 5;
						if(posX + this.popupWin.getWidth() >= this.displayWidth)
							posX = this.displayWidth - this.popupWin.getWidth() - 5;
						if(posY + this.buttonHeight < 0)
							posY = - this.buttonHeight;
						
						Log.d("!","key.x:"+k.x+" key.y:"+k.y+"\tpopupHeight:"+this.popupWin.getHeight()+" popupWidth:"+this.popupWin.getWidth()+"\tpopupX:"+posX+" popupY:"+posY);
						
						this.popupWin.showAtLocation(this, Gravity.NO_GRAVITY, posX, posY);
						//this.popupWin.showAtLocation(this, Gravity.NO_GRAVITY, k.x, k.y - this.popupWin.getHeight()-5);
						//Log.d("!","showing popup ... "+this.popupWin.isShowing());
						this.invalidate();
					}*/
					/*else if(k.repeatable){
						vibrate();
						this.timeTouchedKey = me.getEventTime();
						keyboardActionListener.onKey(keys[this.touchingKeyIndex].codes[0], null);
					}*/
				//}//else if end
			}
			return true;
		}//ACTION_MOVE


		if (me.getActionMasked() == MotionEvent.ACTION_UP){
			this.removeCallbacks(this.keyLongClick);
			Log.d("!","ACTION_UP");
			int index = getKeysIndex(Math.round(me.getX(0)), Math.round(me.getY(0)));
			if(index != NOT_A_KEY){
				keyboardActionListener.onKey(keys[index].codes[0], null);

			}
			this.touchingKey = false;
			this.touchingKeyIndex = NOT_A_KEY;
			this.timeTouchedKey = 0;

			if (this.popupWin.isShowing())
				this.popupWin.dismiss();
			
			this.invalidate();
			return true;
		}


		return true;
	}









	protected void handlePopUpWindowTouch(MotionEvent me){
		int[] loc = new int[2];
		this.popupContent.getLocationOnScreen(loc);
		Log.d("!", "x:"+loc[0]+" y:"+loc[1]+" rawX:"+me.getRawX()+" rawY:"+me.getRawY());
		
		
		
		if (me.getActionMasked() == MotionEvent.ACTION_MOVE){
			Log.d("!","ACTION_MOVE");
			int dX = (int) me.getRawX() - loc[0] - this.popupContent.padding;
			int dY = (int) me.getRawY() - loc[1] - buttonHeight - this.popupContent.padding;
			Log.d("!","!!!!!!!!DELTA dX: "+dX+" dY:"+dY);
			if(dX < 0 || dX > this.popupWin.getWidth() - 3*this.popupContent.padding || dY < 0 || dY > this.popupWin.getHeight()){
				int index = -1;
				if(this.popupContent.selKeyIdx != index){
					this.popupContent.selKeyIdx = index;
					this.popupContent.invalidate();
				}
			}else{
				int row = dY / buttonHeight;
				int col = dX / this.popupContent.keyWidth;
				int index = this.popupContent.keysInCol * row + col;
				
				if(this.popupContent.selKeyIdx != index){
					this.popupContent.selKeyIdx = index;
					this.popupContent.invalidate();
				}
			}
			
			
			Log.d("!","key........ width:"+this.keyboard.getKeys().get(12).width+" height:"+this.keyboard.getKeys().get(12).height);
			
		}//ACTION_MOVE
		
		
		if (me.getActionMasked() == MotionEvent.ACTION_UP){
			Log.d("!","ACTION_UP");
			
			MyKeyboardView.this.touchingKey = false;
			MyKeyboardView.this.touchingKeyIndex = NOT_A_KEY;
			MyKeyboardView.this.timeTouchedKey = 0;

			if (MyKeyboardView.this.popupWin.isShowing())
				MyKeyboardView.this.popupWin.dismiss();
			
			if(this.popupContent.selKeyIdx != -1 && this.popupContent.selKeyIdx < this.popupContent.keys.length){
				keyboardActionListener.onKey(this.popupContent.keys[this.popupContent.selKeyIdx].label.charAt(0), null);
				this.popupContent.selKeyIdx = -1;
			}
			
			MyKeyboardView.this.invalidate();
		}//ACTION_UP
		
	}
	
	
	












	public void setKeyboard(Keyboard keyboard) {
		Log.d("!","keys set");
		//if (keyboard != null) {
		//    showPreview(NOT_A_KEY);
		//}
		// Remove any pending messages
		//removeMessages();
		this.keyboard = keyboard;
		this.keys = keyboard.getKeys().toArray(new Key[keyboard.getKeys().size()]);
		
		
		
		int curHeight = this.keys[0].height;
		
		for(Key k : this.keys){
			Log.d("!","y:"+k.y+" height:"+k.height);
			k.height = buttonHeight;
			k.y = (k.y/curHeight) * buttonHeight;
		}
		
		
		
		
		
		
		
		//List<Key> keys = mKeyboard.getKeys();
		//mKeys = keys.toArray(new Key[keys.size()]);
		requestLayout();
		// Hint to reallocate the buffer if the size changed
		kbdChanged = true;
		invalidateAllKeys();
		//computeProximityThreshold(keyboard);
		//mMiniKeyboardCache.clear(); // Not really necessary to do every time, but will free up views
		// Switching to a different keyboard should abort any pending keys so that the key up
		// doesn't get delivered to the old or new keyboard
		//mAbortKey = true; // Until the next ACTION_DOWN
		//
	}
	public Keyboard getKeyboard() {
		return keyboard;
	}









	public boolean setShifted(boolean shifted) {
    	if (this.keyboard != null) {
            if (this.keyboard.setShifted(shifted)) {
                // The whole keyboard probably needs to be redrawn
                invalidateAllKeys();
                return true;
            }
            invalidateAllKeys();
        }
        return false;
	}

	public boolean isShifted() {
		
    	if (this.keyboard != null) {
            return this.keyboard.isShifted();
        }

		return false;
	}









	/*
    public void setPreviewEnabled(boolean previewEnabled) {
        isPreviewShown = previewEnabled;
    }
    public boolean isPreviewEnabled() {
        return isPreviewShown;
    }
	 */



	/////////////////
	public void setOnKeyboardActionListener(OnKeyboardActionListener listener) {
		keyboardActionListener = listener;
	}
	protected OnKeyboardActionListener getOnKeyboardActionListener() {
		return keyboardActionListener;
	}

	
	private void vibrate(){
		if(isHapticOn)
			vibrator.vibrate(25);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	protected class FloatyView extends View{
		Context ctx;
		MyKeyboardView kbdView;
		Key[] keys;
		int keyWidth;
		int keysInCol;
		int padding = 3;
		int selKeyIdx = -1;
		
		
		public FloatyView(Context context, MyKeyboardView kbdView, int keyWidth) {
			super(context);
			this.ctx = context;
			this.kbdView = kbdView;
			this.keyWidth = keyWidth;
		}
		
		
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			if(keys == null){
				setMeasuredDimension(0, 0);
			}else{
				setMeasuredDimension((keysInCol)*keyWidth + 2*padding, ((int) Math.ceil((float)keys.length/keysInCol))*buttonHeight + 2*padding);
			}
		}



		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			Paint bgPaint = new Paint();
			bgPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
			bgPaint.setColor(backgroundColor);
			//bgPaint.setShadowLayer(15, 0, 0, new Color().argb(220, 0, 0, 0));
			canvas.drawRoundRect(new RectF(0, 0, this.getWidth(), this.getHeight()), btnRoundedness, btnRoundedness, bgPaint);
			//canvas.drawRoundRect(new RectF(0, 0, this.getWidth(), this.getHeight()), 6, 6, bgPaint);
			//((keys.length-1)/keysInCol+1)*keyHeight + 2*padding
			for(int i=0; i<keys.length; i++){
				if(i == selKeyIdx)
					kbdView.drawButton(canvas, keys[i], true);
				else
					kbdView.drawButton(canvas, keys[i], false);
			}
		}
		
		
		
		public void setKeys(CharSequence keysChars, int kbdRes){
			//Log.d("!","btn key chars: "+keysChars);
			this.keys = new Key[keysChars.length()];
			
			this.keysInCol = keysChars.length();
			if(keysInCol > 7){
				this.keysInCol = (int)  Math.ceil((float)keysChars.length()/2);
			}
			
			Row r = new Row(new Keyboard(ctx, kbdRes));
			for(int i=0; i<keys.length; i++){
				keys[i] = new Key(r);
				keys[i].label = keysChars.subSequence(i, i+1);
				//Log.d("!","char: "+keysChars.subSequence(i, i+1));
				//keys[i].label = ""+keysChars.charAt(i);
				keys[i].x = this.keyWidth * (i%(this.keysInCol)) + this.padding;
				keys[i].y = buttonHeight * (i/this.keysInCol) + this.padding;
				keys[i].width = this.keyWidth;
				keys[i].height = buttonHeight;
				
				//Log.d("!","...spam... this.keyHeight: "+this.keyHeight+" keys[i].height: "+keys[i].height);
			}
			
			kbdView.popupWin.setWidth((keysInCol)*keyWidth + 2*padding);
			kbdView.popupWin.setHeight(  ((int) Math.ceil((float)keys.length/keysInCol))*buttonHeight + 2*padding);
			
			this.requestLayout();
		}
		
		
		
		
		
	}//private class end








	
	
}//class end
