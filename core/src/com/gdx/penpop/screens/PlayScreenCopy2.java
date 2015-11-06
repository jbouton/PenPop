package com.gdx.penpop.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.utils.Array;
import com.gdx.penpop.PenPop;
import com.gdx.penpop.assets.Assets;
import com.gdx.penpop.player.Penguin;
import com.gdx.penpop.sprites.SpriteInfo;
import com.gdx.penpop.targets.IceBall;
import com.gdx.penpop.tween.BodyAccessor;
import com.gdx.penpop.tween.SpriteAccessor;

import java.util.LinkedList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class PlayScreenCopy2 implements Screen, ApplicationListener, InputProcessor, GestureListener {
	public static boolean bDraw = true;
	private OrthographicCamera camera;
	private Penguin penguin;
	private SpriteBatch spriteBatch;
	private float lerp = 0.1f;
	private TweenManager tweenManager = new TweenManager();
	private TextureAtlas atlas;
	private float screenHeight, screenWidth;
	public ParticleEffect particleEffectArray[];
	public float viewDistance = 5.5f;
	private PenPop game;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private static final float WB = 0.01f;
	private static final float BW = 100f;
	private Assets assets;
	private Vector3 position;
	private SpriteInfo tempSprite;
	private float aspectRatio;
	private LinkedList<Body> deleteBodyList, activeList;
	private Sprite[] bgSpriteArray;
	private Array<Body> bodies;
	private int ballCount = 1;

	public PlayScreenCopy2(PenPop game) {
		this.game = game;
	}

	private void createCollisionListener() {
		world.setContactListener(new ContactListener() {
			private SpriteInfo SpriteInfoB;
			private SpriteInfo SpriteInfoA;
			private String spriteTypeA;
			private String spriteTypeB;

			@Override
			public void beginContact(Contact contact) {
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
						Vector2 vc = penguin.getPenguinBody().getLinearVelocity();
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
					if (spriteTypeA.equals("pen") && spriteTypeB.equals("icicle")) {
						activeList.add(fixtureB.getBody());

					}
					if (spriteTypeA.equals("icicle") && spriteTypeB.equals("pen")) {
						activeList.add(fixtureA.getBody());

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
		camera.viewportHeight = (1080 * aspectRatio)*1.5f;
		camera.viewportWidth = (1920 * aspectRatio)*1.5f;
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

	private final TweenCallback deleteCallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (source.getUserData() != null) {
				Body bdy = (Body) source.getUserData();

				tweenManager.killTarget(bdy);
				// System.out.println(bdy.getFixtureList().get(0).getShape().getRadius());
				if (bdy.getFixtureList().get(0).getShape().getRadius() < .1) {
					// createIceBall();
					deleteBodyList.add(bdy);
				}

			}

		}
	};
	private float gx;
	private Vector2 gravity;
	private float gy;

	public void createIceBall(float x, float y) {
		IceBall iceBall = new IceBall(world, x, y, .5f, assets);
		iceBall.createIceBall();
		ballCount++;
		SpriteInfo tempSprite = (SpriteInfo) iceBall.getIceBody().getUserData();
		// tempSprite.setScale(2, 2);
		tempSprite.setColor(1, 1, 1, .8f);
		float rand = (float) (Math.random() * 360);
		iceBall.getIceBody().setTransform(iceBall.getIceBody().getPosition().x, iceBall.getIceBody().getPosition().y, rand);
		tempSprite.setRotation((float) rand);
		Timeline.createParallel().push(Tween.to(iceBall.getIceBody(), BodyAccessor.RADIUS, .3f).target(1)).push(Tween.to(tempSprite, SpriteAccessor.SCALE_XY, .3f).target(2))

				.start(tweenManager);

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(pointer==0){
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
			Timeline.createParallel().push(Timeline.createSequence().push(Tween.to(iceBall1.getIceBody(), BodyAccessor.RADIUS, .3f).target(1f)).push(Tween.to(iceBall1.getIceBody(), BodyAccessor.RADIUS, 2.5f).target(.01f)).setUserData(iceBall1.getIceBody()).setCallback(deleteCallback).start(tweenManager)).push(
					Timeline.createSequence().push(Tween.to(tempSprite, SpriteAccessor.SCALE_XY, .3f).target(2f)).push(Tween.to(tempSprite, SpriteAccessor.SCALE_XY, 2.5f).target(.01f))

							.start(tweenManager));
		/*
		 * Timeline.createParallel() .push(Tween.to(iceBall1.getIceBody(),
		 * BodyAccessor.RADIUS, .3f).target(.5f)) .push(Tween.to(tempSprite,
		 * SpriteAccessor.SCALE_XY, .3f).target(1f))
		 * 
		 * .start(tweenManager);
		 */
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
		// System.out.println(iceBall1.getIceBody().getPosition().y-penguin.getPenguinBody().getPosition().y
		// );
		if (penguin.getPenguinBody().getPosition().y < -1)
			game.setScreen(game.mainMenuScreen);

		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		// Gdx.gl.glClearColor(1, 1,1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//gy = Gdx.input.getAccelerometerY();
		//gx = Gdx.input.getAccelerometerX();
		//gravity = new Vector2(gy * 2, -5);
		//world.setGravity(gravity);

		world.step(delta, 6, 4);
		// set up camera for world
		Matrix4 cameraCopy = camera.combined.cpy();
		//debugRenderer.render(world, cameraCopy.scl(BW));
		try {
			tweenManager.update(Gdx.graphics.getDeltaTime());
		} catch (Exception e) {

		}
		position = camera.position;
		// if (penguin.getPenguinBody().getPosition().y < position.y)
		//position.y += ((BW * penguin.getPenguinBody().getPosition().y) - position.y) * lerp;
		position.x += ((BW * penguin.getPenguinBody().getPosition().x) - position.x) * lerp;
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
			// if (bDraw)
			// spriteTemp.draw(spriteBatch);
		}
		position = camera.position;
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
					// System.out.println("below -2");
					// deleteBodyList.add(worldBodie);
				}
				if (!(tempSprite.getName().equals("pen") || tempSprite.getName().equals("armLeft") || tempSprite.getName().equals("armRight")))
					updateDraw(worldBodie, spriteBatch);

			}
		}
		updateDraw(penguin.getPenguinBody(), spriteBatch);
		updateDraw(penguin.getLeftArmbody(), spriteBatch);
		updateDraw(penguin.getRightArmBody(), spriteBatch);
		for (int i = 0; i < particleEffectArray.length; i++) {
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
		spriteBatch.end();

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

	@Override
	public void show() {
		// setup input
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
		world = new World(new Vector2(0, -10), true);
		debugRenderer = new Box2DDebugRenderer();
		float centerX = Gdx.graphics.getWidth() / 2 * WB;
		float centerY = Gdx.graphics.getWidth() / 2 * WB;
		System.out.println(centerX + "," + centerY);
		float r = 50;
		penguin = new Penguin(world, 0, (screenHeight*WB), .2f, assets);
		penguin.createPenguin();
		penguin.getPenguinBody().setLinearDamping(.2f);
		Timeline.createParallel()
				.push(Tween.to(penguin.getPenguinBody(), BodyAccessor.POSITION_XY, 1.3f).target(centerX,centerY))
				.push(Tween.to(penguin.getRightArmBody(), BodyAccessor.POSITION_XY, 1.3f).target(centerX,centerY))
				.push(Tween.to(penguin.getLeftArmbody(), BodyAccessor.POSITION_XY, 1.3f).target(centerX,centerY))
				.start(tweenManager);

		activeList = new LinkedList<Body>();
		deleteBodyList = new LinkedList<Body>();
		//IceBall iceBall = new IceBall(world, (screenWidth / 2) * WB, screenHeight / 2 * WB, 1f, assets);
		//iceBall.createIceBall();
		createIceBall((screenWidth / 2) * WB, screenHeight / 2 * WB);
		int ballCount = 100;
		/*
		 * for (int i = 0; i < ballCount; i++) { Vector2 vc = new
		 * Vector2(penguin.getPenguinBody().getPosition());
		 * //System.out.println("vc.x"+vc.x); float x = (float)((screenWidth /
		 * 2+ r) * Math.cos(2 * Math.PI * i / ballCount)); float y =
		 * (float)((screenWidth / 2+ r) * Math.sin(2 * Math.PI * i /
		 * ballCount)); //System.out.println(x*WB+","+y*WB);
		 * createIceBall(x*WB,y*WB); }
		 */
		// for (int i = 0; i < 20; i++) {
		// createIceBall(i,(screenHeight / 2)*WB);
		// }
		particleEffectArray = new ParticleEffect[5];
		for (int i = 0; i < particleEffectArray.length; i++) {
			particleEffectArray[i] = new ParticleEffect();
			particleEffectArray[i].load(Gdx.files.internal("data/star"), Gdx.files.internal("data"));
		}
		spriteBatch = new SpriteBatch();
		// Setup background sprites
		bgSpriteArray = new Sprite[5];
		for (int i = 0; i < bgSpriteArray.length; i++) {
			SpriteInfo spriteTemp = new SpriteInfo(assets.getSprite("paper"));
			spriteTemp.setSize(screenWidth, screenHeight);
			spriteTemp.setPosition(screenWidth * i, 0);
			bgSpriteArray[i] = spriteTemp;
		}
		/*
		 * for (int i = 0; i < 10; i++) { Icicle icicle = new Icicle(world,
		 * (screenWidth / 2 * i) * WB, screenHeight * WB, .5f, assets);
		 * icicle.createIcicle(); Body bdy = icicle.getIceBody(); float rand =
		 * (float) (Math.random() * 10); float rand1 = (float) (Math.random() *
		 * 2) + 3;
		 * 
		 * Tween.to(bdy, BodyAccessor.DRIP, rand + 2).target(-1 *
		 * (rand1)).start(tweenManager); }
		 */

		createCollisionListener();

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
