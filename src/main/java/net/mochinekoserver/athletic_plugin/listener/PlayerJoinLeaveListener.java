package net.mochinekoserver.athletic_plugin.listener;

import net.mochinekoserver.athletic_plugin.manager.ScoreboardManager;
import net.mochinekoserver.athletic_plugin.manager.TimeManager;
import net.mochinekoserver.athletic_plugin.status.TimerStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ScoreboardManager scoreboardManager = ScoreboardManager.getInstance(player.getUniqueId());
        TimeManager timeManager = TimeManager.getInstance(player.getUniqueId());
        scoreboardManager.setScoreboard();
        if (TimeManager.getStatus() == TimerStatus.RUNNING) {
            timeManager.startTimer();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ScoreboardManager scoreboardManager = ScoreboardManager.getInstance(player.getUniqueId());
        TimeManager timeManager = TimeManager.getInstance(player.getUniqueId());
        timeManager.stopTimer();
        ScoreboardManager.removeInstance(player.getUniqueId());
    }
}
