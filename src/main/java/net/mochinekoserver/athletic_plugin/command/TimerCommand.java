package net.mochinekoserver.athletic_plugin.command;

import net.mochinekoserver.athletic_plugin.manager.ScoreboardManager;
import net.mochinekoserver.athletic_plugin.manager.TimeManager;
import net.mochinekoserver.athletic_plugin.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender send, Command cmd, String s, String[] args) {
        Player player = (Player) send;
        ScoreboardManager scoreboardManager = ScoreboardManager.getInstance(player.getUniqueId());
        TimeManager timeManager = TimeManager.getInstance(player.getUniqueId());
        if (cmd.getName().equalsIgnoreCase("start_mytimer")) {
            timeManager.startTimer();
            PluginUtil.sendInfoMessage(player, "自分のタイマーを開始させました。");
        }
        else if (cmd.getName().equalsIgnoreCase("stop_mytimer")) {
            timeManager.stopTimer();
            PluginUtil.sendInfoMessage(player, "自分のタイマーを停止させました。");
        }
        if (cmd.getName().equalsIgnoreCase("start_globaltimer")) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                TimeManager.getInstance(online.getUniqueId()).startTimer();
            }
            PluginUtil.sendInfoMessage(player, "全体のタイマーを開始させました。");
        }
        else if (cmd.getName().equalsIgnoreCase("stop_globaltimer")) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                TimeManager.getInstance(online.getUniqueId()).stopTimer();
            }
            PluginUtil.sendInfoMessage(player, "全体のタイマーを停止させました。");
        }
        return false;
    }

}
