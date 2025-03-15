# Lucky Defense Bot V2

Lucky Defense Bot V2 is an automation project designed to farm resources in the game Lucky Defense. The bot automates gameplay in normal mode to collect recruitment scrolls and coins efficiently.

---

## Requirements

1. **ADB (Android Debug Bridge)**
   - Download ADB from the [official Android website](https://developer.android.com/tools/adb).
   - Add ADB to your system PATH:
     - **Windows**:
       1. Locate the directory where you extracted ADB.
       2. Open the Start menu, search for "Environment Variables," and select "Edit the system environment variables."
       3. Click "Environment Variables" and find the "Path" variable under "System variables."
       4. Click "Edit," then "New," and paste the path to the ADB directory.
       5. Click OK to save.
     - **Linux/macOS**:
       1. Add the ADB path to your `.bashrc`, `.zshrc`, or equivalent file by appending:
          ```
          export PATH=$PATH:/path/to/adb
          ```
       2. Save the file and run `source ~/.bashrc` or `source ~/.zshrc`.

2. **Mumu Emulator 12**
   - Download and install Mumu Player 12 from the [official website](https://www.mumuplayer.com/).

3. **Git**
   - Install Git from [git-scm.com](https://git-scm.com/).
   - Add Git to your environment PATH following similar steps as ADB.

4. **IntelliJ IDEA Community Edition**
   - Download and install IntelliJ IDEA Community Edition from [JetBrains](https://www.jetbrains.com/idea/download/). gwapo kaayo ko

---

## Setup Instructions

### 1. Clone the Project
   Open a terminal and run the following command to clone the project repository:
   ```
   git clone -b dev https://github.com/DrineDev/Lucky-Defense-Bot-V2.git
   ```

### 2. Prepare Mumu Emulator
   - Open Mumu Player 12.
   - Connect the emulator to ADB:
     ```
     adb connect 127.0.0.1:7555
     ```
   - Verify the connection:
     ```
     adb devices
     ```
     The emulator should appear in the list of connected devices.
   - Set the screen resolution for the emulator:
     ```
     adb shell wm size 540x960
     ```

### 3. Open the Project in IntelliJ IDEA
   - Launch IntelliJ IDEA Community Edition.
   - Open the cloned project directory.
   - Locate and run `Main.java` from the project files.

---

## Important Gameplay Settings

Before running the bot, ensure the following:

1. **Unit Skins**
   - Units in Lucky Defense must **not** have any skins equipped.

2. **Favorite Units**
   - The only favorite units should be **Frog Prince** and **Dragon**.

3. **Difficulty**
   - Set the game difficulty to **Normal**.

These settings are critical for the bot to function correctly.

---

## Update Instructions

1. Open a terminal in the bot's project directory.
2. Pull the latest updates from the repository:
   ```bash
   git pull origin dev
   ```
3. Restart the bot by running `Main.java` in IntelliJ IDEA.

---

## Reinstall Instructions

1. Delete the existing bot directory:
   ```bash
   rm -rf Lucky-Defense-Bot-V2
   ```
   (For Windows, use File Explorer or `rmdir /s /q Lucky-Defense-Bot-V2` in the command prompt.)
2. Re-clone the repository:
   ```bash
   git clone -b dev https://github.com/DrineDev/Lucky-Defense-Bot-V2.git
   ```
3. Follow the **Setup Instructions** to prepare and launch the bot again.

---

## Feedback and Reporting Issues

If you encounter any glitches or issues, please leave feedback or report them on the [GitHub Issues page](https://github.com/DrineDev/Lucky-Defense-Bot-V2/issues). Your input helps improve the project!

---

Enjoy automating your Lucky Defense farming experience!

