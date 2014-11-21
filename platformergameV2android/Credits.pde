
void credits()
{
if (creditscroll)
  if (transl8 < 2*width)
 transl8 += scrollspeed;
 else
 creditscroll=false;
  
  fill (200,70,90);
    text("game made by GP3", width*0.5-(2*width),(height*0.1));
  text("Digital Media CW2", width*0.5-(2*width),(height*0.2));
  text("assests from unity-chan", width*0.5-(2*width),(height*0.3));
  
  image (unitychanlicense, width*0.5-(width*2),height*0.5);
  
 rect ((width*0.5)-(width*0.3*0.5)-2*width, height*0.65, width*0.3, height*0.1);
 
//button control
if(mouseX-transl8 > width*0.35 -2*width && mouseX-transl8 < width*0.65 -2*width && mouseY > height*0.65 && mouseY < height*0.75)
{fill(100,35,45);
  rect ((width*0.5)-(width*0.3*0.5)-2*width, height*0.65, width*0.3, height*0.1);
if (mousePressed)
{startmenuscroll = true;
creditscroll = false;
settingscroll = false;
}
}

fill(0);
text ("Back",width*0.5-2*width, height*0.7);
  
}
