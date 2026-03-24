package net.mochinekoserver.athletic_plugin;

import net.mochinekoserver.athletic_plugin.command.HideShowCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("hide").setExecutor(new HideShowCommand());
        getCommand("show").setExecutor(new HideShowCommand());

        getLogger().info("プラグインの初期化が完了しました。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
