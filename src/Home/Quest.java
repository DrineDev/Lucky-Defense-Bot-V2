package Home;

import Basic.Screenshot;
import GUI.MainFrame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Quest {

    /**
     * Automates quest reward retrieval
     * @throws IOException
     * @throws InterruptedException
     */

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void autoQuest() throws IOException, InterruptedException {
        String currentTime = LocalDateTime.now().format(dtf);

        Screenshot.screenshotGameState();
        if(!HomeNotifications.checkQuestNotification()) {
            System.out.println("[" + currentTime + "]" + " No quest notifications...");
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
            System.out.println("[" + currentTime + "]" + " No achievement notifications...");
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

        System.out.println("[" + currentTime + "]" + " Auto quests complete...");
    }

}
