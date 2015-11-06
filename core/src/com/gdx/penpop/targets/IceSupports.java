package com.gdx.penpop.targets;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.penpop.assets.Assets;
import com.gdx.penpop.sprites.SpriteInfo;

public class IceSupports {

	private float x, y, size;
	private World world;
	private Assets assets;
	private Body body;
	private Body gearBody,gearBase;
	public IceSupports(World world, float x, float y, float size, Assets assets) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.assets = assets;
		this.size = size;

	}

	public void createIceBall() {
		// gear base
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.x = x;
		bodyDef.position.y = y;
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(1);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.shape = circleShape;
		gearBody = world.createBody(bodyDef);
		gearBody.createFixture(fixtureDef);

		circleShape = new CircleShape();
		circleShape.setRadius(size);

		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;
		boxBodyDef.position.x = x;
		boxBodyDef.position.y = y;

		fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.friction = 0f;
		fixtureDef.density = 4.10f;
		fixtureDef.restitution = 1.5f;
		 fixtureDef.isSensor = true;
		fixtureDef.filter.groupIndex = -1;
		gearBase = world.createBody(boxBodyDef);
		SpriteInfo spriteTemp = new SpriteInfo(assets.getSprite("star"));
		spriteTemp.setName("star");
		gearBase.setUserData(spriteTemp);
		gearBase.createFixture(fixtureDef);
		// iceBody.createFixture(circleShape, .1f);
		
		RevoluteJointDef wheeljointDef = new RevoluteJointDef();
		wheeljointDef.bodyA = gearBase;
		wheeljointDef.bodyB = gearBody;
		wheeljointDef.localAnchorA.y = 0;
		wheeljointDef.localAnchorB.y = 0;
		wheeljointDef.motorSpeed = .5f;
		wheeljointDef.enableMotor = true;
		wheeljointDef.maxMotorTorque = 100000f;
		// Joint joint =
		// wheeljointDef.bodyA.getWorld().createJoint(wheeljointDef);
		world.createJoint(wheeljointDef);

		circleShape.dispose();
	}

	public Body getIceBody() {
		return gearBase;
	}

	public void setIceBody(Body iceBody) {
		this.body = gearBase;
	}

}
