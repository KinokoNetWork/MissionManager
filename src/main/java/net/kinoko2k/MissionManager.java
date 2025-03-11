package net.kinoko2k;

import net.kinoko2k.Commands.DailyCommand;
import net.kinoko2k.Commands.DailyReloadCommand;
import net.kinoko2k.Missions.MissionListener;
import net.kinoko2k.Missions.MissionsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MissionManager extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        MissionsManager.setConfig(config);

        MissionsManager.loadNewMissions();
        saveConfig();

        getServer().getPluginManager().registerEvents(new MissionListener(), this);
        getServer().getPluginManager().registerEvents(new DailyCommand(), this);

        getCommand("daily").setExecutor(new DailyCommand());
        getCommand("reloaddaily").setExecutor(new DailyReloadCommand(this));

        Bukkit.getLogger().info("[MissionsPlugin] プラグインが有効化されました！");
    }

    @Override
    public void onDisable() {
        saveConfig();
        Bukkit.getLogger().info("[MissionsPlugin] プラグインが無効化されました！");
    }
}
