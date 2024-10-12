package Match;

import Basic.Screenshot;

import java.io.IOException;

public class PlayGame {

    public static void waitFor90() {
        // wait for 90 monsters
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

        // spawn
        System.out.println("Summoning now!");
        for(int i = 0; i < 10; i++) {
            MatchBasic.pressSummon();
        }
    }

    public static void waitForGolem() {
        while(!MatchBasic.isGolemPresent()) {
            System.out.println("Waiting for Golem...");
        }

        System.out.println("Golem can be challenged!");
        MatchBasic.pressGolem();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        MatchBasic.challengeGolem();
    }
}
