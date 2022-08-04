package me.minercoffee.simpleminecraftbot.stafflog.ontask;

public class MovementChecker implements Runnable {
    private final AFKManager afkManager;
    public MovementChecker(AFKManager afkManager) {
        this.afkManager = afkManager;
    }

    @Override
    public void run() {
        afkManager.checkAllPlayersAFKStatus();
    }
}