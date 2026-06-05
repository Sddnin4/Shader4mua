package me.bonmua;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Manager {

    private static final int TICKS_PER_DAY = 24000;
    private static final MiniMessage MM = MiniMessage.miniMessage();

    private final Main plugin;
    private final State state;
    private BukkitTask task;

    public Manager(Main plugin) {
        this.plugin = plugin;
        this.state = new State();
    }

    public State getState() {
        return state;
    }

    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, TICKS_PER_DAY, TICKS_PER_DAY);
        applySeasonShader(state.getCurrentSeason());
    }

    public void stop() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    private void tick() {
        Season before = state.getCurrentSeason();
        state.incrementDay();
        Season after = state.getCurrentSeason();

        if (before != after) {
            onSeasonChange(after);
        }
    }

    public void forceSeason(Season season) {
        state.setCurrentSeason(season);
        state.setCurrentDay(1);
        onSeasonChange(season);
    }

    public void forceNextSeason() {
        forceSeason(state.getCurrentSeason().next());
    }

    private void onSeasonChange(Season season) {
        String announcement = switch (season) {
            case XUAN -> "<gradient:#a8edea:#fed6e3><bold>✦ Mùa Xuân đã đến! Vạn vật hồi sinh, hoa nở rộ khắp nơi. ✦</bold></gradient>";
            case HA  -> "<gradient:#f7971e:#ffd200><bold>✦ Mùa Hạ rực lửa! Ánh nắng chói chang trải dài vô tận. ✦</bold></gradient>";
            case THU -> "<gradient:#f4791f:#659999><bold>✦ Mùa Thu về! Lá vàng rơi nhẹ, gió hiu hiu mát. ✦</bold></gradient>";
            case DONG -> "<gradient:#e0eafc:#cfdef3><bold>✦ Mùa Đông lạnh giá! Tuyết trắng phủ khắp đất trời. ✦</bold></gradient>";
        };

        Bukkit.getServer().broadcast(MM.deserialize(announcement));
        applySeasonShader(season);
    }

    public void applySeasonShader(Season season) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            applyShaderToPlayer(player, season);
        }
    }

    public void applyShaderToPlayer(Player player, Season season) {
        // Xoá tất cả hiệu ứng potion cũ liên quan đến shader trước
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.NAUSEA);
        player.removePotionEffect(PotionEffectType.WITHER);
        player.removePotionEffect(PotionEffectType.HUNGER);

        switch (season) {
            case XUAN -> {
                // Xuân: Night Vision nhẹ → tông sáng, ấm áp, trong trẻo
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.NIGHT_VISION,
                        Integer.MAX_VALUE, 0, false, false, false
                ));
            }
            case HA -> {
                // Hạ: Night Vision cường độ cao hơn → màn hình rất sáng, chói chang
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.NIGHT_VISION,
                        Integer.MAX_VALUE, 1, false, false, false
                ));
            }
            case THU -> {
                // Thu: Hunger (vignette tối nhẹ ở viền) → cảm giác ảm đạm, lá vàng
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.HUNGER,
                        Integer.MAX_VALUE, 0, false, false, false
                ));
            }
            case DONG -> {
                // Đông: Blindness nhẹ (darkens + vignette rõ) + Night Vision cùng lúc
                // → màn hình tối, xám lạnh, tạo cảm giác đông giá
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.BLINDNESS,
                        Integer.MAX_VALUE, 0, false, false, false
                ));
            }
        }

        // Gửi tiêu đề mùa nhỏ cho người chơi
        player.sendActionBar(MM.deserialize(season.getMiniMessageTag()));
    }
}
