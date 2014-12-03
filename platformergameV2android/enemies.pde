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

  void run()
  {
    if (posx > eniX - width && posx < eniX + width)
    {
      breath();
      playercheck();
    }
  }

  void breath()
  {
    image(enemyimg[d], eniX, eniY);
    d++;
    if (d == enemyimg.length)
      d = 0;
  }

  void playercheck()
  {
    if (posx < eniX + enemyimg[0].width && posx +chan.jumpup1.width  > eniX && posy < eniY+enemyimg[0].height && posy +chan.jumpup1.height > eniY)
    {
      gameover = true;
      gameoverV = int(random(3));
      if (gameoverV ==0)
        voiceBooom.play();
      if (gameoverV ==1)
        voicegameover1.play();
      if (gameoverV ==2)
        voicegameover2.play();
    }
  }
}

