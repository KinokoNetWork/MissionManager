package net.kinoko2k.Missions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class MissionsManager {
    private static FileConfiguration config;

    private static final List<String> ALL_MISSIONS = Arrays.asList(
            "BREAK_2000_DIRT",
            "JUMP_5000_TIMES",
            "WALK_10KM",
            "KILL_500_MOBS",
            "FISH_300_TIMES",
            "CHAT_2500_TIMES",
            "DROP_2000_ITEMS",
            "STAY_NEAR_KINOKO_20S",
            "OPEN_3000_INVENTORY"
    );

    private static List<String> activeMissions = new ArrayList<>();
    private static Map<String, Integer> missionProgress = new HashMap<>();
    private static final Set<String> completedMissions = new HashSet<>();

    public static void setConfig(FileConfiguration cfg) {
        config = cfg;
    }

    public static void loadNewMissions() {
        if (config == null) {
            Bukkit.getLogger().severe("[MissionsPlugin] 設定ファイルがロードされていません！");
            return;
        }

        Random random = new Random();
        activeMissions.clear();
        missionProgress.clear();

        List<String> shuffledMissions = new ArrayList<>(ALL_MISSIONS);
        Collections.shuffle(shuffledMissions, random);
        activeMissions.addAll(shuffledMissions.subList(0, 3));

        for (String mission : activeMissions) {
            missionProgress.put(mission, 0);
        }

        saveProgress();
    }

    public static void resetMissions() {
        loadNewMissions();
    }

    public static void updateMissionProgress(String mission, int amount) {
        if (!activeMissions.contains(mission)) return;

        int currentProgress = missionProgress.getOrDefault(mission, 0);
        int goal = getMissionGoal(mission);

        if (currentProgress >= goal) return;

        missionProgress.put(mission, Math.min(currentProgress + amount, goal));
        saveProgress();
        checkMissionCompletion(mission);
    }

    private static void saveProgress() {
        if (config == null) return;
        config.set("missions.active", activeMissions);
        config.set("missions.progress", missionProgress);
        Bukkit.getPluginManager().getPlugin("MissionManager").saveConfig();
    }

    private static void checkMissionCompletion(String mission) {
        int goal = getMissionGoal(mission);
        if (missionProgress.get(mission) >= goal) {
            if (!completedMissions.contains(mission)) {
                rewardPlayers();
                completedMissions.add(mission);
                Bukkit.broadcastMessage("§aミッション達成: " + mission);
            }
        }
    }

    private static void rewardPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1));
            player.sendMessage("§eミッション報酬として鉄1個を受け取りました！");
        }
    }

    public static Map<String, Integer> getMissionProgress() {
        return missionProgress;
    }

    public static List<String> getActiveMissions() {
        return activeMissions;
    }

    public static int getMissionGoal(String mission) {
        return switch (mission) {
            case "BREAK_2000_DIRT" -> 2000;
            case "JUMP_5000_TIMES" -> 5000;
            case "WALK_10KM" -> 10000;
            case "KILL_500_MOBS" -> 500;
            case "FISH_300_TIMES" -> 300;
            case "CHAT_2500_TIMES" -> 2500;
            case "DROP_2000_ITEMS" -> 2000;
            case "STAY_NEAR_KINOKO_20S" -> 1;
            case "OPEN_3000_INVENTORY" -> 3000;
            default -> Integer.MAX_VALUE;
        };
    }
}
