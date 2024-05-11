package com.tankGame.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Classes for game-related information
 */
public class GameInfo {
    //Read from configuration file
    //Number of levels
    private static int levelCount;

    static{
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("level/gameinfo"));
            levelCount = Integer.parseInt(prop.getProperty("levelCount"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getLevelCount() {
        return levelCount;
    }

}
