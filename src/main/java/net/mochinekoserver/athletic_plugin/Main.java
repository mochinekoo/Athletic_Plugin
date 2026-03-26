package net.mochinekoserver.athletic_plugin;

import net.mochinekoserver.athletic_plugin.command.HideShowCommand;
import net.mochinekoserver.athletic_plugin.command.TimerCommand;
import net.mochinekoserver.athletic_plugin.listener.PlayerJoinLeaveListener;
import net.mochinekoserver.athletic_plugin.manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("hide").setExecutor(new HideShowCommand());
        getCommand("show").setExecutor(new HideShowCommand());

        getCommand("start_mytimer").setExecutor(new TimerCommand());
        getCommand("stop_mytimer").setExecutor(new TimerCommand());
        getCommand("start_globaltimer").setExecutor(new TimerCommand());
        getCommand("stop_globaltimer").setExecutor(new TimerCommand());

        PluginManager plm = getServer().getPluginManager();
        plm.registerEvents(new PlayerJoinLeaveListener(), this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            ScoreboardManager.getInstance(player.getUniqueId()).setScoreboard();
        }

        getLogger().info("プラグインの初期化が完了しました。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
