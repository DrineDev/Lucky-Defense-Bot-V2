package Home;

import Basic.Coordinates;
import Basic.PixelColorChecker;
import Basic.Screenshot;

import java.awt.*;
import java.io.IOException;

import static Logger.Logger.log;
import static java.lang.Thread.sleep;

public class Recruit {

    /**
     * Check if can press One More
     * @return
     */
    public static boolean checkOneMore() {
        Color color = new Color(50, 145, 219);
        Coordinates coordinates = new Coordinates(177, 777);
        return PixelColorChecker.checkColorMatch(coordinates, color, "Resources/GameState.png", 5);
    }

    /**
     * Recruit 1X
     * @throws IOException
     */
    public static void recruit1X() throws IOException {
        Screenshot.screenshotGameState();
        if(HomeNotifications.checkRecruitNotification()) {
            log("Not enough recruitment scrolls for 1X.");
            return;
        }

        try {
            ButtonsHome.pressRecruit();
            sleep(3000);
            Screenshot.screenshotGameState();

            while(HomeNotifications.checkRecruit1XNotification()) {
                ButtonsHome.pressRecruit1X();
                sleep(10000);
                Screenshot.screenshotGameState();
                while(checkOneMore()) {
                    ButtonsHome.pressOneMore();
                    sleep(10000);
                    Screenshot.screenshotGameState();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log("Finished recruiting 1x.");
        ButtonsHome.closeRecruit();
        Screenshot.screenshotGameState();
    }

    /**
     * Recruit 10X
     * @throws IOException
     */
    public static void recruit10X() throws IOException {
        Screenshot.screenshotGameState();
        if(HomeNotifications.checkRecruitNotification()) {
            log("Not enough recruitment scrolls for 10X.");
            return;
        }

        try {
            ButtonsHome.pressRecruit();
            sleep(3000);
            Screenshot.screenshotGameState();

            while(HomeNotifications.checkRecruit10XNotification()) {
                ButtonsHome.pressRecruit10X();
                sleep(10000);
                Screenshot.screenshotGameState();
                while(checkOneMore()) {
                    ButtonsHome.pressOneMore();
                    sleep(10000);
                    Screenshot.screenshotGameState();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log("Finished recruiting 10x.");
        ButtonsHome.closeRecruit();
        Screenshot.screenshotGameState();
    }
}
