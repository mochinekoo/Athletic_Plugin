package net.mochinekoserver.athletic_plugin.manager;

import net.mochinekoserver.athletic_plugin.Main;
import net.mochinekoserver.athletic_plugin.status.TimerStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ScoreboardManager {

    private static final Map<UUID, ScoreboardManager> board_map = new HashMap<>();
    private static final String SCOREBOARD_NAME = "Athletic";
    private static final String DISPLAY_NAME = ChatColor.AQUA + "Athletic";

    //スコアボード
    private final OfflinePlayer player;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private BukkitTask task;
    private Map<Integer, Score> score_map;

    //other
    private final TimeManager timeManager;

    private ScoreboardManager(UUID uuid) {
        this.player = Bukkit.getOfflinePlayer(uuid);
        Scoreboard new_scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = new_scoreboard.registerNewObjective(SCOREBOARD_NAME, "dummy", DISPLAY_NAME);
        this.scoreboard = this.objective.getScoreboard();
        this.score_map = new HashMap<>();
        this.timeManager = TimeManager.getInstance(uuid);

        board_map.put(uuid, this);
    }

    /**
     * スコアボードインスタンスを取得する関数
     * @param uuid スコアボードを取得したいプレイヤー
     */
    public static ScoreboardManager getInstance(UUID uuid) {
        if (!board_map.containsKey(uuid)) {
            board_map.put(uuid, new ScoreboardManager(uuid));
        }

        return board_map.get(uuid);
    }

    /**
     * スコアボードインスタンスを削除する関数
     */
    public static void removeInstance(UUID uuid) {
        board_map.remove(uuid);
    }

    /**
     * スコアを取得する関数
     * @param score スコアを取得したい場所
     */
    public Score getScore(int score) {
        if (!score_map.containsKey(score)) {
            score_map.put(score, new Score(getObjective(), null, score));
        }

        return score_map.get(score);
    }

    /**
     * スコアボードを設定する関数
     */
    public void setScoreboard() {
        Objective player_obj = getObjective();
        resetScore();
        player_obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        if (TimeManager.getStatus() == TimerStatus.WAITING) {
            //開始前
            player_obj.getScore("===============").setScore(30);
            player_obj.getScore(" ").setScore(29);
            player_obj.getScore( ChatColor.GOLD + "開始待機中...").setScore(28);
            player_obj.getScore("  ").setScore(27);
            // player_obj.getScore("現在の人数：" + ).setScore(26);
            player_obj.getScore("   ").setScore(25);
            player_obj.getScore("============").setScore(24);
        }
        else if (TimeManager.getStatus() == TimerStatus.RUNNING) {
            //開始済み
            player_obj.getScore("===============").setScore(30);
            player_obj.getScore(" ").setScore(29);
            player_obj.getScore( ChatColor.GOLD + "経過時間：").setScore(28);
            // player_obj.getScore("hh:mm").setScore(26);
            player_obj.getScore("   ").setScore(25);
            player_obj.getScore("============").setScore(24);
        }

        updateScoreboard();
        Bukkit.getLogger().info("[ScoreboardManager] スコアボードを設定しました。");
    }

    /**
     * スコアボードを更新する関数
     * @apiNote 手動で呼び出す必要なし
     */
    private void updateScoreboard() {
        if (task == null) {
            this.task = new BukkitRunnable() {
                @Override
                public void run() {
                    int timer = timeManager.getTimer();
                    if (TimeManager.getStatus() == TimerStatus.WAITING) {
                        getScore(26).updateScore(ChatColor.GOLD + "現在の人数：" + Bukkit.getOnlinePlayers().size());
                    }
                    else if (TimeManager.getStatus() == TimerStatus.RUNNING) {
                        int hour = timer / 60 / 60;
                        int min = timer / 60;
                        int sec = timer % 60;
                        getScore(26).updateScore(String.format("%02d:%02d:%02d", hour, min, sec));
                    }

                    //スコアボードセット
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    player.getPlayer().setScoreboard(scoreboard);

                }
            }.runTaskTimer(Main.getPlugin(Main.class), 0L, 2L);
        }
    }

    /**
     * スコアボードをリセットする関数
     * @apiNote 手動で呼び出す必要なし
     */
    public void resetScore() {
        Set<String> scores = getScoreboard().getEntries();
        for (String score : scores) {
            getScoreboard().resetScores(score);
        }
    }

    public Objective getObjective() {
        return objective;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public OfflinePlayer getOfflinePlayer() {
        return player;
    }

    public Player getPlayer() {
        return player.getPlayer();
    }

    public BukkitTask getTask() {
        return task;
    }

    public static class Score {
        private String name = null;
        private final int score;
        private final Objective objective;

        public Score(Objective objective, String name, int score) {
            this.name = name;
            this.score = score;
            this.objective = objective;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public Objective getObjective() {
            return objective;
        }

        public void updateScore(String name) {
            if (this.name != null) {
                objective.getScoreboard().resetScores(this.name);
            }
            this.name = name;
            objective.getScore(name).setScore(score);
        }
    }

}
