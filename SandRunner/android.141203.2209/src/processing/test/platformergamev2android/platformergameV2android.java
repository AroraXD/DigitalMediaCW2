package processing.test.platformergamev2android;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.BufferedInputStream; 
import java.net.MalformedURLException; 
import java.net.URL; 
import android.app.Activity; 
import android.os.Bundle; 
import android.media.*; 
import android.media.audiofx.Visualizer; 
import android.content.res.AssetFileDescriptor; 
import android.hardware.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class platformergameV2android extends PApplet {

//assets (sprites, music, font, backgrounds, sounds)from http://unity-chan.com/

Maxim maxim;
AudioPlayer startmusic;
AudioPlayer stagemusic;
AudioPlayer gameovermusic;
AudioPlayer voiceStart;
AudioPlayer voice321;
AudioPlayer voiceBooom;
AudioPlayer voicegameover1;
AudioPlayer voicegameover2;
AudioPlayer coinget;
AudioPlayer button;

//calls the class Sprit
Sprite chan;

//player positions
float posx, posy;

//size of player
int spriteheight;
int spritewidth;

coin [] coinCollection = new  coin[500];
float coinX = 100;
float coinY = 100;

eni [] enemyCollection = new  eni[250];

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

//picks which gameover voice will play
int gameoverV = 0;

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

public void setup()
{
  //size of the windiw
 

  frameRate (60);
  background(0);

  BG1 =loadImage("BG_01.png");
  BG2 =loadImage("BG_02.png");

  BG1.resize(width, height);
  BG2.resize(0, height);


  logo = loadImage ("Sand Runner.png");
  logo.resize(0, height/4);
  
  unitychanlicense = loadImage ("Dark_Silhouette.png");
  maxim = new Maxim(this);
  voiceStart = maxim.loadFile("uni14941.wav");
  voiceStart.setLooping(false);
  voice321 = maxim.loadFile("uni14931.wav");
  voice321.setLooping(false);
  voiceBooom = maxim.loadFile("uni15001.wav");
  voiceBooom.setLooping(false);
  voicegameover1 = maxim.loadFile("univ0010.wav");
  voicegameover1.setLooping(false);
  voicegameover2 = maxim.loadFile("univ1091.wav");
  voicegameover2.setLooping(false);


  button =maxim.loadFile("button press.wav");
  button.setLooping(false);
  button.volume(2);

  startmusic = maxim.loadFile("title.wav");
  stagemusic = maxim.loadFile("stage.wav");
  gameovermusic = maxim.loadFile("start.wav");
  coinget = maxim.loadFile("coinget.wav");
  coinget.setLooping(false);
  imageMode(CENTER);
  smooth();
  noStroke();

  image (unitychanlicense, width*0.5f, height*0.5f);
  silkscreen = createFont("slkscrb.ttf", 30, true);
  textFont (silkscreen, 50);
  textAlign (CENTER, CENTER);

  //sets size of the player
  spriteheight = (width+height)/20;
  spritewidth =  1540/spriteheight;

  //creates the sprite
  chan = new Sprite(width*0.3f, height*0.5f);

  ground = (height*8)/10;

  for (int h = 0; h <coinCollection.length; h++)
    coinCollection[h] = new coin (random(width, width*100), random(height-BG2.height, ground));

  for (int m = 0; m <enemyCollection.length; m++)
    enemyCollection[m] = new eni (random(width, width*100), random(height-BG2.height, ground));
}

public void draw()
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
public void score()
{
  textAlign(LEFT);
  fill(200, 70, 90);
  text ("score:"+score, (width*0.1f)-transl8, 0+(height*0.1f));

  if (highscore > 0)
    text("highscore "+ highscore, (width*0.1f)-transl8, 0+(height*0.15f));

  textAlign (CENTER, CENTER);
}

public void pausebutton()
{
  if (start && !gameover)
  { 
    fill (200, 70, 90);
    rect(width*0.75f-transl8, height*0.05f, width*0.2f, height*0.08f);
    fill(0);
    text("pause",width*0.85f-transl8,height*0.09f);
    //note; pause button is not a button yet
  }
}

//stuff that happens when the game is paused
public void pause()
{
  if (!gameover)
  {

    //fading background
    fill(0, 1.5f);
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
    rect ((width*0.5f)-(width*0.3f*0.5f)-transl8, height*0.35f, width*0.3f, height*0.1f);
    rect ((width*0.5f)-(width*0.3f*0.5f)-transl8, height*0.5f, width*0.3f, height*0.1f);

    fill(100, 35, 45);
    if (mouseX > width*0.35f && mouseX < width*0.65f && mouseY > height*0.35f && mouseY < height*0.45f)
    {
      rect ((width*0.5f)-(width*0.3f*0.5f)-transl8, height*0.35f, width*0.3f, height*0.1f);
      if (mousePressed)
        paused = false;
    }

    fill(100, 35, 45);
    if (mouseX > width*0.35f && mouseX < width*0.65f && mouseY > height*0.5f && mouseY < height*0.6f)
    {
      rect ((width*0.5f)-(width*0.3f*0.5f)-transl8, height*0.5f, width*0.3f, height*0.1f);
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
    text ("resume", width*0.5f-transl8, height*0.4f);
    text ("reset", width*0.5f-transl8, height*0.55f);
  }
}


public void credits()
{
  if (creditscroll)
    if (transl8 < 2*width)
      transl8 += scrollspeed;
    else
      creditscroll=false;

  fill (200, 70, 90);
  text("game made by GP3", width*0.5f-(2*width), (height*0.1f));
  text("Digital Media CW2", width*0.5f-(2*width), (height*0.2f));
  text("assests from unity-chan", width*0.5f-(2*width), (height*0.3f));

  image (unitychanlicense, width*0.5f-(width*2), height*0.5f);

  rect ((width*0.5f)-(width*0.3f*0.5f)-2*width, height*0.65f, width*0.3f, height*0.1f);

  //button control
  if (mouseX-transl8 > width*0.35f -2*width && mouseX-transl8 < width*0.65f -2*width && mouseY > height*0.65f && mouseY < height*0.75f)
  {
    fill(100, 35, 45);
    if (mousePressed)
    {
      startmenuscroll = true;
      creditscroll = false;
      settingscroll = false;
    }
  }

  fill(0);
  text ("Back", width*0.5f-2*width, height*0.7f);
}

public void play()
{
  if (music)
    stagemusic.play();
  startmusic.stop();

  //background
  image (BG1, (width*0.5f)-transl8, height*0.5f);

  for (int j = -3; j < 3* (posx/width); j++)
  if(BG2.width*j+width > -transl8-BG2.width && BG2.width*j+width < width+BG2.width-transl8)
    image (BG2, BG2.width*j+width, height-(BG2.height*0.5f));

  transl8 -=scrollspeed;

  for (int c =0; c < coinCollection.length; c++)
    coinCollection[c].run();

  for (int c =0; c < enemyCollection.length; c++)
    enemyCollection[c].run();
}
public void settings()
{
  if (settingscroll)
  {
    if (transl8 <width)
      transl8 += scrollspeed;
    else 
      settingscroll = false;
  }


  fill (200, 70, 90);
  rect ((width*0.5f)-(width*0.3f*0.5f)-width, height*0.35f, width*0.3f, height*0.1f);
  rect ((width*0.5f)-(width*0.3f*0.5f)-width, height*0.5f, width*0.3f, height*0.1f);
  rect ((width*0.5f)-(width*0.3f*0.5f)-width, height*0.65f, width*0.3f, height*0.1f);

  //button controls
  fill(100, 35, 45);
  if (mouseX-transl8 > width*0.35f-width && mouseX-transl8 < width*0.65f-width && mouseY > height*0.35f && mouseY < height*0.45f)
    if (mousePressed)
    {
      music = !music;
    }

  if (mouseX-transl8 > width*0.35f-width && mouseX-transl8 < width*0.65f-width && mouseY > height*0.5f && mouseY < height*0.6f)
    if (mousePressed)
    {
      gamecontrol = !gamecontrol;
    }

  if (mouseX-transl8 > width*0.35f-width && mouseX-transl8 < width*0.65f-width && mouseY > height*0.65f && mouseY < height*0.75f)
    if (mousePressed)
    {
      {
        creditscroll = false;
        settingscroll = false;
        startmenuscroll = true;
      }
    }

  if (music)
    fill(59, 191, 4);
  else
    fill(0);
  text ("Music", width*0.5f-width, height*0.4f);

  fill(0);
  text ("Controls", width*0.5f-width, height*0.55f);
  text ("Back", width*0.5f-width, height*0.7f);

  gamecontrols();
}

public void gamecontrols()
{
  if (gamecontrol == true)
  {
    fill (200, 70, 90);
    rect ((width*0.1f)-width, (height*0.1f), width*0.8f, height*0.65f);
    fill(0);
    text("game controls", width*0.5f-width, height*0.15f);
    textSize(30);
    text("Hold down to fill jump bar", width*0.5f-width, height*0.2f);
    text("release to jump", width*0.5f-width, height*0.3f);
    text("press pause to pause the game", width*0.5f-width, height*0.4f);
    text("collect coins to increase your score", width*0.5f-width, height*0.5f);
    text("avoid the pink enemies", width*0.5f-width, height*0.6f);
    text("gl hf", width*0.5f-width, height*0.7f);
    textSize(50);

    if (mousePressed)
      gamecontrol = false;
  }
}

/*
The MIT License (MIT)
 
 Copyright (c) 2013 Mick Grierson, Matthew Yee-King, Marco Gillies
 
 Permission is hereby granted, free of charge, to any person obtaining a copy\u2028of 
 this software and associated documentation files (the "Software"), to 
 deal\u2028in the Software without restriction, including without limitation 
 the rights\u2028to use, copy, modify, merge, publish, distribute, sublicense, 
 and/or sell\u2028copies of the Software, and to permit persons to whom the 
 Software is\u2028furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included 
 in \u2028all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\u2028IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\u2028FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\u2028AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\u2028LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\u2028OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\u2028THE SOFTWARE.
 */

// putting this up in global scope for consistency with maxim.js
// eventually, this should be inside Maxim in all versions of the library...
float[] mtof = {
  0f, 8.661957f, 9.177024f, 9.722718f, 10.3f, 10.913383f, 11.562325f, 12.25f, 12.978271f, 13.75f, 14.567617f, 15.433853f, 16.351599f, 17.323914f, 18.354048f, 19.445436f, 20.601723f, 21.826765f, 23.124651f, 24.5f, 25.956543f, 27.5f, 29.135235f, 30.867706f, 32.703197f, 34.647827f, 36.708096f, 38.890873f, 41.203445f, 43.65353f, 46.249302f, 49.f, 51.913086f, 55.f, 58.27047f, 61.735413f, 65.406395f, 69.295654f, 73.416191f, 77.781746f, 82.406891f, 87.30706f, 92.498604f, 97.998856f, 103.826172f, 110.f, 116.540939f, 123.470825f, 130.81279f, 138.591309f, 146.832382f, 155.563492f, 164.813782f, 174.61412f, 184.997208f, 195.997711f, 207.652344f, 220.f, 233.081879f, 246.94165f, 261.62558f, 277.182617f, 293.664764f, 311.126984f, 329.627563f, 349.228241f, 369.994415f, 391.995422f, 415.304688f, 440.f, 466.163757f, 493.883301f, 523.25116f, 554.365234f, 587.329529f, 622.253967f, 659.255127f, 698.456482f, 739.988831f, 783.990845f, 830.609375f, 880.f, 932.327515f, 987.766602f, 1046.502319f, 1108.730469f, 1174.659058f, 1244.507935f, 1318.510254f, 1396.912964f, 1479.977661f, 1567.981689f, 1661.21875f, 1760.f, 1864.655029f, 1975.533203f, 2093.004639f, 2217.460938f, 2349.318115f, 2489.015869f, 2637.020508f, 2793.825928f, 2959.955322f, 3135.963379f, 3322.4375f, 3520.f, 3729.31f, 3951.066406f, 4186.009277f, 4434.921875f, 4698.63623f, 4978.031738f, 5274.041016f, 5587.651855f, 5919.910645f, 6271.926758f, 6644.875f, 7040.f, 7458.620117f, 7902.132812f, 8372.018555f, 8869.84375f, 9397.272461f, 9956.063477f, 10548.082031f, 11175.303711f, 11839.821289f, 12543.853516f, 13289.75f
};








//import android.content.res.Resources;
 
 






public class Maxim {

  private float sampleRate = 44100;

  public final float[] mtof = {
    0, 8.661957f, 9.177024f, 9.722718f, 10.3f, 10.913383f, 11.562325f, 12.25f, 12.978271f, 13.75f, 14.567617f, 15.433853f, 16.351599f, 17.323914f, 18.354048f, 19.445436f, 20.601723f, 21.826765f, 23.124651f, 24.5f, 25.956543f, 27.5f, 29.135235f, 30.867706f, 32.703197f, 34.647827f, 36.708096f, 38.890873f, 41.203445f, 43.65353f, 46.249302f, 49.f, 51.913086f, 55.f, 58.27047f, 61.735413f, 65.406395f, 69.295654f, 73.416191f, 77.781746f, 82.406891f, 87.30706f, 92.498604f, 97.998856f, 103.826172f, 110.f, 116.540939f, 123.470825f, 130.81279f, 138.591309f, 146.832382f, 155.563492f, 164.813782f, 174.61412f, 184.997208f, 195.997711f, 207.652344f, 220.f, 233.081879f, 246.94165f, 261.62558f, 277.182617f, 293.664764f, 311.126984f, 329.627563f, 349.228241f, 369.994415f, 391.995422f, 415.304688f, 440.f, 466.163757f, 493.883301f, 523.25116f, 554.365234f, 587.329529f, 622.253967f, 659.255127f, 698.456482f, 739.988831f, 783.990845f, 830.609375f, 880.f, 932.327515f, 987.766602f, 1046.502319f, 1108.730469f, 1174.659058f, 1244.507935f, 1318.510254f, 1396.912964f, 1479.977661f, 1567.981689f, 1661.21875f, 1760.f, 1864.655029f, 1975.533203f, 2093.004639f, 2217.460938f, 2349.318115f, 2489.015869f, 2637.020508f, 2793.825928f, 2959.955322f, 3135.963379f, 3322.4375f, 3520.f, 3729.31f, 3951.066406f, 4186.009277f, 4434.921875f, 4698.63623f, 4978.031738f, 5274.041016f, 5587.651855f, 5919.910645f, 6271.926758f, 6644.875f, 7040.f, 7458.620117f, 7902.132812f, 8372.018555f, 8869.84375f, 9397.272461f, 9956.063477f, 10548.082031f, 11175.303711f, 11839.821289f, 12543.853516f, 13289.75f
  };

  private AndroidAudioThread audioThread;

  public Maxim (PApplet app) {
    audioThread = new AndroidAudioThread(sampleRate, 256, false);
    audioThread.start();
  }

  public float[] getPowerSpectrum() {
    return audioThread.getPowerSpectrum();
  }

  /** 
   *  load the sent file into an audio player and return it. Use
   *  this if your audio file is not too long want precision control
   *  over looping and play head position
   * @param String filename - the file to load
   * @return AudioPlayer - an audio player which can play the file
   */
  public AudioPlayer loadFile(String filename) {
    // this will load the complete audio file into memory
    AudioPlayer ap = new AudioPlayer(filename, sampleRate);
    audioThread.addAudioGenerator(ap);
    // now we need to tell the audiothread
    // to ask the audioplayer for samples
    return ap;
  }

  /**
   * Create a wavetable player object with a wavetable of the sent
   * size. Small wavetables (<128) make for a 'nastier' sound!
   * 
   */
  public WavetableSynth createWavetableSynth(int size) {
    // this will load the complete audio file into memory
    WavetableSynth ap = new WavetableSynth(size, sampleRate);
    audioThread.addAudioGenerator(ap);
    // now we need to tell the audiothread
    // to ask the audioplayer for samples
    return ap;
  }
  /**
   * Create an AudioStreamPlayer which can stream audio from the
   * internet as well as local files.  Does not provide precise
   * control over looping and playhead like AudioPlayer does.  Use this for
   * longer audio files and audio from the internet.
   */
  public AudioStreamPlayer createAudioStreamPlayer(String url) {
    AudioStreamPlayer asp = new AudioStreamPlayer(url);
    return asp;
  }
}



/**
 * This class can play audio files and includes an fx chain 
 */
public class AudioPlayer implements Synth, AudioGenerator {
  private FXChain fxChain;
  private boolean isPlaying;
  private boolean isLooping;
  private boolean analysing;
  private FFT fft;
  private int fftInd;
  private float[] fftFrame;
  private float[] powerSpectrum;

  //private float startTimeSecs;
  //private float speed;
  private int length;
  private short[] audioData;
  private float startPos;
  private float readHead;
  private float dReadHead;
  private float sampleRate;
  private float masterVolume;

  float x1, x2, y1, y2, x3, y3;

  public AudioPlayer(float sampleRate) {
    fxChain = new FXChain(sampleRate);
    this.dReadHead = 1;
    this.sampleRate = sampleRate;
    this.masterVolume = 1;
  }

  public AudioPlayer (String filename, float sampleRate) {
    //super(filename);
    this(sampleRate);
    try {
      // how long is the file in bytes?
      long byteCount = getAssets().openFd(filename).getLength();
      //System.out.println("bytes in "+filename+" "+byteCount);

      // check the format of the audio file first!
      // only accept mono 16 bit wavs
      InputStream is = getAssets().open(filename); 
      BufferedInputStream bis = new BufferedInputStream(is);

      // chop!!

      int bitDepth;
      int channels;
      boolean isPCM;
      // allows us to read up to 4 bytes at a time 
      byte[] byteBuff = new byte[4];

      // skip 20 bytes to get file format
      // (1 byte)
      bis.skip(20);
      bis.read(byteBuff, 0, 2); // read 2 so we are at 22 now
      isPCM = ((short)byteBuff[0]) == 1 ? true:false; 
      //System.out.println("File isPCM "+isPCM);

      // skip 22 bytes to get # channels
      // (1 byte)
      bis.read(byteBuff, 0, 2);// read 2 so we are at 24 now
      channels = (short)byteBuff[0];
      //System.out.println("#channels "+channels+" "+byteBuff[0]);
      // skip 24 bytes to get sampleRate
      // (32 bit int)
      bis.read(byteBuff, 0, 4); // read 4 so now we are at 28
      sampleRate = bytesToInt(byteBuff, 4);
      //System.out.println("Sample rate "+sampleRate);
      // skip 34 bytes to get bits per sample
      // (1 byte)
      bis.skip(6); // we were at 28...
      bis.read(byteBuff, 0, 2);// read 2 so we are at 36 now
      bitDepth = (short)byteBuff[0];
      //System.out.println("bit depth "+bitDepth);
      // convert to word count...
      bitDepth /= 8;
      // now start processing the raw data
      // data starts at byte 36
      int sampleCount = (int) ((byteCount - 36) / (bitDepth * channels));
      audioData = new short[sampleCount];
      int skip = (channels -1) * bitDepth;
      int sample = 0;
      // skip a few sample as it sounds like shit
      bis.skip(bitDepth * 4);
      while (bis.available () >= (bitDepth+skip)) {
        bis.read(byteBuff, 0, bitDepth);// read 2 so we are at 36 now
        //int val = bytesToInt(byteBuff, bitDepth);
        // resample to 16 bit by casting to a short
        audioData[sample] = (short) bytesToInt(byteBuff, bitDepth);
        bis.skip(skip);
        sample ++;
      }

      float secs = (float)sample / (float)sampleRate;
      //System.out.println("Read "+sample+" samples expected "+sampleCount+" time "+secs+" secs ");      
      bis.close();


      // unchop
      readHead = 0;
      startPos = 0;
      // default to 1 sample shift per tick
      dReadHead = 1;
      isPlaying = false;
      isLooping = true;
      masterVolume = 1;
    } 
    catch (FileNotFoundException e) {

      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setAnalysing(boolean analysing_) {
    this.analysing = analysing_;
    if (analysing) {// initialise the fft
      fft = new FFT();
      fftInd = 0;
      fftFrame = new float[1024];
      powerSpectrum = new float[fftFrame.length/2];
    }
  }

  public float getAveragePower() {
    if (analysing) {
      // calc the average
      float sum = 0;
      for (int i=0; i<powerSpectrum.length; i++) {
        sum += powerSpectrum[i];
      }
      sum /= powerSpectrum.length;
      return sum;
    } else {
      System.out.println("call setAnalysing to enable power analysis");
      return 0;
    }
  }
  public float[] getPowerSpectrum() {
    if (analysing) {
      return powerSpectrum;
    } else {
      System.out.println("call setAnalysing to enable power analysis");
      return null;
    }
  }

  /** 
   *convert the sent byte array into an int. Assumes little endian byte ordering. 
   *@param bytes - the byte array containing the data
   *@param wordSizeBytes - the number of bytes to read from bytes array
   *@return int - the byte array as an int
   */
  private int bytesToInt(byte[] bytes, int wordSizeBytes) {
    int val = 0;
    for (int i=wordSizeBytes-1; i>=0; i--) {
      val <<= 8;
      val |= (int)bytes[i] & 0xFF;
    }
    return val;
  }

  /**
   * Test if this audioplayer is playing right now
   * @return true if it is playing, false otherwise
   */
  public boolean isPlaying() {
    return isPlaying;
  }

  /**
   * Set the loop mode for this audio player
   * @param looping 
   */
  public void setLooping(boolean looping) {
    isLooping = looping;
  }

  /**
   * Move the start pointer of the audio player to the sent time in ms
   * @param timeMs - the time in ms
   */
  public void cue(int timeMs) {
    //startPos = ((timeMs / 1000) * sampleRate) % audioData.length;
    //readHead = startPos;
    //System.out.println("AudioPlayer Cueing to "+timeMs);
    if (timeMs >= 0) {// ignore crazy values
      readHead = (((float)timeMs / 1000f) * sampleRate) % audioData.length;
      //System.out.println("Read head went to "+readHead);
    }
  }

  /**
   *  Set the playback speed,
   * @param speed - playback speed where 1 is normal speed, 2 is double speed
   */
  public void speed(float speed) {
    //System.out.println("setting speed to "+speed);
    dReadHead = speed;
  }

  /**
   * Set the master volume of the AudioPlayer
   */

  public void volume(float volume) {
    masterVolume = volume;
  }

  /**
   * Get the length of the audio file in samples
   * @return int - the  length of the audio file in samples
   */
  public int getLength() {
    return audioData.length;
  }
  /**
   * Get the length of the sound in ms, suitable for sending to 'cue'
   */
  public float getLengthMs() {
    return ((float) audioData.length / sampleRate * 1000f);
  }

  /**
   * Start playing the sound. 
   */
  public void play() {
    isPlaying = true;
  }

  /**
   * Stop playing the sound
   */
  public void stop() {
    isPlaying = false;
  }

  /**
   * implementation of the AudioGenerator interface
   */
  public short getSample() {
    if (!isPlaying) {
      return 0;
    } else {
      short sample;
      readHead += dReadHead;
      if (readHead > (audioData.length - 1)) {// got to the end
        //% (float)audioData.length;
        if (isLooping) {// back to the start for loop mode
          readHead = readHead % (float)audioData.length;
        } else {
          readHead = 0;
          isPlaying = false;
        }
      }

      // linear interpolation here
      // declaring these at the top...
      // easy to understand version...
      //      float x1, x2, y1, y2, x3, y3;
      x1 = floor(readHead);
      x2 = x1 + 1;
      y1 = audioData[(int)x1];
      y2 = audioData[(int) (x2 % audioData.length)];
      x3 = readHead;
      // calc 
      y3 =  y1 + ((x3 - x1) * (y2 - y1));
      y3 *= masterVolume;
      sample = fxChain.getSample((short) y3);
      if (analysing) {
        // accumulate samples for the fft
        fftFrame[fftInd] = (float)sample / 32768f;
        fftInd ++;
        if (fftInd == fftFrame.length - 1) {// got a frame
          powerSpectrum = fft.process(fftFrame, true);
          fftInd = 0;
        }
      }
      // println(audioData[(int)x1]);
      return sample;
      //return (short)y3;
      //return audioData[(int)x1];
    }
  }

  public void setAudioData(short[] audioData) {
    //println(audioData[100]);
    this.audioData = audioData;
  }

  public short[] getAudioData() {
    return audioData;
  }

  public void setDReadHead(float dReadHead) {
    this.dReadHead = dReadHead;
  }

  ///
  //the synth interface
  // 

  public void ramp(float val, float timeMs) {
    fxChain.ramp(val, timeMs);
  } 



  public void setDelayTime(float delayMs) {
    fxChain.setDelayTime( delayMs);
  }

  public void setDelayFeedback(float fb) {
    fxChain.setDelayFeedback(fb);
  }

  public void setFilter(float cutoff, float resonance) {
    fxChain.setFilter( cutoff, resonance);
  }
}

/**
 * This class can play wavetables and includes an fx chain
 */
public class WavetableSynth extends AudioPlayer {

  private short[] sine;
  private short[] saw;
  private short[] wavetable;
  private float sampleRate;

  public WavetableSynth(int size, float sampleRate) {
    super(sampleRate);
    sine = new short[size];
    for (float i = 0; i < sine.length; i++) {
      float phase;
      phase = TWO_PI / size * i;
      sine[(int)i] = (short) (sin(phase) * 32768);
    }
    saw = new short[size];
    for (float i = 0; i<saw.length; i++) {
      saw[(int)i] = (short) (i / (float)saw.length *32768);
    }

    this.sampleRate = sampleRate;
    setAudioData(saw);
    setLooping(true);
  }
  //    public short getSample() {
  //      return (short) random(0, 65536);
  //    }


  public void setFrequency(float freq) {
    if (freq > 0) {
      //System.out.println("freq freq "+freq);
      setDReadHead((float)getAudioData().length / sampleRate * freq);
    }
  }

  /** for consistency with maxim.js */
  public void waveTableSize(int size) {
  }

  /** alias to loadWaveForm for consistency with maxim.js*/
  public void loadWaveTable(float[] wavetable_) {
    loadWaveForm(wavetable_);
  }

  public void loadWaveForm(float[] wavetable_) {
    if (wavetable == null || wavetable_.length != wavetable.length) {
      // only reallocate if there is a change in length
      wavetable = new short[wavetable_.length];
    }
    for (int i=0; i<wavetable.length; i++) {
      wavetable[i] = (short) (wavetable_[i] * 32768);
    }
    setAudioData(wavetable);
  }
}

public interface Synth {
  public void volume(float volume);
  public void ramp(float val, float timeMs);  
  public void setDelayTime(float delayMs);  
  public void setDelayFeedback(float fb);  
  public void setFilter(float cutoff, float resonance);
  public void setAnalysing(boolean analysing);
  public float getAveragePower();
  public float[] getPowerSpectrum();
}

public class AndroidAudioThread extends Thread
{
  private int minSize;
  private AudioTrack track;
  private short[] bufferS;
  private float[] bufferF;
  private ArrayList audioGens;
  private boolean running;

  private FFT fft;
  private float[] fftFrame;


  public AndroidAudioThread(float samplingRate, int bufferLength) {
    this(samplingRate, bufferLength, false);
  }

  public AndroidAudioThread(float samplingRate, int bufferLength, boolean enableFFT)
  {
    audioGens = new ArrayList();
    minSize =AudioTrack.getMinBufferSize( (int)samplingRate, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT );        
    //println();
    // note that we set the buffer just to something small
    // not to the minSize
    // setting to minSize seems to cause glitches on the delivery of audio 
    // to the sound card (i.e. ireegular delivery rate)
    bufferS = new short[bufferLength];
    bufferF = new float[bufferLength];

    track = new AudioTrack( AudioManager.STREAM_MUSIC, (int)samplingRate, 
    AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, 
    minSize, AudioTrack.MODE_STREAM);
    track.play();

    if (enableFFT) {
      try {
        fft = new FFT();
      }
      catch(Exception e) {
        println("Error setting up the audio analyzer");
        e.printStackTrace();
      }
    }
  }

  /**
   * Returns a recent snapshot of the power spectrum as 8 bit values
   */
  public float[] getPowerSpectrum() {
    // process the last buffer that was calculated
    if (fftFrame == null) {
      fftFrame = new float[bufferS.length];
    }
    for (int i=0; i<fftFrame.length; i++) {
      fftFrame[i] = ((float) bufferS[i] / 32768f);
    }
    return fft.process(fftFrame, true);
    //return powerSpectrum;
  }

  // overidden from Thread
  public void run() {
    running = true;
    while (running) {
      //System.out.println("AudioThread : ags  "+audioGens.size());
      for (int i=0; i<bufferS.length; i++) {
        // we add up using a 32bit int
        // to prevent clipping
        int val = 0;
        if (audioGens.size() > 0) {
          for (int j=0; j<audioGens.size (); j++) {
            AudioGenerator ag = (AudioGenerator)audioGens.get(j);
            val += ag.getSample();
          }
          val /= audioGens.size();
        }
        bufferS[i] = (short) val;
      }
      // send it to the audio device!
      track.write( bufferS, 0, bufferS.length );
    }
  }

  public void addAudioGenerator(AudioGenerator ag) {
    audioGens.add(ag);
  }
}

/**
 * Implement this interface so the AudioThread can request samples from you
 */
public interface AudioGenerator {
  /** AudioThread calls this when it wants a sample */
  public short getSample();
}


public class FXChain {
  private float currentAmp;
  private float dAmp;
  private float targetAmp;
  private boolean goingUp;
  private Filter filter;

  private float[] dLine;   

  private float sampleRate;

  public FXChain(float sampleRate_) {
    sampleRate = sampleRate_;
    currentAmp = 1;
    dAmp = 0;
    // filter = new MickFilter(sampleRate);
    filter = new RLPF(sampleRate);

    filter.setFilter(sampleRate, 0.5f);
  }

  public void ramp(float val, float timeMs) {
    // calc the dAmp;
    // - change per ms
    targetAmp = val;
    dAmp = (targetAmp - currentAmp) / (timeMs / 1000 * sampleRate);
    if (targetAmp > currentAmp) {
      goingUp = true;
    } else {
      goingUp = false;
    }
  }


  public void setDelayTime(float delayMs) {
  }

  public void setDelayFeedback(float fb) {
  }

  public void volume(float volume) {
  }


  public short getSample(short input) {
    float in;
    in = (float) input / 32768;// -1 to 1

    in =  filter.applyFilter(in);
    if (goingUp && currentAmp < targetAmp) {
      currentAmp += dAmp;
    } else if (!goingUp && currentAmp > targetAmp) {
      currentAmp += dAmp;
    }  

    if (currentAmp > 1) {
      currentAmp = 1;
    }
    if (currentAmp < 0) {
      currentAmp = 0;
    }  
    in *= currentAmp;  
    return (short) (in * 32768);
  }

  public void setFilter(float f, float r) {
    filter.setFilter(f, r);
  }
}


/**
 * Represents an audio source is streamed as opposed to being completely loaded (as WavSource is)
 */
public class AudioStreamPlayer {
  /** a class from the android API*/
  private MediaPlayer mediaPlayer;
  /** a class from the android API*/
  private Visualizer viz; 
  private byte[] waveformBuffer;
  private byte[] fftBuffer;
  private byte[] powerSpectrum;

  /**
   * create a stream source from the sent url 
   */
  public AudioStreamPlayer(String url) {
    try {
      mediaPlayer = new MediaPlayer();
      //mp.setAuxEffectSendLevel(1);
      mediaPlayer.setLooping(true);

      // try to parse the URL... if that fails, we assume it
      // is a local file in the assets folder
      try {
        URL uRL = new URL(url);
        mediaPlayer.setDataSource(url);
      }
      catch (MalformedURLException eek) {
        // couldn't parse the url, assume its a local file
        AssetFileDescriptor afd = getAssets().openFd(url);
        //mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        mediaPlayer.setDataSource(afd.getFileDescriptor());
        afd.close();
      }

      mediaPlayer.prepare();
      //mediaPlayer.start();
      //println("Created audio with id "+mediaPlayer.getAudioSessionId());
      viz = new Visualizer(mediaPlayer.getAudioSessionId());
      viz.setEnabled(true);
      waveformBuffer = new byte[viz.getCaptureSize()];
      fftBuffer = new byte[viz.getCaptureSize()/2];
      powerSpectrum = new byte[viz.getCaptureSize()/2];
    }
    catch (Exception e) {
      println("StreamSource could not be initialised. Check url... "+url+ " and that you have added the permission INTERNET, RECORD_AUDIO and MODIFY_AUDIO_SETTINGS to the manifest,");
      e.printStackTrace();
    }
  }

  public void play() {
    mediaPlayer.start();
  }

  public int getLengthMs() {
    return mediaPlayer.getDuration();
  }

  public void cue(float timeMs) {
    if (timeMs >= 0 && timeMs < getLengthMs()) {// ignore crazy values
      mediaPlayer.seekTo((int)timeMs);
    }
  }

  /**
   * Returns a recent snapshot of the power spectrum as 8 bit values
   */
  public byte[] getPowerSpectrum() {
    // calculate the spectrum
    viz.getFft(fftBuffer);
    short real, imag;
    for (int i=2; i<fftBuffer.length; i+=2) {
      real = (short) fftBuffer[i];
      imag = (short) fftBuffer[i+1];
      powerSpectrum[i/2] = (byte) ((real * real)  + (imag * imag));
    }
    return powerSpectrum;
  }

  /**
   * Returns a recent snapshot of the waveform being played 
   */
  public byte[] getWaveForm() {
    // retrieve the waveform
    viz.getWaveForm(waveformBuffer);
    return waveformBuffer;
  }
} 

/**
 * Use this class to retrieve data about the movement of the device
 */
public class Accelerometer implements SensorEventListener {
  private SensorManager sensorManager;
  private Sensor accelerometer;
  private float[] values;

  public Accelerometer() {
    sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    values = new float[3];
  }


  public float[] getValues() {
    return values;
  }

  public float getX() {
    return values[0];
  }

  public float getY() {
    return values[1];
  }

  public float getZ() {
    return values[2];
  }

  /**
   * SensorEventListener interace
   */
  public void onSensorChanged(SensorEvent event) {
    values = event.values;
    //float[] vals = event.values;
    //for (int i=0; i<vals.length;i++){
    //  println(" sensor! "+vals[i]);
    //}
  }

  /**
   * SensorEventListener interace
   */
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }
}

public interface Filter {
  public void setFilter(float f, float r);
  public float applyFilter(float in);
}

/** https://github.com/supercollider/supercollider/blob/master/server/plugins/FilterUGens.cpp */

public class RLPF implements Filter {
  float a0, b1, b2, y1, y2;
  float freq;
  float reson;
  float sampleRate;
  boolean changed;

  public RLPF(float sampleRate_) {
    this.sampleRate = sampleRate_;
    reset();
    this.setFilter(sampleRate / 4, 0.01f);
  }
  private void reset() {
    a0 = 0.f;
    b1 = 0.f;
    b2 = 0.f;
    y1 = 0.f;
    y2 = 0.f;
  }
  /** f is in the range 0-sampleRate/2 */
  public void setFilter(float f, float r) {
    // constrain 
    // limit to 0-1 
    f = constrain(f, 0, sampleRate/4);
    r = constrain(r, 0, 1);
    // invert so high r -> high resonance!
    r = 1-r;
    // remap to appropriate ranges
    f = map(f, 0, sampleRate/4, 30, sampleRate / 4);
    r = map(r, 0, 1, 0.005f, 2);

    // println("rlpf: f "+f+" r "+r);

    this.freq = f * TWO_PI / sampleRate;
    this.reson = r;
    changed = true;
  }

  public float applyFilter(float in) {
    float y0;
    if (changed) {
      float D = tan(freq * reson * 0.5f);
      float C = ((1.f-D)/(1.f+D));
      float cosf = cos(freq);
      b1 = (1.f + C) * cosf;
      b2 = -C;
      a0 = (1.f + C - b1) * .25f;
      changed = false;
    }
    y0 = a0 * in + b1 * y1 + b2 * y2;
    y2 = y1;
    y1 = y0;
    if (Float.isNaN(y0)) {
      reset();
    }
    return y0;
  }
}

/** https://github.com/micknoise/Maximilian/blob/master/maximilian.cpp */

class MickFilter implements Filter {

  private float f, res;
  private float cutoff, z, c, x, y, out;
  private float sampleRate;

  MickFilter(float sampleRate) {
    this.sampleRate = sampleRate;
  }

  public void setFilter(float f, float r) {
    f = constrain(f, 0, 1);
    res = constrain(r, 0, 1);
    f = map(f, 0, 1, 25, sampleRate / 4);
    r = map(r, 0, 1, 1, 25);
    this.f = f;
    this.res = r;    

    //println("mickF: f "+f+" r "+r);
  }
  public float applyFilter(float in) {
    return lores(in, f, res);
  }

  public float lores(float input, float cutoff1, float resonance) {
    //cutoff=cutoff1*0.5;
    //if (cutoff<10) cutoff=10;
    //if (cutoff>(sampleRate*0.5)) cutoff=(sampleRate*0.5);
    //if (resonance<1.) resonance = 1.;

    //if (resonance>2.4) resonance = 2.4;
    z=cos(TWO_PI*cutoff/sampleRate);
    c=2-2*z;
    float r=(sqrt(2.0f)*sqrt(-pow((z-1.0f), 3.0f))+resonance*(z-1))/(resonance*(z-1));
    x=x+(input-y)*c;
    y=y+x;
    x=x*r;
    out=y;
    return out;
  }
}


/*
 * This file is part of Beads. See http://www.beadsproject.net for all information.
 * CREDIT: This class uses portions of code taken from MPEG7AudioEnc. See readme/CREDITS.txt.
 */

/**
 * FFT performs a Fast Fourier Transform and forwards the complex data to any listeners. 
 * The complex data is a float of the form float[2][frameSize], with real and imaginary 
 * parts stored respectively.
 * 
 * @beads.category analysis
 */
public class FFT {

  /** The real part. */
  protected float[] fftReal;

  /** The imaginary part. */
  protected float[] fftImag;

  private float[] dataCopy = null;
  private float[][] features;
  private float[] powers;
  private int numFeatures;

  /**
   * Instantiates a new FFT.
   */
  public FFT() {
    features = new float[2][];
  }

  /* (non-Javadoc)
   * @see com.olliebown.beads.core.UGen#calculateBuffer()
   */
  public float[] process(float[] data, boolean direction) {
    if (powers == null) powers = new float[data.length/2];
    if (dataCopy==null || dataCopy.length!=data.length)
      dataCopy = new float[data.length];
    System.arraycopy(data, 0, dataCopy, 0, data.length);

    fft(dataCopy, dataCopy.length, direction);
    numFeatures = dataCopy.length;
    fftReal = calculateReal(dataCopy, dataCopy.length);
    fftImag = calculateImaginary(dataCopy, dataCopy.length);
    features[0] = fftReal;
    features[1] = fftImag;
    // now calc the powers
    return specToPowers(fftReal, fftImag, powers);
  }

  public float[] specToPowers(float[] real, float[] imag, float[] powers) {
    float re, im;
    double pow;
    for (int i=0; i<powers.length; i++) {
      //real = spectrum[i][j].re();
      //imag = spectrum[i][j].im();
      re = real[i];
      im = imag[i];
      powers[i] = (re*re + im * im);
      powers[i] = (float) Math.sqrt(powers[i]) / 10;
      // convert to dB
      pow = (double) powers[i];
      powers[i] = (float)(10 *  Math.log10(pow * pow)); // (-100 - 100)
      powers[i] = (powers[i] + 100) * 0.005f; // 0-1
    }
    return powers;
  }

  /**
   * The frequency corresponding to a specific bin 
   * 
   * @param samplingFrequency The Sampling Frequency of the AudioContext
   * @param blockSize The size of the block analysed
   * @param binNumber 
   */
  public  float binFrequency(float samplingFrequency, int blockSize, float binNumber)
  {    
    return binNumber*samplingFrequency/blockSize;
  }

  /**
   * Returns the average bin number corresponding to a particular frequency.
   * Note: This function returns a float. Take the Math.round() of the returned value to get an integral bin number. 
   * 
   * @param samplingFrequency The Sampling Frequency of the AudioContext
   * @param blockSize The size of the fft block
   * @param freq  The frequency
   */

  public  float binNumber(float samplingFrequency, int blockSize, float freq)
  {
    return blockSize*freq/samplingFrequency;
  }

  /** The nyquist frequency for this samplingFrequency 
   * 
   * @params samplingFrequency the sample
   */
  public  float nyquist(float samplingFrequency)
  {
    return samplingFrequency/2;
  }

  /*
   * All of the code below this line is taken from Holger Crysandt's MPEG7AudioEnc project.
   * See http://mpeg7audioenc.sourceforge.net/copyright.html for license and copyright.
   */

  /**
   * Gets the real part from the complex spectrum.
   * 
   * @param spectrum
   *            complex spectrum.
   * @param length 
   *       length of data to use.
   * 
   * @return real part of given length of complex spectrum.
   */
  protected  float[] calculateReal(float[] spectrum, int length) {
    float[] real = new float[length];
    real[0] = spectrum[0];
    real[real.length/2] = spectrum[1];
    for (int i=1, j=real.length-1; i<j; ++i, --j)
      real[j] = real[i] = spectrum[2*i];
    return real;
  }

  /**
   * Gets the imaginary part from the complex spectrum.
   * 
   * @param spectrum
   *            complex spectrum.
   * @param length 
   *       length of data to use.
   * 
   * @return imaginary part of given length of complex spectrum.
   */
  protected  float[] calculateImaginary(float[] spectrum, int length) {
    float[] imag = new float[length];
    for (int i=1, j=imag.length-1; i<j; ++i, --j)
      imag[i] = -(imag[j] = spectrum[2*i+1]);
    return imag;
  }

  /**
   * Perform FFT on data with given length, regular or inverse.
   * 
   * @param data the data
   * @param n the length
   * @param isign true for regular, false for inverse.
   */
  protected  void fft(float[] data, int n, boolean isign) {
    float c1 = 0.5f; 
    float c2, h1r, h1i, h2r, h2i;
    double wr, wi, wpr, wpi, wtemp;
    double theta = 3.141592653589793f/(n>>1);
    if (isign) {
      c2 = -.5f;
      four1(data, n>>1, true);
    } else {
      c2 = .5f;
      theta = -theta;
    }
    wtemp = Math.sin(.5f*theta);
    wpr = -2.f*wtemp*wtemp;
    wpi = Math.sin(theta);
    wr = 1.f + wpr;
    wi = wpi;
    int np3 = n + 3;
    for (int i=2, imax = n >> 2, i1, i2, i3, i4; i <= imax; ++i) {
      /** @TODO this can be optimized */
      i4 = 1 + (i3 = np3 - (i2 = 1 + (i1 = i + i - 1)));
      --i4; 
      --i2; 
      --i3; 
      --i1; 
      h1i =  c1*(data[i2] - data[i4]);
      h2r = -c2*(data[i2] + data[i4]);
      h1r =  c1*(data[i1] + data[i3]);
      h2i =  c2*(data[i1] - data[i3]);
      data[i1] = (float) ( h1r + wr*h2r - wi*h2i);
      data[i2] = (float) ( h1i + wr*h2i + wi*h2r);
      data[i3] = (float) ( h1r - wr*h2r + wi*h2i);
      data[i4] = (float) (-h1i + wr*h2i + wi*h2r);
      wr = (wtemp=wr)*wpr - wi*wpi + wr;
      wi = wi*wpr + wtemp*wpi + wi;
    }
    if (isign) {
      float tmp = data[0]; 
      data[0] += data[1];
      data[1] = tmp - data[1];
    } else {
      float tmp = data[0];
      data[0] = c1 * (tmp + data[1]);
      data[1] = c1 * (tmp - data[1]);
      four1(data, n>>1, false);
    }
  }

  /**
   * four1 algorithm.
   * 
   * @param data
   *            the data.
   * @param nn
   *            the nn.
   * @param isign
   *            regular or inverse.
   */
  private  void four1(float data[], int nn, boolean isign) {
    int n, mmax, istep;
    double wtemp, wr, wpr, wpi, wi, theta;
    float tempr, tempi;

    n = nn << 1;        
    for (int i = 1, j = 1; i < n; i += 2) {
      if (j > i) {
        // SWAP(data[j], data[i]);
        float swap = data[j-1];
        data[j-1] = data[i-1];
        data[i-1] = swap;
        // SWAP(data[j+1], data[i+1]);
        swap = data[j];
        data[j] = data[i]; 
        data[i] = swap;
      }      
      int m = n >> 1;
      while (m >= 2 && j > m) {
        j -= m;
        m >>= 1;
      }
      j += m;
    }
    mmax = 2;
    while (n > mmax) {
      istep = mmax << 1;
      theta = 6.28318530717959f / mmax;
      if (!isign)
        theta = -theta;
      wtemp = Math.sin(0.5f * theta);
      wpr = -2.0f * wtemp * wtemp;
      wpi = Math.sin(theta);
      wr = 1.0f;
      wi = 0.0f;
      for (int m = 1; m < mmax; m += 2) {
        for (int i = m; i <= n; i += istep) {
          int j = i + mmax;
          tempr = (float) (wr * data[j-1] - wi * data[j]);  
          tempi = (float) (wr * data[j]   + wi * data[j-1]);  
          data[j-1] = data[i-1] - tempr;
          data[j]   = data[i] - tempi;
          data[i-1] += tempr;
          data[i]   += tempi;
        }
        wr = (wtemp = wr) * wpr - wi * wpi + wr;
        wi = wi * wpr + wtemp * wpi + wi;
      }
      mmax = istep;
    }
  }
}

class coin
{
  PImage [] coinimg;

  float coinx;
  float coiny;
  int k = 0;
  boolean coindestroyed = false;
  int coinsize = (height+width)/50;

  coin(float coinX, float coinY)
  {
    coinx = coinX;
    coiny = coinY;

    coinimg = new PImage[4];

    for (int o = 0; o<coinimg.length; o++)
    {
      coinimg[o] = loadImage ("coin0"+o+".png");
      coinimg[o].resize (0, coinsize);
    }
  }
  public void run()
  {
    if (posx > coinx - width && posx < coinx + width)
    {
      if (!coindestroyed)
      {
        spin();
        playercheck();
      }
    }
  }

  //makes coin run through 3 images
  public void spin()
  {
    image(coinimg[k], coinx, coiny);
    k++;
    if (k == coinimg.length)
      k = 0;
  }

  //checks if the player position is the same as the coin
  public void playercheck()
  {
    if (posx < coinx + coinimg[0].width && posx +chan.jumpup1.width  > coinx && posy < coiny+coinimg[0].height && posy +chan.jumpup1.height> coiny)
    {
      score+= 10;
      coinget.play();
      coindestroyed = true;
    }
  }
}

class eni
{
  PImage [] enemyimg;

  float eniX, eniY;
  int d;
  int enemysize = (height+width)/45;

  eni(float eX, float eY)
  {
    eniX = eX;
    eniY = eY;

    enemyimg = new PImage[3];

    for (int o = 0; o<enemyimg.length; o++)
    {
      enemyimg[o] = loadImage ("eni0"+o+".png");
      enemyimg[o].resize(0, enemysize);
    }
  }

  public void run()
  {
    if (posx > eniX - width && posx < eniX + width)
    {
      breath();
      playercheck();
    }
  }

  public void breath()
  {
    image(enemyimg[d], eniX, eniY);
    d++;
    if (d == enemyimg.length)
      d = 0;
  }

  public void playercheck()
  {
    if (posx < eniX + enemyimg[0].width && posx +chan.jumpup1.width  > eniX && posy < eniY+enemyimg[0].height && posy +chan.jumpup1.height > eniY)
    {
      gameover = true;
      gameoverV = PApplet.parseInt(random(3));
      if (gameoverV ==0)
        voiceBooom.play();
      if (gameoverV ==1)
        voicegameover1.play();
      if (gameoverV ==2)
        voicegameover2.play();
    }
  }
}

public void gameoverscreen()
{
  if (gameover)
  {
    if (music)
    {
      stagemusic.stop();
      startmusic.stop();
      gameovermusic.play();
    }
    
    if (score > highscore)
      highscore= score;

    //fading background
    fill(0, 1.5f);
    rect(0-transl8, 0, width, height);

    fill (200, 70, 90);
    text("game over", width*0.5f-transl8, height*0.2f);
    text("score:"+score, width*0.5f-transl8, height*0.3f);
    text("highscore: "+highscore, width*0.5f-transl8, height*0.4f);

    //retry button
    rect ((width*0.5f)-(width*0.3f*0.5f)-transl8, height*0.65f, width*0.3f, height*0.1f);
    fill(0);
    text ("RETRY", width*0.5f-transl8, height*0.7f);

    if (mouseX > width*0.35f && mouseX < width*0.65f && mouseY > height*0.65f && mouseY < height*0.75f)
      if (mousePressed)
      {
        reset();
      }
  }
}

//resets game
public void reset()
{
  gameover = false;
  paused = false;
  start = false;
  transl8= 0;
  chan = new Sprite(width*0.3f, 200);
  score = 0;
  gameovermusic.stop();

  for (int h = 0; h <coinCollection.length; h++)
    coinCollection[h] = new coin (random(width, width*100), random(height-BG2.height, ground));

  for (int m = 0; m <enemyCollection.length; m++)
    enemyCollection[m] = new eni (random(width, width*100), random(height-BG2.height, ground));
}

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
  int maxjumpower = 150;
  float jumpower = 0;
  float gravity = 6;
  float speedy = 0;
  boolean falling = false, jumping = false;

  //variables for the sprite animations
  int s = 1;
  int r = 1;
  int jumpup = 1;
  int down = 1;
  int jumpvoice = 0;

  AudioPlayer voiceJump;
  AudioPlayer voiceJump2;


  Sprite(float startX, float startY)
  {

    voiceJump = maxim.loadFile("uni14951.wav");
    voiceJump.setLooping(false);
    voiceJump2 = maxim.loadFile("univ0001.wav");
    voiceJump2.setLooping(false);
   
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

    stand1.resize(0, spriteheight);
    stand2.resize(0, spriteheight);
    stand3.resize(0, spriteheight);
    run1.resize(0, spriteheight);
    run2.resize(0, spriteheight);
    run3.resize(0, spriteheight);
    run4.resize(0, spriteheight);
    run5.resize(0, spriteheight);
    run6.resize(0, spriteheight);
    run7.resize(0, spriteheight);
    jumpup1.resize(0, spriteheight);
    jumpup2.resize(0, spriteheight);
    jumptop1.resize(0, spriteheight);
    jumptop2.resize(0, spriteheight);
    jumptop3.resize(0, spriteheight);
    jumpdown1.resize(0, spriteheight);
    jumpdown2.resize(0, spriteheight);
  }

  public void run()
  {

    onscreen();

    grav(); 

    jumpbar();

    if (start || startmenuscroll)
    {
      posx+= speed; 
      state = 2;
    } else
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

  public void grav()
  {
    //calculates gravity
    //if above ground level it will lower speed y
    //if at ground it will make it 0
    if (posy+(stand1.height)/2 < ground)
    {
      speedy+= gravity;
    }
    posy+=speedy;
    if (posy+(stand1.height)/2 >= ground)
    {
      speedy = 0;
    }

    if (speedy > 0)
    {
      falling = true;
      jumping = false;
      fall();
    } else
      falling = false;

    if (speedy < 0)
    {
      jumpinganimation();
      jumping = true;
    }
  }

  public void fall() 
  {
    if (down == 1)
      image (jumpdown1, posx, posy);
    if (down ==2)
      image (jumpdown2, posx, posy);
    down++;
    if (down ==3)
      down = 1;
  }

  public void jump ()
  {
    //needs to be updated so she can jump off things other than the ground
    if (posy+(stand1.height)/2 >= ground)
      speedy -= jumpower;
    if (falling == false)
    {
      jumpinganimation();

      if (!voiceJump.isPlaying() && !voiceJump.isPlaying() && !voiceJump.isPlaying())
      {
        jumpvoice = PApplet.parseInt(random(2));
        if (jumpvoice == 0)
          voiceJump2.play();
        if (jumpvoice == 1)
          voiceJump.play();
    
      }
    }
  }

  public void jumpinganimation()
  {
    if (jumpup ==1)
      image (jumpup1, posx, posy);
    if (jumpup==2)
      image (jumpup2, posx, posy);
    jumpup++;
    if (jumpup ==3)
      jumpup =1;
  }

  public void stand()
  {
    if (falling == false && jumping == false)
    {
      if (s ==1)
        image (stand1, posx, posy);
      if (s ==2)
        image (stand2, posx, posy);
      if (s ==3)
      {
        image (stand3, posx, posy);
      } 
      s= s+1;
      if (s==4)
        s=1;
    }
  }

  public void right()
  {
    //posx+= speed ;
    if (falling == false && jumping == false)
    {
      if (r ==1)
      {
        image (run1, posx, posy);
      }
      if (r ==2)
      {
        image (run2, posx, posy);
      }
      if (r ==3)
      {
        image (run3, posx, posy);
      }
      if (r ==4)
      {
        image (run4, posx, posy);
      }
      if (r ==5)
      {
        image (run5, posx, posy);
      }
      if (r ==6)
      {
        image (run6, posx, posy);
      }
      if (r ==7)
      {
        image (run7, posx, posy);
      }
      r++;
      if (r==8)
        r=1;
    }
  }

  public void left()
  {
    posx-= speed ;
    if (falling == false && jumping == false)
    {
      //sprites are being mirrored. code taken from https://processing.org/discourse/beta/num_1242102535.html
      pushMatrix();
      //flip across x axis
      scale(-1, 1);
      //The x position is negative because we flipped
      //restore previous translation,etc

      if (r ==1)
      {
        image (run1, -posx, posy);
      }
      if (r ==2)
      {
        image (run2, -posx, posy);
      }
      if (r ==3)
      {
        image (run3, -posx, posy);
      }
      if (r ==4)
      {
        image (run4, -posx, posy);
      }
      if (r ==5)
      {
        image (run5, -posx, posy);
      }
      if (r ==6)
      {
        image (run6, -posx, posy);
      }
      if (r ==7)
      {
        image (run7, -posx, posy);
      }

      popMatrix(); 

      r++;
      if (r==8)
        r=1;
    }
  }

  //checks if the player is in the bounderies of the screen 
  public void onscreen()
  {
    if (posx < 0-transl8)
      posx = 0-transl8;

    if (posx >width-transl8)
      posx = width-transl8;

    if (posy < 0)
    {
      posy = 0;
      speedy=0;
    }

    if (posy + (stand1.height)/2 > ground)
      posy =ground - (stand1.height)/2;
  }

  public void touchcontrols()
  {
    if (start)
    {
      state = 2;
      if (mousePressed == true) 
      {
        if (jumpower < maxjumpower)
        {
          jumpower+= 10;
          if (jumpower >maxjumpower) 
          {
            jumpower=maxjumpower;
          }
        }
      } else 
        if (!mousePressed && jumpower > 0)
      {
        state = 4;
        jumpower -= 6;
        if (jumpower < 0)
          jumpower = 0;
      }
    }
  }

  public void jumpbar()
  {
    if (start)
    {
      fill(0);
      rect(width*0.1f-transl8, height*0.85f, width*0.8f, height*0.1f);
      fill (200, 70, 90);
      rect(width*0.11f-transl8, height*0.86f, (width*0.78f)*jumpower/maxjumpower, height*0.08f);
      fill(50);
      textSize(height*0.05f);
      text("JUMP POWER", width*0.5f-transl8, height*0.9f);
      textSize(50);
    }
  }
}

public void startmenu()
{
  if (!start)
    if (music)
      startmusic.play();
    else
      startmusic.stop();

  //background
  image (BG1, (width*0.5f)-transl8, height*0.5f);

  //floor for the startmenu, settings and credits
  for (int i = 8; i >-18; i--)
    image (BG2, BG2.width*i+width, height-(BG2.height*0.5f));


  //logo
  image (logo, width*0.5f, height*0.18f);

  fill (200, 70, 90);
  rect ((width*0.5f)-(width*0.3f*0.5f), height*0.35f, width*0.3f, height*0.1f);
  rect ((width*0.5f)-(width*0.3f*0.5f), height*0.5f, width*0.3f, height*0.1f);
  rect ((width*0.5f)-(width*0.3f*0.5f), height*0.65f, width*0.3f, height*0.1f);
  startmenubuttons();
  fill (0);
  text ("Start", width*0.5f, height*0.4f);
  text ("Settings", width*0.5f, height*0.55f);
  text ("Credits", width*0.5f, height*0.7f);

  if (startmenuscroll)
    if (transl8 >0)
      transl8 -= scrollspeed;
    else 
      startmenuscroll = false;
}

public void startmenubuttons()
{
  fill(100, 35, 45);
  if (mouseX-transl8 > width*0.35f && mouseX-transl8 < width*0.65f && mouseY > height*0.35f && mouseY < height*0.45f)
    if (mousePressed)
    {
      {
        start = true;
        voiceStart.play();
        settingscroll = false;
        creditscroll = false;
        startmenuscroll = false;
      }
    }

  if (mouseX-transl8 > width*0.35f && mouseX-transl8 < width*0.65f && mouseY > height*0.5f && mouseY < height*0.6f)
    if (mousePressed)
    {
      {
        button.play();
        settingscroll = true;
        creditscroll = false;
        startmenuscroll = false;
      }
    }

  if (mouseX-transl8 > width*0.35f && mouseX-transl8 < width*0.65f && mouseY > height*0.65f && mouseY < height*0.75f)
    if (mousePressed)
    {
      {
        button.play();
        creditscroll = true;
        settingscroll = false;
        startmenuscroll = false;
      }
    }
}


  public int sketchWidth() { return displayWidth; }
  public int sketchHeight() { return displayHeight; }
}
