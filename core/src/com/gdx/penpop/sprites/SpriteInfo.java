package com.gdx.penpop.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteInfo extends Sprite{
	@Override
	public float getX() {
		return (int)super.getX();
	}

	private String name,status;
	private int index;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public SpriteInfo() {
		super();
	}

	public SpriteInfo(Sprite sprite) {
		super(sprite);
	}

	public SpriteInfo(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
		super(texture, srcX, srcY, srcWidth, srcHeight);
	}

	public SpriteInfo(Texture texture, int srcWidth, int srcHeight) {
		super(texture, srcWidth, srcHeight);
	}

	public SpriteInfo(Texture texture) {
		super(texture);
	}

	public SpriteInfo(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
		super(region, srcX, srcY, srcWidth, srcHeight);
	}

	public SpriteInfo(TextureRegion region) {
		super(region);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}


}
