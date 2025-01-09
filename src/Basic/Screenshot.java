package Basic;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import Logger.Logger;

import static Logger.Logger.log;

public class Screenshot {
    private static final String SCREENSHOT_PATH = "Resources/GameState.png";

    /**
     * Screenshot game then store to Resources/GameState.png for processing
     * @throws IOException
     */
    public static void screenshotGameState() throws IOException {
        log("GameState.png updated...");
        Path outputPath = Paths.get(SCREENSHOT_PATH).toAbsolutePath();
        ProcessBuilder pb = new ProcessBuilder("adb", "exec-out", "screencap", "-p");
        pb.redirectOutput(outputPath.toFile());
        Process process = pb.start();
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("ADB command failed with exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("ADB command was interrupted", e);
        }
    }
}
