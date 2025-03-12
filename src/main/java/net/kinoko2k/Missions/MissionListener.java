package net.kinoko2k.Missions;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.UUID;

public class MissionListener implements Listener {
    private final HashMap<UUID, Integer> playerNearbyTime = new HashMap<>();
    private final HashMap<UUID, Long> playerEnterTime = new HashMap<>();

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
            MissionsManager.updateMissionProgress("WALK_10KM", (int) event.getFrom().distance(event.getTo()));
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
    public void onItemDrop(PlayerDropItemEvent event) {
        MissionsManager.updateMissionProgress("DROP_2000_ITEMS", event.getItemDrop().getItemStack().getAmount());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getName().equalsIgnoreCase("Kinoko_2K")) {
            return;
        }

        Player target = Bukkit.getPlayer("Kinoko_2K");

        if (target != null && target.isOnline()) {
            Location playerLoc = player.getLocation();
            Location targetLoc = target.getLocation();

            // エラー回避用
            if (!playerLoc.getWorld().equals(targetLoc.getWorld())) {
                playerEnterTime.remove(player.getUniqueId());
                return;
            }

            if (playerLoc.distance(targetLoc) <= 3) {
                UUID playerId = player.getUniqueId();

                if (!playerEnterTime.containsKey(playerId)) {
                    playerEnterTime.put(playerId, System.currentTimeMillis());
                }

                if (playerEnterTime.containsKey(playerId)) {
                    long timeInside = (System.currentTimeMillis() - playerEnterTime.get(playerId)) / 1000;

                    if (timeInside >= 20) {
                        MissionsManager.updateMissionProgress("STAY_NEAR_KINOKO_20S", 1);
                        playerEnterTime.remove(playerId);
                    }
                }
            } else {
                playerEnterTime.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        MissionsManager.updateMissionProgress("OPEN_3000_INVENTORY", 1);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        MissionsManager.updateMissionProgress("CHAT_2500_TIMES", 1);
    }

    @EventHandler
    public void onCreeperExplode(ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            MissionsManager.updateMissionProgress("CREEPER_EXPLODE_30_TIMES", 1);
        }
    }

    @EventHandler
    public void onSheepDye(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Sheep) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (isDye(item)) {
                MissionsManager.updateMissionProgress("DYE_SHEEP_30_TIMES", 1);
            }
        }
    }

    private boolean isDye(ItemStack item) {
        if (item == null) return false;

        return switch (item.getType()) {
            case INK_SAC, RED_DYE, GREEN_DYE, LAPIS_LAZULI, COCOA_BEANS,
                 PURPLE_DYE, CYAN_DYE, LIGHT_GRAY_DYE, GRAY_DYE, PINK_DYE,
                 LIME_DYE, YELLOW_DYE, LIGHT_BLUE_DYE, MAGENTA_DYE, ORANGE_DYE, BONE_MEAL -> true;
            default -> false;
        };
    }
}