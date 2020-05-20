package work.crystalnet.plugin.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class DefaultModule implements ItemModule {

	public static final String NOT_FOUND_MESSAGE = "Not found click adapter";

	@Override
	public void onClick(Player clicker, ItemStack item) {
		Bukkit.getLogger().warning(NOT_FOUND_MESSAGE);
	}

	@Override
	public void onHeld(Player helder, ItemStack item) {
		Bukkit.getLogger().warning(NOT_FOUND_MESSAGE);
	}
}
