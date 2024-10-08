import Emulator.EmulatorBasic;
import Home.buttonsHome;

public class Main {

    public static void main(String[] args) {
        buttonsHome.pressRecruit();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buttonsHome.pressRecruit1X();
    }
}
