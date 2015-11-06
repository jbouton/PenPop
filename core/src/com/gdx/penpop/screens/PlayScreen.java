package com.gdx.penpop.screens;

import java.util.ArrayList;
import java.util.LinkedList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bouton.penpop.player.Player;
import com.gdx.penpop.PenPop;
import com.gdx.penpop.assets.Assets;
import com.gdx.penpop.player.EnemyPenguin;
import com.gdx.penpop.player.Penguin;
import com.gdx.penpop.player.Penguin;
import com.gdx.penpop.sprites.SpriteInfo;
import com.gdx.penpop.targets.IceBall;
import com.gdx.penpop.targets.TargetSprite;
import com.gdx.penpop.tween.BodyAccessor;
import com.gdx.penpop.tween.SpriteAccessor;

public class PlayScreen implements Screen, ApplicationListener, InputProcessor, GestureListener {
	private int vW, vH;
	private Viewport viewport;
	public static boolean bDraw = true;
	private OrthographicCamera camera;
		private Penguin penguin;
	private SpriteBatch spriteBatch;
	private ArrayList<TargetSprite> targetList;
	private ArrayList<EnemyPenguin> enemyPenguins;
	private ArrayList<Body> waterBodies;
	private Body[] iceBodyArray, iceSupportArray;
	private float lerp = 0.1f;
	private TweenManager tweenManager = new TweenManager();
	private Player player;
	private TextureAtlas atlas;
	private Texture waterTexture;
	private float screenHeight, screenWidth;
	private ShapeRenderer shapeDebugger;
	int iGrass = 10, itargets = 0;
	private float scrollTimer;
	private Sprite waterSprite;
	private Rectangle tempRec;
	private boolean bAllGone;
	public ParticleEffect particleEffectArray[];
	public float viewDistance = 5.5f;
	private boolean bUp = false;
	private PenPop game;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private static final float WB = 0.01f;
	private static final float BW = 100f;
	private Body magnetBody, playerBody, groundBody, holeBody, ceilingBody;
	private float fForce = .02f, waterSpeed;
	private Vector2 playerVelocity = new Vector2();
	private Body iceBody, penguinBody, leftArmbody, rightArmBody;
	private Vector2 force;
	private float dX, uX, iceBodyWidth;
	private SpriteInfo spriteInfoTemp;
	private Assets assets;
	private float iceFlowCounter, penMoved;
	private SpriteInfo tempSprite2;
	private Vector3 position;
	private float prev_x = 1000;
	private SpriteInfo tempSprite;
	private Sprite waterSprite1;
	private float iceOffset = 2.00f, iceWidth = 480;
	private boolean bWaterChange;
	private boolean bChangeBall;
	private int iceLength = 10, iceThick = 150;
	private int iceHeight = 200;
	private int iceSupportHeight = 75;
	private Vector2 penguinSpeed = new Vector2();
	private Vector2 iceDrop = new Vector2(0, 0);
	private EnemyPenguin penguin1;
	private float aspectRatio;
	private float penFall;
	private IceBall iceBall1, iceBall2;
	private LinkedList<Body> ballListShow, deleteBodyList, activeList;
	private int ballCountShow, ballCountDelete;
	private Body bodyTemp;
	private Sprite[] bgSpriteArray;
	private boolean bFling;
	private float curHeight;

	public PlayScreen(PenPop game) {
		this.game = game;
	}

	private void createCollisionListener() {
		world.setContactListener(new ContactListener() {
			private SpriteInfo tempZspriteB;
			private SpriteInfo tempZspriteA;
			private String spriteTypeA;
			private String spriteTypeB;

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				WorldManifold worldManifold = contact.getWorldManifold();
				if (fixtureA.getBody().getUserData() != null && fixtureB.getBody().getUserData() != null) {
					tempZspriteA = (SpriteInfo) fixtureA.getBody().getUserData();
					tempZspriteB = (SpriteInfo) fixtureB.getBody().getUserData();
					spriteTypeA = tempZspriteA.getName();
					spriteTypeB = tempZspriteB.getName();
					//System.out.println(spriteTypeA + "," + spriteTypeB);
					if (spriteTypeA.equals("pen") && spriteTypeB.equals("ball")) {
						for (int i = 0; i < particleEffectArray.length; i++) {
							if (particleEffectArray[i].isComplete() == true) {
								Vector2[] contactVector = worldManifold.getPoints();
								particleEffectArray[i].setPosition(contactVector[0].x * BW, (contactVector[0].y * BW));
								ParticleEmitter em = particleEffectArray[i].getEmitters().get(0);
								Vector2 vc = penguin.getPenguinBody().getLinearVelocity();
								// System.out.println("x:"+vc.x+"y:"+vc.y);
								float vY = Math.abs(vc.y);
								float vX = Math.abs(vc.x);
								// System.out.println(vY + "," + vX);
								if (vY > 3 || vX > 3) {
									if (vY > vX) {
										em.setMaxParticleCount(((int) Math.abs(vY) * 20));

									} else {
										em.setMaxParticleCount(((int) Math.abs(vX) * 20));

									}
								} else {
									em.setMaxParticleCount(5);

								}
								// System.out.println(em.getMaxParticleCount());
								particleEffectArray[i].start();
								break;

							}
						}
					}
					if (spriteTypeA.equals("iceFlow")) {
						//fixtureA.setSensor(true);
						//fixtureA.getBody().setType(BodyType.DynamicBody);

					}
					if (spriteTypeA.equals("pen") && spriteTypeB.equals("iceFlow")) {
						//fixtureB.setSensor(true);
						activeList.add(fixtureB.getBody());

					}
					if (spriteTypeA.equals("iceFlow") && spriteTypeB.equals("pen")) {
						//fixtureA.setSensor(true);
						activeList.add(fixtureA.getBody());

					}

				}
			}

			@Override
			public void endContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				if (fixtureA.getBody().getUserData() != null && fixtureB.getBody().getUserData() != null) {
					tempZspriteA = (SpriteInfo) fixtureA.getBody().getUserData();
					tempZspriteB = (SpriteInfo) fixtureB.getBody().getUserData();
					spriteTypeA = tempZspriteA.getName();
					spriteTypeB = tempZspriteB.getName();
					// System.out.println(spriteTypeA +","+spriteTypeB);

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
		tweenManager.killAll();
		//atlas.dispose();

	}



	@Override
	public void resize(int width, int height) {
		// System.out.println("resize");
		viewport.update(width, height);
		camera.update();

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

	private final TweenCallback deleteCallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (source.getUserData() != null) {
				Body bdy = (Body) source.getUserData();
				tweenManager.killTarget(bdy);
				deleteBodyList.add(bdy);

			}

		}
	};
	private Array<Body> bodies;
	private float xball;
	private float yball;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 touchPos = new Vector3(0, 0, 0);
		touchPos.x = screenX;
		touchPos.y = screenY;
		touchPos.z = 0;
		camera.unproject(touchPos);
		IceBall iceBall1 = new IceBall(world, ((touchPos.x * WB)), ((touchPos.y) * WB), .1f, assets);
		iceBall1.createIceBall();
		// ballListShow.add(ballCountShow,iceBall1.getIceBody());
		// ballCountShow++;
		SpriteInfo tempSprite = (SpriteInfo) iceBall1.getIceBody().getUserData();
		tempSprite.setScale(.1f, .1f);
		tempSprite.setColor(1, 1, 1, .8f);
		float rand = (float) (Math.random() * 360);

		iceBall1.getIceBody().setTransform(iceBall1.getIceBody().getPosition().x, iceBall1.getIceBody().getPosition().y, rand);
		tempSprite.setRotation((float) rand);
		Timeline.createParallel().push(Timeline.createSequence().push(Tween.to(iceBall1.getIceBody(), BodyAccessor.RADIUS, .3f).target(.5f)).push(Tween.to(iceBall1.getIceBody(), BodyAccessor.RADIUS, 2.5f).target(.01f)).setUserData(iceBall1.getIceBody()).setCallback(deleteCallback).start(tweenManager)).push(
				Timeline.createSequence().push(Tween.to(tempSprite, SpriteAccessor.SCALE_XY, .3f).target(1f)).push(Tween.to(tempSprite, SpriteAccessor.SCALE_XY, 2.5f).target(.01f))

						.start(tweenManager));
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		uX = screenX;
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
	public void render() {

	}
	@Override
	public void render(float delta) {
		// System.out.println(iceBall1.getIceBody().getPosition().y-penguin.getPenguinBody().getPosition().y
		// );

		//if (penguin.getPenguinBody().getPosition().y < -1){
		//	penguin=null;
		//	penguin = new Penguin(world, (screenWidth / 2) * WB, 4, .2f, assets);
		//	penguin.createPenguin();
		//}
		//System.out.println(curHeight+":"+(BW * penguin.getPenguinBody().getPosition().y)+":"+(viewport.getWorldHeight()));
		if ((curHeight-(BW * penguin.getPenguinBody().getPosition().y)>(viewport.getWorldHeight()))){
			this.dispose();
			game.setScreen(game.mainMenuScreen);
		}

		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(delta, 6, 3);
		// set up camera for world
		Matrix4 cameraCopy = camera.combined.cpy();
		//debugRenderer.render(world, cameraCopy.scl(BW));

		scrollTimer += (Gdx.graphics.getDeltaTime() / 5);
		waterSprite.setU(scrollTimer);
		waterSprite.setU2(scrollTimer - 1);
		waterSprite1.setU(scrollTimer);
		waterSprite1.setU2(scrollTimer + 1);
		try {
			tweenManager.update(Gdx.graphics.getDeltaTime());
		} catch (Exception e) {

		}
		position = camera.position;
		//System.out.println("camera:" +  position.y +",pen:"+((BW * penguin.getPenguinBody().getPosition().y)-viewport.getScreenHeight()/2));
		if(BW * penguin.getPenguinBody().getPosition().y>curHeight)
			curHeight=BW * penguin.getPenguinBody().getPosition().y;
		//if (position.y  < ((BW * penguin.getPenguinBody().getPosition().y)-viewport.getScreenHeight()/2));
		//position.y += ((BW * penguin.getPenguinBody().getPosition().y) - position.y) * lerp;
		position.y = curHeight;
		position.x = viewport.getScreenWidth()/2;

		camera.update();

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

		for (Sprite spriteTemp : bgSpriteArray) {
			if (camera.position.x < (spriteTemp.getX() - (spriteTemp.getWidth() * 2))) {
				spriteTemp.setPosition(spriteTemp.getX() - (bgSpriteArray.length * spriteTemp.getWidth()), 0);
			}
			if (camera.position.x > (spriteTemp.getX() + (spriteTemp.getWidth() * 2))) {
				spriteTemp.setPosition(spriteTemp.getX() + (bgSpriteArray.length * spriteTemp.getWidth()), 0);
			}
			 //if (bDraw)
			 //spriteTemp.draw(spriteBatch);
		}

		for (EnemyPenguin penTemp : enemyPenguins) {
			updateDraw(penTemp.getPenguinBody(), spriteBatch);
			updateDraw(penTemp.getLeftArmbody(), spriteBatch);
			updateDraw(penTemp.getRightArmBody(), spriteBatch);

		}
		position = camera.position;
		waterSprite.setPosition(position.x - waterSprite.getWidth() / 2, waterSprite.getY());
		waterSprite1.setPosition(position.x - waterSprite.getWidth() / 2, waterSprite.getY());
		if (bDraw) {
			// waterSprite.draw(spriteBatch);
			// waterSprite1.draw(spriteBatch);
		}
		// for (Sprite tempSprite : targetList) {
		// tempSprite.draw(spriteBatch);
		// }
		/*
		 * for(Body bodyTemp:ballListShow){ tempSprite = (SpriteInfo)
		 * bodyTemp.getUserData(); xball = BW * (bodyTemp.getWorldCenter().x) -
		 * tempSprite.getWidth() / 2; yball = BW * (bodyTemp.getPosition().y) -
		 * tempSprite.getHeight() / 2; tempSprite.setX(xball);
		 * tempSprite.setY(yball); tempSprite.draw(spriteBatch);
		 * 
		 * }
		 */
		//
		// ballCountDelete=0;

		for (Body tempBody : activeList) {
			tempBody.setType(BodyType.DynamicBody);
		}
		activeList.clear();
		bodies = new Array<Body>();

		world.getBodies(bodies);
		// System.out.println(world.getBodyCount());
		for (Body worldBodie : bodies) {
			if (worldBodie.getUserData() != null) {
				tempSprite = (SpriteInfo) worldBodie.getUserData();

				if (worldBodie.getPosition().y < -2) {
					System.out.println("below -2");
					//deleteBodyList.add(worldBodie);

				}
				//if (!(tempSprite.getName().equals("pen") || tempSprite.getName().equals("armLeft") || tempSprite.getName().equals("armRight")))
					updateDraw(worldBodie, spriteBatch);

			}
		}
		//if(penguin!=null) {
			//updateDraw(penguin.getPenguinBody(), spriteBatch);
		//	updateDraw(penguin.getLeftArmbody(), spriteBatch);
			//updateDraw(penguin.getRightArmBody(), spriteBatch);
		//}
		for (int i = 0; i < particleEffectArray.length; i++) {
			if(bDraw)
				particleEffectArray[i].draw(spriteBatch, delta);
		}
		for (Body bodyTemp : deleteBodyList) {

			try {
				SpriteInfo tempSprite = (SpriteInfo) bodyTemp.getUserData();
				System.out.println("Delete " + tempSprite.getName());
				tweenManager.killTarget(tempSprite);
				tweenManager.killTarget(bodyTemp);
				world.destroyBody(bodyTemp);
				bodyTemp.setUserData(null);
			} catch (Exception e) {
			}
		}
		deleteBodyList.clear();
		spriteBatch.end();
		for (int i = 0; i < particleEffectArray.length; i++) {
			//particleEffectArray[i].update(Gdx.graphics.getDeltaTime());
		}
		// if (penguin.getPenguinBody().getPosition().y < .5f) {
		// particleEffect.setPosition(penguin.getPenguinBody().getPosition().x *
		// BW + penguin.getWidth() / 2, penguin.getPenguinBody().getPosition().y
		// * BW - penguin.getHeight() / 2);
		// particleEffect.setDuration(1);
		// particleEffect.start();
		// }
		//for (EnemyPenguin penTemp : enemyPenguins) {
			//if (penTemp.getPenguinBody().getPosition().y < -5f)
			//	game.setScreen(game.mainMenuScreen);

		//}
		//if(bDraw==false)
		//debugRenderer.render(world, cameraCopy.scl(BW));


	}

	private void updateDraw(Body worldBodie, SpriteBatch spriteBatch) {
		SpriteInfo tempSprite = (SpriteInfo) worldBodie.getUserData();
		if (tempSprite.getName() != null)
			// System.out.println(tempSprite.getName());
			tempSprite.setPosition((worldBodie.getPosition().x * BW) - tempSprite.getWidth() / 2, (worldBodie.getPosition().y * BW) - tempSprite.getHeight() / 2);
		tempSprite.setRotation(MathUtils.radiansToDegrees * worldBodie.getAngle());
		if (bDraw)
			tempSprite.draw(spriteBatch);
	}

	private void createGround(float x, float y) {
		// Ground body
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(x * WB, y * WB);
		groundBody = world.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox((screenWidth), 0);
		groundBody.createFixture(groundBox, 0.0f);

	}

	private void createWater(float x, float y, Sprite sprite) {
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(.1f);
		BodyDef circleBodyDef = new BodyDef();
		circleBodyDef = new BodyDef();
		circleBodyDef.type = BodyType.DynamicBody;
		circleBodyDef.position.x = x * this.WB;
		circleBodyDef.position.y = y * this.WB;

		FixtureDef waterDefixtureDef = new FixtureDef();
		waterDefixtureDef.shape = circleShape;
		waterDefixtureDef.friction = .4f;
		waterDefixtureDef.density = 1f;
		waterDefixtureDef.restitution = 0f;
		// playerDefixtureDef.isSensor = true;
		// fixtureDef.filter.groupIndex = 2;
		waterBodies.add(world.createBody(circleBodyDef));
		playerBody.setUserData(player);
		// spiderBodies.get(i).setAngularVelocity(20f);
		waterBodies.get(waterBodies.size() - 1).createFixture(waterDefixtureDef);
		circleShape.dispose();

	}

	private void createMagnet(float x, float y) {
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(.3f);
		BodyDef circleBodyDef = new BodyDef();
		circleBodyDef = new BodyDef();
		circleBodyDef.type = BodyType.StaticBody;
		circleBodyDef.position.x = x * this.WB;
		circleBodyDef.position.y = y * this.WB;

		FixtureDef magnetDefixtureDef = new FixtureDef();
		magnetDefixtureDef.shape = circleShape;
		magnetDefixtureDef.friction = 10.4f;
		magnetDefixtureDef.density = 10f;
		magnetDefixtureDef.restitution = 0f;
		// magnetDefixtureDef.isSensor = true;
		// fixtureDef.filter.groupIndex = 2;
		magnetBody = world.createBody(circleBodyDef);
		// spiderBodies.get(i).setAngularVelocity(20f);
		magnetBody.createFixture(magnetDefixtureDef);
		circleShape.dispose();

	}

	private void createIce(float pX, float pY, float pWidth) {
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(pWidth/8, pWidth);

		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.StaticBody;
		boxBodyDef.position.x = pX;
		boxBodyDef.position.y = pY;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = boxPoly;
		fixtureDef.friction = 0.4f;
		fixtureDef.density = 2.10f;
		fixtureDef.restitution = 0f;
		// fixtureDef.isSensor = true;
		// fixtureDef.filter.groupIndex = -1;
		iceBody = world.createBody(boxBodyDef);
		iceBody.createFixture(fixtureDef);
		iceBody.createFixture(boxPoly, 1);
		SpriteInfo spriteTemp = new SpriteInfo();
		spriteTemp=assets.getSprite("iceFlow");
		spriteTemp.setName("iceFlow");
		spriteTemp.setScale(0);
		boxBodyDef.type = BodyType.StaticBody;
		boxBodyDef.position.x = pX;
		boxBodyDef.position.y = pY;
		iceBody.setUserData(spriteTemp);
		boxPoly.setAsBox(pWidth / 8, pWidth / 8);
		//fixtureDef = new FixtureDef();
		//fixtureDef.shape = boxPoly;
		//fixtureDef.friction = 0.4f;
		//fixtureDef.density = .0f;
		//fixtureDef.restitution = 0f;

		//Body boxBody1 = world.createBody(boxBodyDef);
		//boxBody1.setUserData(spriteTemp);
		//boxBody1.createFixture(fixtureDef);
		//boxBody1.createFixture(boxPoly, 1);

		//boxPoly.dispose();

		// bHead = true;

	}

	private void createIceBlock(float x, float y) {

		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(1, 5.0f);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(1.5f);
		BodyDef circleBodyDef = new BodyDef();
		circleBodyDef = new BodyDef();
		circleBodyDef.type = BodyType.DynamicBody;
		circleBodyDef.position.x = x * this.WB;
		circleBodyDef.position.y = y * this.WB;

		FixtureDef holetDefixtureDef = new FixtureDef();
		holetDefixtureDef.shape = circleShape;
		holetDefixtureDef.friction = 10.4f;
		holetDefixtureDef.density = 15f;
		holetDefixtureDef.restitution = 0f;
		// magnetDefixtureDef.isSensor = true;
		// fixtureDef.filter.groupIndex = 2;
		holeBody = world.createBody(circleBodyDef);
		// spiderBodies.get(i).setAngularVelocity(20f);
		holeBody.createFixture(holetDefixtureDef);
		circleShape.dispose();

	}

	@Override
	public void show() {
		System.out.println("Hit show");
		curHeight = 0;
		// setup input
		Tween.registerAccessor(Body.class, new BodyAccessor());
		Tween.registerAccessor(SpriteInfo.class, new SpriteAccessor());
		Tween.call(deleteCallback).start(tweenManager);
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(this);
		im.addProcessor(gd);
		im.addProcessor(this);
		Gdx.input.setInputProcessor(im);
		iceBodyArray = new Body[iceLength];
		iceSupportArray = new Body[iceLength];
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.rotate(90);
		vW = 1080;
		vH = 720;
		viewport = new StretchViewport(vW, vH, camera);

		assets = new Assets();
		world = new World(new Vector2(0, -5), true);
		debugRenderer = new Box2DDebugRenderer();
		enemyPenguins = new ArrayList<EnemyPenguin>();
		for (int i = 0; i < 0; i++) {
			penguin1 = new EnemyPenguin(world, (screenWidth / 2) * WB, screenHeight * WB, .2f, assets);
			penguin1.createPenguin();
			enemyPenguins.add(penguin1);
		}
		//penguin = new Penguin(world, (screenWidth / 2) * WB, (screenHeight) * WB, .2f, assets);
		penguin = new Penguin(world, (screenWidth / 2) * WB, screenHeight / 2 * WB-2, .2f, assets);
		penguin.createPenguin();
		activeList = new java.util.LinkedList();
		deleteBodyList = new java.util.LinkedList();
		iceBall1 = new IceBall(world, (screenWidth / 2) * WB, screenHeight / 2 * WB-4, .5f, assets);
		iceBall1.createIceBall();
/*
		for (int i = 0; i < 10; i++) {
			Icicle icicle = new Icicle(world, (screenWidth / 2 * i) * WB, screenHeight * WB, .5f, assets);
			icicle.createIcicle();
			Body bdy = icicle.getIceBody();
			float rand = (float) (Math.random() * 10);
			float rand1 = (float) (Math.random() * 2) + 3;

			Tween.to(bdy, BodyAccessor.DRIP, rand + 2).target(-1 * (rand1)).start(tweenManager);
		}
		*/
		// ballListShow.add(ballCountShow,iceBall1.getIceBody());
		// ballCountShow++;
		particleEffectArray = new ParticleEffect[5];
		for (int i = 0; i < particleEffectArray.length; i++) {
			particleEffectArray[i] = new ParticleEffect();
			particleEffectArray[i].load(Gdx.files.internal("data/star"), Gdx.files.internal("data"));
		}
		spriteBatch = new SpriteBatch();
		waterTexture = new Texture(Gdx.files.internal("data/water.png"));
		waterTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		waterSprite = new Sprite(waterTexture);
		waterSprite1 = new Sprite(waterTexture);

		bgSpriteArray = new Sprite[5];
		for (int i = 0; i < bgSpriteArray.length; i++) {
			SpriteInfo spriteTemp = new SpriteInfo(assets.getSprite("paper"));
			spriteTemp.setSize(vW, vH);
			//spriteTemp.setColor(Color.BLUE);
			spriteTemp.setPosition(vW * i, 0);
			bgSpriteArray[i] = spriteTemp;
		}

		createCollisionListener();
		for(int i =0;i<10;i++)
			createIce(0,(i*2), .9f);
		for(int i =0;i<10;i++)
			createIce(screenWidth*WB,(i*2), .9f);
		//for(int i =1;i<10;i++)
			//createIce((i*2), 4, 1f);
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
		/*force = new Vector2(velocityX / 1150, -velocityY / 1150);
		if (uX - dX > 50) {
			if (velocityX < -50)
				penguin.getPenguinBody().applyLinearImpulse(force, penguin.getPenguinBody().getWorldCenter(), true);
			if (velocityX > 50)
				penguin.getPenguinBody().applyLinearImpulse(force, penguin.getPenguinBody().getWorldCenter(), true);
		} else {
			penguin.getPenguinBody().applyLinearImpulse(force, penguin.getPenguinBody().getWorldCenter(), true);
		}
		*/

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
