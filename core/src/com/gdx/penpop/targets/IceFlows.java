package com.gdx.penpop.targets;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.penpop.assets.Assets;

public class IceFlows {
	
	private float x,y,size;
	private World world;
	private Assets assets;
	private Body iceBody,iceBlockBody;
	
	public IceFlows(World world,float x,float y, float size,Assets assets){
		this.world = world;
		this.x = x;
		this.y=y;
		this.assets=assets;
		this.size=size;
		
		
	}
	
	public void createIceFlow(){
	PolygonShape boxPoly = new PolygonShape();
	boxPoly.setAsBox(size,size/8);

	BodyDef boxBodyDef = new BodyDef();
	boxBodyDef.type = BodyType.StaticBody;
	boxBodyDef.position.x = x;
	boxBodyDef.position.y = y;

	FixtureDef fixtureDef = new FixtureDef();
	fixtureDef.shape = boxPoly;
	fixtureDef.friction = 0.4f;
	fixtureDef.density = 2.10f;
	fixtureDef.restitution = 0f;
	// fixtureDef.isSensor = true;
	// fixtureDef.filter.groupIndex = -1;
	iceBody = world.createBody(boxBodyDef);
	iceBody.setLinearDamping(6f);
	iceBody.setUserData(assets.getSprite("iceFlow"));
	iceBody.createFixture(fixtureDef);
	iceBody.createFixture(boxPoly, .1f);

	
	
	boxPoly.dispose();
	}

	public Body getIceBody() {
		return iceBody;
	}

}
