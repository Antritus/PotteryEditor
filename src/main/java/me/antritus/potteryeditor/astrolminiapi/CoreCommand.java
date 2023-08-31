package me.antritus.potteryeditor.astrolminiapi;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0.0-snapshot
 * @author antritus
 */
public abstract class CoreCommand extends BukkitCommand {
	public static void registerCommand(JavaPlugin plugin, CoreCommand command) {
		try {
			Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
			commandMap.register(command.getLabel(), plugin.getName(), command);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	protected CoreCommand(@NotNull String name) {
		super(name);
	}

	protected CoreCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}
	protected String build(int pos, String[] args){
		StringBuilder builder = new StringBuilder();
		for (int i = pos; i < args.length; i++){
			if (builder.length() > 0)
				builder.append(" ");
			builder.append(args[i]);
		}
		return builder.toString();
	}
}
