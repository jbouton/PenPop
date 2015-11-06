package com.gdx.penpop.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.CircleShape;

public class ShapeAccessor implements TweenAccessor<CircleShape> {
	public static final int RADIUS = 1;
	@Override
	public int getValues(CircleShape target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case RADIUS:
			returnValues[0] = target.getRadius();
			return 1;
			
		}

		assert false;
		return -1;
	}

	@Override
	public void setValues(CircleShape target, int tweenType, float[] newValues) {
		switch (tweenType) {
			case RADIUS:
			target.setRadius(newValues[0]);
			break;
		}
	}
}