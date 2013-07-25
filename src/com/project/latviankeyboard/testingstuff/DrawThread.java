package com.project.latviankeyboard.testingstuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
	private SurfaceHolder _surfaceHolder;
    private DancingMan _dancingView;
    private boolean _run = false;
    public List<Integer> charList = new ArrayList();
    int frameCount = 0;
    Random generator = new Random(); 




    public DrawThread(SurfaceHolder surfaceHolder, DancingMan dancingMan) {
        _surfaceHolder = surfaceHolder;
        _dancingView = dancingMan;
    }


    public void setRunning(boolean run) { //Allow us to stop the thread
        _run = run;
    }


    @Override
    public void run() {
        Canvas c;
        while (_run) {     //When setRunning(false) occurs, _run is 
                           //set to false and loop ends, stopping thread
            	
            	frameCount = 0;
            	try{
	            	if(!charList.isEmpty()){
	        			_dancingView.dance = generator.nextInt(7);
	        		}else{
	        			_dancingView.dance = 0;
	        		}
            	}catch(Exception ex){
            		_dancingView.dance = 0;
            	}
	            while(frameCount < 10){
	            	c = null;
		            try {
			            		c = _surfaceHolder.lockCanvas(null);           
			            		frameCount += 1;
				                synchronized (_surfaceHolder) {
			                		Thread.sleep(1000/20);
			                		_dancingView.draw(c);
				                }
			            }
		            catch(Exception ex){
		            } finally {
		                if (c != null) {
		                    _surfaceHolder.unlockCanvasAndPost(c);
		                }
		            }
	            }
	            if(!charList.isEmpty()){
	            	charList.remove(0);
	            }
        }
    }
}
