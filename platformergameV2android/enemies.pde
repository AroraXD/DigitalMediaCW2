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
    breath();
    playercheck();
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
    if (posx < eniX + enemyimg[0].width && posx +12  > eniX && posy - 20 < eniY+enemyimg[0].height && posy > eniY)
    {
      gameover = true;
      voiceBooom.play();
    }
  }
}

