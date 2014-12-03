void gameoverscreen()
{
  if (gameover)
  {
    stagemusic.stop();
    startmusic.stop();
    gameovermusic.play();

    if (score > highscore)
      highscore= score;

    //fading background
    fill(0, 1.5);
    rect(0-transl8, 0, width, height);

    fill (200, 70, 90);
    text("game over", width*0.5-transl8, height*0.2);
    text("score:"+score, width*0.5-transl8, height*0.3);
    text("highscore: "+highscore, width*0.5-transl8, height*0.4);

    //retry button
    rect ((width*0.5)-(width*0.3*0.5)-transl8, height*0.65, width*0.3, height*0.1);
    fill(0);
    text ("RETRY", width*0.5-transl8, height*0.7);

    if (mouseX > width*0.35 && mouseX < width*0.65 && mouseY > height*0.65 && mouseY < height*0.75)
      if (mousePressed)
      {
        //resets game
        gameover = false;
        paused = false;
        start = false;
        transl8= 0;
        chan = new Sprite(width*0.3, 200);
        score = 0;
        gameovermusic.stop();

        for (int h = 0; h <coinCollection.length; h++)
          coinCollection[h] = new coin (random(width, width*100), random(height-BG2.height, ground));

        for (int m = 0; m <enemyCollection.length; m++)
          enemyCollection[m] = new eni (random(width, width*100), random(height-BG2.height, ground));
      }
  }
}

