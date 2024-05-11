package com.tankGame.map;

import com.tankGame.util.Constant;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Player tank's base camp
 */
public class TankHouse {
    //XY coordinates of player tank's base camp
    public static final int HOUSE_X = (Constant.FRAME_WIDTH-3*MapTile.tileW >> 1)+2;
    public static final int HOUSE_Y = Constant.FRAME_HEIGHT-2 *MapTile.tileW;

    //There are six map blocks.
    private List<MapTile> tiles = new ArrayList<>();
    public TankHouse() {
        tiles.add(new MapTile(HOUSE_X,HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X,HOUSE_Y+MapTile.tileW));
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW,HOUSE_Y));

        tiles.add(new MapTile(HOUSE_X+MapTile.tileW*2,HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW*2,HOUSE_Y+MapTile.tileW));
        //map block with words
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW,HOUSE_Y+MapTile.tileW));
        //set type of map element blocks
        tiles.get(tiles.size()-1).setType(MapTile.TYPE_HOUSE);
    }

    public  void draw(Graphics g){
        for (MapTile tile : tiles) {
            tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }
}
