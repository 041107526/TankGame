package com.tankGame.game;

import com.tankGame.map.GameMap;
import com.tankGame.tank.EnemyTank;
import com.tankGame.tank.MyTank;
import com.tankGame.tank.Tank;
import com.tankGame.util.MusicUtil;
import com.tankGame.util.MyUtil;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.tankGame.util.Constant.*;

/**
 * The main window class of the game
 * All of the game presentation is to be implemented in this class.
 */
public class GameFrame extends Frame implements Runnable{
    //Loaded when the first usage
    private Image overImg  = null;

    //1: Define an image that is the same size as the screen
    private BufferedImage bufImg = new BufferedImage(FRAME_WIDTH,FRAME_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
    //game state
    private static int gameState;
    //index of the menu
    private static int menuIndex;
    //height of the title bar
    public static int titleBarH;

    //define the Tank Object
    private static Tank myTank;
    //enemy tank containers
    private static List<Tank> enemies = new ArrayList<>();

    //keep track of how many enemies have spawned on the level
    private static int bornEnemyCount;
    //keep track of how many enemies have been killed on the level
    public static int killEnemyCount;

    //Define map-related content
    private static GameMap gameMap = new GameMap();;

    /**
     * Initialise the window
     */
    public GameFrame() {
        initFrame();
        initEventListener();
        //Start a thread for refreshing the window
        new Thread(this).start();
    }


    /**
     * The method to get to the next level
     */
    private void nextLevel() {
        startGame(LevelInof.getInstance().getLevel()+1);
    }
    //Beginning of passage animation
    public static int flashTime;
    public static final int RECT_WIDTH = 40;
    public static final int RECT_COUNT = FRAME_WIDTH/RECT_WIDTH+1;
    public static boolean isOpen = false;
    public static void startCrossLevel(){
        gameState = STATE_CROSS;
        flashTime = 0;
        isOpen = false;
    }


    //Draw pass game animation
    public void drawCross(Graphics g){
        gameMap.drawBk(g);
        myTank.draw(g);
        gameMap.drawCover(g);

        g.setColor(Color.BLACK);
        //Disable Venetian Blind Effect
        if(!isOpen) {
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, FRAME_HEIGHT);
            }
            //
            if (flashTime++ - RECT_WIDTH > 5) {
                isOpen = true;
                //初始化下一个地图
                gameMap.initMap(LevelInof.getInstance().getLevel()+1);
            }
        }else{
            //open Venetian Blind Effect
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, FRAME_HEIGHT);
            }
            if(flashTime-- == 0){
                startGame(LevelInof.getInstance().getLevel());
            }
        }
    }

    /**
     * Initialise the game
     */
    private void initGame() {
        gameState = STATE_MENU;
    }

    /**
     * Initialise the attributes
     */
    private void initFrame() {
        //set the title
        setTitle(GAME_TITLE);
        //set window size
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        //set the coordinates of the upper left corner of the window
        setLocation(FRAME_X, FRAME_Y);
        //set the window size unchangeable
        setResizable(false);
        //set window visible
        setVisible(true);
        //height of the title bar
        titleBarH = getInsets().top;
    }


    /**
     * update method is a method of the Frame class that inherits the method
     * This method is responsible for everything that is drawn,
     * and everything that needs to be explicit in the screen is called from within this method.
     * This method cannot be called actively.
     * The method must be called back by calling repaint().
     * @param g1 system-supplied brushes
     */
    public void update(Graphics g1) {
        //2: Getting the picture's brush
        Graphics g = bufImg.getGraphics();
        //3: Use the picture brush to draw all the content onto the picture
        g.setFont(GAME_FONT);
        switch (gameState) {
            case STATE_MENU:
                drawMenu(g);
                break;
            case STATE_HELP:
                drawHelp(g);
                break;
            case STATE_ABOUT:
                drawAbout(g);
                break;
            case STATE_RUN:
                drawRun(g);
                break;
            case STATE_LOST:
                drawLost(g,"Failed the Game!");
                break;
            case STATE_WIN:
                drawWin(g);
                break;
            case STATE_CROSS:
                drawCross(g);
                break;
        }

        //4: Use the system brush to draw pictures onto the frame.
        g1.drawImage(bufImg,0,0,null);
    }

    /**
     * Draw the end of the game
     * @param g brushes
     */
    private void drawLost(Graphics g,String str) {
        //Load only once
        if(overImg == null){
            overImg = MyUtil.createImage("res/over.jpg");
        }
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);

        int imgW = overImg.getWidth(null);
        int imgH = overImg.getHeight(null);

        g.drawImage(overImg, FRAME_WIDTH - imgW >> 1, FRAME_HEIGHT-imgH >>1,null);

        //Adding a Keystroke Alert Message
        g.setColor(Color.WHITE);
        g.drawString(OVER_STR0,10,FRAME_HEIGHT-20);
        g.drawString(OVER_STR1,FRAME_WIDTH-310,FRAME_HEIGHT-20);

        //draws words of failure to pass next level
        g.setColor(Color.WHITE);
        g.drawString(str,FRAME_WIDTH/2-80,50);
    }

    /**
     * Game winning interface
     * @param g brushes
     */
    private void drawWin(Graphics g){
        drawLost(g,"Passed the Game!");
    }

    //draw contents of the game's running state
    private void drawRun(Graphics g) {
        //Draw a black background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        //Draw collision layer
        gameMap.drawBk(g);

        drawEnemies(g);

        myTank.draw(g);

        //Draw cover layer of the map
        gameMap.drawCover(g);

        drawExplodes(g);

        //Call method of collision of bullets and tanks
        bulletCollideTank();

        //Call method of collision of bullets and map element blocks
        bulletAndTanksCollideMapTile();
    }
    //Draws all enemy tanks and removes them from the container if they are already dead.
    private void drawEnemies(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if(enemy.isDie()){
                enemies.remove(i);
                i--;
                continue;
            }
            enemy.draw(g);
        }
    }

    private Image helpImg;
    private Image aboutImg;
    private void drawAbout(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(aboutImg == null){
            aboutImg = MyUtil.createImage("res/about.png");
        }
        int width = aboutImg.getWidth(null);
        int height = aboutImg.getHeight(null);

        int x = FRAME_WIDTH - width >>1;
        int y = FRAME_HEIGHT - height >> 1;
        g.drawImage(aboutImg,x,y,null);

        g.setColor(Color.WHITE);
        g.drawString("Any key to continue",10,FRAME_HEIGHT-10);

    }

    private void drawHelp(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(helpImg == null){
            helpImg = MyUtil.createImage("res/help.png");
        }
        int width = helpImg.getWidth(null);
        int height = helpImg.getHeight(null);

        int x = FRAME_WIDTH - width >>1;
        int y = FRAME_HEIGHT - height >> 1;
        g.drawImage(helpImg,x,y,null);

        g.setColor(Color.WHITE);
        g.drawString("Any key to continue",10,FRAME_HEIGHT-10);
    }

    /**
     * Draw the contents of the menu state
     *
     * @param g brushes
     */
    private void drawMenu(Graphics g) {
        //Draw a black background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        final int STR_WIDTH = 140;
        int x = (FRAME_WIDTH - STR_WIDTH) / 2;
        int y = FRAME_HEIGHT / 3;
        //row spacing
        final int DIS = 50;

        for (int i = 0; i < MENUS.length; i++) {
            if (i == menuIndex) {//The colour of the selected menu item is set to red
                g.setColor(Color.RED);
            } else {//The colour of the unselected menu item is set to white
                g.setColor(Color.WHITE);
            }
            g.drawString(MENUS[i], x, y + DIS * i);
        }
    }


    /**
     * Initialise the event listener for the window
     */
    private void initEventListener() {
        //Add listener events
        addWindowListener(new WindowAdapter() {
            //点击关闭按钮的时候，方法会被自动调用
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //Add a key listener event
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //Get the key value of the pressed key
                int keyCode = e.getKeyCode();
                //Different game states are given different ways to handle them.
                switch (gameState) {
                    case STATE_MENU:
                        keyPressedEventMenu(keyCode);
                        break;
                    case STATE_HELP:
                        keyPressedEventHelp(keyCode);
                        break;
                    case STATE_ABOUT:
                        keyPressedEventAbout(keyCode);
                        break;
                    case STATE_RUN:
                        keyPressedEventRun(keyCode);
                        break;
                    case STATE_LOST:
                        keyPressedEventLost(keyCode);
                        break;
                    case STATE_WIN:
                        keyPressedEventWin(keyCode);
                        break;
                }
            }

            //Content of the callback when the button is released
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                //Different game states are given different ways to handle them.
                if(gameState == STATE_RUN){
                    keyReleasedEventRun(keyCode);
                }
            }
        });
    }

    //Key handling for pass game
    private void keyPressedEventWin(int keyCode) {
        keyPressedEventLost(keyCode);
    }

    //Handling method when the button is released
    private void keyReleasedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setState(Tank.STATE_STAND);
                break;
        }
    }
    //Handling method of game over
    private void keyPressedEventLost(int keyCode) {
        //end game
        if(keyCode == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }else if(keyCode == KeyEvent.VK_ENTER){
            setGameState(STATE_MENU);
            //Many of the game's actions need to be turned off,
            //and some of the certain attributes need to be reset
            resetGame();
        }
    }

    //Reset game
    private void resetGame(){
        killEnemyCount = 0;
        menuIndex = 0;
        //Player tank bullets returned to the object pool
        myTank.bulletsReturn();
        //Destroy player tank
        myTank = null;
        //Resources for Emptying the Enemy
        for (Tank enemy : enemies) {
            enemy.bulletsReturn();
        }
        enemies.clear();
        //Emptying map resources
        gameMap = null;
    }
    //Keystroke handling method while the game is running
    private void keyPressedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myTank.setDir(Tank.DIR_UP);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myTank.setDir(Tank.DIR_DOWN);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myTank.setDir(Tank.DIR_LEFT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setDir(Tank.DIR_RIGHT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_SPACE:
                myTank.fire();
                break;
        }
    }

    private void keyPressedEventAbout(int keyCode) {
        setGameState(STATE_MENU);
    }

    private void keyPressedEventHelp(int keyCode) {
        setGameState(STATE_MENU);
    }

    //Handling method of keys in the menu state
    private void keyPressedEventMenu(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (--menuIndex < 0) {
                    menuIndex = MENUS.length - 1;
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if(++menuIndex > MENUS.length -1){
                    menuIndex = 0;
                }
                break;
            case KeyEvent.VK_ENTER:
                switch(menuIndex){
                    case 0:
                        startGame(1);
                        break;
                    case 1:
                        //Continue the game. Go to the screen for selecting a level
                        break;
                    case 2:
                        setGameState(STATE_HELP);
                        break;
                    case 3:
                        setGameState(STATE_ABOUT);
                        break;
                    case 4:
                        System.exit(0);
                        break;
                }
                break;
        }
    }

    /**
     * Start game and load the information of current level
     */
    private static void startGame(int level) {
        enemies.clear();
        if(gameMap == null){
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        MusicUtil.playStart();
        killEnemyCount = 0;
        bornEnemyCount = 0;
        gameState = STATE_RUN;
        //Create tank object, enemy tank object
        myTank = new MyTank(FRAME_WIDTH/3,FRAME_HEIGHT-Tank.RADIUS,Tank.DIR_UP);

        //Use a separate thread for controlling the production of enemy tanks
        new Thread(){
            @Override
            public void run() {
                while(true){
                    if(LevelInof.getInstance().getEnemyCount()>bornEnemyCount&&
                            enemies.size() < ENEMY_MAX_COUNT){
                        Tank enemy = EnemyTank.createEnemy();
                        enemies.add(enemy);
                        bornEnemyCount ++;
                    }
                    try {
                        Thread.sleep(ENEMY_BORN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //只有在游戏run 状态下才创建敌人坦克
                    if(gameState != STATE_RUN){
                        enemies.clear();
                        break;
                    }
                }
            }
        }.start();
    }


    @Override
    public void run() {
        while(true){
            //Call repaint method, callback update method
            repaint();
            try {
                Thread.sleep(REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Handling method of tank bullets colliding with tanks
    private void bulletCollideTank(){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.collideBullets(myTank.getBullets());
        }
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            myTank.collideBullets(enemy.getBullets());
        }
    }

    //Handling method of bullets and map element blocks collide
    private void bulletAndTanksCollideMapTile(){
        //Collision of bullets and map blocks of player tank
        myTank.bulletsCollideMapTiles(gameMap.getTiles());

        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.bulletsCollideMapTiles(gameMap.getTiles());
        }
        //Collision of player tank and map blocks
        if(myTank.isCollideTile(gameMap.getTiles())){
            myTank.back();
        }
        //Collision of enemy tanks and map blocks
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if(enemy.isCollideTile(gameMap.getTiles())){
                enemy.back();
            }
        }
        //Clean up all destroyed map blocks
        gameMap.clearDestroyTile();
    }

    //Explosive effects on all tanks
    private void drawExplodes(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.drawExplodes(g);
        }
        myTank.drawExplodes(g);
    }

    //Get game status and modifying game status
    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }
    public static int getGameState() {
        return gameState;
    }

    /**
     * This method is to check if the current is the last level
     * @return true or false
     */
    public static boolean isLastLevel(){
        //The current level is consistent with the total level
        int currLevel = LevelInof.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
        return currLevel == levelCount;
    }

    /**
     * This method is to check if pass the current level
     * @return true or false
     */
    public static boolean isCrossLevel(){
        //The number of enemies eliminated corresponds to the number of enemies in the level.
        return killEnemyCount == LevelInof.getInstance().getEnemyCount();
    }



}
