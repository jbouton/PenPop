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

public class FishTarget {

	private float x, y, size;
	private World world;
	private Assets assets;
	private Body body;

	public FishTarget(World world, float x, float y, float size, Assets assets) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.assets = assets;
		this.size = size;
	
	}

	public void createFishTarget() {
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);

		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;
		boxBodyDef.position.x = x;
		boxBodyDef.position.y = y;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.friction = 0f;
		fixtureDef.density = .5f;
		fixtureDef.restitution = .8f;
	    fixtureDef.isSensor = true;
	    //fixtureDef.filter.groupIndex = -1;
		body = world.createBody(boxBodyDef);
		SpriteInfo spriteTemp = new SpriteInfo(assets.getSprite("star"));
		spriteTemp.setScale(1);
		spriteTemp.setName("star");
		body.setUserData(spriteTemp);
		body.createFixture(fixtureDef);
		//body.setGravityScale(.01f);
		body.setAngularVelocity(.5f);
		float rand = (float) (Math.random() * 250)+100;
		body.applyForce(new Vector2(0,rand),body.getWorldCenter(),true);

		circleShape.dispose();
	}

	public Body getbody() {
		return body;
	}

	public void setbody(Body body) {
		this.body = body;
	}

}
