void gameoverscreen()
{
  if (gameover)
  {
    stagemusic.stop();
    startmusic.stop();

    //fading background
    fill(0, 1.5);
    rect(0-transl8, 0, width, height);

    fill (200, 70, 90);
    text("game over", width*0.5-transl8, height*0.2);
    text("score:"+score, width*0.5-transl8, height*0.3);

    //retry button
    rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.35, width*0.3, height*0.1);
    fill(0);
    text ("TRY AGAIN?", width*0.5-transl8, height*0.4);

    if (mouseX > width*0.35 && mouseX < width*0.65 && mouseY > height*0.35 && mouseY < height*0.45)
      if (mousePressed)
      {
        gameover = false;
        paused = false;
        start = false;
        transl8= 0;
        chan = new Sprite(width*0.3, 200);
        score = 0;
      }
  }
}

