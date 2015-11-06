package com.gdx.penpop.tween;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.penpop.sprites.SpriteInfo;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<SpriteInfo> {
	public static final int SKEW_X2X3 = 1;
	public static final int SCALE_XY = 2;
	public static final int ROTATE = 3;
	public static final int POSITION_XY = 4;
	public static final int SCALE_SIZE_XY = 5;
	public static final int SCALE_Y = 6;
	
	@Override
	public int getValues(SpriteInfo target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case SKEW_X2X3:
			float[] vs = target.getVertices();
			returnValues[0] = vs[SpriteBatch.X2] - target.getX();
			returnValues[1] = vs[SpriteBatch.X3] - target.getX()
					- target.getWidth();
			return 2;

		case SCALE_XY:
			returnValues[0] = target.getScaleY();
	  		return 1;
		case SCALE_Y:
			returnValues[0] = target.getScaleY();
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
	public void setValues(SpriteInfo target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case SKEW_X2X3:
			float x2 = target.getX();
			float x3 = x2 + target.getWidth();
			float[] vs = target.getVertices();
			vs[SpriteBatch.X2] = x2 + newValues[0];
			vs[SpriteBatch.X3] = x3 + newValues[1];
			break;
		case SCALE_XY:
			target.setScale( newValues[0], newValues[0]);
			//System.out.println(newValues[0]+","+newValues[0]);
			break;
		case SCALE_Y:
			target.setScale(target.getScaleX(), newValues[0]);
			//target.setOrigin(target.getX(), target.getY()-newValues[0]);
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