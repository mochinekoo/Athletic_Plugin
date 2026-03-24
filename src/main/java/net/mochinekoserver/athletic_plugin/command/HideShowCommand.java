package net.mochinekoserver.athletic_plugin.command;

import net.mochinekoserver.athletic_plugin.Main;
import net.mochinekoserver.athletic_plugin.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class HideShowCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender send, Command cmd, String s, String[] args) {
        Player player = (Player) send;
        if (cmd.getName().equalsIgnoreCase("hide")) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.hidePlayer(Main.getPlugin(Main.class), online);
            }
            PluginUtil.sendInfoMessage(player, "全プレイヤーを非表示にしました。");
        }
        else if (cmd.getName().equalsIgnoreCase("show")) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(Main.getPlugin(Main.class), online);
            }
            PluginUtil.sendInfoMessage(player, "全プレイヤーを表示にしました。");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender send, Command cmd, String s, String[] args) {
        return List.of();
    }

}
