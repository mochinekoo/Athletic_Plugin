package net.mochinekoserver.athletic_plugin.manager;

import net.mochinekoserver.athletic_plugin.Main;
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

    public static BoardType globalType = BoardType.WAITING;
    private static final Map<UUID, ScoreboardManager> board_map = new HashMap<>();
    private static final String SCOREBOARD_NAME = "Athletic";
    private static final String DISPLAY_NAME = ChatColor.AQUA + "Athletic";

    //スコアボード
    private final OfflinePlayer player;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private BukkitTask task;
    private BoardType type;
    private Map<Integer, Score> score_map;
    private int timer = 0;
    private BukkitTask timerTask = null;

    private ScoreboardManager(UUID uuid) {
        this.player = Bukkit.getOfflinePlayer(uuid);
        Scoreboard new_scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = new_scoreboard.registerNewObjective(SCOREBOARD_NAME, "dummy", DISPLAY_NAME);
        this.scoreboard = this.objective.getScoreboard();
        this.score_map = new HashMap<>();
        this.type = BoardType.WAITING;

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

        if (type == BoardType.WAITING) {
            //開始前
            player_obj.getScore("===============").setScore(30);
            player_obj.getScore(" ").setScore(29);
            player_obj.getScore( ChatColor.GOLD + "開始待機中...").setScore(28);
            player_obj.getScore("  ").setScore(27);
            // player_obj.getScore("現在の人数：" + ).setScore(26);
            player_obj.getScore("   ").setScore(25);
            player_obj.getScore("============").setScore(24);
        }
        else if (type == BoardType.RUNNING) {
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
                    if (globalType == BoardType.WAITING) {
                        getScore(26).updateScore(ChatColor.GOLD + "現在の人数：" + Bukkit.getOnlinePlayers().size());
                    }
                    else if (type == BoardType.RUNNING) {
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

    public void startTimer() {
        if (timerTask != null) return;
        type = BoardType.RUNNING;
        setScoreboard();

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (type == BoardType.RUNNING) {
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

    public void setType(BoardType type) {
        this.type = type;
    }

    public BoardType getType() {
        return type;
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

    public enum BoardType {
        WAITING,
        RUNNING
    }

}
