package Home;

import Basic.Coordinates;
import Basic.PixelColorChecker;

import java.awt.*;

public class HomeNotifications {

    // General function to check if there is a notification
    private static boolean checkNotification(Coordinates coordinates) {
        Color notificationColor = new Color(247, 30, 81);
        return PixelColorChecker.checkColorMatch(coordinates, notificationColor, "Resources/GameState.png", 5);
    }

    public static boolean checkQuestNotification() {
        Coordinates coordinates = new Coordinates(516, 181);
        return checkNotification(coordinates);
    }

    public static boolean checkRecruitNotification() {
        Coordinates coordinates = new Coordinates(516, 103);
        return !checkNotification(coordinates);
    }

    public static boolean checkRecruit1XNotification() {
        Coordinates coordinates = new Coordinates(477, 737);
        return checkNotification(coordinates);
    }

    public static boolean checkRecruit10XNotification() {
        Coordinates coordinates = new Coordinates(255, 737);
        return checkNotification(coordinates);
    }

    public static boolean checkInboxNotification() {
        Coordinates coordinates = new Coordinates(516, 55);
        return checkNotification(coordinates);
    }

    public static boolean checkHunterPassNotification() {
        Coordinates coordinates = new Coordinates(516, 293);
        return checkNotification(coordinates);
    }

    public static boolean checkShopNotification() {
        Coordinates coordinates = new Coordinates(80, 885);
        return checkNotification(coordinates);
    }

    public static boolean checkBattleNotification() {
        Coordinates coordinates = new Coordinates(312, 870);
        return checkNotification(coordinates);
    }

    public static boolean checkMailBoxNotification() {
        Coordinates coordinates = new Coordinates(501, 330);
        return checkNotification(coordinates);
    }

    public static boolean checkReceiveNotification() {
        Coordinates coordinates = new Coordinates(463, 267);
        return checkNotification(coordinates);
    }

    public static boolean checkTopQuestNotification() {
        Coordinates coordinates = new Coordinates(464, 334);
        return checkNotification(coordinates);
    }

    public static boolean checkAchievementNotification() {
        Coordinates coordinates = new Coordinates(483, 783);
        return checkNotification(coordinates);
    }

    public static boolean checkTopAchievementNotification() {
        Coordinates coordinates = new Coordinates(463, 204);
        return checkNotification(coordinates);
    }
}
