package Emulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmulatorBasic {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Finds a specific file on the system starting from a directory.
     */
    public static Path findFile(String startDir, String fileName) {
        final Path[] foundPath = {null};
        try {
            Files.walkFileTree(Paths.get(startDir), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.getFileName().toString().equalsIgnoreCase(fileName)) {
                        foundPath[0] = file.toAbsolutePath();
                        return FileVisitResult.TERMINATE; // Stop when found
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundPath[0];
    }

    /**
     * Adds ADB to the system PATH environment variable.
     */
    public static void addToPath(String adbPath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "cmd.exe", "/c", "setx PATH \"%PATH%;" + adbPath + "\""
            );
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("[INFO] ADB path added successfully: " + adbPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if ADB is available in the system path.
     */
    public static boolean isADBAvailable() {
        try {
            Process process = Runtime.getRuntime().exec("adb version");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output;
            while ((output = reader.readLine()) != null) {
                if (output.contains("Android Debug Bridge")) {
                    System.out.println("[INFO] ADB is already available in PATH.");
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("[INFO] ADB not found in PATH.");
        }
        return false;
    }

    /**
     * Checks if MuMuPlayer.exe exists at a known location.
     */
    public static boolean isMuMuPlayerAvailable() {
        Path muMuPath = findFile("C:\\", "MuMuPlayer.exe");
        if (muMuPath != null) {
            System.out.println("[INFO] MuMuPlayer found at: " + muMuPath);
            return true;
        }
        System.out.println("[INFO] MuMuPlayer not found.");
        return false;
    }

    /**
     * Main method to open the emulator.
     */
    public static void openEmulator() {
        String currentTime = LocalDateTime.now().format(dtf);
        System.out.println("[" + currentTime + "] Starting emulator setup...");

        boolean adbAvailable = isADBAvailable();
        boolean muMuAvailable = isMuMuPlayerAvailable();

        // Find ADB if not available
        if (!adbAvailable) {
            Path adbPath = findFile("C:\\", "adb.exe");
            if (adbPath != null) {
                addToPath(adbPath.getParent().toString());
                System.out.println("[INFO] ADB configured successfully.");
            } else {
                System.out.println("[ERROR] ADB not found. Aborting...");
                return;
            }
        }

        // Find MuMuPlayer if not available
        Path muMuPath = findFile("C:\\", "MuMuPlayer.exe");
        if (muMuPath == null) {
            System.out.println("[ERROR] MuMuPlayer.exe not found. Aborting...");
            return;
        }

        try {
            // Open MuMuPlayer
            Process process = Runtime.getRuntime().exec(muMuPath.toString());
            System.out.println("[" + currentTime + "] MuMuPlayer.exe launched.");
            Thread.sleep(5000);

            // Connect ADB
            process = Runtime.getRuntime().exec("adb connect 127.0.0.1:7555");
            System.out.println("[" + currentTime + "] ADB connected to emulator.");
            Thread.sleep(5000);

            // Set emulator screen size
            process = Runtime.getRuntime().exec("adb shell wm size 540x960");
            System.out.println("[" + currentTime + "] Emulator screen size set to 540x960.");
            Thread.sleep(5000);

        } catch (IOException | InterruptedException i) {
            System.out.println("[" + currentTime + "] Process opening failed: " + i.getMessage());
        }
    }
}

