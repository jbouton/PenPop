package com.gdx.penpop.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.gdx.penpop.assets.Assets;
import com.gdx.penpop.sprites.SpriteInfo;

public class Crab extends SpriteInfo {
	private float x, y, size;
	private Body body, leftArmbody, rightArmBody, rightLeg1, rightLeg2, rightLeg3, leftLeg1, leftLeg2, leftLeg3;
	private World world;
	private Assets assets;

	public Crab(World world, float x, float y, float size, Assets assets) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.size = size;
		this.assets = assets;
	}

	public void createCrab() {

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
		playerDefixtureDef.restitution = 1f;
		playerDefixtureDef.filter.groupIndex = 0;
		body = world.createBody(circleBodyDef);
		SpriteInfo spriteTemp = new SpriteInfo();
		spriteTemp = assets.getSprite("pen");
		body.setUserData(spriteTemp);
		body.createFixture(playerDefixtureDef);
		circleShape.dispose();

		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(size * .8f, size * .2f);
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;
		boxBodyDef.position.x = (x + .4f);
		boxBodyDef.position.y = (y + .5f);
		boxBodyDef.angle = 2;
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = boxPoly;
		fixtureDef.friction = 0.0f;
		fixtureDef.density = (.001f);
		fixtureDef.restitution = 0;
		fixtureDef.isSensor = true;
		fixtureDef.filter.groupIndex = 1;
		leftArmbody = world.createBody(boxBodyDef);
		leftArmbody.createFixture(fixtureDef);
		spriteTemp = new SpriteInfo();
		spriteTemp = assets.getSprite("armLeft");
		leftArmbody.setUserData(spriteTemp);
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 90 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 50 * MathUtils.degreesToRadians;
		jointDef.bodyA = body;
		jointDef.bodyB = leftArmbody;
		jointDef.localAnchorB.set(-.2f, 0);
		jointDef.localAnchorA.set(.3f, .3f);
		world.createJoint(jointDef);

		boxBodyDef.type = BodyType.DynamicBody;
		fixtureDef.filter.groupIndex = 2;
		fixtureDef.isSensor = true;
		boxBodyDef.position.x = (x - .4f);
		boxBodyDef.position.y = (y + .5f);
		boxBodyDef.angle = 2;
		rightArmBody = world.createBody(boxBodyDef);
		spriteTemp = new SpriteInfo();
		spriteTemp = assets.getSprite("armRight");
		rightArmBody.setUserData(spriteTemp);
		rightArmBody.createFixture(fixtureDef);
		jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 110 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 70 * MathUtils.degreesToRadians;
		jointDef.bodyA = body;
		jointDef.bodyB = rightArmBody;
		jointDef.localAnchorB.set(-.2f, 0);
		jointDef.localAnchorA.set(-.3f, .3f);
		world.createJoint(jointDef);

		// right leg 1
		boxPoly.setAsBox(size * .6f, size * .1f);
		boxBodyDef.type = BodyType.DynamicBody;
		fixtureDef.filter.groupIndex = 3;
		fixtureDef.isSensor = true;
		boxBodyDef.position.x = (x - .6f);
		boxBodyDef.position.y = (y);
		boxBodyDef.angle = 0;
		rightLeg1 = world.createBody(boxBodyDef);
		spriteTemp = new SpriteInfo();
		spriteTemp = assets.getSprite("armRight");
		rightLeg1.setUserData(spriteTemp);
		rightLeg1.createFixture(fixtureDef);
		jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 30 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 0 * MathUtils.degreesToRadians;
		jointDef.bodyA = body;
		jointDef.bodyB = rightLeg1;
		jointDef.localAnchorB.set(.3f, 0);
		jointDef.localAnchorA.set(-.3f, 0);
		world.createJoint(jointDef);

		// right leg 2
		boxPoly.setAsBox(size * .6f, size * .1f);
		boxBodyDef.type = BodyType.DynamicBody;
		fixtureDef.filter.groupIndex = 4;
		fixtureDef.isSensor = true;
		boxBodyDef.position.x = (x - .6f);
		boxBodyDef.position.y = (y - .2f);
		boxBodyDef.angle = 0;
		rightLeg2 = world.createBody(boxBodyDef);
		spriteTemp = new SpriteInfo();
		spriteTemp = assets.getSprite("armRight");
		rightLeg2.setUserData(spriteTemp);
		rightLeg2.createFixture(fixtureDef);
		jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 30 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 0 * MathUtils.degreesToRadians;
		jointDef.bodyA = body;
		jointDef.bodyB = rightLeg2;
		jointDef.localAnchorB.set(.3f, 0);
		jointDef.localAnchorA.set(-.2f, -.2f);
		world.createJoint(jointDef);

		// right leg 3
		boxPoly.setAsBox(size * .6f, size * .1f);
		boxBodyDef.type = BodyType.DynamicBody;
		fixtureDef.filter.groupIndex = 4;
		fixtureDef.isSensor = true;
		boxBodyDef.position.x = (x - .6f);
		boxBodyDef.position.y = (y - .3f);
		boxBodyDef.angle = 0;
		rightLeg3 = world.createBody(boxBodyDef);
		spriteTemp = new SpriteInfo();
		spriteTemp = assets.getSprite("armRight");
		rightLeg3.setUserData(spriteTemp);
		rightLeg3.createFixture(fixtureDef);
		jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 30 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 0 * MathUtils.degreesToRadians;
		jointDef.bodyA = body;
		jointDef.bodyB = rightLeg3;
		jointDef.localAnchorB.set(.3f, 0);
		jointDef.localAnchorA.set(-.1f, -.4f);
		world.createJoint(jointDef);

		// left leg 1
		boxPoly.setAsBox(size * .6f, size * .1f);
		boxBodyDef.type = BodyType.DynamicBody;
		fixtureDef.filter.groupIndex = 3;
		fixtureDef.isSensor = true;
		boxBodyDef.position.x = (x + .6f);
		boxBodyDef.position.y = (y);
		//boxBodyDef.angle = 2;
		leftLeg1 = world.createBody(boxBodyDef);
		spriteTemp = new SpriteInfo();
		spriteTemp = assets.getSprite("armLeft");
		leftLeg1.setUserData(spriteTemp);
		leftLeg1.createFixture(fixtureDef);
		jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 130 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 160 * MathUtils.degreesToRadians;
		jointDef.bodyA = body;
		jointDef.bodyB = leftLeg1;
		jointDef.localAnchorB.set(.3f, 0);
		jointDef.localAnchorA.set(.3f, 0);
		world.createJoint(jointDef);

		// left leg 2
		boxPoly.setAsBox(size * .6f, size * .1f);
		boxBodyDef.type = BodyType.DynamicBody;
		fixtureDef.filter.groupIndex = 4;
		fixtureDef.isSensor = true;
		boxBodyDef.position.x = (x + .6f);
		boxBodyDef.position.y = (y - .2f);
		//boxBodyDef.angle = 2;
		leftLeg2 = world.createBody(boxBodyDef);
		spriteTemp = new SpriteInfo();
		spriteTemp = assets.getSprite("armLeft");
		leftLeg2.setUserData(spriteTemp);
		leftLeg2.createFixture(fixtureDef);
		jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 130 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 160 * MathUtils.degreesToRadians;
		jointDef.bodyA = body;
		jointDef.bodyB = leftLeg2;
		jointDef.localAnchorB.set(.3f, 0);
		jointDef.localAnchorA.set(.2f, -.2f);
		world.createJoint(jointDef);

		// left leg 3
		boxPoly.setAsBox(size * .6f, size * .1f);
		boxBodyDef.type = BodyType.DynamicBody;
		fixtureDef.filter.groupIndex = 4;
		fixtureDef.isSensor = true;
		boxBodyDef.position.x = (x + .6f);
		boxBodyDef.position.y = (y - .3f);
		//boxBodyDef.angle = 2;
		leftLeg3 = world.createBody(boxBodyDef);
		spriteTemp = new SpriteInfo();
		spriteTemp = assets.getSprite("armLeft");
		leftLeg3.setUserData(spriteTemp);
		leftLeg3.createFixture(fixtureDef);
		jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 130 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = 160 * MathUtils.degreesToRadians;
		jointDef.bodyA = body;
		jointDef.bodyB = leftLeg3;
		jointDef.localAnchorB.set(.3f, 0);
		jointDef.localAnchorA.set(.1f, -.4f);
		world.createJoint(jointDef);

	}

	public Body getbody() {
		return body;
	}

	public Body getLeftArmbody() {
		return leftArmbody;
	}

	public Body getRightArmBody() {
		return rightArmBody;
	}

	public Body getRightLeg1() {
		return rightLeg1;
	}

	public void setRightLeg1(Body rightLeg1) {
		this.rightLeg1 = rightLeg1;
	}

	public Body getRightLeg2() {
		return rightLeg2;
	}

	public void setRightLeg2(Body rightLeg2) {
		this.rightLeg2 = rightLeg2;
	}

	public Body getRightLeg3() {
		return rightLeg3;
	}

	public void setRightLeg3(Body rightLeg3) {
		this.rightLeg3 = rightLeg3;
	}

	public Body getLeftLeg1() {
		return leftLeg1;
	}

	public void setLeftLeg1(Body leftLeg1) {
		this.leftLeg1 = leftLeg1;
	}

	public Body getLeftLeg2() {
		return leftLeg2;
	}

	public void setLeftLeg2(Body leftLeg2) {
		this.leftLeg2 = leftLeg2;
	}

	public Body getLeftLeg3() {
		return leftLeg3;
	}

	public void setLeftLeg3(Body leftLeg3) {
		this.leftLeg3 = leftLeg3;
	}

}