package me.bonmua;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private Manager manager;

    @Override
    public void onEnable() {
        manager = new Manager(this);
        manager.start();

        Cmd commandHandler = new Cmd(this);
        registerCommand("season", commandHandler);
        registerCommand("setseason", commandHandler);
        registerCommand("nextseason", commandHandler);

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Shader Bốn Mùa đã khởi động! Mùa hiện tại: "
                + manager.getState().getCurrentSeason().getDisplayName()
                + " - Ngày " + manager.getState().getCurrentDay());
    }

    @Override
    public void onDisable() {
        if (manager != null) {
            manager.stop();
        }
        getLogger().info("Shader Bốn Mùa đã tắt.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Áp dụng shader mùa hiện tại cho người chơi mới vào server
        manager.applyShaderToPlayer(event.getPlayer(), manager.getState().getCurrentSeason());
    }

    private void registerCommand(String name, Cmd handler) {
        PluginCommand cmd = getCommand(name);
        if (cmd != null) {
            cmd.setExecutor(handler);
        }
    }

    public Manager getManager() {
        return manager;
    }
}
