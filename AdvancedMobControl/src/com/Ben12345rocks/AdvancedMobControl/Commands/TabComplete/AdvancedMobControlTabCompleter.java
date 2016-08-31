package com.Ben12345rocks.AdvancedMobControl.Commands.TabComplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.Ben12345rocks.AdvancedCore.Utils;
import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedMobControl.Config.ConfigEntity;

// TODO: Auto-generated Javadoc
/**
 * The Class AdvancedMobControlTabCompleter.
 */
public class AdvancedMobControlTabCompleter implements TabCompleter {

	/** The plugin. */
	com.Ben12345rocks.AdvancedMobControl.Main plugin = com.Ben12345rocks.AdvancedMobControl.Main.plugin;

	/**
	 * Gets the tab complete options.
	 *
	 * @param sender
	 *            the sender
	 * @param args
	 *            the args
	 * @param argNum
	 *            the arg num
	 * @return the tab complete options
	 */
	public ArrayList<String> getTabCompleteOptions(CommandSender sender,
			String[] args, int argNum) {
		ArrayList<String> cmds = new ArrayList<String>();
		for (CommandHandler commandHandler : plugin.advancedMobControlCommands) {

			if (sender.hasPermission(commandHandler.getPerm())) {
				String[] cmdArgs = commandHandler.getArgs();
				if (cmdArgs.length > argNum) {
					boolean argsMatch = true;
					for (int i = 0; i < argNum; i++) {
						if (args.length >= i) {
							if (!commandHandler.argsMatch(args[i], i)) {
								argsMatch = false;
							}
						}
					}

					if (argsMatch) {
						String[] cmdArgsList = cmdArgs[argNum].split("&");
						for (String arg : cmdArgsList) {
							if (arg.equalsIgnoreCase("(player)")) {
								for (Object playerOb : Bukkit
										.getOnlinePlayers().toArray()) {
									Player player = (Player) playerOb;
									if (!cmds.contains(player.getName())) {
										cmds.add(player.getName());
									}
								}
							} else if (arg.equalsIgnoreCase("(entity)")) {
								for (String entity : ConfigEntity.getInstance()
										.getEntitysNames()) {
									if (!cmds.contains(entity)) {
										cmds.add(entity);
									}
								}
							} else if (arg
									.equalsIgnoreCase("(entitydamagecause)")) {
								for (DamageCause damage : DamageCause.values()) {
									String str = damage.toString();
									if (!cmds.contains(str)) {
										cmds.add(str);
									}
								}
							} else if (arg
									.equalsIgnoreCase("(entityspawnreason)")) {
								for (SpawnReason damage : SpawnReason.values()) {
									String str = damage.toString();
									if (!cmds.contains(str)) {
										cmds.add(str);
									}
								}
							} else if (arg.equalsIgnoreCase("(boolean)")) {
								if (!cmds.contains("True")) {
									cmds.add("True");
								}
								if (!cmds.contains("False")) {
									cmds.add("False");
								}
							} else if (arg.equalsIgnoreCase("(number)")) {

							} else if (!cmds.contains(arg)) {
								cmds.add(arg);
							}
						}

					}
				}
			}
		}

		Collections.sort(cmds, String.CASE_INSENSITIVE_ORDER);

		return cmds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.command.TabCompleter#onTabComplete(org.bukkit.command.
	 * CommandSender, org.bukkit.command.Command, java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String alias, String[] args) {

		ArrayList<String> tab = new ArrayList<String>();

		ArrayList<String> cmds = new ArrayList<String>();

		for (CommandHandler commandHandler : plugin.advancedMobControlCommands) {
			cmds.addAll(commandHandler.getTabCompleteOptions(sender, args,
					args.length - 1));
		}

		for (int i = 0; i < cmds.size(); i++) {
			if (Utils.getInstance().startsWithIgnoreCase(cmds.get(i),
					args[args.length - 1])) {
				tab.add(cmds.get(i));
			}
		}

		return tab;

	}

}
