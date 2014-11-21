void play()
{
  if (music)
  stagemusic.play();
  startmusic.stop();
  
  for(int j = 0; j < 10; j++)
image (BG2,BG2.width*j+width,height-(BG2.height*0.5));

transl8 -=scrollspeed;

for(int c =0; c < coinCollection.length; c++)
coinCollection[c].run();

}
