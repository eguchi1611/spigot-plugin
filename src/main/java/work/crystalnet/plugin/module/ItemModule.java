package work.crystalnet.plugin.module;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemModule {

	default boolean invoke(Player invoker, ItemStack item, ItemStack registed) {
		return item.isSimilar(registed);
	}

	default void onClick(Player clicker, ItemStack item) {
	}

	default void onHeld(Player helder, ItemStack item) {
	}
}
