package ru.asteris.astlib.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import ru.asteris.astlib.Main;
import ru.asteris.astlib.utils.ColorUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private HikariDataSource dataSource;
    private boolean enabled;

    public void connect() {
        FileConfiguration config = Main.getInstance().getConfig();
        this.enabled = config.getBoolean("mysql.enable");

        if (!enabled) {
            return;
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getString("mysql.host") + ":" + config.getString("mysql.port") + "/" + config.getString("mysql.database"));
        hikariConfig.setUsername(config.getString("mysql.username"));
        hikariConfig.setPassword(config.getString("mysql.password"));
        hikariConfig.setMaximumPoolSize(config.getInt("mysql.pool-size"));
        hikariConfig.setMaxLifetime(config.getLong("mysql.max-lifetime"));
        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("useSSL", "false");

        try {
            dataSource = new HikariDataSource(hikariConfig);
            String msg = config.getString("messages.db-connected");
            if (msg != null && !msg.isEmpty()) {
                Bukkit.getConsoleSender().sendMessage(ColorUtils.colorize(msg));
            }
        } catch (Exception e) {
            String msg = config.getString("messages.db-error");
            if (msg != null && !msg.isEmpty()) {
                Bukkit.getConsoleSender().sendMessage(ColorUtils.colorize(msg));
            }
        }
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        if (!enabled || dataSource == null) {
            return null;
        }
        return dataSource.getConnection();
    }

    public boolean isEnabled() {
        return enabled;
    }
}