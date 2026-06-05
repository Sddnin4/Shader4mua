package me.bonmua;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Cmd implements CommandExecutor {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private final Main plugin;

    public Cmd(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        Manager manager = plugin.getManager();
        State state = manager.getState();

        switch (command.getName().toLowerCase()) {
            case "season" -> {
                sender.sendMessage(MM.deserialize(
                        "<gray>➤ Mùa hiện tại: " + state.getCurrentSeason().getMiniMessageTag()
                        + " <gray>| Ngày: <white>" + state.getCurrentDay() + "<gray>/90"
                ));
                return true;
            }

            case "setseason" -> {
                if (!sender.hasPermission("shader4mua.admin")) {
                    sender.sendMessage(MM.deserialize("<red>✗ Bạn không có quyền dùng lệnh này."));
                    return true;
                }
                if (args.length < 1) {
                    sender.sendMessage(MM.deserialize("<red>✗ Cú pháp: /setseason <XUAN|HA|THU|DONG>"));
                    return true;
                }
                try {
                    Season target = Season.valueOf(args[0].toUpperCase());
                    manager.forceSeason(target);
                    sender.sendMessage(MM.deserialize(
                            "<green>✔ Đã chuyển sang " + target.getMiniMessageTag()
                    ));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(MM.deserialize("<red>✗ Mùa không hợp lệ. Dùng: XUAN | HA | THU | DONG"));
                }
                return true;
            }

            case "nextseason" -> {
                if (!sender.hasPermission("shader4mua.admin")) {
                    sender.sendMessage(MM.deserialize("<red>✗ Bạn không có quyền dùng lệnh này."));
                    return true;
                }
                manager.forceNextSeason();
                sender.sendMessage(MM.deserialize(
                        "<green>✔ Đã chuyển sang mùa tiếp theo: " + manager.getState().getCurrentSeason().getMiniMessageTag()
                ));
                return true;
            }

            default -> {
                return false;
            }
        }
    }
}
