package com.tankGame.util;


import com.tankGame.tank.EnemyTank;
import com.tankGame.tank.Tank;

import java.util.ArrayList;
import java.util.List;

/**
 * Enemy tank object pool
 */
public class EnemyTanksPool {

    public static final int DEFAULT_POOL_SIZE = 20;
    public static final int POOL_MAX_SIZE = 20;

    private static List<Tank> pool = new ArrayList<>();
    static{
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++) {
            pool.add(new EnemyTank());
        }
    }

    /**
     * Get an object from the object pool.
     * @return enemy tank object instance
     */
    public static Tank get(){
        Tank tank = null;
        if(pool.size() == 0){
            tank = new EnemyTank();
        }else{
            tank = pool.remove(0);
        }
        return tank;
    }
    public static void theReturn(Tank tank){
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(tank);
    }
}
