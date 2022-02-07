package com.egyir;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.*;
import java.util.ArrayList;






public class Egyir extends Game {

	public class MyInput extends InputAdapter{



		//Called when a finger was lifted or a mouse button was released.
		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button){
			mouse_pos = new Coord(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

			System.out.println("Touch Up");


			final Vector2 pressedPosition = new Vector2();
			final Vector2 currentPosition = new Vector2();

			currentPosition.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

				isFired = true;

			if(button == 0){
				System.out.println("Button 1");
			}
			else if(button == 2){
				System.out.println("Button 2");
			}



			return false;


		}



	}


	//this function calculate the projectile motion using the projectile equation
	public class ProjectileEquation {

		public float gravity;  //gravity
		public Vector2 startVelocity = new Vector2();  //starting velocity
		public Vector2 startPoint = new Vector2(); //the x and y values of where it all starts

		public float getX(float t) {
			return startVelocity.x * t + startPoint.x;
		}

		public float getY(float t) {
			return 0.5f * gravity * t * t + startVelocity.y * t + startPoint.y;
		}

		public float getTForGivenX(float x) {

			return (x - startPoint.x) / (startVelocity.x);
		}
	}

	public static class Controller {

		public float power = 50f;
		public float angle = 0f;
		public boolean charging = false;

		public boolean fixedHorizontalDistance = true;

	}

	boolean mouseReleased;
	static boolean mouseRelease;






	public boolean mouseReleased(){
		mouseReleased = false;
		return mouseReleased;
	}



	public class ControllerLogic {

		boolean wasPressed;
		Controller controller;
		ProjectileEquation projectileEquation;


		final Vector2 pressedPosition = new Vector2();
		final Vector2 currentPosition = new Vector2();
		final Vector2 tmp = new Vector2();

		public ControllerLogic(Controller controller, Vector2 slingshotPosition) {
			this.controller = controller;
			wasPressed = false;
			this.pressedPosition.set(slingshotPosition);
		}

		public void update(float delta) {

			if (Gdx.input.isTouched()) {

				if (!wasPressed) {
					// pressedPosition.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
					wasPressed = true;
					controller.charging = true;
				}

				currentPosition.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

				tmp.set(currentPosition).sub(pressedPosition);
				//tmp.mul(-1f);

				controller.angle = tmp.angle();
				controller.power = tmp.len();


			} else {

				if (wasPressed) {
					// shoot
					controller.charging = false;
					wasPressed = false;
				}

			}

		}

	}



//
//	public static class TrajectoryActor extends Actor {
//
//		private Controller controller;
//		private ProjectileEquation projectileEquation;
//		private Sprite trajectorySprite;
//
//		public int trajectoryPointCount = 50;
//		public float timeSeparation = 1f;
//
//		public TrajectoryActor(Controller controller, float gravity, Sprite trajectorySprite) {
//			this.controller = controller;
//			this.trajectorySprite = trajectorySprite;
//			this.projectileEquation = new ProjectileEquation();
//			this.projectileEquation.gravity = gravity;
//		}
//
//		@Override
//		public void act(float delta) {
//			super.act(delta);
//
//			if (!controller.charging)
//				return;
//
//			System.out.println("controller.power: "+ controller.power);
//			System.out.println("controller.angle: "+ controller.angle);
//			projectileEquation.startVelocity.set(controller.power, 0f);
//			System.out.println("Set Velocity: " + projectileEquation.startVelocity);
//			projectileEquation.startVelocity.rotate(controller.angle);
//			System.out.println("Set Rotate: " + projectileEquation.startVelocity);
//		}
//
//		@Override
//		public void draw(Batch batch, float parentAlpha) {
//			if (!controller.charging)
//				return;
//
//			float t = 0f;
//			float a = 1f;
//			float adiff = a / trajectoryPointCount;
//			float width = this.getWidth();
//			float height = this.getHeight();
//			float widthDiff = width / trajectoryPointCount;
//			float heightDiff = height / trajectoryPointCount;
//
//			float timeSeparation = this.timeSeparation;
//
//			System.out.println("StartPoint: " + projectileEquation.startPoint);
//			System.out.println("StartVelocity: " + projectileEquation.startVelocity);
//			System.out.println("Fixed Horizontal Distance: " + controller.fixedHorizontalDistance);
//			if (controller.fixedHorizontalDistance)
//				timeSeparation = projectileEquation.getTForGivenX(15f);
//				System.out.println("TimeSeperation: " + timeSeparation);
//
//			for (int i = 0; i < trajectoryPointCount; i++) {
//				float x = this.getX() + projectileEquation.getX(t);
//				float y = this.getY() + projectileEquation.getY(t);
//
//				System.out.println("Position Point: ("+ x +" , " + y + ")" );
////
////				System.out.println("this.getX(): " + this.getX());
////				System.out.println("this.getY(): " + this.getY());
////				System.out.println("getX(): " +  x);
////				System.out.println("getY(): " +  y);
////				System.out.println("t: " +  t);
////				System.out.println("timeSeparation: " +  timeSeparation);
////				System.out.println("projectileEquation.getX(t): " +  projectileEquation.getX(t));
////				System.out.println("projectileEquation.getY(t): " +  projectileEquation.getY(t));
//
//
//
//				this.getColor().a = a;
//
//				batch.setColor(this.getColor());
//				batch.draw(trajectorySprite, x, y, width, height);
//
//				a -= adiff;
//				t += timeSeparation;
//
//				//width -= widthDiff;
//				//height -= heightDiff;
//			}
//		}
//
//		@Override
//		public Actor hit(float x, float y, boolean touchable) {
//			return null;
//		}
//
//	}


	static class Coord{
		float x,y;

		static Coord polar (double r, double theta)
		{
			return new Coord((float)Math.cos(theta)*r, (float)Math.sin(theta)*r);
		}

		Coord (double x, double y)
		{
			this.x = (float)x; this.y = (float)y;
		}

		Coord rotated (double angle)
		{
			// Return a rotated vector by making a new one with the same radius
			// and adding the angles.
			return Coord.polar(radius(), angle + theta());
		}

		float theta_deg ()
		{
			return (float)(theta() / (Math.PI*2) * 360);
		}

		Coord theta_deg (double t)
		{
			return theta( (t / 360) * Math.PI*2 );
		}

		float theta ()
		{
			return (float)Math.atan2(y, x);
		}

		Coord theta (double t)
		{
			return polar(radius(), t);
		}

		float radius ()
		{
			return (float)Math.sqrt(x*x+y*y);
		}

		Coord radius (double r)
		{
			return polar(r, theta());
		}

		Coord plus (Coord o)
		{
			return new Coord(x+o.x, y+o.y);
		}

		Coord minus (Coord o)
		{
			return new Coord(x-o.x, y-o.y);
		}

		Coord times (Coord o)
		{
			return new Coord(x*o.x, y*o.y);
		}

		Coord times (double d)
		{
			return times(new Coord(d,d));
		}

		Coord position (TextureRegion s)
		{
			//s.setOriginBasedPosition(x, y);
			s.setRegionX((int)x);
			s.setRegionY((int)y);
			return this;
		}

		Coord rotation (TextureRegion s)
		{
			//s.setRotation(theta_deg());
			return this;
		}

		public String toString ()
		{
			return "("+x+","+y+")";
		}
	}






	class Bird extends TextureRegion{


		Coord position = new Coord(0,0);
		Coord velocity = new Coord(8,8);
		Coord acceleration = new Coord(0, -0.1f);

		boolean tick ()
		{
			position = position.plus(velocity);
			velocity = velocity.plus(acceleration);

			return true;
		}

	}

//
//	public static class BirdActor extends Actor{
//		private Controller controller;
//		private ProjectileEquation projectileEquation;
//		private TextureRegion birdActorRegion;
//
//		public int birdPointCount = 50;
//		public float timeSeperation = 1f;
//
//		public BirdActor(Controller controller, float gravity, TextureRegion birdActorRegion){
//			this.controller = controller;
//			this.birdActorRegion = birdActorRegion;
//			this.projectileEquation = new ProjectileEquation();
//			this.projectileEquation.gravity = gravity;
//		}
//
//
//		@Override
//		public void act(float delta){
//			super.act(delta);
//
//			if(!controller.charging)
//				return;
////			System.out.print("controller.power: "+ controller.power);
////			System.out.print("controller.angle: "+ controller.angle);
//			projectileEquation.startVelocity.set(controller.power, 0f);
//			projectileEquation.startVelocity.rotate(controller.angle);
//		}
//
//
//		@Override
//		public void draw(Batch batch, float parentAlpha){
//
//			if(!controller.charging)
//				return;
//
//			float t = 0f;
//			float a = 1f;
//			float adiff = a/birdPointCount;
//			float width = birdActorRegion.getRegionWidth()/20;
//			float height = birdActorRegion.getRegionHeight()/20;;
//			float widthDiff = width/birdPointCount;
//			float heightDiff = height/birdPointCount;
//
//			float timeSeperation = projectileEquation.getTForGivenX(15f);
//
//
//			for(int i = 0; i < birdPointCount; i++){
//
//				float x = this.getX() + projectileEquation.getX(t);
//				float y = this.getY() + projectileEquation.getY(t);
//
//
//				//this.getColor().a = a;
//
//				batch.draw(birdActorRegion, x, y, width, height);
//
//				a -= adiff;
//				t += timeSeperation;
//
//				//width -= widthDiff;
//				//height -= heightDiff;
//
//
//
//
//			}
//		}
//
//		@Override
//		public Actor hit(float x, float y, boolean touchable) {
//			return null;
//		}
//	}







	boolean isFired;

	Coord screen_size;
	Coord mouse_pos = new Coord(0,0);

	Coord arrow_pos = new Coord(35,35);
	Coord arrow_desired_pos = arrow_pos;
	Coord arrow_vel = new Coord(0,0);

	Coord moon_pos = new Coord(400, 300);
	Coord moon_vel = new Coord(0,0);
	
	public SpriteBatch batch;

	//GAME BACKGROUND IMAGE
	Sprite backgroundImage;
	Sprite birdImage;

	//ANGRY BIRDS
	Animation<TextureRegion> birdAnimation;
	Animation<TextureRegion> birdAnimationLeft;
	Animation<TextureRegion> birdAnimationDown;
	Animation<TextureRegion> birdAnimationUp;

	Texture birdFrameOne;
	Texture birdFrameTwo;
	Texture birdFrameThree;
	Texture birdFrameFour;
	Texture birdFrameFive;
	Texture birdFrameSix;
	Texture birdFrameSeven;
	Texture birdFrameEight;

	Texture birdFrameOneLeft;
	Texture birdFrameTwoLeft;
	Texture birdFrameThreeLeft;
	Texture birdFrameFourLeft;
	Texture birdFrameFiveLeft;
	Texture birdFrameSixLeft;
	Texture birdFrameSevenLeft;
	Texture birdFrameEightLeft;

	Texture birdFrameOneUp;
	Texture birdFrameTwoUp;
	Texture birdFrameThreeUp;
	Texture birdFrameFourUp;
	Texture birdFrameFiveUp;
	Texture birdFrameSixUp;
	Texture birdFrameSevenUp;
	Texture birdFrameEightUp;

	Texture birdFrameOneDown;
	Texture birdFrameTwoDown;
	Texture birdFrameThreeDown;
	Texture birdFrameFourDown;
	Texture birdFrameFiveDown;
	Texture birdFrameSixDown;
	Texture birdFrameSevenDown;
	Texture birdFrameEightDown;

	TextureRegion currentBirdFrame;
	TextureRegion currentBirdFrameLeft;
	TextureRegion currentBirdFrameUp;
	TextureRegion currentBirdFrameDown;



	int numOfBirdsFrames = 8;
	int birdXPosition = 90;
	int birdYPosition= 195;
	Bird b = new Bird();
	Vector2 birdStartPosition;

	Coord birdPosition = new Coord(birdXPosition, birdYPosition);
	Coord birdVelocity = new Coord(0, 0);
	//int birdHeight;
	//int birdWidth;

	Vector2 birdInitialStartPosition;




	//MONSTER BIRDS
	Animation<TextureRegion> monsterBirdAnimation;
	Texture monsterBirdFrameOne;
	Texture monsterBirdFrameTwo;
	int numoOfMonsterFrames = 2;
	TextureRegion currentMonsterFrame;
	int monsterXPosition = 500;
	int monsterYPosition= 235;
	int constantMonsterHeight = 100;
	Coord monsterPositionOne = new Coord(monsterXPosition, constantMonsterHeight);
	Coord monsterPositionTwo = new Coord(monsterXPosition, constantMonsterHeight + monsterYPosition/5);
	Coord monsterPositionThree = new Coord(monsterXPosition, constantMonsterHeight + (2 * monsterYPosition/5));




	//CRATE
	Animation<TextureRegion> crateAnimation;
	Texture crateFrameOne;
	Texture crateFrameTwo;
	int numOfCratesFrames = 2;
	TextureRegion currentCrateFrame;
	int constantCrateWidth = 580;
	int crateXPosition = 256;
	int crateYPosition = 256;
	int constantCrateHeight = 100;

	Coord cratePositionOne = new Coord((constantMonsterHeight  - crateXPosition/15)-crateXPosition/3 , constantCrateHeight);
	Coord cratePositionTwo = new Coord((constantMonsterHeight  - crateXPosition/15)-crateXPosition/3, constantCrateHeight + monsterYPosition/5);
	Coord cratePositionThree = new Coord((constantMonsterHeight  - crateXPosition/15)-crateXPosition/3, constantCrateHeight + (2 * monsterYPosition/5));




	//A VARIABLE FOR TRACKING ELAPSED TIME FOR THE ANIMATION
	float stateTime;

	//GAME SCREEN
	int screenWidth;
	int screenHeight;
	int screenX;
	int screenY;
	float t = 0f;
	float xValue;
	float timeSeparation;


	public Stage stage;
	public BitmapFont bitmapFont;
	public Texture targetTexture;
	public Texture backgroundTexture;
	public Sprite backgroundSprite;
	public Texture groundTexture;
	public Sprite groundSprite;
	public Sprite slingshotSprite;
	public Texture slingshotTexture;

	//TrajectoryActor trajectoryActor;
	float ValueY, ValueX;


	public float gravity3;
	public Vector2 startVelocity = new Vector2();
	public Vector2 startPoint = new Vector2();

	float gravity1;
	Vector2 gravity;
//	Vector2 startVelocity;   // = (50f, 0);
//	Vector2 startPoint ;  //= (90, 195);
	float projectileX, projectileY, forGivenProjectileX;

	//float gravity;
	private float throwAngle=50;
	private float deltaTime=0.000001f;
	private Vector2 initialVelocity;


	private ShapeRenderer shapeRenderer;

	private Controller controller;
	private ControllerLogic controllerLogic;
	private ProjectileEquation projectileEquation;

	ArrayList<TextureRegion> angrybirds = new ArrayList<TextureRegion>();





	public float getX(float t) {
		return startVelocity.x * t + startPoint.x;
	}

	public float getY(float t) {
		return 0.5f * gravity3 * t * t + startVelocity.y * t + startPoint.y;
	}

	public float getTForGivenX(float x) {
		// x = startVelocity.x * t + startPoint.x
		// x - startPoint.x = startVelocity.x * t
		// t = (x - startPoint.x) / (startVelocity.x);
		return (x - startPoint.x) / (startVelocity.x);
	}



	@Override
	public void create () {


		//listen to the input either the mouse or keyboard will input.
		Gdx.input.setInputProcessor(new MyInput());


		//GAME SCREEN
		screenWidth = Gdx.graphics.getWidth(); //width
		screenHeight = Gdx.graphics.getHeight(); //height
		screenX = Gdx.input.getX();
		screenY = Gdx.input.getY();

		//SPRITE BATCH FOR DRAWING AND RESETING SPRITE. IN THE CASE OF ANIMATION
		//IT WILL BE REPONSIBLE FOR RESETTING AND THE ELAPSED TIME TO O
		batch = new SpriteBatch();



		shapeRenderer = new ShapeRenderer();
		bitmapFont = new BitmapFont();


		gravity1 = -10f;


		targetTexture = new Texture(Gdx.files.internal("white-circle.png"));
		targetTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);


		{
			slingshotTexture = new Texture(Gdx.files.internal("slingshot.png"));
			slingshotTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

			slingshotSprite = new Sprite(slingshotTexture);
			slingshotSprite.setPosition(128 - slingshotSprite.getWidth() * 0.5f, 50);
		}









		//GAME BACKGROUND
		int backgroundXOrigin = screenWidth - screenWidth; //0
		int backgroundYOrigin = screenHeight - screenHeight; //0
		backgroundImage = new Sprite(new Texture(Gdx.files.internal("Full-Background.png")));
		backgroundImage.setOrigin(backgroundXOrigin, backgroundYOrigin);
		backgroundImage.setScale(0.35f); //scaling the background



		//ANGRY BIRDS

		//Get each frame and save it as a texture
		birdFrameOne = new Texture(Gdx.files.internal("frame-1.png"));
		birdFrameTwo = new Texture(Gdx.files.internal("frame-2.png"));
		birdFrameThree = new Texture(Gdx.files.internal("frame-3.png"));
		birdFrameFour = new Texture(Gdx.files.internal("frame-4.png"));
		birdFrameFive = new Texture(Gdx.files.internal("frame-5.png"));
		birdFrameSix = new Texture(Gdx.files.internal("frame-6.png"));
		birdFrameSeven = new Texture(Gdx.files.internal("frame-7.png"));
		birdFrameEight = new Texture(Gdx.files.internal("frame-8.png"));

		birdFrameOneLeft = new Texture(Gdx.files.internal("AngryBirdsLeft/frame-1.png"));
		birdFrameTwoLeft = new Texture(Gdx.files.internal("AngryBirdsLeft/frame-2.png"));
		birdFrameThreeLeft = new Texture(Gdx.files.internal("AngryBirdsLeft/frame-3.png"));
		birdFrameFourLeft = new Texture(Gdx.files.internal("AngryBirdsLeft/frame-4.png"));
		birdFrameFiveLeft = new Texture(Gdx.files.internal("AngryBirdsLeft/frame-5.png"));
		birdFrameSixLeft = new Texture(Gdx.files.internal("AngryBirdsLeft/frame-6.png"));
		birdFrameSevenLeft = new Texture(Gdx.files.internal("AngryBirdsLeft/frame-7.png"));
		birdFrameEightLeft = new Texture(Gdx.files.internal("AngryBirdsLeft/frame-8.png"));

		birdFrameOneUp = new Texture(Gdx.files.internal("AngryBirdsUp/frame-1.png"));
		birdFrameTwoUp = new Texture(Gdx.files.internal("AngryBirdsUp/frame-2.png"));
		birdFrameThreeUp = new Texture(Gdx.files.internal("AngryBirdsUp/frame-3.png"));
		birdFrameFourUp = new Texture(Gdx.files.internal("AngryBirdsUp/frame-4.png"));
		birdFrameFiveUp = new Texture(Gdx.files.internal("AngryBirdsUp/frame-5.png"));
		birdFrameSixUp = new Texture(Gdx.files.internal("AngryBirdsUp/frame-6.png"));
		birdFrameSevenUp = new Texture(Gdx.files.internal("AngryBirdsUp/frame-7.png"));
		birdFrameEightUp = new Texture(Gdx.files.internal("AngryBirdsUp/frame-8.png"));

		birdFrameOneDown = new Texture(Gdx.files.internal("AngryBirdsDown/frame-1.png"));
		birdFrameTwoDown = new Texture(Gdx.files.internal("AngryBirdsDown/frame-2.png"));
		birdFrameThreeDown = new Texture(Gdx.files.internal("AngryBirdsDown/frame-3.png"));
		birdFrameFourDown = new Texture(Gdx.files.internal("AngryBirdsDown/frame-4.png"));
		birdFrameFiveDown = new Texture(Gdx.files.internal("AngryBirdsDown/frame-5.png"));
		birdFrameSixDown = new Texture(Gdx.files.internal("AngryBirdsDown/frame-6.png"));
		birdFrameSevenDown = new Texture(Gdx.files.internal("AngryBirdsDown/frame-7.png"));
		birdFrameEightDown = new Texture(Gdx.files.internal("AngryBirdsDown/frame-8.png"));


		//Create a Texture Region
		TextureRegion [] birdFrames = new TextureRegion[numOfBirdsFrames];
		TextureRegion [] birdFramesLeft = new TextureRegion[numOfBirdsFrames];
		TextureRegion [] birdFramesUp = new TextureRegion[numOfBirdsFrames];
		TextureRegion [] birdFramesDown = new TextureRegion[numOfBirdsFrames];

		//Save the texture bird Frame in the Texture Region
		birdFrames[0] = new TextureRegion(birdFrameOne);
		birdFrames[1] = new TextureRegion(birdFrameTwo);
		birdFrames[2] = new TextureRegion(birdFrameThree);
		birdFrames[3] = new TextureRegion(birdFrameFour);
		birdFrames[4] = new TextureRegion(birdFrameFive);
		birdFrames[5] = new TextureRegion(birdFrameSix);
		birdFrames[6] = new TextureRegion(birdFrameSeven);
		birdFrames[7] = new TextureRegion(birdFrameEight);


		birdFramesLeft[0] = new TextureRegion(birdFrameOneLeft);
		birdFramesLeft[1] = new TextureRegion(birdFrameTwoLeft);
		birdFramesLeft[2] = new TextureRegion(birdFrameThreeLeft);
		birdFramesLeft[3] = new TextureRegion(birdFrameFourLeft);
		birdFramesLeft[4] = new TextureRegion(birdFrameFiveLeft);
		birdFramesLeft[5] = new TextureRegion(birdFrameSixLeft);
		birdFramesLeft[6] = new TextureRegion(birdFrameSevenLeft);
		birdFramesLeft[7] = new TextureRegion(birdFrameEightLeft);


		birdFramesUp[0] = new TextureRegion(birdFrameOneUp);
		birdFramesUp[1] = new TextureRegion(birdFrameTwoUp);
		birdFramesUp[2] = new TextureRegion(birdFrameThreeUp);
		birdFramesUp[3] = new TextureRegion(birdFrameFourUp);
		birdFramesUp[4] = new TextureRegion(birdFrameFiveUp);
		birdFramesUp[5] = new TextureRegion(birdFrameSixUp);
		birdFramesUp[6] = new TextureRegion(birdFrameSevenUp);
		birdFramesUp[7] = new TextureRegion(birdFrameEightUp);


		birdFramesDown[0] = new TextureRegion(birdFrameOneDown);
		birdFramesDown[1] = new TextureRegion(birdFrameTwoDown);
		birdFramesDown[2] = new TextureRegion(birdFrameThreeDown);
		birdFramesDown[3] = new TextureRegion(birdFrameFourDown);
		birdFramesDown[4] = new TextureRegion(birdFrameFiveDown);
		birdFramesDown[5] = new TextureRegion(birdFrameSixDown);
		birdFramesDown[6] = new TextureRegion(birdFrameSevenDown);
		birdFramesDown[7] = new TextureRegion(birdFrameEightDown);





		//Initialize the Animation with the frame interval and arrays of frames
		birdAnimation = new Animation<TextureRegion>(0.050f, birdFrames);
		birdAnimationLeft = new Animation<TextureRegion>(0.050f, birdFramesLeft);
		birdAnimationUp = new Animation<TextureRegion>(0.050f, birdFramesUp);
		birdAnimationDown = new Animation<TextureRegion>(0.050f, birdFramesDown);





		//GAME ENEMY BIRDS
		//Get the monster image and save it as a texture
		monsterBirdFrameOne = new Texture(Gdx.files.internal("monster-1.png"));
		monsterBirdFrameTwo = new Texture(Gdx.files.internal("monster-2.png"));


		//Create A Texture Region
		TextureRegion [] monsterFrames = new TextureRegion[numoOfMonsterFrames];

		//add the texture to the Texture Region
		monsterFrames[0] = new TextureRegion(monsterBirdFrameOne);
		monsterFrames[1] = new TextureRegion(monsterBirdFrameTwo);

		//Intialize the Animation with the frame interval and arrays of frames
		monsterBirdAnimation = new Animation<TextureRegion>(0.090f, monsterFrames);




		//BOX
		crateFrameOne = new Texture(Gdx.files.internal("crates.png"));
		crateFrameTwo = new Texture(Gdx.files.internal("crates.png"));

		TextureRegion [] crateFrames = new TextureRegion[numOfCratesFrames];


		crateFrames[0] = new TextureRegion(crateFrameOne);
		crateFrames[1] = new TextureRegion(crateFrameTwo);


		crateAnimation = new Animation<TextureRegion>(0.90f, crateFrames);



		//Gdx.input.setInputProcessor(this);
		//gravity=(0, -(Gdx.graphics.getHeight()*.05f));
		//-Gdx.graphics.getHeight()*.05f
		gravity=new Vector2(0, -9.8f);
		float throwVelocity=Gdx.graphics.getWidth()*.3f;
		System.out.println("THROW VELOCITY: " + throwVelocity);
//		initialVelocity=new Vector2((float)(throwVelocity*Math.sin(throwAngle * Math.PI / 180) - 110),(float)(throwVelocity*Math.cos(throwAngle * Math.PI / 180) - 110));
		float xx = (float)(throwVelocity*Math.sin(throwAngle * Math.PI / 180) - 110);
		float yy = (float)(throwVelocity*Math.cos(throwAngle * Math.PI / 180) - 110);
		//projectileTwo.startVelocity =
		initialVelocity = new Vector2(xx,yy);
		//startVelocity = initialVelocity;
		System.out.println("INITIAL VELOCITY: " + initialVelocity);
		//initialVelocity=new Vector2((float)(throwVelocity*Math.sin(30)),(float)(throwVelocity*Math.cos(30)));
		//initialVelocity = new Vector2(2f,1f);


		//-63.999996,13.000018
		//System.out.print("controller.power: "+ controller.power);
		//System.out.print("controller.angle: "+ controller.angle);
		//startVelocity = new Vector2(50f, 0f);
		//startVelocity.rotate(9);
		System.out.print("rotatew: "+ startVelocity);
		birdStartPosition = new Vector2(50, 120);



//		projectileEquation.startVelocity = new Vector2(50f, 0f);
//		projectileEquation.startVelocity.rotate(0);

		//time
		stateTime = 0f;


		//Sprite trajectorySprite = new Sprite(targetTexture);

		controller = new Controller();
		controllerLogic = new ControllerLogic(controller, new Vector2(128f, 50 + birdXPosition * 0.7f));

//		TrajectoryActor trajectoryActor = new TrajectoryActor(controller, gravity1, trajectorySprite);
//		BirdActor birdActor = new BirdActor(controller, gravity1, currentBirdFrame);
//
//		trajectoryActor.setX(128f);
//		birdActor.setX(90);
//		birdActor.setY(195);
//		birdActor.setWidth(10f);
//		birdActor.setHeight(10f);
//		trajectoryActor.setY(50 + slingshotSprite.getHeight() * 0.7f);
//		trajectoryActor.setWidth(10f);
//		trajectoryActor.setHeight(10f);
//
//		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
//		stage.addActor(trajectoryActor);
//		//stage.addActor(birdActor);


	}



	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);


		//Accumulate Elapsed Animation
		stateTime += Gdx.graphics.getDeltaTime();
		final Vector2 pressedPosition = new Vector2();
		final Vector2 currentPosition = new Vector2();
		final Vector2 tmp = new Vector2();




		//Get current bird frame of animation for the current stateTime
		currentBirdFrame = birdAnimation.getKeyFrame(stateTime, true);
		currentBirdFrameLeft = birdAnimationLeft.getKeyFrame(stateTime, true);
		currentBirdFrameUp = birdAnimationUp.getKeyFrame(stateTime, true);
		currentBirdFrameDown = birdAnimationDown.getKeyFrame(stateTime, true);

		//Get current monster frame of animaton for the current stateTime
		currentMonsterFrame = monsterBirdAnimation.getKeyFrame(stateTime, true);


		//Get current frame of animation for the current state Time
		currentCrateFrame = crateAnimation.getKeyFrame(stateTime, true);

		batch.begin();

		float delta = Gdx.graphics.getDeltaTime();

		controllerLogic.update(delta);
		

		float x = Gdx.graphics.getWidth() * 0.2f;
		float y = Gdx.graphics.getHeight() * 0.9f;



		//render background image
		backgroundImage.draw(batch);

		batch.draw(currentMonsterFrame, 550, 70, currentBirdFrame.getRegionWidth()/25, currentMonsterFrame.getRegionHeight()/8);
		batch.draw(currentMonsterFrame, 550, 70 + currentMonsterFrame.getRegionHeight()/8,currentBirdFrame.getRegionWidth()/25, currentMonsterFrame.getRegionHeight()/8);
		batch.draw(currentMonsterFrame, 550, 70 + (2 * currentMonsterFrame.getRegionHeight()/8),currentBirdFrame.getRegionWidth()/25, currentMonsterFrame.getRegionHeight()/8);
		batch.draw(currentCrateFrame, (580 - currentBirdFrame.getRegionWidth()/15)-currentCrateFrame.getRegionWidth()/3 , 70, currentCrateFrame.getRegionWidth()/5, currentCrateFrame.getRegionHeight()/5);
		batch.draw(currentCrateFrame, (580 - currentBirdFrame.getRegionWidth()/15)-currentCrateFrame.getRegionWidth()/3 , (70 + currentCrateFrame.getRegionWidth()/5), currentCrateFrame.getRegionWidth()/5, currentCrateFrame.getRegionHeight()/5);
		batch.draw(currentCrateFrame, (580 - currentBirdFrame.getRegionWidth()/15)-currentCrateFrame.getRegionWidth()/3 , (70 + 2*currentCrateFrame.getRegionWidth()/5), currentCrateFrame.getRegionWidth()/5, currentCrateFrame.getRegionHeight()/5);
		batch.draw(currentCrateFrame, (580 - currentBirdFrame.getRegionWidth()/15)-currentCrateFrame.getRegionWidth()/3 , (70 + 3*currentCrateFrame.getRegionWidth()/5), currentCrateFrame.getRegionWidth()/5, currentCrateFrame.getRegionHeight()/5);
		batch.draw(currentCrateFrame, (580 - currentBirdFrame.getRegionWidth()/15)-currentCrateFrame.getRegionWidth()/3 , (70 + 4*currentCrateFrame.getRegionWidth()/5), currentCrateFrame.getRegionWidth()/5, currentCrateFrame.getRegionHeight()/5);
		batch.draw(currentCrateFrame, (580 - currentBirdFrame.getRegionWidth()/15)-currentCrateFrame.getRegionWidth()/3 , (70 + 5*currentCrateFrame.getRegionWidth()/5), currentCrateFrame.getRegionWidth()/5, currentCrateFrame.getRegionHeight()/5);
		batch.draw(currentCrateFrame, (580 - currentBirdFrame.getRegionWidth()/15)-currentCrateFrame.getRegionWidth()/3 , (70 + 6*currentCrateFrame.getRegionWidth()/5), currentCrateFrame.getRegionWidth()/5, currentCrateFrame.getRegionHeight()/5);
		batch.end();


		float x1, y1, birdX = 0, birdY = 0;
		float timeSeparation = 1f;
		float xVaule = 15f;
		Vector2 startPoint4 = new Vector2();
		Vector2 startPointXX = new Vector2();
		Vector2 startPointYY = new Vector2();

		float velocity = 0.003f;





		if (controller.charging) {

			pressedPosition.set(128f, 50 + slingshotSprite.getHeight() * 0.7f);
			//pressedPosition.set(birdStartPosition);
			currentPosition.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

			tmp.set(currentPosition).sub(pressedPosition);
			//tmp.mul(-1f);

			ValueX = tmp.angle();
			System.out.println("ValueXANgle: " + ValueX);
            ValueY = tmp.len();
			System.out.println("ValueYPower: " + ValueY);
			startPointXX.set(ValueY,0f);
			startPointYY.rotate(ValueX);

			batch.begin();
			//b.position.position(currentBirdFrame);
			batch.draw(currentBirdFrame, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(),currentBirdFrame.getRegionWidth()/30, currentBirdFrame.getRegionHeight()/30);
			birdStartPosition.x = Gdx.input.getX();
			birdStartPosition.y = Gdx.graphics.getHeight() - Gdx.input.getY();
			birdInitialStartPosition = birdStartPosition;


			if (controller.fixedHorizontalDistance) {
				timeSeparation = (15f - Gdx.input.getX()) / (startVelocity.x);
			}


			batch.end();


		} else {



			if(isFired){


//

				if(controller.fixedHorizontalDistance)
					velocity = getTForGivenX(0.004f);








//
//
//
//				if(birdStartPosition.x > screenWidth ){
//					System.out.println("Out Of BoundsX1");
//
//
//
//					startVelocity.set(ValueY, 0f);
//
//					startVelocity.rotate(ValueX);
//
//
//					System.out.println("Start velocity power reflect:  "+ startVelocity);
//
//					System.out.println("Start velocity rotae reflect: "+ startVelocity);
//
//
//					birdX = -getX(t); //initialVelocity.x * t;
//					birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);
//
////					Vector2 reflection = new Vector2(screenX - birdStartPosition.x, screenY - birdStartPosition.y);
////
////					//Start by flipping around the angle the bird bit at
////					//so that it's pointing away from the edges.
////
////					float hey = (float) Math.atan2(reflection.y,reflection.x);
////
////
////					float xxY = screenX - reflection.x;
////					float yyX = screenY - reflection.y;
////
////					birdStartPosition = new Vector2 (xxY, yyX);
////
////					Vector2 bounceVector = startVelocity.rotate((float)Math.PI);
////
////					float angle = ((float)Math.atan2(reflection.y, reflection.x)) - ((float)Math.atan2(bounceVector.x, bounceVector.y));
////
//////					reflection.x = screenX - birdPosition.x;
//////					reflection.y = screenY - birdPosition.y;
////
////					startVelocity.rotate(angle);
////
////
//////					((float)Math.cos(theta)*r, (float)Math.sin(theta)*r)
////////
////////					Coord minus (Coord o)
////////					{
////////						return new Coord(x-o.x, y-o.y);
////////					}
//////
//////					float theta ()
//////					{
//////						return (float)Math.atan2(y, x);
//////					}
////
//////
//////					Coord reflection = moon_pos.minus(cat.pos);
//////					// Start by flipping around the angle the cat hit at, so
//////					// that it's pointing *away* from the moon.
//////
//////
//////					Coord radius (double r)
//////					{
//////						return polar(r, theta());
//////					}
//////
//////					float theta ()
//////					{
//////						return (float)Math.atan2(y, x);
//////					}
//////
//////					static Coord polar (double r, double theta)
//////					{
//////						return new Coord((float)Math.cos(theta)*r, (float)Math.sin(theta)*r);
//////					}
//////
//////
//////
//////
//////					Coord rotated (double angle)
//////					{
//////						// Return a rotated vector by making a new one with the same radius
//////						// and adding the angles.
//////						return Coord.polar(radius(), angle + theta());
//////					}
//////					Coord bounce_vector = cat.vel.rotated(Math.PI);
//////
//////					// Find the difference between the angles.  That is, the cat
//////					// was coming from this angle relative to the reflection angle.
//////					float angle = reflection.theta() - bounce_vector.theta();
//////
//////					// Rotate the reflection the same amount in the opposite
//////					// direction, and that's the bounce angle!  Also set the
//////					// magnitude to be 80% of the cat's original velocity magnitude.
//////					cat.vel = reflection.rotated(angle).radius(cat.vel.radius()*0.8);
//
//				}
//
//				if(birdStartPosition.x < 0 ){
//					System.out.println("Out Of BoundsX2");
//
//					startVelocity.set(ValueY, 0f);
//
//					startVelocity.rotate(ValueX);
//
//
//					System.out.println("Start velocity power reflect:  "+ startVelocity);
//
//					System.out.println("Start velocity rotae reflect: "+ startVelocity);
//
//
//					birdX = getX(t); //initialVelocity.x * t;
//					birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);
//
//				}
//				//System.out.println("startPoint.y: "+ startPoint.y);
//				if(birdStartPosition.y < 65){
//					System.out.println("You Need To Bounce");
//
//					startVelocity.set(ValueY, 0f);
//
//					startVelocity.rotate(ValueX);
//
//
//					System.out.println("Start velocity power reflect:  "+ startVelocity);
//
//					System.out.println("Start velocity rotae reflect: "+ startVelocity);
//
//
//					birdX = -getX(t); //initialVelocity.x * t;
//					birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);
//
//				}
//				if(birdStartPosition.y > screenHeight){
//					//System.out.println("Out Of BoundsY");
//
//					startVelocity.set(ValueY, 0f);
//
//					startVelocity.rotate(ValueX);
//
//
//					System.out.println("Start velocity power reflect:  "+ startVelocity);
//
//					System.out.println("Start velocity rotae reflect: "+ startVelocity);
//
//
//					birdX = -getX(t); //initialVelocity.x * t;
//					birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);
//
//				}
//				if(birdStartPosition.y < 0){
//					//System.out.println("Over Of BoundsY");
//				}
//
//
//				else{
//
//					startVelocity.set(-ValueY, 0f);
//					System.out.println("Start velocity power: "+ startVelocity);
//					startVelocity.rotate(ValueX);
//					System.out.println("Start velocity rotate: "+ startVelocity);
//
//					//wrong one
////					startVelocity.set(ValueY, 0f);
////					System.out.println("Start velocity power: "+ startVelocity);
////					startVelocity.rotate(ValueX);
////					System.out.println("Start velocity rotate: "+ startVelocity);
//
//
//					//updateBall();
//
//					//initialVelocity.x = initialVelocity.x;
//					//initialVelocity.y = initialVelocity.y - (9.8f * t);
//
//					birdX = getX(t); //initialVelocity.x * t;
//					birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);
//
//				}
//
//
//
//				delta = Gdx.graphics.getDeltaTime();
////				birdX =  initialVelocity.x * t;
////				birdY =  (initialVelocity.y * t) - (0.5f * gravity.y* t * t);
//
//
//
//
//
//

//				// update |
//				public void reflectVertical(){
//					if(mAngle > 0 && mAngle < PI){
//						mAngle = PI - mAngle;
//					} else {
//						mAngle = 3 * PI - mAngle;
//					}
//				}
//
//				// update -
//				public void reflectHorizontal(){
//					mAngle = 2 * PI - mAngle;
//				}

			}




			System.out.println("Outside OF BOUNDS FOR X");

			startVelocity.set(-ValueY, 0f);
			System.out.println("Start velocity power: "+ startVelocity);
			startVelocity.rotate(ValueX);
			System.out.println("Start velocity rotate: "+ startVelocity);


			birdX = getX(t); //initialVelocity.x * t;
			birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);







			if(birdStartPosition.x >= screenWidth - currentBirdFrame.getRegionWidth()/25){




//				birdX = getX(t); //initialVelocity.x * t;
//				birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);
				birdX = - birdX;



				batch.begin();



				batch.draw(currentBirdFrameLeft, birdStartPosition.x, birdStartPosition.y, currentBirdFrame.getRegionWidth()/30, currentBirdFrame.getRegionHeight()/30);

				batch.end();

				birdStartPosition.x += birdX;
				birdStartPosition.y += birdY;


				t += 0.00003; //velocity;





			}

			if(birdStartPosition.x <= 5){


//				birdX = getX(t); //initialVelocity.x * t;
//				birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);

				birdX = - birdX;


				batch.begin();

				batch.draw(currentBirdFrame, birdStartPosition.x, birdStartPosition.y, currentBirdFrame.getRegionWidth()/30, currentBirdFrame.getRegionHeight()/30);



				batch.end();

				birdStartPosition.x += birdX;
				birdStartPosition.y += birdY;


				t += 0.00003; //velocity;


			}
			if(birdStartPosition.y >= screenHeight - currentBirdFrame.getRegionHeight()/25){
				System.out.println("OUTSIDE OF BOUNDS FOR Y");

//				birdX = getX(t); //initialVelocity.x * t;
//				birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);

				birdX = - birdX;
				birdY = - birdY;


				batch.begin();

				batch.draw(currentBirdFrame, birdStartPosition.x, birdStartPosition.y, currentBirdFrame.getRegionWidth()/30, currentBirdFrame.getRegionHeight()/30);

				batch.end();

				birdStartPosition.x += birdX;
				birdStartPosition.y += birdY;


				t += 0.00003; //velocity;

			}
			if(birdStartPosition.y <= 75){

				System.out.println("OUTSIDE OF BOUNDS FOR Y LEFT");
				//birdY = -birdY;
				//startVelocity.set(0,0);

				birdY = - birdY;

				batch.begin();

				batch.draw(currentBirdFrame, birdStartPosition.x, birdStartPosition.y, currentBirdFrame.getRegionWidth()/30, currentBirdFrame.getRegionHeight()/30);

				batch.end();

				birdStartPosition.x += birdX;
				birdStartPosition.y += birdY;


				t += 0.00003; //velocity;


			}

			else{


				startVelocity.set(-ValueY, 0f);
				System.out.println("Start velocity power: "+ startVelocity);
				startVelocity.rotate(ValueX);
				System.out.println("Start velocity rotate: "+ startVelocity);

				birdX = getX(t); //initialVelocity.x * t;
				birdY = getY(t); //initialVelocity.y * t - (float)(0.5 * 9.8 * t * t);



				batch.begin();



				batch.draw(currentBirdFrame, birdStartPosition.x, birdStartPosition.y, currentBirdFrame.getRegionWidth()/30, currentBirdFrame.getRegionHeight()/30);
//			batch.draw(currentBirdFrame, currentBirdFrame.getRegionX(), currentBirdFrame.getRegionY(), currentBirdFrame.getRegionWidth()/30, currentBirdFrame.getRegionHeight()/30);

				batch.end();

				birdStartPosition.x += birdX;
				birdStartPosition.y += birdY;


				t += 0.00003; //velocity;



			}








		}






		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			controller.power += 10f * Gdx.graphics.getDeltaTime();
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			controller.power -= 10f * Gdx.graphics.getDeltaTime();
		}

		if (Gdx.input.isKeyPressed(Keys.NUM_5)) {
			controller.fixedHorizontalDistance = false;
		}

		if (Gdx.input.isKeyPressed(Keys.NUM_6)) {
			controller.fixedHorizontalDistance = true;
		}

	}

//	private void updateBall(){
//
//		if(isFired){
//
//			float delta=Gdx.graphics.getDeltaTime();
//			initialVelocity.x=initialVelocity.x+gravity.x*delta*deltaTime;
//			initialVelocity.y=initialVelocity.y+gravity.y*delta*deltaTime;
//			System.out.println("INTIAL VELOCITY" + initialVelocity);
//
//			startPoint.x = (int)(currentBirdFrame.getRegionX()+ initialVelocity.x* delta * deltaTime);
//			startPoint.y = (int)(currentBirdFrame.getRegionY()+initialVelocity.y* delta * deltaTime);
//
//			//ball.setPosition(ball.getX()+initialVelocity.x * delta * deltaTime,ball.getY()+initialVelocity.y * delta * deltaTime);
//
//
//		}
//
//	}

	@Override
	public void dispose () {
		batch.dispose();
		birdFrameOne.dispose();
		birdFrameTwo.dispose();
		birdFrameThree.dispose();
		birdFrameFour.dispose();
		birdFrameFive.dispose();
		birdFrameSix.dispose();
		birdFrameSeven.dispose();
		birdFrameEight.dispose();




	}
}
