package ru.asteris.astlib.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class AstExpansion extends PlaceholderExpansion {

    private final String identifier;
    private final String author;
    private final String version;
    private final BiFunction<OfflinePlayer, String, String> requestFunction;

    public AstExpansion(String identifier, String author, String version, BiFunction<OfflinePlayer, String, String> requestFunction) {
        this.identifier = identifier;
        this.author = author;
        this.version = version;
        this.requestFunction = requestFunction;
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getAuthor() {
        return author;
    }

    @Override
    public @NotNull String getVersion() {
        return version;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (requestFunction != null) {
            return requestFunction.apply(player, params);
        }
        return null;
    }
}