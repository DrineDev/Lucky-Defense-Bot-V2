package Match;

import Basic.Screenshot;
import GUI.MainFrame;
import Home.ButtonsHome;
import Match.GameBoard.GameBoard;
import Match.Units.ProcessUnit;
import GUI.IntroFrame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

public class PlayGame {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void playGame(MainFrame mainFrame) throws IOException, InterruptedException {
        while (true) {
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
            Screenshot.screenshotGameState();

            // GAME LOOP
            while (MatchBasic.isIngame()) {

                if(gameBoard.isBoardComplete()) {
                    while (MatchBasic.isIngame()) {
                        MatchBasic.closeGamble();
                        MatchBasic.closeMythic();
                        MatchBasic.closeUpgrade();

                        MatchBasic.pressUpgrade();
                        MatchBasic.pressUpgradeMythic();
                        waitForGolem(mainFrame);
                    }
                    break;
                }
                gameBoard = processBoard(gameBoard);
                if (MythicBuilder.canBuild("Dragon", gameBoard)) {
                    MatchBasic.pressAnywhere();
                    Thread.sleep(750);
                    MatchBasic.pressBuildFavoriteMythic();
                }

                if (MythicBuilder.canBuild("Frog Prince", gameBoard)) {
                    MatchBasic.pressAnywhere();
                    Thread.sleep(750);
                    MatchBasic.pressBuildFavoriteMythic();
                }

                gambleStones(gameBoard);

                waitForGolem(mainFrame);

                if (MatchBasic.checkIfMax()) {
                    ProcessUnit.emergencySell(gameBoard);
                    System.out.println("Emergency sell executing");
                }

                MatchBasic.pressSummon10X();
                gameBoard.saveBoardState();
            }

            // MATCH IS FINISHED
            appendColoredText(mainFrame, "Match is finished...", "red");
            MatchBasic.pressAnywhere();
            ButtonsHome.pressLobby();
        }
    }



    private static void waitForGolem(MainFrame mainFrame) throws InterruptedException {
        String currentTime = LocalDateTime.now().format(dtf);
        if (!MatchBasic.isGolemPresent()) {
            appendColoredText(mainFrame, "[" + currentTime + "]" + " Golem not found...\n", "red");
            return;
        }

        appendColoredText(mainFrame, "[" + currentTime + "]" + " Golem can be challenged!\n", "green");
        MatchBasic.pressGolem();
        Thread.sleep(1500);
        MatchBasic.challengeGolem();
    }

    private static GameBoard processBoard(final GameBoard gameBoard) throws InterruptedException, IOException {
        HashMap<Integer, Integer> nonEmptySquares = GameBoard.getNonEmptySquares();

        for (Map.Entry<Integer, Integer> entry : nonEmptySquares.entrySet()) {
            int i = entry.getKey();
            int j = entry.getValue();

            // Update the board for square (i, j)
            try {
                gameBoard.updateBoard(i, j);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error updating board at (" + i + ", " + j + "): " + e.getMessage(), e);
            }

            // Check for rewards
            try {
                checkForRewards();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error checking for rewards: " + e.getMessage(), e);
            }

            // Detect and process the unit if it exists at (i, j)
            if (gameBoard.getSquare(i, j).getUnit().getName() != null) {
                ProcessUnit.DetectUnitPlusProcess(gameBoard, i, j);
            }
        }

        return gameBoard;
    }

    private static void upgradeSummonLevel() throws InterruptedException {
        MatchBasic.pressUpgrade();
        Thread.sleep(1000);

        for (int i = 0; i < 13; i++) {
            MatchBasic.pressUpgradeSummoning();
            Thread.sleep(100);
        }

        MatchBasic.closeUpgrade();
    }

    public static void checkForRewards() throws IOException, InterruptedException {

        Screenshot.screenshotGameState();
        if (MatchBasic.isBossClear()) {
            MatchBasic.pressMostRight();
            Thread.sleep(1000);
            MatchBasic.pressSelect();
        }
    }

    private static void gambleStones(GameBoard gameBoard) throws InterruptedException, IOException {
        // MatchBasic.closeUpgrade();
        Thread.sleep(500);
        checkForRewards();
        Screenshot.screenshotGameState();
        if (MatchBasic.checkIfMax())
            ProcessUnit.emergencySell(gameBoard);

        int luckyStones = MatchBasic.checkLuckyStones();
        MatchBasic.pressGamble();
        Thread.sleep(1500);

        for (int i = 0; i < luckyStones; i++) {
            MatchBasic.pressEpicGamble();
            checkForRewards();
            Thread.sleep(500);
        }

        MatchBasic.closeGamble();
    }

    public static void appendColoredText(MainFrame mainFrame, String message, String color) {
        mainFrame.appendToPane(message, color);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SwingUtilities.invokeLater(IntroFrame::new);
    }
}
