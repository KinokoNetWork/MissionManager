package net.kinoko2k.Commands;

import net.kinoko2k.Missions.MissionsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DailyReloadCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public DailyReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("missionmanager.reload")) {
                player.sendMessage(ChatColor.RED + "あなたにはこのコマンドを実行する権限がありません！");
                return true;
            }
            player.sendMessage(ChatColor.GREEN + "ミッションがリセットされました！");
        } else {
            sender.sendMessage(ChatColor.GREEN + "コンソールからミッションをリセットしました！");
        }

        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        MissionsManager.setConfig(config);
        MissionsManager.resetMissions();
        plugin.saveConfig();

        return true;
    }
}