package net.kinoko2k.Missions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class MissionsManager {
    private static final List<String> ALL_MISSIONS = Arrays.asList(
        "BREAK_2000_DIRT",
        "JUMP_5000_TIMES",
        "WALK_100KM",
        "KILL_300_MOBS",
        "FISH_500_TIMES",
        "CHAT_2500_TIMES"
    );

    private static List<String> activeMissions = new ArrayList<>();
    private static Map<String, Integer> missionProgress = new HashMap<>();
    
    public static void loadNewMissions(FileConfiguration config) {
        Random random = new Random();
        activeMissions.clear();
        missionProgress.clear();

        List<String> shuffledMissions = new ArrayList<>(ALL_MISSIONS);
        Collections.shuffle(shuffledMissions, random);
        
        activeMissions.addAll(shuffledMissions.subList(0, 3));
        for (String mission : activeMissions) {
            missionProgress.put(mission, 0);
        }

        config.set("missions.active", activeMissions);
        config.set("missions.progress", missionProgress);
    }

    public static List<String> getActiveMissions() {
        return activeMissions;
    }

    public static void updateMissionProgress(String mission, int amount) {
        if (activeMissions.contains(mission)) {
            missionProgress.put(mission, missionProgress.getOrDefault(mission, 0) + amount);
            checkMissionCompletion(mission);
        }
    }

    private static void checkMissionCompletion(String mission) {
        int goal = switch (mission) {
            case "BREAK_2000_DIRT" -> 2000;
            case "JUMP_5000_TIMES" -> 5000;
            case "WALK_100KM" -> 100000; // 1ブロックは1mに値するらしい
            case "KILL_300_MOBS" -> 300;
            case "FISH_500_TIMES" -> 500;
            case "CHAT_2500_TIMES" -> 2500;
            default -> Integer.MAX_VALUE;
        };

        if (missionProgress.get(mission) >= goal) {
            rewardPlayers();
            Bukkit.broadcastMessage("§aミッション達成: " + mission);
        }
    }

    public static Map<String, Integer> getMissionProgress() {
        return missionProgress;
    }

    public static int getMissionGoal(String mission) {
        return switch (mission) {
            case "BREAK_2000_DIRT" -> 2000;
            case "JUMP_5000_TIMES" -> 5000;
            case "WALK_100KM" -> 100000; // 1ブロックは1mに値するらしい
            case "KILL_300_MOBS" -> 300;
            case "FISH_500_TIMES" -> 500;
            case "CHAT_2500_TIMES" -> 2500;
            default -> Integer.MAX_VALUE;
        };
    }

    private static void rewardPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 3));
            player.sendMessage("§eミッション報酬として鉄3個を受け取りました！");
        }
    }
}
