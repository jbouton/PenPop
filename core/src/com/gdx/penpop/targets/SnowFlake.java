package com.gdx.penpop.targets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.penpop.assets.Assets;
import com.gdx.penpop.sprites.SpriteInfo;

public class SnowFlake {

	private float x, y, size;
	private World world;
	private Assets assets;
	private Body body;

	public SnowFlake(World world, float x, float y, float size, Assets assets) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.assets = assets;
		this.size = size;
	
	}

	public void createSnowFlake() {
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);

		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;
		boxBodyDef.position.x = x;
		boxBodyDef.position.y = y;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.friction = 0f;
		fixtureDef.density = .01f;
		fixtureDef.restitution = .8f;
	    fixtureDef.isSensor = true;
	    //fixtureDef.filter.groupIndex = -1;
		body = world.createBody(boxBodyDef);
		SpriteInfo spriteTemp = new SpriteInfo(assets.getSprite("snow"));
		spriteTemp.setScale(1);
		spriteTemp.setName("snowFlake");
		body.setUserData(spriteTemp);
		body.createFixture(fixtureDef);
		body.setGravityScale(.01f);
		body.setAngularVelocity(.5f);
		float rand = (float) (Math.random() * 5)/10;
		body.setLinearVelocity(new Vector2(rand,0));
		circleShape.dispose();
	}

	public Body getbody() {
		return body;
	}

	public void setbody(Body body) {
		this.body = body;
	}

}
