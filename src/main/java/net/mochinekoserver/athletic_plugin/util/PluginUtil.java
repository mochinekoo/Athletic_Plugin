package net.mochinekoserver.athletic_plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PluginUtil {

    public static String TEXT_INFO = ChatColor.AQUA + "[AthleticPlugin] " + ChatColor.RESET;
    public static String TEXT_ERROR = ChatColor.RED + "[エラー] " + ChatColor.RESET;

    public static void sendInfoMessage(CommandSender sender, String message) {
        sender.sendMessage(TEXT_INFO + message);
    }

    public static void sendInfoMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendInfoMessage(player, message);
        }
    }

    public static void sendErrorMessage(CommandSender sender, String message) {
        sender.sendMessage(TEXT_ERROR + message);
    }

    public static void sendErrorMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendErrorMessage(player, message);
        }
    }
}
