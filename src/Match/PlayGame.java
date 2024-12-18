package Match;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.SwingUtilities;

import GameBoard.GameBoard;
import Units.ProcessUnit;

public class PlayGame {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void playGame(MainFrame mainFrame) throws IOException, InterruptedException {
        while(true) {

            ButtonsHome.pressBattle();
            Thread.sleep(2000);
            ButtonsHome.pressMatch();

            // WAIT FOR LOADING
            appendColoredText(mainFrame, "Waiting for game...", "red");
            while (MatchBasic.isInLobby()) {
                Screenshot.screenshotGameState();
            }
            while (MatchBasic.isFindingMatch()) {
                Screenshot.screenshotGameState();
            }
            while (MatchBasic.isLoading()) {
                Screenshot.screenshotGameState();
            }

            // START OF GAME
            MatchBasic.pressSummon10X();

            GameBoard gameBoard = new GameBoard();
            int upgradePrice = 2;
            Screenshot.screenshotGameState();

            // GAME LOOP
            while (MatchBasic.isIngame()) {
                gameBoard = processBoard(gameBoard);
                if (MythicBuilder.canBuild("Bat Man", gameBoard)) {
                    MatchBasic.pressAnywhere();
                    Thread.sleep(750);
                    MatchBasic.pressBuildFavoriteMythic();
                }
                upgradePrice = gambleStones(gameBoard, upgradePrice);

                if (upgradePrice == 0) break;

                waitForGolem(mainFrame);

                if (MatchBasic.checkIfMax()) {
                    ProcessUnit.emergencySell(gameBoard);
                    System.out.println("Emergency sell executing");
                }

                spamSummon(gameBoard);
                gameBoard.saveBoardState();
            }

            // MATCH IS FINISHED
            appendColoredText(mainFrame, "Match is finished...", "red");
            MatchBasic.pressAnywhere();
            ButtonsHome.pressLobby();
        }
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
    private static int gambleStones(GameBoard gameBoard, int upgradePrice) throws InterruptedException, IOException {
//        MatchBasic.closeUpgrade();
        Thread.sleep(500);
        checkForRewards();
        Screenshot.screenshotGameState();
        if(MatchBasic.checkIfMax())
            ProcessUnit.emergencySell(gameBoard);
        if(!MatchBasic.isIngame())
            return 0;

        int luckyStones = MatchBasic.checkLuckyStones();
        MatchBasic.pressGamble();
        Thread.sleep(1500);

        for (int i = 0; i < luckyStones; i++) {
            if (luckyStones > 15) {
                MatchBasic.closeGamble();
                Thread.sleep(1500);
                MatchBasic.pressUpgrade();
                Thread.sleep(1500);
                MatchBasic.pressUpgradeMythic();
                upgradePrice += 1;
                i += upgradePrice;
            }
            MatchBasic.pressEpicGamble();
            checkForRewards();
            Thread.sleep(500);
        }

        MatchBasic.closeGamble();

        return upgradePrice;
    }

    // TODO : ALGORITHM FOR PLAYING GAME

    public static void appendColoredText(MainFrame mainFrame, String message, String color) {
        mainFrame.appendToPane(message, color);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SwingUtilities.invokeLater(IntroFrame::new);
    }
}
