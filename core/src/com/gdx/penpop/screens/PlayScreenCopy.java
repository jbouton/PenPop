package com.gdx.penpop.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.gdx.penpop.PenPop;
import com.gdx.penpop.assets.Assets;
import com.gdx.penpop.player.Crab;
import com.gdx.penpop.sprites.SpriteInfo;
import com.gdx.penpop.targets.FishTarget;
import com.gdx.penpop.targets.IceBall;
import com.gdx.penpop.targets.IceSupports;
import com.gdx.penpop.targets.Icicle;
import com.gdx.penpop.targets.SnowFlake;
import com.gdx.penpop.tween.BodyAccessor;
import com.gdx.penpop.tween.SpriteAccessor;

import java.util.LinkedList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quad;

public class PlayScreenCopy implements Screen, ApplicationListener, InputProcessor, GestureListener {
	public static boolean bDraw = false;
	private OrthographicCamera camera;
	private Crab crab;
	private SpriteBatch spriteBatch;
	private float lerp = 0.1f;
	private TweenManager tweenManager = new TweenManager();
	private TextureAtlas atlas;
	private float screenHeight, screenWidth;
	public ParticleEffect particleEffectArray[], windEffectArray[];
	public ParticleEffect windParticle;
	public ParticleEffect crabParticle;
	public float viewDistance = 5.5f;
	private PenPop game;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private static final float WB = 0.01f;
	private static final float BW = 100f;
	private Assets assets;
	private Vector3 position;
	private SpriteInfo tempSprite, bgSprite;
	private float aspectRatio;
	private LinkedList<Body> deleteBodyList, activeList;
	private Sprite[] bgSpriteArray;
	private Array<Body> bodies;
	private int ballCount;
	private boolean bShake;
	private int icicleCount;
	private float iciclePixs;
	private Vector2 penSpeed;
	private float timePassed;
	private IceSupports iceSupport;
	private boolean bJump;
	private BitmapFont font;
	private Body curBody;
	private SpriteInfo curSprite;
	private Vector2[] windArray;
	private float windDist;
	private float penAngle;
	public PlayScreenCopy(PenPop game) {
		this.game = game;
	}

	private void createBallJoint(Body ballBody, Body targetBody) {
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.collideConnected = false;
		Vector2 anchor = null;
		anchor = ballBody.getWorldCenter();
		jointDef.bodyA = ballBody;
		jointDef.bodyB = targetBody;
		jointDef.localAnchorB.set(targetBody.getLocalPoint(anchor));
		jointDef.localAnchorA.set(ballBody.getLocalPoint(anchor));
		RevoluteJoint rj = (RevoluteJoint) world.createJoint(jointDef);

	}

	private void createCollisionListener() {
		world.setContactListener(new ContactListener() {
			private SpriteInfo SpriteInfoB;
			private SpriteInfo SpriteInfoA;
			private String spriteTypeA;
			private String spriteTypeB;

			@Override
			public void beginContact(Contact contact) {
				bJump = false;
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				WorldManifold worldManifold = contact.getWorldManifold();
				if (fixtureA.getBody().getUserData() != null && fixtureB.getBody().getUserData() != null) {
					SpriteInfoA = (SpriteInfo) fixtureA.getBody().getUserData();
					SpriteInfoB = (SpriteInfo) fixtureB.getBody().getUserData();
					spriteTypeA = SpriteInfoA.getName();
					spriteTypeB = SpriteInfoB.getName();
					// System.out.println(spriteTypeA +","+spriteTypeB);
					if (spriteTypeA.equals("pen") && spriteTypeB.equals("ball")) {

						// fixtureA.getBody().applyForce(new Vector2(0,8),
						// fixtureA.getBody().getWorldCenter(), true);
						// System.out.println("hit");
						// Tween.to(fixtureB.getBody(), BodyAccessor.RADIUS,
						// .3f).target(fixtureB.getBody().getFixtureList().get(0).getShape().getRadius()-.1f).setUserData(fixtureB.getBody()).setCallback(deleteCallback).start(tweenManager);
						// Tween.to(SpriteInfoB, SpriteAccessor.SCALE_XY,
						// .3f).target(SpriteInfoB.getScaleY()-.2f).start(tweenManager);
						bJump = true;
						Vector2 vc = crab.getbody().getLinearVelocity();
						// fixtureA.getBody().setLinearVelocity(new
						// Vector2(vc.x,10));
						float vY = Math.abs(vc.y);
						float vX = Math.abs(vc.x);

						for (int i = 0; i < particleEffectArray.length; i++) {
							if (particleEffectArray[i].isComplete() == true) {
								Vector2[] contactVector = worldManifold.getPoints();
								particleEffectArray[i].setPosition(contactVector[0].x * BW, (contactVector[0].y * BW));
								ParticleEmitter em = particleEffectArray[i].getEmitters().get(0);

								// System.out.println(vY + "," + vX);
								if (vY > 1 || vX > 1) {
									if (vY > vX) {
										em.setMaxParticleCount(((int) Math.abs(vY) * 20));

									} else {
										em.setMaxParticleCount(((int) Math.abs(vX) * 20));

									}
								} else {
									em.setMaxParticleCount(10);
									em.setMinParticleCount(5);

								}
								// System.out.println(em.getMaxParticleCount());
								particleEffectArray[i].start();
								break;

							}
						}
					}
					if (spriteTypeA.equals("pen") && spriteTypeB.equals("snowFlake")) {
						Tween.to(SpriteInfoB, SpriteAccessor.SCALE_XY, .2f).target(.01f).start(tweenManager);
						Tween.to(fixtureB.getBody(), BodyAccessor.RADIUS2, .2f).target(.01f).setUserData(fixtureB.getBody()).setCallback(deleteCallback).start(tweenManager);
						;

						// System.out.println("Hit snow");

					}
					if (spriteTypeA.equals("pen") && spriteTypeB.equals("star")) {
						Tween.to(SpriteInfoB, SpriteAccessor.SCALE_XY, .2f).target(.01f).setUserData(fixtureB.getBody()).setCallback(deleteCallback).start(tweenManager);

					}
					if (spriteTypeA.equals("icicle") && spriteTypeB.equals("pen")) {
						// tweenManager.killTarget(fixtureA.getBody());
						// activeList.add(fixtureA.getBody());

					}

				}
			}

			@Override
			public void endContact(Contact contact) {

				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				if (fixtureA.getBody().getUserData() != null && fixtureB.getBody().getUserData() != null) {
					SpriteInfoA = (SpriteInfo) fixtureA.getBody().getUserData();
					SpriteInfoB = (SpriteInfo) fixtureB.getBody().getUserData();
					spriteTypeA = SpriteInfoA.getName();
					spriteTypeB = SpriteInfoB.getName();
					// System.out.println(spriteTypeA + "," + spriteTypeB);

				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

		});
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		world.dispose();
		// tweenManager.killAll();
		atlas.dispose();

	}

	@Override
	public void render() {

	}

	@Override
	public void resize(int width, int height) {
		// System.out.println("resize");
		aspectRatio = (float) height / (float) width;
		camera.viewportHeight = (1080 * aspectRatio) * 1.75f;
		camera.viewportWidth = (1920 * aspectRatio) * 1.75f;
		camera.position.x = screenWidth / 2;
		camera.position.y = screenHeight / 2;
		// screenHeight=camera.viewportHeight;
		// screenWidth=camera.viewportWidth;
		camera.update();
		// float aspectRatio = (float) height / (float) width; // swapped these
		// camera = new OrthographicCamera(32f, 32f * aspectRatio);
		// cameraY = camera.position.y;
		// screenCenter = camera.viewportWidth;
		// waterSprite.setSize(camera.viewportWidth, waterSprite.getHeight());
		// waterSprite1.setSize(camera.viewportWidth, waterSprite.getHeight());
		// screenRatio=screenWidth/screenHeight;

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	private final TweenCallback activeCallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			// System.out.println("hit");
			if (source.getUserData() != null) {
				Body bdy = (Body) source.getUserData();

				tweenManager.killTarget(bdy);
				activeList.add(bdy);
				// createIceicle();

			}

		}
	};
	private final TweenCallback deleteCallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (source.getUserData() != null) {
				Body bdy = (Body) source.getUserData();

				tweenManager.killTarget(bdy);
				SpriteInfo infoSprite = (SpriteInfo) bdy.getUserData();
				// System.out.println(bdy.getFixtureList().get(0).getShape().getRadius());
				if (infoSprite.getName().equals("snowFlake")) {
					ballCount = ballCount + 5;
					System.out.println("snow plus 5");
				}
				if (bdy.getFixtureList().get(0).getShape().getRadius() < .1) {
					// createIceBall();
					deleteBodyList.add(bdy);
					// System.out.println("Delete");
				}

			}

		}
	};
	private float gx;
	private Vector2 gravity;
	private float gy;

	public void createIceBall(float x, float y) {
		IceBall iceBall = new IceBall(world, x, y, .1f, assets);
		iceBall.createIceBall();
		// createBallJoint(iceBall.getIceBody(), iceSupport.getIceBody());

		SpriteInfo tempSprite = (SpriteInfo) iceBall.getIceBody().getUserData();
		// tempSprite.setScale(2, 2);
		tempSprite.setColor(1, 1, 1, .8f);
		float rand = (float) (Math.random() * 360);
		iceBall.getIceBody().setTransform(iceBall.getIceBody().getPosition().x, iceBall.getIceBody().getPosition().y, rand);
		tempSprite.setRotation((float) rand);
		Timeline.createParallel().push(Tween.to(iceBall.getIceBody(), BodyAccessor.RADIUS, .3f).target(1)).push(Tween.to(tempSprite, SpriteAccessor.SCALE_XY, .3f).target(1))

		.start(tweenManager);

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (ballCount > 0) {
			if (pointer == 0) {
				Vector3 touchPos = new Vector3(0, 0, 0);
				touchPos.x = screenX;
				touchPos.y = screenY;
				touchPos.z = 0;
				camera.unproject(touchPos);
				IceBall iceBall1 = new IceBall(world, ((touchPos.x * WB)), ((touchPos.y) * WB), .1f, assets);
				iceBall1.createIceBall();
				// ballListShow.add(ballCountShow,iceBall1.getIceBody());
				curBody = iceBall1.getIceBody();
				ballCount--;
				SpriteInfo tempSprite = (SpriteInfo) iceBall1.getIceBody().getUserData();
				tempSprite.setName("ball");
				tempSprite.setScale(.1f, .1f);
				tempSprite.setColor(1, 1, 1, .8f);
				curSprite = tempSprite;
				float rand = (float) (Math.random() * 360);

				iceBall1.getIceBody().setTransform(iceBall1.getIceBody().getPosition().x, iceBall1.getIceBody().getPosition().y, rand);
				tempSprite.setRotation((float) rand);
				Timeline.createParallel().push(Timeline.createSequence().push(Tween.to(iceBall1.getIceBody(), BodyAccessor.RADIUS, .5f).target(2f)).push(Tween.to(iceBall1.getIceBody(), BodyAccessor.RADIUS, 2.5f).target(.01f)).setUserData(iceBall1.getIceBody()).setCallback(deleteCallback).start(tweenManager)).push(
					Timeline.createSequence().push(Tween.to(tempSprite, SpriteAccessor.SCALE_XY, .5f).target(2)).push(Tween.to(tempSprite, SpriteAccessor.SCALE_XY, 2.5f).target(.01f)).start(tweenManager));
				/*
				 * Timeline.createParallel()
				 * .push(Tween.to(iceBall1.getIceBody(), BodyAccessor.RADIUS,
				 * .3f).target(.5f)) .push(Tween.to(tempSprite,
				 * SpriteAccessor.SCALE_XY, .3f).target(1f))
				 * 
				 * .start(tweenManager);
				 */
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (curBody != null) {
			tweenManager.killTarget(curBody);
			Tween.to(curBody, BodyAccessor.RADIUS, 2.5f).target(.01f).setUserData(curBody).setCallback(deleteCallback).start(tweenManager);
			Tween.to(curSprite, SpriteAccessor.SCALE_XY, 2.5f).target(.01f).start(tweenManager);
			curBody = null;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void render(float delta) {
		timePassed = timePassed + delta;
		if (timePassed > 1) {
			// System.out.println(timePassed);
			// createSnowFlake();
			// createFish();
			timePassed = 0;
		}
		// System.out.println(iceBall1.getIceBody().getPosition().y-crab.getbody().getPosition().y
		// );
		if (crab.getbody().getPosition().y < -10.5) {
			//game.setScreen(game.mainMenuScreen);

		}

		penSpeed = crab.getbody().getLinearVelocity();
		if (penSpeed.x > 20)
			crab.getbody().applyForce(new Vector2(0, -10), crab.getbody().getWorldCenter(), true);
		if (bDraw)
			Gdx.gl.glClearColor(1, 1, 1, 0);
		else
			Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gy = Gdx.input.getAccelerometerY();
		gx = Gdx.input.getAccelerometerX();
		//System.out.println(gy);
		Vector2 penPosition = crab.getbody().getWorldCenter();
		crab.getbody().applyForce(new Vector2(gy * 3, 0), new Vector2(penPosition.x - .05f, penPosition.y), true);
		
		world.step(delta, 6, 4);
		// set up camera for world
		Matrix4 cameraCopy = camera.combined.cpy();

		try {
			tweenManager.update(Gdx.graphics.getDeltaTime());
		} catch (Exception e) {

		}
		position = camera.position;
		position.x += ((BW * crab.getbody().getPosition().x) - position.x) * lerp;
		camera.update();

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		bgSprite.setPosition(position.x - (bgSprite.getWidth() / 2), 0);
		if (bDraw)
			bgSprite.draw(spriteBatch);
		for (Sprite spriteTemp : bgSpriteArray) {
			if (camera.position.x < (spriteTemp.getX() - (spriteTemp.getWidth() * 2))) {
				int x = (int) (spriteTemp.getX() - (bgSpriteArray.length * (screenWidth)));
				spriteTemp.setPosition(x, 0);
			}
			if (camera.position.x > (spriteTemp.getX() + (spriteTemp.getWidth() * 2))) {
				int x = (int) (spriteTemp.getX() + (bgSpriteArray.length * (screenWidth)));
				spriteTemp.setPosition(x, 0);
			}
			if (bDraw)
				spriteTemp.draw(spriteBatch);
		}
		for (int i = 0; i < windArray.length; i++) {
			if (camera.position.x > (windArray[i].x * BW) + screenWidth) {
				float x = (windArray[i].x * BW) + (screenWidth * windDist) * windArray.length;
				Vector2 vc = new Vector2(x * WB, 0);
				windArray[i] = vc;
			}
			if (camera.position.x < (windArray[i].x * BW) - screenWidth) {
				float x = (windArray[i].x * BW) - (screenWidth * windDist) * windArray.length;
				Vector2 vc = new Vector2(x * WB, 0);
				windArray[i] = vc;
			}

		}
		position = camera.position;
		for (Body tempBody : activeList) {
			tempBody.setType(BodyType.DynamicBody);
			tempBody.applyForce(new Vector2(-5.2f, 0), new Vector2(0, 0), true);
			/*
			 * if (bShake) { tempBody.applyForce(new Vector2(-5.2f, 0), new
			 * Vector2(0, 0), true); bShake = false; } else { bShake = true;
			 * tempBody.applyForce(new Vector2(5.2f, 0), new Vector2(0, 0),
			 * true); }
			 */
		}
		activeList.clear();
		bodies = new Array<Body>();

		world.getBodies(bodies);
		// System.out.println(icicleCount);
		for (Body worldBodie : bodies) {
			if (worldBodie.getUserData() != null) {
				for (int i = 0; i < windArray.length; i++) {
					Vector2 magnetPosition = windArray[i];
					// System.out.println()
					Vector2 distance = (new Vector2(0, 0));
					distance.add(worldBodie.getWorldCenter());
					distance.sub(magnetPosition);
					float finalDistance = distance.len();
					distance.set(distance.x, distance.y);
					float sum = Math.abs(distance.x) + Math.abs(distance.y);
					distance.scl(((1 / sum) * 15f / finalDistance));
					tempSprite = (SpriteInfo) worldBodie.getUserData();
					if (tempSprite.getName().equals("pen") && finalDistance < 7) {
						distance.scl(((1 / sum) * 160f / finalDistance));
						worldBodie.applyForce(distance, magnetPosition, true);
					}
					if (tempSprite.getName().equals("snowFlake") && finalDistance < 10) {
						// System.out.println("snowflake");
						distance.scl(((1 / sum) / (finalDistance)) * .1f);
						// worldBodie.setAngularVelocity(.1f);
						worldBodie.applyForce(distance, magnetPosition, true);
					}
					if (tempSprite.getName().equals("star") && finalDistance < 10) {
						// System.out.println("snowflake");
						distance.scl(((1 / sum) / (finalDistance)) * .1f);
						// worldBodie.setAngularVelocity(.1f);
						worldBodie.applyForce(distance, magnetPosition, true);
					}

					// worldBodie.setTransform(worldBodie.getPosition(), 0);
				}
				if (worldBodie.getPosition().y < -15) {
					System.out.println("delete below -15 " + tempSprite.getName());
					deleteBodyList.add(worldBodie);

				}
				if (!(tempSprite.getName().equals("pen") || tempSprite.getName().equals("armLeft") || tempSprite.getName().equals("armRight")))
					updateDraw(worldBodie, spriteBatch);

			}
		}
		
		

		for (int i = 0; i < windEffectArray.length; i++) {
			windEffectArray[i].setPosition(windArray[i].x * BW, windArray[i].y);
			if (bDraw)
				windEffectArray[i].draw(spriteBatch);
			windEffectArray[i].update(Gdx.graphics.getDeltaTime());

		}
		updateDraw(crab.getLeftArmbody(), spriteBatch);
		updateDraw(crab.getRightArmBody(), spriteBatch);
		updateDraw(crab.getRightLeg1(), spriteBatch);
		updateDraw(crab.getRightLeg2(), spriteBatch);
		updateDraw(crab.getRightLeg3(), spriteBatch);
		updateDraw(crab.getLeftLeg1(), spriteBatch);
		updateDraw(crab.getLeftLeg2(), spriteBatch);
		updateDraw(crab.getLeftLeg3(), spriteBatch);
		updateDraw(crab.getbody(), spriteBatch);
		Vector2 vcl = new Vector2(crab.getLeftArmbody().getWorldCenter());
		Vector2 vcr = new Vector2(crab.getRightArmBody().getWorldCenter());
		//{ (x1+x2)/2, (y1+y2)/2 }
		
		crabParticle.setPosition(((vcl.x+vcr.x)/2)*BW, ((vcl.y+vcr.y)/2)*BW);
		crabParticle.draw(spriteBatch);
		crabParticle.update(Gdx.graphics.getDeltaTime());
		// ballCountSprite.setPosition(windUp.x*BW, windUp.y*BW);
		// ballCountSprite.draw(spriteBatch);
		for (int i = 0; i < particleEffectArray.length; i++) {
			if (bDraw)
			particleEffectArray[i].draw(spriteBatch, delta);
			particleEffectArray[i].update(Gdx.graphics.getDeltaTime());
		}
		
		for (Body bodyTemp : deleteBodyList) {

			try {
				SpriteInfo tempSprite = (SpriteInfo) bodyTemp.getUserData();
				// System.out.println("Delete " + tempSprite.getName());
				tweenManager.killTarget(tempSprite);
				tweenManager.killTarget(bodyTemp);
				world.destroyBody(bodyTemp);
				bodyTemp.setUserData(null);
			} catch (Exception e) {
			}
		}
		deleteBodyList.clear();
		// ballCountSprite.setPosition(position.x+(screenWidth/3-(ballCountSprite.getWidth()/2)),
		// screenHeight-350);
		// ballCountSprite.draw(spriteBatch);
		if (ballCount < 6)
			font.setColor(Color.RED);
		else
			font.setColor(Color.WHITE);
		font.draw(spriteBatch, String.valueOf(ballCount), position.x + (screenWidth / 3), screenHeight - 150);
		spriteBatch.end();
		 if (bDraw == false)
		    debugRenderer.render(world, cameraCopy.scl(BW));

	}

	private void updateDraw(Body worldBodie, SpriteBatch spriteBatch) {

		SpriteInfo tempSprite = (SpriteInfo) worldBodie.getUserData();
		if (tempSprite.getName() != null)
			if (tempSprite.getName().equals("pen")) {
				if(gy<0){
					if(penAngle<4.5)
					penAngle=penAngle+.1f;
				}
				if(gy>0){
					if(penAngle>1.5)
					penAngle=penAngle-.1f;
				}
				crab.getbody().setTransform(crab.getbody().getWorldCenter(),penAngle);
						
				tempSprite.setPosition((worldBodie.getPosition().x * BW) - tempSprite.getWidth() / 2, (worldBodie.getPosition().y * BW) - tempSprite.getHeight() / 2);
				tempSprite.setRotation(MathUtils.radiansToDegrees * worldBodie.getAngle());
				} else {
				tempSprite.setPosition((worldBodie.getPosition().x * BW) - tempSprite.getWidth() / 2, (worldBodie.getPosition().y * BW) - tempSprite.getHeight() / 2);
				tempSprite.setRotation(MathUtils.radiansToDegrees * worldBodie.getAngle());
			}
		if (bDraw)
			tempSprite.draw(spriteBatch);
	}

	@Override
	public void show() {
		// setup input
		ballCount = 250;
		Tween.registerAccessor(Body.class, new BodyAccessor());
		Tween.registerAccessor(SpriteInfo.class, new SpriteAccessor());
		Tween.call(deleteCallback).start(tweenManager);
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(this);
		im.addProcessor(gd);
		im.addProcessor(this);
		Gdx.input.setInputProcessor(im);
		// screenWidth = Gdx.graphics.getWidth();
		// screenHeight = Gdx.graphics.getHeight();
		screenWidth = 1920;
		screenHeight = 1080;
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		// camera.rotate(90);
		camera.update();
		assets = new Assets();
		world = new World(new Vector2(0, -15), true);
		debugRenderer = new Box2DDebugRenderer();
		float centerX = Gdx.graphics.getWidth() / 2 * WB;
		float centerY = Gdx.graphics.getHeight() / 2 * WB;
		// System.out.println(centerX + "," + centerY);

		/*
		 * Timeline.createParallel() .push(Tween.to(crab.getbody(),
		 * BodyAccessor.POSITION_XY, 1.3f).target(centerX,centerY))
		 * .push(Tween.to(Crab.getRightArmBody(), BodyAccessor.POSITION_XY,
		 * 1.3f).target(centerX,centerY))
		 * .push(Tween.to(Crab.getLeftArmbody(), BodyAccessor.POSITION_XY,
		 * 1.3f).target(centerX,centerY)) .start(tweenManager);
		 */
		activeList = new LinkedList<Body>();
		deleteBodyList = new LinkedList<Body>();
		// createIceBall((screenWidth / 2) * WB, screenHeight / 2 * WB);
		float r = 5 * BW;

		crab = new Crab(world, (screenWidth * windDist) * WB, ((screenHeight/2) * WB), .5f, assets);
		crab.createCrab();
	
		
		crab.getbody().setLinearDamping(.2f);
		int supportSize = 15;
		// iceSupport = new IceSupports(world, (screenWidth / 2)* WB,
		// (screenHeight / 2) * WB, supportSize, assets);
		// iceSupport.createIceBall();

		int ballCount = 30;

		/*
		 * for (int i = 0; i < ballCount; i++) { Vector2 vc = new
		 * Vector2(crab.getbody().getPosition()); //
		 * System.out.println("vc.x"+vc.x); float x = (float) (screenWidth / 2 +
		 * r * Math.cos(2 * Math.PI * i / ballCount)); float y = (float)
		 * (screenHeight / 2 + r * Math.sin(2 * Math.PI * i / ballCount)); //
		 * System.out.println(x*WB+","+y*WB); createIceBall(x * WB, y * WB); }
		 */
		// for (int i = 0; i < 20; i++) {
		// createIceBall(i,(screenHeight / 2)*WB);
		// }
		crabParticle = new ParticleEffect();
		crabParticle.load(Gdx.files.internal("data/crabEmitter"), Gdx.files.internal("data"));
		
		crabParticle.start();
	
		windDist = .5f;
		windArray = new Vector2[5];
		windEffectArray = new ParticleEffect[5];
		for (int i = 0; i < windEffectArray.length; i++) {
			windEffectArray[i] = new ParticleEffect();
			windEffectArray[i].load(Gdx.files.internal("data/bubbleEmitter"), Gdx.files.internal("data"));
			windEffectArray[i].setPosition((screenWidth * windDist) * i, 0);
			windEffectArray[i].start();
			Vector2 wind = new Vector2(((screenWidth * windDist) * i) * WB, 0);
			windArray[i] = wind;
		}
		particleEffectArray = new ParticleEffect[5];
		for (int i = 0; i < particleEffectArray.length; i++) {
			particleEffectArray[i] = new ParticleEffect();
			particleEffectArray[i].load(Gdx.files.internal("data/ballEmitter"), Gdx.files.internal("data"));
		}
		spriteBatch = new SpriteBatch();
		// Setup background sprites

		bgSpriteArray = new Sprite[5];
		for (int i = 0; i < bgSpriteArray.length; i++) {
			SpriteInfo spriteTemp = new SpriteInfo(assets.getSprite("backObj" + i));
			// spriteTemp.setRegionWidth((int) screenWidth);
			spriteTemp.setSize(screenWidth, spriteTemp.getHeight());
			spriteTemp.setPosition((screenWidth) * i, 0);
			bgSpriteArray[i] = spriteTemp;
		}
		// createSnowFlake( (screenWidth/2) * WB, (screenHeight*2 * WB));
		// for (int i = 0; i < 10; i++) {
		// createIceicle();
		// }
		/*
		 * Timer.schedule(new Task(){
		 * 
		 * @Override public void run() { createSnowFlake(); } } , 1 // (delay) ,
		 * 1 // (seconds) );
		 */
		createCollisionListener();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/samplefont.ttf"));
		font = generator.generateFont(55);
		font.setColor(Color.RED);
		generator.dispose();

		bgSprite = new SpriteInfo(assets.getSprite("back"));
		bgSprite.setSize(screenWidth, screenHeight);
		// bgSprite.setColor(bgSprite.getColor().r, bgSprite.getColor().g,
		// bgSprite.getColor().b, .50f);

	}

	public void createSnowFlake() {
		float penX = crab.getbody().getPosition().x;
		float y = ((screenHeight - 125) * WB);
		float rand = (float) (Math.random() * 5);
		if (rand > 2)
			rand = rand * -1;
		SnowFlake snowFlake = new SnowFlake(world, penX + rand, y, .5f, assets);
		snowFlake.createSnowFlake();

	}

	public void createFish() {
		float penX = crab.getbody().getPosition().x;
		float y = ((screenHeight) * WB);
		float rand = (float) (Math.random() * 5);
		if (rand > 2)
			rand = rand * -1;
		FishTarget fishTarget = new FishTarget(world, penX, -1, .5f, assets);
		fishTarget.createFishTarget();

	}

	public void createIceicle() {

		// if (icicleCount < 10) {
		icicleCount++;
		float penX = crab.getbody().getPosition().x;
		float rand = (float) (Math.random() * 5);
		Icicle icicle = null;
		icicle = new Icicle(world, penX + (screenWidth * WB), screenHeight * WB, .5f, assets);
		icicle.createIcicle();
		Body bdy = icicle.getIceBody();
		float rand1 = (float) (Math.random() * 4) + 3;
		tempSprite = (SpriteInfo) bdy.getUserData();
		// tempSprite.setOrigin(tempSprite.getX(),-tempSprite.getHeight());
		// tempSprite.setScale(1,0);
		// tempSprite.setOrigin(0, 0);

		float shake = 2 * WB;
		Tween.to(tempSprite, SpriteAccessor.SCALE_Y, rand).target(-1 * (rand1 * .30f)).start(tweenManager);
		Timeline.createParallel().push(Timeline.createSequence().push(Tween.to(bdy, BodyAccessor.DRIP, rand).target(-1 * rand1).setUserData(bdy)).push(Tween.to(bdy, BodyAccessor.POSITION_XY, .1f).targetRelative(shake, 0).ease(Quad.OUT).repeatYoyo(30, 0.01f).setUserData(bdy).setCallback(activeCallback)

		)

		.push(Timeline.createSequence().push(Tween.to(tempSprite, SpriteAccessor.SCALE_Y, rand).target(-1 * rand1))).start(tweenManager));
		// }

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void create() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// if(penSpeed.x<15)
		// crab.getbody().applyForce(new Vector2(velocityX/20,0),
		// crab.getbody().getWorldCenter(), true);

		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
