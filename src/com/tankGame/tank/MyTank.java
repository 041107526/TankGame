package com.tankGame.tank;

import com.tankGame.util.MyUtil;

import java.awt.*;

/**
 * class player tank
 */
public class MyTank extends Tank{

    //Array of pictures of tanks
    private static Image[] tankImg;

    //It is initialised in the static code block
    static{
        tankImg = new Image[4];
        for (int i = 0; i <tankImg.length ; i++) {
            tankImg[i] = MyUtil.createImage("res/tank1_"+i+".png");
        }
    }

    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
    }

    @Override
    public void drawImgTank(Graphics g) {
        g.drawImage(tankImg[getDir()],getX()-RADIUS,getY()-RADIUS,null);
    }

}
