package com.tankGame.map;

import com.tankGame.game.GameFrame;
import com.tankGame.game.LevelInof;
import com.tankGame.tank.Tank;
import com.tankGame.util.Constant;
import com.tankGame.util.MapTilePool;


import java.awt.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Game Map Class
 */
public class GameMap {

    public static final int MAP_X = Tank.RADIUS*3;
    public static final int MAP_Y = Tank.RADIUS*3 + GameFrame.titleBarH;
    public static final int MAP_WIDTH = Constant.FRAME_WIDTH-Tank.RADIUS*6;
    public static final int MAP_HEIGHT = Constant.FRAME_HEIGHT-Tank.RADIUS*8-GameFrame.titleBarH;

    //地图元素块的容器
    private List<MapTile> tiles = new ArrayList<>();

    //大本营
    private TankHouse house;

    public GameMap() {}
    /**
     * Initialise map element blocks and level information
     */
    public void initMap(int level){
        tiles.clear();
        try {
            loadLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Initialisation player base camp
        house = new TankHouse();
        addHouse();
    }

    /**
     * Initialisation of level information
     * @param level the number of current level
     */
    private void loadLevel(int level) throws Exception{
        //Get a unique instance object of the level information class
        LevelInof levelInof = LevelInof.getInstance();
        levelInof.setLevel(level);

        Properties prop = new Properties();
        prop.load(new FileInputStream("level/lv_"+level));
        //Load in all the map information
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        //Set the number of enemies
        levelInof.setEnemyCount(enemyCount);

        //get the enemy types
        String[] enemyType = prop.getProperty("enemyType").split(",");
        int[] type = new int[enemyType.length];
        for (int i = 0; i < type.length; i++) {
            type[i] = Integer.parseInt(enemyType[i]);
        }
        //set the enemy type
        levelInof.setEnemyType(type);
        //level difficulty
        //If the game difficulty is not designed, then treat it as the default
        String levelType = prop.getProperty("levelType");
        levelInof.setLevelType(Integer.parseInt(levelType==null? "1" : levelType));

        String methodName = prop.getProperty("method");
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));
        //Read all the parameters into the array.
        String[] params = new String[invokeCount];
        for (int i = 1; i <=invokeCount ; i++) {
            params[i-1] = prop.getProperty("param"+i);
        }
        //Use the parameters , call the corresponding invokeMethod
        invokeMethod(methodName,params);
    }

    //Call the corresponding methods based on the method name and parameters
    private void invokeMethod(String name,String[] params){
        for (String param : params) {
            //Get the parameters of the methods on each line
            String[] split = param.split(",");
            //Use an int array to hold the parsed content.
            int[] arr = new int[split.length];
            int i;
            for (i = 0; i < split.length-1; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }
            //The interval between blocks is a multiple of the map block
            final int DIS = MapTile.tileW ;

            //Parsing the last double value
            int dis = (int)(Double.parseDouble(split[i])*DIS);
            switch(name){
                case "addRow":
                    addRow(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,
                            MAP_X+MAP_WIDTH-arr[2]*DIS,arr[3],dis);
                    break;
                case "addCol":
                    addCol(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,
                            MAP_Y+MAP_HEIGHT-arr[2]*DIS,
                            arr[3],dis);
                    break;
                case "addRect":
                    addRect(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,
                            MAP_X+MAP_WIDTH-arr[2]*DIS,
                            MAP_Y+MAP_HEIGHT-arr[3]*DIS,
                            arr[4],dis);
                    break;
            }
        }
    }

    //Add all element blocks of the play base camp to the map's container.
    private void addHouse(){
        tiles.addAll(house.getTiles());
    }

    /**
     * Whether there is an overlap with all the blocks in the tiles collection.
     * @param tiles the container of map element blocks
     * @param x x-coordinate of some point
     * @param y y-coordinate of some point
     * @return Returns true if there is an overlap, otherwise false
     */
    private boolean isCollide(List<MapTile> tiles, int x ,int y){
        for (MapTile tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            if(Math.abs(tileX-x) < MapTile.tileW && Math.abs(tileY-y) < MapTile.tileW){
                return true;
            }
        }
        return false;
    }

    /**
     * Draw only the cover blocks
     * @param g brushes
     */
    public void drawBk(Graphics g){
        for (MapTile tile : tiles) {
            if(tile.getType() != MapTile.TYPE_COVER)
                tile.draw(g);
        }
    }

    /**
     * Draw other cover blocks expect cover blocks
     * @param g brushes
     */
    public void drawCover(Graphics g){
        for (MapTile tile : tiles) {
            if(tile.getType() == MapTile.TYPE_COVER)
                tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    /**
     * Remove all invisible map blocks from the container
     */
    public void clearDestroyTile(){
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if(!tile.isVisible())
                tiles.remove(i);
        }
    }

    /**
     * Adds a row of the specified type of map block to the map block container.
     * @param startX Add the x-coordinate of the start of the map block.
     * @param startY Add the y-coordinate of the start of the map block.
     * @param endX Add the x-coordinate of the end of the map block.
     * @param type type of map blocks
     * @param DIS The spacing between the centre points of the map blocks,
     *            if it's the width of the block, means it's continuous.
     */
    public void addRow(int startX,int startY, int endX, int type, final int DIS){
        int count  = (endX - startX +DIS )/(MapTile.tileW+DIS);
        for (int i = 0; i <count ; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX + i * (MapTile.tileW+DIS));
            tile.setY(startY);
            tiles.add(tile);
        }
    }

    /**
     * Adds a column to the map element block container.
     * @param startX  Start x-coordinate of the column
     * @param startY Start y-coordinate of the column
     * @param endY  end x-coordinate of the column
     * @param type type of map blocks
     * @param DIS Spacing between the centres of adjacent elements
     */
    public void addCol(int startX,int startY, int endY, int type, final int DIS){
        int count  = (endY - startY +DIS)/(MapTile.tileW+DIS);
        for (int i = 0; i <count ; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX );
            tile.setY(startY + i * (MapTile.tileW+DIS));
            tiles.add(tile);
        }
    }

    //Adds an element block to the specified rectangular area
    public void addRect(int startX,int startY,int endX, int endY, int type, final int DIS){
        int rows = (endY-startY+DIS)/(MapTile.tileW+DIS);
        for (int i = 0; i <rows ; i++) {
            addRow(startX,startY+i*(MapTile.tileW+DIS),endX,type,DIS);
        }
    }




}
