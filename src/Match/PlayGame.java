package Match;

import Basic.Press;
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
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import static Logger.Logger.log;

public class PlayGame {

    public static void playGame(MainFrame mainFrame) throws IOException, InterruptedException {
        while (true) {
            Screenshot.screenshotGameState();

            while(!MatchBasic.isInLobby()) Screenshot.screenshotGameState();

            ButtonsHome.pressBattle();
            Thread.sleep(2000);
            ButtonsHome.pressMatch();

            // Wait for loading or match cancellation
            log("Waiting for game...");
            waitForGameToStart();

            // If match was cancelled, restart the process
            if (MatchBasic.isMatchCancelled()) {
                log("Match cancelled, restarting...");
                MatchBasic.pressReturn();
                continue;  // Restart the game loop from the top
            }

            GameBoard gameBoard = new GameBoard();
            log("Gameboard initialized...");

            log("6.5s timer started");
            Thread.sleep(6500);

            // First steps
            performEarlyBonusActions();
            Thread.sleep(2000);
            startGameRound(gameBoard);

            boolean upgradedSummoningLevel = false;

            // Game loop
            while (MatchBasic.isIngame()) {

                processBoard(gameBoard);

                if (gameBoard.isBoardComplete()) {
                    finishGameRound();
                    break;
                }

                handleMythicBuilding(gameBoard);

                gambleStones(gameBoard);

                handleEmergencySell(gameBoard);

                upgradedSummoningLevel = handleSummoning(gameBoard, upgradedSummoningLevel);

                gameBoard.saveBoardState();
            }

            // Match finished
            log("Match is finished.");
            MatchBasic.pressAnywhere();
            ButtonsHome.pressLobby();
            while(!MatchBasic.isInLobby()) Screenshot.screenshotGameState();
            Thread.sleep(2000);
        }
    }


    private static void waitForGameToStart() throws InterruptedException, IOException {
        // Wait for game to start or match to be canceled
        while (true) {
            if (MatchBasic.isMatchCancelled()) {
                log("Match has been cancelled. Restarting the game...");
                // Restart the waiting process if the match is cancelled
                return; // Exit this method and start over
            }

            if (MatchBasic.isInLobby() || MatchBasic.isFindingMatch() || MatchBasic.isLoading()) {
                Screenshot.screenshotGameState();
                Thread.sleep(1000); // Add a small delay to avoid excessive CPU usage
            } else {
                break; // Break out of the loop once the game has started
            }
        }
    }

    private static void startGameRound(GameBoard gameBoard) throws InterruptedException, IOException {
        MatchBasic.pressSummon10X();
        Screenshot.screenshotGameState();
    }

    private static void performEarlyBonusActions() throws InterruptedException {
        MatchBasic.pressUpgrade();
        Thread.sleep(500);
        MatchBasic.pressUpgradeCommon();
        Thread.sleep(500);
        MatchBasic.pressUpgradeEpic();
        Thread.sleep(500);
        MatchBasic.closeUpgrade();
    }

    public static void handleMythicBuilding(GameBoard gameBoard) throws InterruptedException {
        if (MythicBuilder.canBuild("Dragon", gameBoard)) {
            Thread.sleep(5000);
            MatchBasic.pressAnywhere();
            Thread.sleep(500);
            MatchBasic.pressBuildFavoriteMythic();
        }

        if (MythicBuilder.canBuild("Frog Prince", gameBoard)) {
            MatchBasic.pressAnywhere();
            Thread.sleep(5000);
            MatchBasic.pressAnywhere();
            Thread.sleep(500);
            MatchBasic.pressBuildFavoriteMythic();
        }
    }

    private static void handleEmergencySell(GameBoard gameBoard) throws IOException, InterruptedException {
        if (MatchBasic.checkIfMax()) {
            ProcessUnit.emergencySell(gameBoard);
            log("Executing emergency sell...");
        }
    }

    private static boolean handleSummoning(GameBoard gameBoard, boolean upgradedSummoningLevel) throws InterruptedException, IOException {
        if (gameBoard.shouldSummon()) {
            if (!upgradedSummoningLevel) {
                upgradedSummoningLevel = true;
                upgradeSummoningLevel();
            }

            MatchBasic.pressSummon10X();
        }
        return upgradedSummoningLevel;
    }

    private static void upgradeSummoningLevel() throws InterruptedException {
        MatchBasic.pressUpgrade();
        Thread.sleep(500);
        MatchBasic.pressUpgradeSummoning();
        Thread.sleep(500);
        MatchBasic.pressUpgradeSummoning();
        Thread.sleep(500);
        MatchBasic.pressUpgradeSummoning();
        Thread.sleep(500);
        MatchBasic.closeUpgrade();
        Thread.sleep(500);
    }

    private static void finishGameRound() throws InterruptedException, IOException {
        MatchBasic.pressUpgrade();
        Thread.sleep(2500);
        int timer = 2000;
        while (MatchBasic.isIngame()) {
            MatchBasic.pressUpgradeMythic();
            waitForGolem();
            Thread.sleep(timer);
            timer += 1000;
        }
    }


    public static void waitForGolem() throws InterruptedException, IOException {
        MatchBasic.pressGolem();
        Thread.sleep(1500);
        Screenshot.screenshotGameState();
        if (!MatchBasic.isGolemPresent()) {
            log("Golem not found.");
            return;
        }

        log("Golem can be challenged!");
        MatchBasic.challengeGolem();
    }

    private static void processBoard(GameBoard gameBoard) throws InterruptedException, IOException {
        Screenshot.screenshotGameState();
        List<int[]> validSquares = GameBoard.getNonEmptySquares();

        if (validSquares == null || validSquares.isEmpty()) {
            log("[Error] No valid squares found to process.");
            return;
        }

        for (int[] square : validSquares) {
            int i = square[0];
            int j = square[1];

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
            if (GameBoard.getSquare(i, j).getUnit() != null &&
                    GameBoard.getSquare(i, j).getUnit().getName() != null) {
                ProcessUnit.DetectUnitPlusProcess(gameBoard, i, j);
            }
        }
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
        Thread.sleep(500);
        checkForRewards();
        Screenshot.screenshotGameState();

        if (MatchBasic.checkIfMax())
            ProcessUnit.emergencySell(gameBoard);

        int luckyStones = MatchBasic.checkLuckyStones();

        for (int i = 0; i < luckyStones; i++) {
            MatchBasic.pressEpicGamble();
            checkForRewards();

            if (i % 5 == 0) {
                waitForGolem();
            }

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
