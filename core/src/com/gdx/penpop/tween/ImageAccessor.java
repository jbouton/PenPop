package com.gdx.penpop.tween;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gdx.penpop.sprites.SpriteInfo;

import aurelienribon.tweenengine.TweenAccessor;

public class ImageAccessor implements TweenAccessor<Image> {
	public static final int SKEW_X2X3 = 1;
	public static final int SCALE_XY = 2;
	public static final int ROTATE = 3;
	public static final int POSITION_XY = 4;
	public static final int SCALE_SIZE_XY = 5;
	public static final int SCALE_Y = 6;
	
	@Override
	public int getValues(Image target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case SCALE_XY:
			returnValues[0] = target.getScaleY();
	  		return 1;
		case SCALE_Y:
			returnValues[0] = target.getScaleY();
			System.out.println(returnValues[0] );
	  		return 1;
		case SCALE_SIZE_XY:
			returnValues[0] = target.getWidth();
			returnValues[1] = target.getHeight();
			return 2;
			
		case ROTATE:
			returnValues[0] = target.getRotation();
			return 1;
			
		case POSITION_XY:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;
			
		}

		assert false;
		return -1;
	}

	@Override
	public void setValues(Image target, int tweenType, float[] newValues) {
		switch (tweenType) {
			case SCALE_XY:
			target.setScale( newValues[0], newValues[0]);
			//System.out.println(newValues[0]+","+newValues[0]);
			break;
		case SCALE_Y:
			target.setScaleY(newValues[0]);
			//System.out.println(newValues[0]+","+newValues[0]);
			break;
		case SCALE_SIZE_XY:
			target.setSize(newValues[0],newValues[1]);
			break;
		case ROTATE:
			target.setRotation(newValues[0]);
			break;
		case POSITION_XY:
			target.setPosition(newValues[0],newValues[1]);
			break;
		}
	}
}