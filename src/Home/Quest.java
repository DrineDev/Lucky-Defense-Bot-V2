package Home;

import Basic.Screenshot;

import java.io.IOException;

public class Quest {

    /**
     * Automates quest reward retrieval
     * @throws IOException
     * @throws InterruptedException
     */
    public static void autoQuest() throws IOException, InterruptedException {
        Screenshot.screenshotGameState();
        if(!HomeNotifications.checkQuestNotification()) {
            System.out.println("No quest notifications...");
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
            System.out.println("No achievement notifications...");
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

        System.out.println("Auto quests complete...");
    }
}
