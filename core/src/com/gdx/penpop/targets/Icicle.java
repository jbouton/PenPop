package com.gdx.penpop.targets;

import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.penpop.assets.Assets;
import com.gdx.penpop.sprites.SpriteInfo;

public class Icicle {

	private float x, y, size;
	private World world;
	private Assets assets;
	private Body iceBody;
	private int index;

	public Icicle(World world, float x, float y, float size, Assets assets) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.assets = assets;
		this.size = size;
		this.index = index;

	}

	public void createIcicle() {
		Vector2[] vertices = new Vector2[3];
		vertices[0] = new Vector2(-1f, 0);
		vertices[1] = new Vector2(1f, 0);
		vertices[2] = new Vector2(0, -.1f);
		PolygonShape polygon = new PolygonShape();
		polygon.set(vertices);

		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.StaticBody;
		boxBodyDef.position.x = x;
		boxBodyDef.position.y = y;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygon;
		fixtureDef.friction = 0f;
		fixtureDef.density = 66.10f;
		fixtureDef.restitution = .5f;
		fixtureDef.isSensor = true;
		//fixtureDef.filter.groupIndex = 2;
		iceBody = world.createBody(boxBodyDef);
		iceBody.setLinearDamping(10f);
		SpriteInfo spriteTemp = new SpriteInfo(assets.getSprite("iceicle"));
		spriteTemp.setScale(1,0);
		spriteTemp.setOrigin(0,+6);
		spriteTemp.setName("icicle");
		iceBody.setUserData(spriteTemp);
		iceBody.createFixture(fixtureDef);
		// iceBody.createFixture(circleShape, .1f);

		polygon.dispose();
	}

	public Body getIceBody() {
		return iceBody;
	}

	public void setIceBody(Body iceBody) {
		this.iceBody = iceBody;
	}

}
