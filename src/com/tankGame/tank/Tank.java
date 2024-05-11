package com.tankGame.tank;

import com.tankGame.game.Bullet;
import com.tankGame.game.Explode;
import com.tankGame.game.GameFrame;
import com.tankGame.map.MapTile;
import com.tankGame.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class Tank
 */
public abstract class Tank {
    //Four directions
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;
    //半径
    public static final int RADIUS = 20;
    //Default speed 30ms per frame
    public static final int DEFAULT_SPEED = 4;
    //Status of tanks
    public static final int STATE_STAND = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_DIE = 2;
    //Initial life of the tank
    public static final int DEFAULT_HP = 100;
    private int maxHP = DEFAULT_HP;

    private int x,y;

    private int hp = DEFAULT_HP;
    private String name;
    private int atk;
    public static final int ATK_MAX = 25;
    public static final int ATK_MIN = 15;
    private int speed = DEFAULT_SPEED;
    private int dir;
    private int state = STATE_STAND;
    private Color color;
    private boolean isEnemy = false;

    private BloodBar bar = new BloodBar();

    //the container of tanks' bullets
    private List<Bullet> bullets = new ArrayList();
    //Use the container to save all the effects of the explosion on the current tank.
    private List<Explode> explodes = new ArrayList<>();

    public Tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        initTank();
    }

    public Tank(){
        initTank();
    }

    private void initTank(){
        color = MyUtil.getRandomColor();
        name = MyUtil.getRandomName();
        atk = MyUtil.getRandomNumber(ATK_MIN,ATK_MAX);
    }
    /**
     * draw tank
     * @param g brushes
     */
    public void draw(Graphics g){
        logic();

        drawImgTank(g);

        drawBullets(g);

        drawName(g);

        bar.draw(g);
    }

    private void drawName(Graphics g){
        g.setColor(color);
        g.setFont(Constant.SMALL_FONT);
        g.drawString(name, x - RADIUS ,y - 35);
    }

    /**
     * Use a picture to draw the tank
     * @param g
     */
    public abstract void drawImgTank(Graphics g);

    /**
     * Use the systematic way to draw tanks
     * @param g
     */
    private void drawTank(Graphics g){
        g.setColor(color);
        g.fillOval(x-RADIUS,y-RADIUS,RADIUS<<1,RADIUS<<1);
        int endX = x;
        int endY = y;
        switch(dir){
            case DIR_UP:
                endY = y - RADIUS*2;
                g.drawLine(x-1,y,endX-1,endY);
                g.drawLine(x+1,y,endX+1,endY);
                break;
            case DIR_DOWN:
                endY = y + RADIUS*2;
                g.drawLine(x-1,y,endX-1,endY);
                g.drawLine(x+1,y,endX+1,endY);
                break;
            case DIR_LEFT:
                endX = x - 2 * RADIUS;
                g.drawLine(x,y-1,endX,endY-1);
                g.drawLine(x,y+1,endX,endY+1);
                break;
            case DIR_RIGHT:
                endX = x + 2 * RADIUS;
                g.drawLine(x,y-1,endX,endY-1);
                g.drawLine(x,y+1,endX,endY+1);
                break;
        }
        g.drawLine(x,y,endX,endY);
    }

    //Logical handling of tanks
    private void logic(){
        switch(state){
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                break;
            case STATE_DIE:
                break;
        }
    }


    private int oldX = -1, oldY = -1;
    //Function of tank movement
    private void move(){
        oldX = x;
        oldY = y;
        switch (dir){
            case DIR_UP:
                y -= speed;
                if(y < RADIUS + GameFrame.titleBarH){
                    y = RADIUS + GameFrame.titleBarH;
                }
                break;
            case DIR_DOWN:
                y += speed;
                if(y > Constant.FRAME_HEIGHT-RADIUS){
                    y = Constant.FRAME_HEIGHT-RADIUS;
                }
                break;
            case DIR_LEFT:
                x -= speed;
                if(x < RADIUS){
                    x = RADIUS;
                }
                break;
            case DIR_RIGHT:
                x += speed;
                if(x > Constant.FRAME_WIDTH-RADIUS){
                    x = Constant.FRAME_WIDTH-RADIUS;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List getBullets() {
        return bullets;
    }

    public void setBullets(List bullets) {
        this.bullets = bullets;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tank{" +
                "x=" + x +
                ", y=" + y +
                ", hp=" + hp +
                ", atk=" + atk +
                ", speed=" + speed +
                ", dir=" + dir +
                ", state=" + state +
                '}';
    }

    //Time of last fire
    private long fireTime;
    //Minimum intervals between bullet launches
    public static final int FIRE_INTERVAL = 200;

    /**
     * Fire functions of the tank
     * A bullet object is created, the attribute information
     * of the bullet object is obtained through the information of the tank,
     * and then the created bullet is added to the container managed by the tank
     */
    public void fire(){
        if(System.currentTimeMillis() - fireTime >FIRE_INTERVAL) {
            int bulletX = x;
            int bulletY = y;
            switch (dir) {
                case DIR_UP:
                    bulletY -= RADIUS;
                    break;
                case DIR_DOWN:
                    bulletY += RADIUS;
                    break;
                case DIR_LEFT:
                    bulletX -= RADIUS;
                    break;
                case DIR_RIGHT:
                    bulletX += RADIUS;
                    break;
            }
            //Getting Bullet Objects from the Object Pool
            Bullet bullet = BulletsPool.get();
            //Setting Bullet Properties
            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setDir(dir);
            bullet.setAtk(atk);
            bullet.setColor(color);
            bullet.setVisible(true);
            bullets.add(bullet);

            //After firing a bullet, the time of this firing is recorded
            fireTime = System.currentTimeMillis();

        }
    }

    /**
     * Draw all the bullets fired by the current tank.
     * @param g brushes
     */
    private void drawBullets(Graphics g){
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.draw(g);
        }
        //Iterate over all bullets, remove the invisible ones and restore them back to the object pool
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if(!bullet.isVisible()) {
                Bullet remove = bullets.remove(i);
                i--;
                BulletsPool.theReturn(remove);
            }
        }
    }

    /**
     * Draw of all the tank's bullets when the tank is destroyed.
     */
    public void bulletsReturn(){
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            BulletsPool.theReturn(bullet);
        }
        bullets.clear();
    }

    //The way the tank collides with enemy bullets.
    public void collideBullets(List<Bullet> bullets){
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            //Handling method of collision of bullets and tanks
            if(MyUtil.isCollide(this.x,y,RADIUS,bulletX,bulletY)){
                //Bullet disappears
                bullet.setVisible(false);
                //Tanks take damage
                hurt(bullet);
                //Adding an explosion effect
                addExplode(x,y+RADIUS);
            }
        }
    }

    private void addExplode(int x,int y){
        //Add an explosion effect,
        // referenced to the coordinates of the tank currently being hit.
        Explode explode = ExplodesPool.get();
        explode.setX(x);
        explode.setY(y);
        explode.setVisible(true);
        explode.setIndex(0);
        explodes.add(explode);
    }

    //Tanks receive damage
    private void hurt(Bullet bullet){
        int atk = bullet.getAtk();
        hp -= atk;
        if(hp < 0){
            hp = 0;
            die();
        }
    }

    //What needs to be dealt with in the event of a tank destroy
    private void die(){
        //the death of enemy tanks
        if(isEnemy){
            GameFrame.killEnemyCount ++;
            //Destroy enemy tanks and return to object pool.
            EnemyTanksPool.theReturn(this);
            if (GameFrame.isCrossLevel()){
                if(GameFrame.isLastLevel()){
                    //pass level
                    GameFrame.setGameState(Constant.STATE_WIN);
                }else {
                    //next level
                    GameFrame.startCrossLevel();
                }
            }
        }else{
            delaySecondsToOver(3000);
        }
    }

    /**
     * check current tank if it is destroyed
     * @return true or false
     */
    public boolean isDie(){
        return hp <= 0;
    }

    /**
     * Draws the effects of all the explosions on the current tank.
     * @param g brushes
     */
    public void drawExplodes(Graphics g){
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            explode.draw(g);
        }
        //Removes invisible explosion effects and returns them to the object pool.
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            if(!explode.isVisible()){
                Explode remove = explodes.remove(i);
                ExplodesPool.theReturn(remove);
                i--;
            }
        }
    }


    //Inner class to represent the blood bar of the tank
    class BloodBar{
        public static final int BAR_LENGTH = 50;
        public static final int BAR_HEIGHT = 3;

        public void draw(Graphics g){
            //fill in the blanks in yellow
            g.setColor(Color.YELLOW);
            g.fillRect(x - RADIUS , y-RADIUS-BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);
            //Current blood level in red
            g.setColor(Color.RED);
            g.fillRect(x - RADIUS , y-RADIUS-BAR_HEIGHT*2,hp*BAR_LENGTH/maxHP,BAR_HEIGHT);
            //Blue border
            g.setColor(Color.WHITE);
            g.drawRect(x - RADIUS , y-RADIUS-BAR_HEIGHT*2,BAR_LENGTH,BAR_HEIGHT);
        }
    }

    //Collisions of Tank bullets and  map element blocks
    public void bulletsCollideMapTiles(List<MapTile> tiles){

        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if(tile.isCollideBullet(bullets)){
                //Adding an explosion effect
                addExplode(tile.getX()+MapTile.radius,tile.getY() +MapTile.tileW);
                //Handling of map cement blocks not destroyed
                if(tile.getType() == MapTile.TYPE_HARD)
                    continue;
                //Setting up map blocks for destruction
                tile.setVisible(false);
                //Return to Object Pool
                MapTilePool.theReturn(tile);
                //switch to the game over screen for 3 second once the base camp is destroyed
                if(tile.isHouse()){
                    delaySecondsToOver(3000);
                }
            }
        }

    }

    /**
     * Delay some milliseconds Toggle to end of game
     * @param millisSecond milliseconds of delay
     */
    private void delaySecondsToOver(int millisSecond){
        new Thread(){
            public void run() {
                try {
                    Thread.sleep(millisSecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameFrame.setGameState(Constant.STATE_LOST);
            }
        }.start();
    }

    /**
     * Method of collision between the map block and the current tank
     * Extract 8 points from the tile to determine if any of the 8 points
     * have collided with the current tank.
     * The order of the points is traversed clockwise
     * starting with the point in the top left corner.
     */
    public boolean isCollideTile(List<MapTile> tiles){
        final int len = 2;
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
//        }
//        for (MapTile tile : tiles) {
            //If the block is not visible, or if it is an occluded block,
            // no collision is detected.
            if(!tile.isVisible() || tile.getType() == MapTile.TYPE_COVER)
                continue;
            //point-1  upper left
            int tileX = tile.getX();
            int tileY = tile.getY();
            boolean collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            //If it touches it, it returns directly, otherwise it continues to judge the next point
            if(collide){
                return true;
            }
            //point-2 left middle point
            tileX += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //point-3 top right point
            tileX += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //point-4 right midpoint
            tileY += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //point-5 lower right point
            tileY += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //point-6 lower midpoint
            tileX -= MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //point-7 lower left point
            tileX -= MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
            //point-8 left midpoint
            tileY -= MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if(collide){
                return true;
            }
        }
        return false;
    }

    /**
     * Tank rollback method
     */
    public void back(){
        x = oldX;
        y = oldY;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
