package github.com.jakubDoka.directions;
import github.com.jakubDoka.directions.game.Directions;

public class Main {
    public static void main(String[] args) {
        new Thread(new Directions()).start();
    }
}
