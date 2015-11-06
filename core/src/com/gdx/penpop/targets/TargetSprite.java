package com.gdx.penpop.targets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TargetSprite extends Sprite{
	private boolean bHit;
	
	public TargetSprite(TextureRegion region) {
		super(region);
		
	}

	public boolean isbHit() {
		return bHit;
	}

	public void setbHit(boolean bHit) {
		this.bHit = bHit;
	}

}
