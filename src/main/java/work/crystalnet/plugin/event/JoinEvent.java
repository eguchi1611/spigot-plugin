package work.crystalnet.plugin.event;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import work.crystalnet.plugin.CrystalNetwork;
import work.crystalnet.plugin.sidebar.DefaultSidebar;
import work.crystalnet.plugin.task.SendSidebarTask;

public final class JoinEvent implements Listener {

	private final CrystalNetwork plugin;

	public JoinEvent(CrystalNetwork plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		final Player target = event.getPlayer();
		target.setPlayerListName(target.getName());

		// 新規スコアボードの登録
		target.setScoreboard(target.getServer().getScoreboardManager().getNewScoreboard());

		// タブヘッダー・フッターの登録
		sendTabStyle(target);

		// ログイン処理
		plugin.getStatusRecord().loginPlayer(target);

		for (Player player : Bukkit.getOnlinePlayers()) {
			// サイドバーの送信
			Bukkit.getScheduler().runTaskLater(plugin, new SendSidebarTask(player, new DefaultSidebar(plugin, player)),
					10L);

			// プレイヤーログインDetails Send to OP
			if (player.isOp()) {
				sendDetails(player, target);
			}

			// 通知音
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
		}
	}

	private void sendTabStyle(Player target) {
		IChatBaseComponent tabHeader = ChatSerializer.a("{\"text\":\"§ePlaying on §b§lCrystal Network\"}");
		IChatBaseComponent tabFooter = ChatSerializer.a("{\"text\":\"§a§lServer for Parkour & PVP lovers\"}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabHeader);
		try {
			Field field = packet.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(packet, tabFooter);
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Failed to send tab-style", e);
		} finally {
			((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
		}
	}

	private void sendDetails(CommandSender sender, Player target) {
		sender.sendMessage("§6§l⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟Player Login⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟⬟");
		sender.sendMessage("Time: §b" + new Date().toString());
		sender.sendMessage("Name: §b" + target.getName());
		sender.sendMessage("UUID: §b" + target.getUniqueId().toString());
		sender.sendMessage("IP: §b" + target.spigot().getRawAddress().getAddress().getHostAddress());
		sender.sendMessage("Login Count: §6" + plugin.getStatusRecord().getLoginCount(target) + "(+1)");
	}
}
