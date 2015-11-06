package com.bouton.penpop.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Sprite{
	private float yTemp;
	private boolean bUp;

	public Player(TextureRegion region) {
		super(region);
		
	}

	public float getyTemp() {
		return yTemp;
	}

	public void setyTemp(float yTemp) {
		this.yTemp = yTemp;
	}

	public boolean isbUp() {
		return bUp;
	}

	public void setbUp(boolean bUp) {
		this.bUp = bUp;
	}




}
