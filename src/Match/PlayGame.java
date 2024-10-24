package Match;

import Basic.Screenshot;
import GUI.MainFrame;
import Home.ButtonsHome;
import Match.GameBoard.GameBoard;
import Match.Units.ProcessUnit;

import java.io.IOException;

public class PlayGame {

    private static MainFrame mainFrame = new MainFrame();

    public static void playGame() throws IOException, InterruptedException {
        //ButtonsHome.pressBattle();
        //ButtonsHome.pressMatch();
        while(MatchBasic.isFindingMatch())
            Screenshot.screenshotGameState();

        while(MatchBasic.isLoading())
            Screenshot.screenshotGameState();


        waitFor90();
        GameBoard gameBoard = new GameBoard();
        Thread.sleep(2000);
        Screenshot.screenshotGameState();

        while(MatchBasic.isIngame()) {
            gameBoard = readUnits(gameBoard);
            gambleEpic();
            Thread.sleep(1000);
            waitForGolem();

            gameBoard = readUnits(gameBoard);
            buildBatman(gameBoard);
            processUnits(gameBoard);

            spamSummon(gameBoard);
            gameBoard.saveBoardState();

            appendColoredText("Match is finished...", "red");

        }
    }

    private static void waitFor90() throws IOException {
        // wait for 90 monsters
        appendColoredText("Waiting for enemies...\n", "blue");
        while(!MatchBasic.is90enemies()) {
                Screenshot.screenshotGameState();
        }

        // spawn
        appendColoredText("Summoning now!\n", "green");
        for(int i = 0; i < 15; i++) {
            MatchBasic.pressSummon();
        }
    }

    private static void waitForGolem() {
        if(!MatchBasic.isGolemPresent()) {
            appendColoredText("Golem not found...\n", "red");
            return;
        }

        appendColoredText("Golem can be challenged!\n", "green");
        MatchBasic.pressGolem();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        MatchBasic.challengeGolem();
        return;
    }

    private static GameBoard readUnits(GameBoard gameBoard) throws IOException, InterruptedException {
        gameBoard.updateBoard();
        gameBoard.saveBoardState();
        return gameBoard;
    }

    private static GameBoard processUnits(GameBoard gameBoard) {
        ProcessUnit.DetectUnitPlusProcess(gameBoard);
        return gameBoard;
    }

    private static GameBoard spamSummon(GameBoard gameBoard) throws IOException, InterruptedException {
        for(int i = 0; i < 15; i++) {
            MatchBasic.pressSummon();
        }

        return gameBoard;
    }

    private static void upgradeSummonLevel() {
        for(int i = 0; i < 13; i++)
            MatchBasic.pressUpgradeSummoning();
    }

    private static void gambleEpic() throws InterruptedException, IOException {
        int luckyStones = MatchBasic.checkLuckyStones();
        Thread.sleep(2000);
        MatchBasic.pressGamble();
        Thread.sleep(2000);

        for(int i = 0; i < luckyStones; i++) {
            MatchBasic.pressEpicGamble();
            Thread.sleep(2000);
        }

        MatchBasic.closeGamble();
    }

    private static void buildBatman(GameBoard gameBoard) throws IOException, InterruptedException {
        if(MythicBuilder.canBuild("Batman", gameBoard))
            MatchBasic.pressBuildFavoriteMythic();

        gameBoard.updateBoard();
        gameBoard = processUnits(gameBoard);

    }
    // TODO : ALGORITHM FOR PLAYING GAME

    private static void appendColoredText(String message, String color) {
        mainFrame.appendToPane(message, color);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        playGame();
    }
}
