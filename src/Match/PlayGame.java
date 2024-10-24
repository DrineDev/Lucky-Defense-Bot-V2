package Match;

import Basic.Screenshot;
import Home.ButtonsHome;
import Match.GameBoard.GameBoard;
import Match.Units.ProcessUnit;

import java.io.IOException;

public class PlayGame {

    public static void playGame() throws IOException, InterruptedException {
        ButtonsHome.pressBattle();
        ButtonsHome.pressMatch();
        while(MatchBasic.isFindingMatch())
            Screenshot.screenshotGameState();

        while(MatchBasic.isLoading())
            Screenshot.screenshotGameState();

        GameBoard gameBoard = new GameBoard();
        waitFor90();

        while(MatchBasic.isIngame()) {
            gameBoard = readUnits(gameBoard);
            gambleEpic();
            waitForGolem();

            gameBoard = readUnits(gameBoard);
            buildBatman(gameBoard);
            processUnits(gameBoard);

            spamSummon(gameBoard);
            gameBoard.saveBoardState();
        }

        System.out.println("Match is finished...");
    }

    private static void waitFor90() {
        // wait for 90 monsters
        while(!MatchBasic.is90enemies()) {
            System.out.println("Waiting for enemies...");
            try {
                Screenshot.screenshotGameState();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // spawn
        System.out.println("Summoning now!");
        for(int i = 0; i < 15; i++) {
            MatchBasic.pressSummon();
        }
    }

    private static void waitForGolem() {
        if(!MatchBasic.isGolemPresent()) {
            System.out.println("Golem not found...");
            return;
        }

        System.out.println("Golem can be challenged!");
        MatchBasic.pressGolem();
        try {
            Thread.sleep(1000);
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

    private static void gambleEpic() throws InterruptedException {
        int luckyStones = MatchBasic.checkLuckyStones();
        MatchBasic.pressGamble();

        for(int i = 0; i < luckyStones; i++) {
            MatchBasic.pressEpicGamble();
            Thread.sleep(2000);
        }
    }

    private static void buildBatman(GameBoard gameBoard) throws IOException, InterruptedException {
        if(MythicBuilder.canBuild("Batman", gameBoard))
            MatchBasic.pressBuildFavoriteMythic();

        gameBoard.updateBoard();
        gameBoard = processUnits(gameBoard);

    }
    // TODO : ALGORITHM FOR PLAYING GAME

    public static void main(String[] args) throws IOException, InterruptedException {
        playGame();
    }
}
