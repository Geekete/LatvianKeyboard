package com.project.latviankeyboard.dancingkeyboard;

import com.project.latviankeyboard.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DancingMan extends SurfaceView implements SurfaceHolder.Callback {

	Bitmap[] danceBitmap = new Bitmap[7];
	int frame = 0;
	Rect srcRect = new Rect();
	Rect destRect = new Rect();
	public int dance = 0;

	public DancingMan(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		danceBitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.idle);
		danceBitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.spin);
		danceBitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.wiggle_left);
		danceBitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.wiggle_right);
		danceBitmap[4] = BitmapFactory.decodeResource(getResources(), R.drawable.jump);
		danceBitmap[5] = BitmapFactory.decodeResource(getResources(), R.drawable.split);
		danceBitmap[6] = BitmapFactory.decodeResource(getResources(), R.drawable.warp);

		// TODO Auto-generated constructor stub
	}
	public void draw(Canvas canvas){
		destRect.set(0, 0, canvas.getWidth(), canvas.getHeight());
		srcRect.set(danceBitmap[dance].getHeight()*frame, 0, (frame+1)*danceBitmap[dance].getHeight(), danceBitmap[dance].getHeight());
		Paint p = new Paint();
		p.setColor(0xFFFF0000);
		
		if(frame < 10){
			frame ++;
		}else{
			frame = 0;
		}
		canvas.drawColor(0xFF000000);
		canvas.drawBitmap(danceBitmap[dance], srcRect, destRect, p);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
    }

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
	}
}
