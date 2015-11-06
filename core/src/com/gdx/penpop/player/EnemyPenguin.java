package com.gdx.penpop.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.gdx.penpop.assets.Assets;
import com.gdx.penpop.sprites.SpriteInfo;

public class EnemyPenguin extends SpriteInfo {
	private float x, y, size;
	private Body penguinBody, leftArmbody, rightArmBody;
	private World world;
	private Assets assets;

	public EnemyPenguin(World world, float x, float y, float size, Assets assets) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.size = size;
		this.assets = assets;
	}

	public void createPenguin() {

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);
		BodyDef circleBodyDef = new BodyDef();
		circleBodyDef = new BodyDef();
		circleBodyDef.type = BodyType.DynamicBody;
		circleBodyDef.position.x = x;
		circleBodyDef.position.y = y;

		FixtureDef playerDefixtureDef = new FixtureDef();
		playerDefixtureDef.shape = circleShape;
		playerDefixtureDef.friction = 0f;
		playerDefixtureDef.density = 1f;
		playerDefixtureDef.restitution = 0f;
		// playerDefixtureDef.isSensor = true;
		// fixtureDef.filter.groupIndex = 2;
		penguinBody = world.createBody(circleBodyDef);
		Sprite spriteTemp = new Sprite(assets.getSprite("pen"));
		//spriteTemp.setColor(Color.RED);
		penguinBody.setUserData(spriteTemp);
		// spiderBodies.get(i).setAngularVelocity(20f);
		penguinBody.createFixture(playerDefixtureDef);
		circleShape.dispose();

		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(size / 2, size / 6);

		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;
		boxBodyDef.position.x = (x - 1.4f/6);
		boxBodyDef.position.y = (y + .1f/6);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = boxPoly;
		fixtureDef.friction = 0.0f;
		fixtureDef.density = (.2f/6);
		fixtureDef.restitution = 0f;
		// fixtureDef.isSensor = true;
	    fixtureDef.filter.groupIndex = 1;
		leftArmbody = world.createBody(boxBodyDef);
		leftArmbody.createFixture(fixtureDef);
		leftArmbody.createFixture(boxPoly, 1);
		leftArmbody.setUserData(assets.getSprite("armLeft"));
		RevoluteJointDef jointDef = new RevoluteJointDef();
		//jointDef.enableMotor = true;
		//jointDef.motorSpeed = (-1f/6);
		//jointDef.maxMotorTorque = (3f/6);
		jointDef.collideConnected = false;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 170 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 90 * MathUtils.degreesToRadians;
		jointDef.bodyA = penguinBody;
		jointDef.bodyB = leftArmbody;
		jointDef.localAnchorB.set((.3f/6), 0);
		jointDef.localAnchorA.set(size, 0);
		world.createJoint(jointDef);
		rightArmBody = world.createBody(boxBodyDef);
		rightArmBody.setUserData(assets.getSprite("armRight"));
		rightArmBody.createFixture(fixtureDef);
		rightArmBody.createFixture(boxPoly, 1);
		jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		//jointDef.enableMotor = true;
		//jointDef.motorSpeed = (1f/6);
		//jointDef.maxMotorTorque = (3f/6);
		jointDef.enableLimit = true;
		jointDef.upperAngle = 90 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 10 * MathUtils.degreesToRadians;
		jointDef.bodyA = penguinBody;
		jointDef.bodyB = rightArmBody;
		jointDef.localAnchorB.set((.3f/6), 0);
		jointDef.localAnchorA.set(-size, 0);
		world.createJoint(jointDef);

	}

	public Body getPenguinBody() {
		return penguinBody;
	}

	public Body getLeftArmbody() {
		return leftArmbody;
	}

	public Body getRightArmBody() {
		return rightArmBody;
	}

}