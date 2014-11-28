void play()
{
  if (music)
    stagemusic.play();
  startmusic.stop();

  //background
  image (BG1, (width*0.5)-transl8, height*0.5);

  for (int j = -3; j < 3* (posx/width); j++)
    image (BG2, BG2.width*j+width, height-(BG2.height*0.5));

  transl8 -=scrollspeed;

  for (int c =0; c < coinCollection.length; c++)
    coinCollection[c].run();

  for (int c =0; c < enemyCollection.length; c++)
    enemyCollection[c].run();
}
