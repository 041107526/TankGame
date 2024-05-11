package com.tankGame.util;

import java.awt.*;

/**
 * constants class
 * Constants in the game are maintained in this class for easy management at a later stage
 */
public class Constant {
    /******************Game Window Related******************/
    public static final String GAME_TITLE = "Tank Game";

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 600;

    //Dynamically obtain the width and height of the system screen
    public static final int SCREEN_W = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_H = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int FRAME_X = SCREEN_W-FRAME_WIDTH>>1;
    public static final int FRAME_Y = SCREEN_H-FRAME_HEIGHT>>1;


    /*********************Game menu related*********************/
    public static final int STATE_MENU = 0;
    public static final int STATE_HELP = 1;
    public static final int STATE_ABOUT = 2;
    public static final int STATE_RUN = 3;
    public static final int STATE_LOST = 4;
    public static final int STATE_WIN = 5;
    public static final int STATE_CROSS = 6;

    public  static final String[] MENUS = {
            "Start Game",
            "Continue Game",
            "Help Game",
            "About Game",
            "Quit Game"
    };

    public static final String OVER_STR0 = "ESC Key Quit Game";
    public static final String OVER_STR1 = "ENTER Key Return Menu ";

    /*********************Game Font related*********************/
    public static final Font GAME_FONT = new Font("Courier New",Font.BOLD,24);
    public static final Font SMALL_FONT = new Font("Courier New",Font.BOLD,12);



    /*********************Game Enemy related*********************/
    public static final int REPAINT_INTERVAL = 30;
    public static final int ENEMY_MAX_COUNT = 10;
    public static final int ENEMY_BORN_INTERVAL = 5000;

    public static final int ENEMY_AI_INTERVAL = 3000;
    public static final double ENEMY_FIRE_PERCENT = 0.03;


}
