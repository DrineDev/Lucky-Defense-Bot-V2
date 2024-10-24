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
        // START MATCH
        ButtonsHome.pressBattle();
        ButtonsHome.pressMatch();

        // WAIT FOR LOADING
        appendColoredText(mainFrame,"Waiting for game...", "red");
        while(MatchBasic.isFindingMatch()) { Screenshot.screenshotGameState(); }
        while(MatchBasic.isLoading()) { Screenshot.screenshotGameState(); }

        // START OF GAME
//        waitFor90(mainFrame);
        GameBoard gameBoard = new GameBoard();
        Screenshot.screenshotGameState();

        // GAME LOOP
        while(MatchBasic.isIngame()) {
            gameBoard = processBoard(gameBoard);
            gambleStones(gameBoard);
            waitForGolem(mainFrame);

            if(MatchBasic.isMax()) { ProcessUnit.emergencySell(gameBoard); }

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
        Thread.sleep(45000);

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
        Thread.sleep(1000);
        MatchBasic.challengeGolem();
    }

    private static GameBoard processBoard(GameBoard gameBoard) throws InterruptedException, IOException {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 6; j++) {
                gameBoard.updateBoard(i, j);

                if(!(gameBoard.getSquare(i, j).getUnit().getName() == null))
                    ProcessUnit.DetectUnitPlusProcess(gameBoard, i, j);
            }
        }
        return gameBoard;
    }

    private static GameBoard spamSummon(GameBoard gameBoard) throws IOException, InterruptedException {
        for(int i = 0; i < 15; i++) {
            MatchBasic.pressSummon();
            Thread.sleep(250);
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

    private static void gambleStones(GameBoard gameBoard) throws InterruptedException, IOException {
        MatchBasic.closeGamble();
        Thread.sleep(750);
        MatchBasic.closeUpgrade();

        Screenshot.screenshotGameState();
        if(MatchBasic.isMax())
            ProcessUnit.emergencySell(gameBoard);
        if(!MatchBasic.isIngame())
            return;

        int luckyStones = MatchBasic.checkLuckyStones();
        Thread.sleep(1500);
        MatchBasic.pressGamble();
        Thread.sleep(1500);

        for (int i = 0; i < luckyStones; i++) {
            if (luckyStones > 10) {
                MatchBasic.pressLegendaryGamble();
                i += 2;
            }
            MatchBasic.pressEpicGamble();
            Thread.sleep(1250);
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
