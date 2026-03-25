package ru.asteris.astlib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.asteris.astlib.managers.DatabaseManager;
import ru.asteris.astlib.managers.GuiListener;
import ru.asteris.astlib.managers.VaultManager;
import ru.asteris.astlib.managers.WorldGuardManager;
import ru.asteris.astlib.utils.ColorUtils;

public class Main extends JavaPlugin {

    private static Main instance;
    private DatabaseManager databaseManager;
    private VaultManager vaultManager;
    private WorldGuardManager worldGuardManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        vaultManager = new VaultManager();
        if (!vaultManager.setupEconomy()) {
            String vaultMsg = getConfig().getString("messages.vault-not-found");
            if (vaultMsg != null && !vaultMsg.isEmpty()) {
                Bukkit.getConsoleSender().sendMessage(ColorUtils.colorize(vaultMsg));
            }
        }

        worldGuardManager = new WorldGuardManager();
        worldGuardManager.setup();

        databaseManager = new DatabaseManager();
        databaseManager.connect();

        getServer().getPluginManager().registerEvents(new GuiListener(), this);

        String startupMsg = getConfig().getString("messages.startup");
        if (startupMsg != null && !startupMsg.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(ColorUtils.colorize(startupMsg));
        }
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }

        String shutdownMsg = getConfig().getString("messages.shutdown");
        if (shutdownMsg != null && !shutdownMsg.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(ColorUtils.colorize(shutdownMsg));
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public VaultManager getVaultManager() {
        return vaultManager;
    }

    public WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }
}