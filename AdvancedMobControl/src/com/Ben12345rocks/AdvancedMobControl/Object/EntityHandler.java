package com.Ben12345rocks.AdvancedMobControl.Object;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.Ben12345rocks.AdvancedCore.YML.YMLFileHandler;
import com.Ben12345rocks.AdvancedMobControl.Main;

import lombok.Getter;

/**
 * The Class EntityHandler.
 */
public class EntityHandler {
	@Getter
	private ArrayList<EntityHandle> entityHandles;
	private Main plugin;

	public EntityHandler() {
		plugin = Main.plugin;

		load();
	}

	public void create(String value) {
		YMLFileHandler handle = new YMLFileHandler(
				new File(plugin.getDataFolder() + File.separator + "Entities", value + ".yml"));
		handle.setup();
		load();
	}

	public EntityHandle getHandle(EntityType entityType, World world, int looting, SpawnReason reason) {
		ArrayList<EntityHandle> matchedHandles = new ArrayList<EntityHandle>();

		for (EntityHandle h : entityHandles) {
			// entity matches
			if ((h.getType().equalsIgnoreCase("Living") && entityType.isAlive())
					|| h.getType().equalsIgnoreCase(entityType.toString())) {
				// plugin.debug("Entities match");
				// world check
				if (h.getWorld().isEmpty() || h.getWorld().equalsIgnoreCase(world.getName())) {
					// plugin.debug("worlds matched");
					// check looting
					if (h.getLooting() == -1 || h.getLooting() == looting) {
						// plugin.debug("looting matched");
						// check spawn reason
						if (reason == null || h.getSpawnReason().equalsIgnoreCase(reason.toString())) {
							// plugin.debug("reason matched");
							matchedHandles.add(h);
						}
					}
				}
			}
		}

		EntityHandle highestPriority = null;
		for (EntityHandle h : matchedHandles) {
			if (highestPriority == null || highestPriority.getPriority() < h.getPriority()) {
				highestPriority = h;
			}
		}

		// plugin.debug(matchedHandles.size() + ":" + (highestPriority != null));

		return highestPriority;
	}

	public void load() {
		entityHandles = new ArrayList<EntityHandle>();
		File folder = new File(plugin.getDataFolder(), "Entities");
		if (!folder.exists()) {
			folder.mkdirs();
			try {
				folder.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File file : folder.listFiles()) {

			EntityHandle handle = new EntityHandle(file);
			if (handle.getType().isEmpty()) {
				plugin.getLogger().warning("Missing entity type in file: " + handle.getFile().getdFile().getName());
			}
			entityHandles.add(handle);
		}
	}
}
