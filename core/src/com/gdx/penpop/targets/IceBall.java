package com.gdx.penpop.targets;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.penpop.assets.Assets;
import com.gdx.penpop.sprites.SpriteInfo;

public class IceBall {

	private float x, y, size;
	private World world;
	private Assets assets;
	private Body iceBody;

	public IceBall(World world, float x, float y, float size, Assets assets) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.assets = assets;
		this.size = size;
	
	}

	public void createIceBall() {
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);

		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.StaticBody;
		boxBodyDef.position.x = x;
		boxBodyDef.position.y = y;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.friction = 0f;
		fixtureDef.density = 4.10f;
		fixtureDef.restitution = 1f;
		// fixtureDef.isSensor = true;
	    fixtureDef.filter.groupIndex = -1;
		iceBody = world.createBody(boxBodyDef);
		SpriteInfo spriteTemp = new SpriteInfo(assets.getSprite("ball"));
		spriteTemp.setName("ball");
		iceBody.setUserData(spriteTemp);
		iceBody.createFixture(fixtureDef);
		//iceBody.createFixture(circleShape, .1f);

		circleShape.dispose();
	}

	public Body getIceBody() {
		return iceBody;
	}

	public void setIceBody(Body iceBody) {
		this.iceBody = iceBody;
	}

}
