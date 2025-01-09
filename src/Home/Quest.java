package Home;

import Basic.Screenshot;
import GUI.MainFrame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static Logger.Logger.log;

public class Quest {

    /**
     * Automates quest reward retrieval
     * @throws IOException
     * @throws InterruptedException
     */


    public static void autoQuest() throws IOException, InterruptedException {

        Screenshot.screenshotGameState();
        if(!HomeNotifications.checkQuestNotification()) {
            log("No quest notifications.");
            return;
        }

        ButtonsHome.pressQuest();
        Thread.sleep(3000);
        Screenshot.screenshotGameState();

        while(HomeNotifications.checkTopQuestNotification()) {
            ButtonsHome.pressTopQuest();
            Thread.sleep(3000);
            Screenshot.screenshotGameState();
        }

        // TODO : PRESS TREASURE CHESTS
        // WRITE HERE
        // HERE
        // HERE

        if(!HomeNotifications.checkAchievementNotification()) {
            log("No achievement notifications.");
            ButtonsHome.closeQuest();
            Thread.sleep(3000);
            Screenshot.screenshotGameState();
            return;
        }

        ButtonsHome.pressAchievement();
        while(HomeNotifications.checkTopAchievementNotification()) {
            ButtonsHome.pressTopAchievement();
            Thread.sleep(1000);
        }

        log(" Auto quests complete.");
    }

}
