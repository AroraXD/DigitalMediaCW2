void settings()
{
  if (settingscroll)
  {
    if (transl8 <width)
      transl8 += scrollspeed;
    else 
      settingscroll = false;
  }


  fill (200, 70, 90);
  rect ((width*0.5)-(width*0.3*0.5)-width, height*0.35, width*0.3, height*0.1);
  rect ((width*0.5)-(width*0.3*0.5)-width, height*0.5, width*0.3, height*0.1);
  rect ((width*0.5)-(width*0.3*0.5)-width, height*0.65, width*0.3, height*0.1);

  //button controls
  fill(100, 35, 45);
  if (mouseX-transl8 > width*0.35-width && mouseX-transl8 < width*0.65-width && mouseY > height*0.35 && mouseY < height*0.45)
    if (mousePressed)
    {
      music = !music;
      button.play();
    }

  if (mouseX-transl8 > width*0.35-width && mouseX-transl8 < width*0.65-width && mouseY > height*0.5 && mouseY < height*0.6)
    if (mousePressed)
    {
      gamecontrol = !gamecontrol;
    }

  if (mouseX-transl8 > width*0.35-width && mouseX-transl8 < width*0.65-width && mouseY > height*0.65 && mouseY < height*0.75)
    if (mousePressed)
    {
      {
        button.play();
        creditscroll = false;
        settingscroll = false;
        startmenuscroll = true;
      }
    }

  if (music)
    fill(59, 191, 4);
  else
    fill(0);
  text ("Music", width*0.5-width, height*0.4);

  fill(0);
  text ("Controls", width*0.5-width, height*0.55);
  text ("Back", width*0.5-width, height*0.7);

  gamecontrols();
}

void gamecontrols()
{
  if (gamecontrol == true)
  {
    fill (200, 70, 90);
    rect ((width*0.1)-width, (height*0.1), width*0.8, height*0.65);
    fill(0);
    text("game controls", width*0.5-width, height*0.15);
    textSize(30);
    text("Hold down to fill jump bar", width*0.5-width, height*0.2);
    text("release to jump", width*0.5-width, height*0.3);
    text("press pause to pause the game", width*0.5-width, height*0.4);
    text("collect coins to increase your score", width*0.5-width, height*0.5);
    text("avoid the pink enemies", width*0.5-width, height*0.6);
    text("gl hf", width*0.5-width, height*0.7);
    textSize(50);

    if (mousePressed)
      gamecontrol = false;
  }
}

