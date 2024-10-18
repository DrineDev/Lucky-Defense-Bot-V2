package Home;

import java.io.IOException;

public class Quest {
    public static void autoQuest() throws IOException, InterruptedException {
        if(!HomeNotifications.checkQuestNotification()) {
            System.out.println("No quest notifications...");
            return;
        }

        ButtonsHome.pressQuest();
        Thread.sleep(1000);
        while(HomeNotifications.checkTopQuestNotification()) {
            ButtonsHome.pressTopQuest();
            Thread.sleep(1000);
        }

        // TODO : PRESS TREASURE CHESTS
        // WRITE HERE
        // HERE
        // HERE

        if(!HomeNotifications.checkAchievementNotification()) {
            System.out.println("No achievement notifications...");
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
