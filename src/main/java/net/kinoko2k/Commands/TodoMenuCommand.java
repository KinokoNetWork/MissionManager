package net.kinoko2k.Commands;

import net.kinoko2k.Missions.MissionsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TodoMenuCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみ実行可能です！");
            return true;
        }

        Player player = (Player) sender;
        openTodoMenu(player);
        return true;
    }

    public void openTodoMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GREEN + "今日のミッション");

        List<String> missions = MissionsManager.getActiveMissions();
        Map<String, Integer> progress = MissionsManager.getMissionProgress();

        for (int i = 0; i < missions.size(); i++) {
            String missionKey = missions.get(i);
            int currentProgress = progress.getOrDefault(missionKey, 0);
            int goal = MissionsManager.getMissionGoal(missionKey);

            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + getMissionName(missionKey));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "達成状況: " + ChatColor.AQUA + currentProgress + "/" + goal);
            meta.setLore(lore);

            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        player.openInventory(inv);
    }

    private String getMissionName(String missionKey) {
        return switch (missionKey) {
            case "BREAK_2000_DIRT" -> "土ブロックを 2000 個壊す";
            case "JUMP_5000_TIMES" -> "ジャンプを 5000 回する";
            case "WALK_100KM" -> "100km 以上歩く";
            case "KILL_300_MOBS" -> "モンスターを 300 体倒す";
            case "FISH_500_TIMES" -> "魚を 500 匹釣る";
            case "CHAT_2500_TIMES" -> "チャットを 2500 回送信する";
            default -> "不明なミッション";
        };
    }

    @org.bukkit.event.EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GREEN + "今日のミッション")) {
            event.setCancelled(true);
        }
    }
}
