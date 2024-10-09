import Basic.*;
import Emulator.*;
import Home.*;
import Match.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        while(!MatchBasic.is90enemies()) {
            System.out.println("Waiting for enemies...");
            try {
                Screenshot.screenshotGameState();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        System.out.println("Summoning now!");
    }
}
