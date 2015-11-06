package com.gdx.penpop.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class ImageInfo extends Image {
	public ImageInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	ImageInfo(Drawable drawable, Scaling scaling, int align) {
		super(drawable, scaling, align);
		// TODO Auto-generated constructor stub
	}

	ImageInfo(Drawable drawable, Scaling scaling) {
		super(drawable, scaling);
		// TODO Auto-generated constructor stub
	}

	ImageInfo(Drawable drawable) {
		super(drawable);
		// TODO Auto-generated constructor stub
	}

	ImageInfo(NinePatch patch) {
		super(patch);
		// TODO Auto-generated constructor stub
	}

	ImageInfo(Skin skin, String drawableName) {
		super(skin, drawableName);
		// TODO Auto-generated constructor stub
	}

	public ImageInfo(Texture texture) {
		super(texture);
		// TODO Auto-generated constructor stub
	}

	private String name,status;

	public ImageInfo(TextureRegion texReg) {
		// TODO Auto-generated constructor stub
	}

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

	

}
