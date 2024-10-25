package Match;

import Basic.Screenshot;
import GUI.IntroFrame;
import GUI.MainFrame;
import Home.ButtonsHome;
import Match.GameBoard.GameBoard;
import Match.Units.ProcessUnit;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayGame {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void playGame(MainFrame mainFrame) throws IOException, InterruptedException {
//        ButtonsHome.pressBattle();
//        Thread.sleep(2000);
//        ButtonsHome.pressWithFriend();
//        Thread.sleep(2000);
//        ButtonsHome.pressCreateRoom();
//
//        // WAIT FOR LOADING
//        appendColoredText(mainFrame,"Waiting for game...", "red");
//        while(MatchBasic.isInLobby()) { Screenshot.screenshotGameState(); }
//        while(MatchBasic.isFindingMatch()) { Screenshot.screenshotGameState(); }
//        while(MatchBasic.isLoading()) { Screenshot.screenshotGameState(); }
//
//        // START OF GAME
//        waitFor90(mainFrame);
        GameBoard gameBoard = new GameBoard();
        Screenshot.screenshotGameState();

        // GAME LOOP
        while(MatchBasic.isIngame()) {
            gameBoard = processBoard(gameBoard);
            if(MythicBuilder.canBuild("Bat Man", gameBoard)) {
                MatchBasic.pressAnywhere();
                Thread.sleep(750);
                MatchBasic.pressBuildFavoriteMythic();
            }
            gambleStones(gameBoard);
            waitForGolem(mainFrame);

            if(MatchBasic.checkIfMax()) { ProcessUnit.emergencySell(gameBoard);System.out.println("Emergency sell executing"); }

            spamSummon(gameBoard);
            gameBoard.saveBoardState();
        }

        // MATCH IS FINISHED
        appendColoredText(mainFrame,"Match is finished...", "red");
    }

    private static void waitFor90(MainFrame mainFrame) throws IOException, InterruptedException {
        // wait for 90 monsters
        String currentTime = LocalDateTime.now().format(dtf);
        appendColoredText(mainFrame,"[" + currentTime + "]" + " Waiting for enemies...\n", "blue");
        while(!MatchBasic.is90enemies()) { Screenshot.screenshotGameState(); }

        // spawn
        appendColoredText(mainFrame,"[" + currentTime + "]" + " Summoning now!\n", "green");
        for(int i = 0; i < 15; i++) { MatchBasic.pressSummon(); }
    }

    private static void waitForGolem(MainFrame mainFrame) throws InterruptedException {
        String currentTime = LocalDateTime.now().format(dtf);
        if(!MatchBasic.isGolemPresent()) {
            appendColoredText(mainFrame, "[" + currentTime + "]" + " Golem not found...\n", "red");
            return;
        }

        appendColoredText(mainFrame, "[" + currentTime + "]" + " Golem can be challenged!\n", "green");
        MatchBasic.pressGolem();
        Thread.sleep(1500);
        MatchBasic.challengeGolem();
    }

    private static GameBoard processBoard(GameBoard gameBoard) throws InterruptedException, IOException {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 6; j++) {
                gameBoard.updateBoard(i, j);
                checkForRewards();
                if(!(gameBoard.getSquare(i, j).getUnit().getName() == null))
                    gameBoard = ProcessUnit.DetectUnitPlusProcess(gameBoard, i, j);
            }
        }
        return gameBoard;
    }

    private static GameBoard spamSummon(GameBoard gameBoard) throws IOException, InterruptedException {
        for(int i = 0; i < 15; i++) {
            MatchBasic.pressSummon();
            checkForRewards();
        }
        Thread.sleep(1000);
        return gameBoard;
    }

    private static void upgradeSummonLevel() throws InterruptedException {
        MatchBasic.pressUpgrade();
        Thread.sleep(1000);

        for(int i = 0; i < 13; i++) {
            MatchBasic.pressUpgradeSummoning();
            Thread.sleep(250);
        }

        MatchBasic.closeUpgrade();
    }
    public static void checkForRewards() throws IOException, InterruptedException {

        Screenshot.screenshotGameState();
        if(MatchBasic.isBossClear()) {
            MatchBasic.pressMostRight();
            Thread.sleep(1000);
            MatchBasic.pressSelect();
        }
    }
    private static void gambleStones(GameBoard gameBoard) throws InterruptedException, IOException {
//        MatchBasic.closeUpgrade();
        Thread.sleep(500);
        checkForRewards();
        Screenshot.screenshotGameState();
        if(MatchBasic.checkIfMax())
            ProcessUnit.emergencySell(gameBoard);
        if(!MatchBasic.isIngame())
            return;

        int luckyStones = MatchBasic.checkLuckyStones();
        MatchBasic.pressGamble();
        Thread.sleep(1500);

        for (int i = 0; i < luckyStones; i++) {
            if (luckyStones > 10) {
                MatchBasic.pressLegendaryGamble();
                i += 2;
            }
            MatchBasic.pressEpicGamble();
            checkForRewards();
            Thread.sleep(500);
        }

        MatchBasic.closeGamble();
    }

    // TODO : ALGORITHM FOR PLAYING GAME

    public static void appendColoredText(MainFrame mainFrame, String message, String color) {
        mainFrame.appendToPane(message, color);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SwingUtilities.invokeLater(IntroFrame::new);
    }
}
