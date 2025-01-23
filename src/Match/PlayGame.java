package Match;

import Basic.Screenshot;
import GUI.MainFrame;
import Home.ButtonsHome;
import Match.GameBoard.GameBoard;
import Match.Units.ProcessUnit;

import java.io.IOException;
import java.util.List;

import static Logger.Logger.log;

public class PlayGame {

    private static final int GAME_START_WAIT = 2000;
    private static final int INITIAL_GAME_DELAY = 4000;
    private static final int SHORT_DELAY = 500;
    private static final int STANDARD_DELAY = 1000;
    private static int upgradeCost = 2;

    public static void playGame(MainFrame mainFrame) throws IOException, InterruptedException {
        while (true) {
            try {
                if (!handleGameStart()) {
                    continue;  // Match was cancelled, restart the loop
                }

                // Game started successfully, initialize board and begin play
                GameBoard gameBoard = new GameBoard();
                log("Gameboard initialized...");

                log(INITIAL_GAME_DELAY + "ms timer started");
                Thread.sleep(INITIAL_GAME_DELAY);

                if (!handleGameplay(gameBoard)) {
                    safeReturnToLobby();
                    continue;  // If game ended unexpectedly, restart the loop
                }

                // Handle end of match
                handleMatchEnd();
            } catch (Exception e) {
                log("Error in game loop: " + e.getMessage());
                // Attempt to return to lobby on error
                safeReturnToLobby();
            }
        }
    }

    private static boolean handleGameStart() throws Exception {
        Screenshot.screenshotGameState();

        // Ensure we're in lobby before starting
        while (!MatchBasic.isInLobby()) {
            Screenshot.screenshotGameState();
            Thread.sleep(SHORT_DELAY);
        }

        // Start match search
        ButtonsHome.pressBattle();
        Thread.sleep(GAME_START_WAIT);
        ButtonsHome.pressMatch();

        return waitForGameToStart();
    }

    private static boolean waitForGameToStart() throws Exception {
        log("Waiting for game...");
        long startTime = System.currentTimeMillis();
        int checkInterval = 500; // Check every 500ms
        int timeout = 60000; // 60 second timeout

        while (System.currentTimeMillis() - startTime < timeout) {
            Screenshot.screenshotGameState();

            // Check match cancelled first
            if (MatchBasic.isMatchCancelled()) {
                log("Match cancelled, returning to lobby...");
                MatchBasic.pressReturn();
                return false;
            }

            // Check if we're in game
            if (!MatchBasic.isInLobby() && !MatchBasic.isFindingMatch() && !MatchBasic.isLoading()) {
                log("Game started successfully");
                return true;
            }

            Thread.sleep(checkInterval);
        }

        log("Timeout waiting for game to start");
        safeReturnToLobby();
        return false;
    }

    private static boolean handleGameplay(GameBoard gameBoard) throws IOException, InterruptedException {
        // Initial setup
        performEarlyBonusActions();
        Thread.sleep(GAME_START_WAIT);
        startGameRound(gameBoard);

        boolean upgradedSummoningLevel = false;
        int idleCounter = 0;

        // Main game loop
        while (MatchBasic.isIngame()) {
            boolean boardChanged = processBoard(gameBoard);

            if (gameBoard.isBoardComplete()) {
                finishGameRound();
                return true;
            }

            handleMythicBuilding(gameBoard);
            upgradeCost = gambleStones(gameBoard, upgradeCost);
            upgradedSummoningLevel = handleSummoning(gameBoard, upgradedSummoningLevel);
        }

        return false;
    }

    private static boolean processBoard(GameBoard gameBoard) throws IOException, InterruptedException {
        Screenshot.screenshotGameState();
        List<int[]> validSquares = GameBoard.getNonEmptySquares();
        boolean boardChanged = false;

        if (validSquares == null || validSquares.isEmpty()) {
            return false;
        }

        for (int[] square : validSquares) {
            int i = square[0];
            int j = square[1];

            if (gameBoard.updateBoard(i, j)) {
                boardChanged = true;
            }

            checkForRewards();

            if (GameBoard.getSquare(i, j).getUnit() != null &&
                    GameBoard.getSquare(i, j).getUnit().getName() != null) {
                ProcessUnit.DetectUnitPlusProcess(gameBoard, i, j);
            }
        }

        return boardChanged;
    }

    private static void safeReturnToLobby() {
        log("Returning to lobby.");

        try {
            ButtonsHome.pressLobby();
            Thread.sleep(GAME_START_WAIT);
        } catch (Exception e) {
            log("Error returning to lobby: " + e.getMessage());
        }
    }

    // Keeping existing helper methods but with improved error handling
    private static void handleMatchEnd() throws IOException, InterruptedException {
        log("Match is finished.");
        MatchBasic.pressAnywhere();
        ButtonsHome.pressLobby();

        // Wait for return to lobby with timeout
        long startTime = System.currentTimeMillis();
        while (!MatchBasic.isInLobby() && System.currentTimeMillis() - startTime < 10000) {
            Screenshot.screenshotGameState();
            Thread.sleep(SHORT_DELAY);
        }

        Thread.sleep(GAME_START_WAIT);
    }

    private static void startGameRound(GameBoard gameBoard) throws InterruptedException, IOException {
        MatchBasic.press2X();
        MatchBasic.pressSummon10X();
        Screenshot.screenshotGameState();
    }

    private static void performEarlyBonusActions() throws InterruptedException {
        MatchBasic.pressUpgrade();
        Thread.sleep(500);
        MatchBasic.pressUpgradeCommon();
        Thread.sleep(100);
        MatchBasic.pressUpgradeEpic();
        Thread.sleep(100);
        MatchBasic.closeUpgrade();
    }

    public static void handleMythicBuilding(GameBoard gameBoard) throws InterruptedException {
        buildFrog(gameBoard);
        buildDragon(gameBoard);
    }

    public static void buildFrog(GameBoard gameBoard) throws InterruptedException {
        MythicBuilder.buildMythic("Frog Prince", gameBoard);
    }

    public static void buildDragon(GameBoard gameBoard) throws InterruptedException {
        MythicBuilder.buildMythic("Dragon", gameBoard);
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
        Thread.sleep(100);
        MatchBasic.pressUpgradeSummoning();
        Thread.sleep(100);
        MatchBasic.pressUpgradeSummoning();
        Thread.sleep(100);
        MatchBasic.closeUpgrade();
        Thread.sleep(500);
    }

    private static void finishGameRound() throws InterruptedException, IOException {
        MatchBasic.pressUpgrade();
        Thread.sleep(2500);
        int timer = 2000;
        while (MatchBasic.isIngame()) {
            MatchBasic.pressUpgradeMythic();
            upgradeCost++;
            waitForGolem();
            Thread.sleep(timer);
            timer += 1000;
        }

        upgradeCost = 2;
    }

    public static void waitForGolem() throws InterruptedException, IOException {
        MatchBasic.pressGolem();
        Thread.sleep(500);
        Screenshot.screenshotGameState();
        if (!MatchBasic.isGolemPresent()) {
            log("Golem not found.");
            return;
        }

        log("Golem can be challenged!");
        MatchBasic.challengeGolem();
    }

    public static void checkForRewards() throws IOException, InterruptedException {

        Screenshot.screenshotGameState();
        if (MatchBasic.isBossClear()) {
            MatchBasic.pressMostRight();
            Thread.sleep(1000);
            MatchBasic.pressSelect();
        }
    }

    private static int gambleStones(GameBoard gameBoard, int upgradeCost) throws InterruptedException, IOException {
        MatchBasic.pressGamble();
        Thread.sleep(500);
        Screenshot.screenshotGameState();

        if (MatchBasic.checkIfMax()) {
            log("Executing emergency sell...");
            ProcessUnit.emergencySell(gameBoard);
        } else {
            int luckyStones = MatchBasic.checkLuckyStones();

            if (luckyStones > 20) {
                upgradeCost = upgradeMythicLevel(upgradeCost);
            }

            for (int i = 0; i < luckyStones; i++) {
                MatchBasic.pressEpicGamble();

                if (i % 5 == 0) {
                    waitForGolem();
                }

                if (i % 4 == 0) {
                    checkForRewards();
                }
            }

            MatchBasic.closeGamble();
        }

        return upgradeCost;
    }

    private static int upgradeMythicLevel(int upgradeCost) throws InterruptedException {
        MatchBasic.closeGamble();
        Thread.sleep(500);
        MatchBasic.pressUpgrade();
        Thread.sleep(500);
        MatchBasic.pressUpgradeMythic();
        Thread.sleep(100);
        MatchBasic.pressUpgradeMythic();
        Thread.sleep(100);
        MatchBasic.closeUpgrade();
        Thread.sleep(500);
        MatchBasic.pressGamble();
        upgradeCost += 2;
        return upgradeCost;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Screenshot.screenshotGameState();
    }

}
