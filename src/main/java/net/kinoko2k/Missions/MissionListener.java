package net.kinoko2k.Missions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.inventory.TradeSelectEvent;

public class MissionListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.DIRT) {
            MissionsManager.updateMissionProgress("BREAK_2000_DIRT", 1);
        }
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        MissionsManager.updateMissionProgress("JUMP_5000_TIMES", 1);
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent event) {
        if (event.getFrom().distance(event.getTo()) > 0) {
            MissionsManager.updateMissionProgress("WALK_100KM", (int) event.getFrom().distance(event.getTo()));
        }
    }

    @EventHandler
    public void onKillMob(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            MissionsManager.updateMissionProgress("KILL_500_MOBS", 1);
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            MissionsManager.updateMissionProgress("FISH_500_TIMES", 1);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        MissionsManager.updateMissionProgress("CHAT_2500_TIMES", 1);
    }
}
