package com.tankGame.util;

import java.awt.*;

/**
 * utility class
 */
public class MyUtil {
    private  MyUtil(){}

    /**
     * Get the random number of the specified interval
     * @param min Minimum value of the interval
     * @param max Maximum value of the interval
     * @return random number
     */
    public static final int getRandomNumber(int min, int max){
        return (int)(Math.random()*(max-min)+min);
    }

    /**
     * Get the random color
     * @return random color
     */
    public static final Color getRandomColor(){
        int red = getRandomNumber(0,256);
        int blue = getRandomNumber(0,256);
        int green = getRandomNumber(0,256);
        return new Color(red,green,blue);
    }

    /**
     * Check whether a point is inside a certain square.
     * @param rectX x-coordinate of the centre of the square
     * @param rectY y-coordinate of the centre of the square
     * @param radius half the length of the sides of a square
     * @param pointX x-coordinate of the point
     * @param pointY y-coordinate of the point
     * @return Returns true if the point is inside the square, false otherwise.
     */
    public static final boolean isCollide(int rectX,int rectY,int radius,int pointX,int pointY){
        //Distance between the centre of the square and the x-y axis of the point
        int disX = Math.abs(rectX - pointX);
        int disY = Math.abs(rectY - pointY);
        if (disX <= radius && disY <= radius)
            return true;
        return false;
    }

    /**
     * Create loaded image objects based on the resource path of the image
     * @param path Path to the image resource
     * @return the created image
     */
    public static final Image createImage(String path){
        return Toolkit.getDefaultToolkit().createImage(path);
    }

    private static final String[] NAMES = {
            "Blaze", "Venom", "Phoenix", "Fury" , "Shadow" ,
            "Storm" , "Ace" , "Nova" , "Rogue" , "Fissure",
            "Perseus" , "Blaze" , "Ratchet" , "Cobra" , "Reaper" ,
            "Colt" , "Rigs" , "Crank" , "Ripley" , "Creep" ,
            "Roadkill" , "Daemon", "Hijacker" , "Locker" , "Manifesto",
            "Fury" , "Thunder" , "Dragonite" , "Repulsor" , "Revenger" ,
            "DrDisconnect" , "Stabber" , "Asaurus Rex" , "Uber Fast" , "Disguised" ,
            "MindOf" , "Dr Fast", "Popper" , "Big Fast" , "It Is Ye" ,
            "I am Fire" , "Captain Fire" , "The Fire Dude" , "Mr Cool" , "Let’s Go" ,
            "Captain Fire" , "Bloodbath" , "Conqueror" , "Total Tiger" , "Endeavour" ,
            "All Muscles", "The Fox", "Skyward", "Clockwork Cavern" , "Pixelated" ,
            "Neon Nebula" , "Ember Isles" , "Dragonkin" , "Starfall" , "Whispering" ,
            "Glitch", "Clocktower"

    };


    /**
     * Get a random name
     * @return random name
     */
    public static final String getRandomName(){
        return NAMES[getRandomNumber(0,NAMES.length)];
    }
}
