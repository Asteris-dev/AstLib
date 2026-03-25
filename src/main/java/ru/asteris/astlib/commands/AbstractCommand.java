package ru.asteris.astlib.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import ru.asteris.astlib.Main;
import ru.asteris.astlib.utils.ColorUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCommand extends Command {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public AbstractCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
        register();
    }

    public AbstractCommand(String name) {
        this(name, "", "/" + name, new ArrayList<>());
    }

    public void addSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length > 0) {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) {
                if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
                    String noPerm = Main.getInstance().getConfig().getString("messages.no-permission");
                    if (noPerm != null && !noPerm.isEmpty()) {
                        sender.sendMessage(ColorUtils.colorize(noPerm));
                    }
                    return true;
                }
                subCommand.execute(sender, args);
                return true;
            }
        }
        execute(sender, args);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (SubCommand subCommand : subCommands.values()) {
                if (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission())) {
                    if (subCommand.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(subCommand.getName());
                    }
                }
            }
            return completions;
        } else if (args.length > 1) {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) {
                return subCommand.tabComplete(sender, args);
            }
        }
        return new ArrayList<>();
    }

    private void register() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(Main.getInstance().getName().toLowerCase(), this);
        } catch (Exception ignored) {
        }
    }
}