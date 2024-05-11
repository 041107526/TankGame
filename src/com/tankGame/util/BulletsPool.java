package com.tankGame.util;

import com.tankGame.game.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * Bullet Object Pool Class
 */
public class BulletsPool {

    public static final int DEFAULT_POOL_SIZE = 200;
    public static final int POOL_MAX_SIZE = 300;

    //Container for keeping all the bullets
    private static List<Bullet> pool = new ArrayList<>();
    //Create 200 bullet objects to add to the container when the class is loaded
    static{
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++) {
            pool.add(new Bullet());
        }
    }

    /**
     * Get a bullet object from the pond
     * @return bullet object instance
     */
    public static Bullet get(){
        Bullet bullet = null;
        //the size of bullet object pool is equals to 0
        if(pool.size() == 0){
            bullet = new Bullet();
        }else{//There are still objects in bullet object pool,
            // take the first position bullet object.
            bullet = pool.remove(0);
        }
        return bullet;
    }
    //When bullets are destroyed, return them to the bullet object pool.
    public static void theReturn(Bullet bullet){
        //The number of bullets in the pool has reached its maximum.
        // Then no more will be returned.
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(bullet);
    }
}
