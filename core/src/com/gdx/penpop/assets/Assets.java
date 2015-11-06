package com.gdx.penpop.assets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.penpop.sprites.SpriteInfo;

public class Assets {
	private TextureAtlas atlas;
	private TextureRegion texReg;
	private Map<String, SpriteInfo> spriteMap;

	public Assets() {
		String[] spriteList = { "hair", "pen", "armLeft","armRight","paper","iceFlow","ball" };
		atlas = new TextureAtlas(Gdx.files.internal("data/penpack.pack"));
		spriteMap = new HashMap<String, SpriteInfo>();
		for (int i = 0; i < spriteList.length; i++) {
			loadSprites(spriteList[i]);
		}

	}

	public void loadSprites(String spriteName) {
		texReg = atlas.findRegion(spriteName);
		SpriteInfo tempSprite = new SpriteInfo(texReg);
		//tempSprite.setScale(tempSprite.getScaleX()*screenRatio,tempSprite.getScaleY()*screenRatio);
		//tempSprite.setScale(1);
		tempSprite.setName(spriteName);

		spriteMap.put(spriteName, tempSprite);
	}

	public SpriteInfo getSprite(String spriteName){

		SpriteInfo tempSprite = spriteMap.get(spriteName);
		return tempSprite;

	}

}
