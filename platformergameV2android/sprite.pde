class Sprite
{
  PImage stand1;
  PImage stand2;
  PImage stand3;

  PImage run1;
  PImage run2;
  PImage run3;
  PImage run4;
  PImage run5;
  PImage run6;
  PImage run7;

  PImage jumpup1;
  PImage jumpup2;
  PImage jumptop1;
  PImage jumptop2;
  PImage jumptop3;
  PImage jumpdown1;
  PImage jumpdown2;

float speed = scrollspeed;
float jumpower = 20;
float gravity = 1;
float speedy = 0;
boolean falling = false, jumping = false;

//variables for the sprite animations
int s = 1;
int r = 1;
int jumpup = 1;
int down = 1;

AudioPlayer voiceBooom;
AudioPlayer voiceJump;


Sprite(float startX, float startY)
{
  
voiceBooom = maxim.loadFile("uni15001.wav");
voiceJump = maxim.loadFile("uni14951.wav");
  
voiceBooom.setLooping(false);
voiceJump.setLooping(false);

posx = startX;
posy= startY;
stand1 = loadImage ("UnityChan_footwprk_0.png");
stand2 = loadImage ("UnityChan_footwprk_1.png");
stand3 = loadImage ("UnityChan_footwprk_2.png");

run1 = loadImage ("UnityChan_run_0.png");
run2 = loadImage ("UnityChan_run_2.png");
run3 = loadImage ("UnityChan_run_3.png");
run4 = loadImage ("UnityChan_run_4.png");
run5 = loadImage ("UnityChan_run_5.png");
run6 = loadImage ("UnityChan_run_6.png");
run7 = loadImage ("UnityChan_run_7.png");

jumpup1 = loadImage ("UnityChan_jump_up_0.png");
jumpup2 = loadImage ("UnityChan_jump_up_1.png");
jumptop1 = loadImage ("UnityChan_jump_top_0.png");
jumptop2 = loadImage ("UnityChan_jump_top_1.png");
jumptop3 = loadImage ("UnityChan_jump_top_2.png");
jumpdown1 = loadImage ("UnityChan_jump_down_0.png");
jumpdown2 = loadImage ("UnityChan_jump_down_1.png");

}

void run()
{
 
onscreen();

grav(); 

if (start || startmenuscroll)
{posx+= speed; 
state = 2;}
else
if (creditscroll || settingscroll)
state = 3;
else 
state= 1;

touchcontrols();

if (state == 1)
stand();
if (state == 2)
right();
if (state == 3)
left();
if (state == 4)
jump();

}

void grav()
{
  //calculates gravity
  //if above ground level it will lower speed y
  //if at ground it will make it 0
if (posy+(stand1.height)/2 < ground)
  {speedy+= gravity;
  }
posy+=speedy;
if (posy+(stand1.height)/2 >= ground)
  {speedy = 0;
  }
   
  if (speedy > 0)
{falling = true;
jumping = false;
fall();}
else
falling = false;

if (speedy < 0)
{jumpinganimation();
jumping = true;}
}

void fall() 
{
  if (down == 1)
image (jumpdown1,posx,posy);
if (down ==2)
image (jumpdown2,posx,posy);
down++;
if (down ==3)
down = 1;
}

void jump ()
{
//needs to be updated so she can jump off things other than the ground
  if (posy+(stand1.height)/2 >= ground)
  speedy -= jumpower;
  if (falling == false)
  {
  jumpinganimation();
  voiceJump.play();
  }
}

void jumpinganimation()
{
if (jumpup ==1)
  image (jumpup1,posx,posy);
  if (jumpup==2)
  image (jumpup2,posx,posy);
  jumpup++;
  if (jumpup ==3)
  jumpup =1;
  
}

void stand()
{
  if (falling == false && jumping == false)
  {
if (s ==1)
  image (stand1,posx,posy);
if (s ==2)
  image (stand2,posx,posy);
if (s ==3)
  {image (stand3,posx,posy);} 
s= s+1;
if (s==4)
s=1;
  }
}

void right()
{
 //posx+= speed ;
 if (falling == false && jumping == false)
 {
if (r ==1)
  {image (run1,posx,posy);}
if (r ==2)
  {image (run2,posx,posy);}
if (r ==3)
  {image (run3,posx,posy);}
if (r ==4)
  {image (run4,posx,posy);}
if (r ==5)
  {image (run5,posx,posy);}
if (r ==6)
  {image (run6,posx,posy);}
if (r ==7)
  {image (run7,posx,posy);}
  r++;
  if (r==8)
  r=1;
 }
}

void left()
{
 posx-= speed ;
 if (falling == false && jumping == false)
 {
   //sprites are being mirrored. code taken from https://processing.org/discourse/beta/num_1242102535.html
 pushMatrix();
//flip across x axis
scale(-1,1);
//The x position is negative because we flipped
//restore previous translation,etc

if (r ==1)
  {image (run1,-posx,posy);}
if (r ==2)
  {image (run2,-posx,posy);}
if (r ==3)
  {image (run3,-posx,posy);}
if (r ==4)
  {image (run4,-posx,posy);}
if (r ==5)
  {image (run5,-posx,posy);}
if (r ==6)
  {image (run6,-posx,posy);}
if (r ==7)
  {image (run7,-posx,posy);}
  
  popMatrix(); 
  
  r++;
  if (r==8)
  r=1;
 }
}

//checks if the player is in the bounderies of the screen 
void onscreen()
{
if (posx < 0-transl8)
posx = 0-transl8;

if (posx >width-transl8)
posx = width-transl8;
}

}

/*
void keyPressed()
{
if (key == 'd' || keyCode == RIGHT)
if (keyPressed == true) 
    state = 2;
  else
  state = 1;
  
if (key == 'a' || keyCode == LEFT)
if (keyPressed == true) 
    state = 3;
      
if (key == ' ' ||key == 'w' ||keyCode == UP)
if (keyPressed == true) 
    state = 4;
    
 if (key == '1')
 {paused = !paused;}
} 

void keyReleased() {
  state = 1;
}
*/

void touchcontrols()
{
  if (start)
  {
    state = 2;
if (mousePressed == true) 
{
state = 4;
}
}
}



