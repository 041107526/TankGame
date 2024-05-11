package com.tankGame.game;

import com.tankGame.util.MyUtil;

/**
 *
 * Used to manage information about the current level: Singleton Pattern class
 */
public class LevelInof {
    //private constructor
    private LevelInof(){}

    //Define static variables of this class type to point to unique instances of the class
    private static LevelInof instance;


    //All access to the unique instance of the class is through the method
    //This method has security concerns,
    // and multiple instances may be created in a multi-threaded situation

    public static LevelInof getInstance(){
        if(instance == null){
            instance = new LevelInof();
        }
        return instance;
    }
    //Level number
    private int level;
    //Number of enemies in the level
    private int enemyCount;
    //Required time to pass, -1 means unlimited time
    private int crossTime = -1;
    //Enemy type information
    private int[] enemyType;
    //Game Difficulty >=1
    private int levelType;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int getCrossTime() {
        return crossTime;
    }

    public void setCrossTime(int crossTime) {
        this.crossTime = crossTime;
    }

    public int[] getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(int[] enemyType) {
        this.enemyType = enemyType;
    }

    public int getLevelType() {
        return levelType <=0 ? 1 : levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    //Get a random element of an array of enemy types.
    //Get a random enemy type
    public int getRandomEnemyType(){
       int index = MyUtil.getRandomNumber(0,enemyType.length);
       return enemyType[index];
    }
}
