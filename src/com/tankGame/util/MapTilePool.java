package com.tankGame.util;

import com.tankGame.map.MapTile;
import com.tankGame.tank.EnemyTank;
import com.tankGame.tank.Tank;

import java.util.ArrayList;
import java.util.List;

public class MapTilePool{

    public static final int DEFAULT_POOL_SIZE = 50;
    public static final int POOL_MAX_SIZE = 70;

    private static List<MapTile> pool = new ArrayList<>();
    static{
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++) {
            pool.add(new MapTile());
        }
    }

    /**
     * Getting an exploded object from a pond
     * @return tile object instance
     */
    public static MapTile get(){
        MapTile tile = null;
        //池塘被掏空了！
        if(pool.size() == 0){
            tile = new MapTile();
        }else{//池塘中还有对象，拿走第一个位置的子弹对象
            tile = pool.remove(0);
        }
        return tile;
    }
    public static void theReturn(MapTile tile){
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(tile);
    }
}
