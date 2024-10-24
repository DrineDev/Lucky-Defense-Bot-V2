package Match;

import Basic.Screenshot;
import GUI.MainFrame;
import Home.ButtonsHome;
import Match.GameBoard.GameBoard;
import Match.Units.ProcessUnit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayGame {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void playGame(MainFrame mainFrame) throws IOException, InterruptedException {
        //ButtonsHome.pressBattle();
        //ButtonsHome.pressMatch();
        while(MatchBasic.isFindingMatch())
            Screenshot.screenshotGameState();

        while(MatchBasic.isLoading())
            Screenshot.screenshotGameState();


        waitFor90(mainFrame);
        GameBoard gameBoard = new GameBoard();
        Thread.sleep(2000);
        Screenshot.screenshotGameState();

        while(MatchBasic.isIngame()) {
            gameBoard = readUnits(gameBoard);
            gambleEpic();
            Thread.sleep(1000);
            waitForGolem(mainFrame);

            gameBoard = readUnits(gameBoard);
            buildBatman(gameBoard);
            processUnits(gameBoard);

            spamSummon(gameBoard);
            gameBoard.saveBoardState();

            appendColoredText(mainFrame,"Match is finished...", "red");

        }
    }

    private static void waitFor90(MainFrame mainFrame) throws IOException {
        // wait for 90 monsters
        String currentTime = LocalDateTime.now().format(dtf);
        appendColoredText(mainFrame,"[" + currentTime + "]" + " Waiting for enemies...\n", "blue");
        while(!MatchBasic.is90enemies()) {
                Screenshot.screenshotGameState();
        }

        // spawn
        appendColoredText(mainFrame,"[" + currentTime + "]" + " Summoning now!\n", "green");
        for(int i = 0; i < 15; i++) {
            MatchBasic.pressSummon();
        }
    }

    private static void waitForGolem(MainFrame mainFrame) {
        String currentTime = LocalDateTime.now().format(dtf);
        if(!MatchBasic.isGolemPresent()) {
            appendColoredText(mainFrame, "[" + currentTime + "]" + " Golem not found...\n", "red");
            return;
        }

        appendColoredText(mainFrame, "[" + currentTime + "]" + " Golem can be challenged!\n", "green");
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

    public static void appendColoredText(MainFrame mainFrame, String message, String color) {
        mainFrame.appendToPane(message, color);
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        playGame();
//    }
}
