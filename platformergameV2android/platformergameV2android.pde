//assets (sprites, music, font, backgrounds, sounds)from http://unity-chan.com/
//concept of classes learnt from http://youtu.be/9UcL8B0GQuE?list=PL19223D55BA16ECDF

Maxim maxim;
AudioPlayer startmusic;
AudioPlayer stagemusic;
AudioPlayer voiceStart;
AudioPlayer voice321;
AudioPlayer coinget;
//calls the class Sprit
Sprite chan;

//player positions
float posx,posy;

coin [] coinCollection = new  coin[10];
float coinX = 100;
 float coinY = 100;

//concept for pause taken from https://code.musiccircleproject.com/code-circle-client/?media/3263
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

PImage unitychanlogo;
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

float scrollspeed = 5;

void setup()
{
  //size of the windiw
  size(displayWidth, displayHeight);
  
  frameRate (30);
  background(0);

  BG1 =loadImage("BG_01.png");
  BG2 =loadImage("BG_02.png");

  unitychanlogo = loadImage ("UnityChan_logo.png");
  unitychanlicense = loadImage ("Dark_Silhouette.png");
  maxim = new Maxim(this);
  voiceStart = maxim.loadFile("uni14941.wav");
  voiceStart.setLooping(false);
  voice321 = maxim.loadFile("uni14931.wav");
  voice321.setLooping(false);
  startmusic = maxim.loadFile("title.wav");
  stagemusic = maxim.loadFile("stage.wav");
  coinget = maxim.loadFile("coinget.wav");
  coinget.setLooping(false);
  imageMode(CENTER);
  smooth();
  noStroke();
  
  image (unitychanlicense,width*0.5,height*0.5);
silkscreen = loadFont ("Silkscreen-Bold-30.vlw");
textFont (silkscreen,30);
textAlign (CENTER,CENTER);

//creates the sprite
chan = new Sprite(100,200);

ground = height-50;

for (int h = 0; h <coinCollection.length; h++)
coinCollection[h] = new coin (random(width,width*2),height-BG2.height);
}

void draw()
{
 
 translate(transl8,0);
if (paused)
pause();
else
{
startmenu();
settings();
credits();
score();
//if the start button has been pressed stuff happens
if (start==true)
{
play();
}

chan.run();
}
}

//score system
void score()
{
 if (start)
 {
   fill(200,70,90);
text (score,(width*0.1)-transl8,0+(height*0.1));
 }
}

//stuff that happens when the game is paused
void pause()
{
 loadPixels();
for(int t =0;t< pixels.length ;t++)
{float rand = random(255);
  color c = pixels[t];
  c= c+(t/2);
  pixels[t] = c;
}
updatePixels(); 

fill (200,70,90);
rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.35, width*0.3, height*0.1);
rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.5, width*0.3, height*0.1);

  fill(100,35,45);
if(mouseX > width*0.35 && mouseX < width*0.65 && mouseY > height*0.35 && mouseY < height*0.45)
{rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.35, width*0.3, height*0.1);
if (mousePressed)
paused = false;}

  fill(100,35,45);
if(mouseX > width*0.35 && mouseX < width*0.65 && mouseY > height*0.5 && mouseY < height*0.6)
{rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.5, width*0.3, height*0.1);
if (mousePressed)
{paused = false;
start = false;
transl8= 0;
chan = new Sprite(100,200);
}
}

fill(0);
text ("resume",width*0.5-transl8, height*0.4);
text ("reset",width*0.5-transl8, height*0.55);
}


