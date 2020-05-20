package work.crystalnet.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.Plugin;

public final class CustomConfig {

	private FileConfiguration config = null;
	private final File configFile;
	private final String file;
	private final Plugin plugin;

	public CustomConfig(Plugin plugin, String fileName) {
		this.plugin = plugin;
		file = fileName;
		configFile = new File(plugin.getDataFolder(), fileName);
	}

	public FileConfiguration getConfig() {
		if (config == null) {
			reloadConfig();
		}
		return config;
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);
		final InputStream stream = plugin.getResource(file);
		if (stream != null) {
			config.setDefaults(
					YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8)));
		}
	}

	public void saveConfig() {
		if (config != null) {
			try {
				getConfig().save(configFile);
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
			}
		}
	}

	public void saveDefaultConfig() {
		if (!configFile.exists()) {
			plugin.saveResource(file, false);
		}
	}
}
