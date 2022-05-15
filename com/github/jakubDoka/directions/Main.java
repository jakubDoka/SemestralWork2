package com.github.jakubDoka.directions;
import com.github.jakubDoka.directions.game.Directions;

public class Main {
    public static void main(String[] args) {
        new Thread(new Directions()).start();
    }
}
