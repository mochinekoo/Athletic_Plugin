package net.mochinekoserver.athletic_plugin.manager;

import net.mochinekoserver.athletic_plugin.Main;
import net.mochinekoserver.athletic_plugin.status.TimerStatus;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeManager {

    private static Map<UUID, TimeManager> mapInstance = new HashMap<>();
    private static TimerStatus status = TimerStatus.WAITING;
    private static int global_timer = 0;
    private static BukkitTask global_timer_task;
    private static BossBar global_timer_bossbar;

    private final UUID uuid;
    private int timer = 0;
    private BukkitTask timerTask = null;

    private TimeManager(UUID uuid) {
        this.uuid = uuid;
    }

    public static TimeManager getInstance(UUID uuid) {
        if (!mapInstance.containsKey(uuid)) {
            mapInstance.put(uuid, new TimeManager(uuid));
        }
        return mapInstance.get(uuid);
    }

    public static TimerStatus getStatus() {
        return status;
    }

    public static void setStatus(@Nonnull TimerStatus status) {
        TimeManager.status = status;
        if (status == TimerStatus.RUNNING && global_timer_task == null) {
            startGlobalTimer();
        }
    }

    public static int getGlobalTimer() {
        return global_timer;
    }

    public static void setGlobalTimer(int time) {
        if (global_timer < 0) return;
        TimeManager.global_timer = time;
    }

    public static void startGlobalTimer() {
        if (global_timer_task != null) return;

        global_timer_bossbar = Bukkit.createBossBar("全体の経過時間：00:00:00", BarColor.BLUE, BarStyle.SEGMENTED_6);
        global_timer_task = new BukkitRunnable() {
            @Override
            public void run() {
                global_timer++;
                int hour = global_timer / 60 / 60;
                int min = global_timer / 60;
                int sec = global_timer % 60;
                global_timer_bossbar.setTitle(String.format("全体の経過時間：%02d:%02d:%02d", hour, min, sec));
                for (Player player: Bukkit.getOnlinePlayers()) {
                    global_timer_bossbar.addPlayer(player);
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
    }

    public static void stopGlobalTimer() {
        if (global_timer_task == null) return;
        global_timer_task.cancel();
        global_timer_task = null;
    }

    public void startTimer() {
        if (timerTask != null) return;
        setStatus(TimerStatus.RUNNING);
        ScoreboardManager.getInstance(uuid).setScoreboard();

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (status == TimerStatus.RUNNING) {
                    timer++;
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
    }

    public void stopTimer() {
        if (timerTask == null) return;
        timerTask.cancel();
        timerTask = null;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        if (timer < 0) return;
        this.timer = timer;
    }

}
