package com.tankGame.util;

import com.tankGame.game.Explode;
import java.util.ArrayList;
import java.util.List;

/**
 * Explosive Effects Object Pool
 */
public class ExplodesPool {

    public static final int DEFAULT_POOL_SIZE = 10;
    public static final int POOL_MAX_SIZE = 20;

    //Container for saving all explosion effects
    private static List<Explode> pool = new ArrayList<>();
    //
    static{
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++) {
            pool.add(new Explode());
        }
    }

    /**
     * Getting an exploded object from a pond
     * @return explode object instance
     */
    public static Explode get(){
        Explode explode = null;
        if(pool.size() == 0){
            explode = new Explode();
        }else{
            explode = pool.remove(0);
        }
        return explode;
    }

    public static void theReturn(Explode explode){
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(explode);
    }
}
