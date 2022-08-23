package me.minercoffee.simpleminecraftbot.stafflog.ontask;


import me.minercoffee.simpleminecraftbot.Main;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AFKManager {

    private final HashMap<Player, Long> lastMovement = new HashMap<>();

    private final HashMap<Player, Boolean> previousData = new HashMap<>();

    public void playerJoined(Player player){
        lastMovement.put(player, System.currentTimeMillis());
    }

    public void playerLeft(Player player){
        lastMovement.remove(player);
    }


    public void playerMoved(Player player){

        lastMovement.put(player, System.currentTimeMillis());
        checkPlayerAFKStatus(player);
    }
    public boolean isAFK(Player player){

        if(lastMovement.containsKey(player)){
            if(lastMovement.get(player) == -1L){
                return true;
            }else{
                long timeElapsed = System.currentTimeMillis() - lastMovement.get(player) *-1;
                // means the player is afk
                //see if they have moved since 10 minute
                //600000
                long MOVEMENT_THRESHOLD = 600000L;
                return timeElapsed >= MOVEMENT_THRESHOLD;
            }
        }else {
            lastMovement.put(player, System.currentTimeMillis());
                }
        return false;
    }

    public void checkAllPlayersAFKStatus(){

        for (Map.Entry<Player, Long> entry : lastMovement.entrySet()){
            checkPlayerAFKStatus(entry.getKey());
        }
    }

    public void checkPlayerAFKStatus(Player player){
        if (lastMovement.containsKey(player)){

            boolean nowAFK = isAFK(player);

            if (previousData.containsKey(player)){

                boolean wasAFK = previousData.get(player);

                if(wasAFK && !nowAFK){
                    previousData.put(player, false);
                    Main.instance.playerLogListener.AfkclockIn(player.getUniqueId());

                }else if(!wasAFK && nowAFK){
                    Main.instance.playerLogListener.afkclockOut(player);
                    previousData.put(player, true);
                }

            }else{
                previousData.put(player, nowAFK);
            }

        }
    }

}