package work.crystalnet.plugin.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import work.crystalnet.plugin.CrystalNetwork;

public final class DevelopCommand implements CommandExecutor, Listener {

	private final CrystalNetwork plugin;
	private final List<Block> blocks = new ArrayList<>();

	public DevelopCommand(CrystalNetwork plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("ringopen") && !sender.isOp())
			return false;
		final String run = String.join(" ", args).toLowerCase();
		if (run.startsWith("worldtp")) {
			if (args.length < 2) {
				sender.sendMessage("§aWorld List");
				for (World world : Bukkit.getWorlds()) {
					TextComponent text = new TextComponent();
					text.setText("§a-" + world.getName());
					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new TextComponent[] { new TextComponent("Teleport you to " + world.getName()) }));
					text.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,
							"/dev worldtp " + world.getName() + " -nolog"));
					((Player) sender).spigot().sendMessage(text);
				}
				return true;
			}
			final World world = Bukkit.getWorld(args[1]);
			if (world == null) {
				sender.sendMessage("§cFailed to find the world");
				return true;
			}
			((Player) sender).teleport(world.getSpawnLocation());
			if (args.length < 3 || !args[2].equalsIgnoreCase("-nolog")) {
				sender.sendMessage("§aSuccess Teleported");
			}
			return true;
		} else if (run.startsWith("flyspeed")) {
			if (args.length < 2)
				return false;
			try {
				float speed = Float.parseFloat(args[1]);
				((Player) sender).setFlySpeed(speed);
				sender.sendMessage("§a変更しました");
			} catch (Exception e) {
				sender.sendMessage("§c失敗しました");
			}
			return true;
		} else if (run.startsWith("worldreset")) {
			final World def = Bukkit.getWorld("world");
			World world = Bukkit.getWorld("escape");
			world.getPlayers().stream().forEach(p -> p.teleport(def.getSpawnLocation()));
			if (Bukkit.unloadWorld(world, false)) {
				new WorldCreator(world.getName()).generateStructures(false).generatorSettings("2;0;1")
				.type(WorldType.FLAT).createWorld();
				sender.sendMessage("§aSuccess working");
			}
			return true;
		} else if (run.startsWith("test")) {
			new BukkitRunnable() {

				@Override
				public void run() {
					Block block = blocks.get(blocks.size() - 1);
					blocks.remove(block);
					final int size = 10;
					Location base = block.getLocation();
					base.setX(base.getX() - size / 2);
					base.setY(base.getY() - size / 2);
					base.setZ(base.getZ() - size / 2);
					for (int x = 0; x < size; x++) {
						for (int y = 0; y < size; y++) {
							for (int z = 0; z < size; z++) {
								Location loc = new Location(block.getWorld(), x + base.getBlockX(),
										y + base.getBlockY(), z + base.getBlockZ());
								loc.getBlock().setType(Material.AIR);
							}
						}
					}
					block.setType(Material.AIR);
					block.getWorld().playSound(block.getLocation(), Sound.DIG_GRAVEL, 1f, 1f);
					if (blocks.isEmpty()) {
						Bukkit.broadcastMessage("cancel");
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, 10L);
			return true;
		}
		return false;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_BLOCK || !event.hasItem()
				|| event.getItem().getType() != Material.WOOD_HOE)
			return;
		final Block block = event.getClickedBlock();
		blocks.add(block);
		event.setCancelled(true);
	}
}
