package work.crystalnet.plugin;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import work.crystalnet.plugin.command.DevelopCommand;
import work.crystalnet.plugin.database.StatusRecord;

public final class CrystalNetwork extends JavaPlugin implements Listener {

	private StatusRecord statusRecord;
	private CustomConfig itemConfig;

	@Override
	public void onEnable() {
		statusRecord = new StatusRecord(this);
		// コマンドの登録
		getCommand("develop").setExecutor(new DevelopCommand(this));

		// コンフィグの登録
		itemConfig = new CustomConfig(this, "items.yml");

		// アイテムモジュールの登録
		getServer().getPluginManager().registerEvents(this, this);
		World world = new WorldCreator("escape").generateStructures(false).generatorSettings("2;0;1")
				.type(WorldType.FLAT).createWorld();
		world.setAutoSave(false);
	}

	public Configuration getItemConfig() {
		return itemConfig.getConfig();
	}

	public StatusRecord getStatusRecord() {
		return statusRecord;
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(event.toWeatherState());
	}
}
