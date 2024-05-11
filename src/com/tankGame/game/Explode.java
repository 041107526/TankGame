package com.tankGame.game;

import com.tankGame.util.MyUtil;

import java.awt.*;

/**
 * Explode class
 */
public class Explode {
    public static final int EXPLODE_FRAME_COUNT = 12;
    //导入资源
    private static Image[] img;
    //爆炸效果的图片的宽度和高度
    private static int explodeWidth;
    private static int explodeHeight;
    static {
        img = new Image[EXPLODE_FRAME_COUNT/3];
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.createImage("res/boom_"+i+".png");
        }
    }


    private int x,y;
    //Subscript of the currently playing frame [0-11]
    private int index;
    //
    private boolean visible = true;

    public Explode() {
        index = 0;
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        index = 0;
    }

    public void draw(Graphics g){
        //Determination of the width and height of the explosion effect image
        if(explodeHeight <= 0){
            explodeHeight = img[0].getHeight(null);
            explodeWidth = img[0].getWidth(null)>>1;
        }
        if(!visible)return;
        g.drawImage(img[index/3] , x-explodeWidth ,y-explodeHeight ,null);
        index ++;
        //After playing the last frame, set to invisible
        if(index >= EXPLODE_FRAME_COUNT ){
            visible = false;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Explode{" +
                "x=" + x +
                ", y=" + y +
                ", index=" + index +
                ", visible=" + visible +
                '}';
    }
}
