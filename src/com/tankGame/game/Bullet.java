package com.tankGame.game;

import com.tankGame.tank.Tank;
import com.tankGame.util.Constant;

import java.awt.*;

/**
 * Bullet class
 */
public class Bullet {
    //The default speed of bullets is twice the speed of tanks
    public static final int DEFAULT_SPEED = Tank.DEFAULT_SPEED << 1;
    //radius of the bullet
    public static final int RADIUS = 5;

    private int x,y;
    private int speed = DEFAULT_SPEED;
    private int dir;
    private int atk;
    private Color color;
    //子弹是否可见
    private boolean visible = true;

    public Bullet(int x, int y, int dir, int atk,Color color) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.atk = atk;
        this.color = color;
    }

    /**
     * This constructor for object pools, all properties are defaults
     */
    public Bullet() {
    }

    /**
     * Method of drawing bullets
     * @param g
     */
    public void draw(Graphics g){
        if(!visible)return;

        logic();
        g.setColor(color);
        g.fillOval(x-RADIUS,y-RADIUS,RADIUS<<1,RADIUS<<1);
    }

    /**
     * The move logic of bullets
     */
    private void logic(){
        move();
    }

    /**
     * This method is used for bullets' move depending on tank's direction.
     */
    private void move(){
        switch (dir){
            case Tank.DIR_UP:
                y -= speed;
                if(y <= 0){
                    visible = false;
                }
                break;
            case Tank.DIR_DOWN:
                y += speed;
                if(y > Constant.FRAME_HEIGHT){
                    visible = false;
                }
                break;
            case Tank.DIR_LEFT:
                x -= speed;
                if(x < 0){
                    visible = false;
                }
                break;
            case Tank.DIR_RIGHT:
                x += speed;
                if(x > Constant.FRAME_WIDTH){
                    visible = false;
                }
                break;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
