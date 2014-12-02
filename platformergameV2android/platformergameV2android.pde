//assets (sprites, music, font, backgrounds, sounds)from http://unity-chan.com/

Maxim maxim;
AudioPlayer startmusic;
AudioPlayer stagemusic;
AudioPlayer gameovermusic;
AudioPlayer voiceStart;
AudioPlayer voice321;
AudioPlayer voiceBooom;
AudioPlayer coinget;

//calls the class Sprit
Sprite chan;

//player positions
float posx, posy;

//size of player
int spriteheight;
int spritewidth;

coin [] coinCollection = new  coin[50];
float coinX = 100;
float coinY = 100;

eni [] enemyCollection = new  eni[25];

boolean paused = false;

//has the start button been pressed
boolean start = false;

//controls if sound is turned off in the settings menu
boolean music = true;

//if the control menu is present on screen
boolean gamecontrol = false;

//if setting/credit has been selected
boolean settingscroll = false;
boolean creditscroll = false;
boolean startmenuscroll = false;

PImage logo;
PImage unitychanlicense;
PFont silkscreen;

PImage BG1;
PImage BG2;

//sets the state of the sprite 1 =standing, 2 = run right, etc
//see sprite tab for more details
int state =1;

//sets ground level. the player can only jump when on the ground
int ground;

//the x positon of the screen compared to the start of the sketch
float transl8 = 0;

//position variable for the credits
float creditpos = 1;
//if the player is in front of the credits this is true

//game score
int score = 0;
int highscore = 0;

//speed of the background movement and player running
float scrollspeed = 15;

boolean gameover = false;

void setup()
{
  //size of the windiw
  size(displayWidth, displayHeight);

  frameRate (60);
  background(0);

  BG1 =loadImage("BG_01.png");
  BG2 =loadImage("BG_02.png");

  BG1.resize(width, height);
  BG2.resize(0, height);


  logo = loadImage ("Sand Runner.png");
  logo.resize(width/2,0);
  unitychanlicense = loadImage ("Dark_Silhouette.png");
  maxim = new Maxim(this);
  voiceStart = maxim.loadFile("uni14941.wav");
  voiceStart.setLooping(false);
  voice321 = maxim.loadFile("uni14931.wav");
  voice321.setLooping(false);
  voiceBooom = maxim.loadFile("uni15001.wav");
  voiceBooom.setLooping(false);


  startmusic = maxim.loadFile("title.wav");
  stagemusic = maxim.loadFile("stage.wav");
  gameovermusic = maxim.loadFile("start.wav");
  coinget = maxim.loadFile("coinget.wav");
  coinget.setLooping(false);
  imageMode(CENTER);
  smooth();
  noStroke();

  image (unitychanlicense, width*0.5, height*0.5);
  silkscreen = createFont("slkscrb.ttf", 30, true);
  textFont (silkscreen, 30);
  textAlign (CENTER, CENTER);

  //sets size of the player
  spriteheight = (width+height)/20;
  spritewidth =  1540/spriteheight;

  //creates the sprite
  chan = new Sprite(width*0.3, height*0.5);

  ground = (height*8)/10;

  for (int h = 0; h <coinCollection.length; h++)
    coinCollection[h] = new coin (random(width, width*10), random(height-BG2.height, ground));

  for (int m = 0; m <enemyCollection.length; m++)
    enemyCollection[m] = new eni (random(width, width*10), random(height-BG2.height, ground));
}

void draw()
{

  translate(transl8, 0);
  if (paused || gameover)
    pause();
  else
  {
    if (!start)
      startmenu();

    settings();
    credits();

    //if the start button has been pressed stuff in play happens
    if (start==true)
    {
      play();
      score();
    }

    chan.run();
  }
  pausebutton();
  gameoverscreen();
  fill(255);
  text(frameRate, 100-transl8, 100);
}

//score system
void score()
{

  fill(200, 70, 90);
  text ("score:"+score, (width*0.1)-transl8, 0+(height*0.1));
  
  if(highscore > 0)
  text("highscore "+ highscore, (width*0.1)-transl8, 0+(height*0.2));
}

void pausebutton()
{
  if (start && !gameover)
  { 
    fill (200, 70, 90);
    rect(width*0.85-transl8, height*0.05, width*0.1, height*0.1);
  }
}

//stuff that happens when the game is paused
void pause()
{
  if (!gameover)
  {

    //fading background
    fill(0, 1.5);
    rect(0-transl8, 0, width, height);

    /*
  loadPixels();
     for (int t =0; t< pixels.length; t++)
     {
     float rand = random(255);
     color c = pixels[t];
     c= c+(t/2);
     pixels[t] = c;
     }
     updatePixels(); */

    fill (200, 70, 90);
    rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.35, width*0.3, height*0.1);
    rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.5, width*0.3, height*0.1);

    fill(100, 35, 45);
    if (mouseX > width*0.35 && mouseX < width*0.65 && mouseY > height*0.35 && mouseY < height*0.45)
    {
      rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.35, width*0.3, height*0.1);
      if (mousePressed)
        paused = false;
    }

    fill(100, 35, 45);
    if (mouseX > width*0.35 && mouseX < width*0.65 && mouseY > height*0.5 && mouseY < height*0.6)
    {
      rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.5, width*0.3, height*0.1);
      if (mousePressed)
      {
        paused = false;
        start = false;
        transl8= 0;
        chan = new Sprite(100, 200);
        score = 0;
      }
    }

    fill(0);
    text ("resume", width*0.5-transl8, height*0.4);
    text ("reset", width*0.5-transl8, height*0.55);
  }
}
