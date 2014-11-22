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
  void run()
  {
    if (!coindestroyed)
    {
      spin();
      playercheck();
    }
  }

  //makes coin run through 3 images
  void spin()
  {
    image(coinimg[k], coinx, coiny);
    k++;
    if (k == coinimg.length)
      k = 0;
  }

  //checks if the player position is the same as the coin
  void playercheck()
  {
    if (posx < coinx + coinimg[0].width && posx +12  > coinx && posy - 20 < coiny+coinimg[0].height && posy > coiny)
    {

      score+= 10;
      coinget.play();
      coindestroyed = true;
    }
  }
}

