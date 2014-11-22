void startmenu()
{
  if (!start)
    if (music)
      startmusic.play();
    else
      startmusic.stop();

  //background
  image (BG1, (width*0.5)-transl8, height*0.5);

  //floor for the startmenu, settings and credits
  for (int i = 8; i >-18; i--)
    image (BG2, BG2.width*i+width, height-(BG2.height*0.5));


  //logo
  image (unitychanlogo, width*0.5, height*0.18);

  fill (200, 70, 90);
  rect ((width*0.5)-(width*0.3*0.5), height*0.35, width*0.3, height*0.1);
  rect ((width*0.5)-(width*0.3*0.5), height*0.5, width*0.3, height*0.1);
  rect ((width*0.5)-(width*0.3*0.5), height*0.65, width*0.3, height*0.1);
  startmenubuttons();
  fill (0);
  text ("Start", width*0.5, height*0.4);
  text ("Settings", width*0.5, height*0.55);
  text ("Credits", width*0.5, height*0.7);

  if (startmenuscroll)
    if (transl8 >0)
      transl8 -= scrollspeed;
    else 
      startmenuscroll = false;
}

void startmenubuttons()
{
  fill(100, 35, 45);
  if (mouseX-transl8 > width*0.35 && mouseX-transl8 < width*0.65 && mouseY > height*0.35 && mouseY < height*0.45)
    if (mousePressed)
    {
      {
        start = true;
        voiceStart.play();
      }
    }

  if (mouseX-transl8 > width*0.35 && mouseX-transl8 < width*0.65 && mouseY > height*0.5 && mouseY < height*0.6)
    if (mousePressed)
    {
      {
        settingscroll = true;
        creditscroll = false;
        startmenuscroll = false;
      }
    }

  if (mouseX-transl8 > width*0.35 && mouseX-transl8 < width*0.65 && mouseY > height*0.65 && mouseY < height*0.75)
    if (mousePressed)
    {
      {
        creditscroll = true;
        settingscroll = false;
        startmenuscroll = false;
      }
    }
}

