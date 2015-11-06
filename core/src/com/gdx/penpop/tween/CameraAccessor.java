package com.gdx.penpop.tween;

import com.badlogic.gdx.graphics.OrthographicCamera;
import aurelienribon.tweenengine.TweenAccessor;

public class CameraAccessor implements TweenAccessor<OrthographicCamera> {
	public static final int CAMERA_XP = 1;
	public static final int CAMERA_YP = 2;
	public static final int CAMERA_XM = 3;
	public static final int CAMERA_YM = 4;
	private static final float WORLD_TO_BOX = 0.01f;
	private static final float BOX_TO_WORLD = 100f;

	@Override
	public int getValues(OrthographicCamera target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case CAMERA_XP: {
			returnValues[0] = target.position.x;
			//System.out.println("getY=" + returnValues[0]);
			return 1;
		}
		case CAMERA_YP: {
			returnValues[0] = target.position.y;
			//System.out.println("getY=" + returnValues[0]);
			return 1;
		}
		case CAMERA_XM: {
			returnValues[0] = target.position.x;
			//System.out.println("getY=" + returnValues[0]);
			return 1;
		}
		case CAMERA_YM: {
			returnValues[0] = target.position.y;
			//System.out.println("getY=" + returnValues[0]);
			return 1;
		}
		}

		assert false;
		return -1;
	}

	@Override
	public void setValues(OrthographicCamera target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case CAMERA_XP: {
			target.position.x += newValues[0] * WORLD_TO_BOX;
			//System.out.println("setY=" + newValues[0]);
			break;
		}
		case CAMERA_YP: {
			target.position.y += newValues[0] * WORLD_TO_BOX;
			//System.out.println("setY=" + newValues[0]);
			break;
		}
		case CAMERA_XM: {
			target.position.x -= newValues[0] * WORLD_TO_BOX;
			//System.out.println("setY=" + newValues[0]);
			break;
		}
		case CAMERA_YM: {
			if(target.position.y<0)
			  target.position.y += -(newValues[0] * WORLD_TO_BOX);
			else 
			  target.position.y -= newValues[0] * WORLD_TO_BOX;
			//System.out.println("setY=" + newValues[0]);
			break;
		}
		}
	}

}