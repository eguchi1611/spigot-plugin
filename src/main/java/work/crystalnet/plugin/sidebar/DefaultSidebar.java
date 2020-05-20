package work.crystalnet.plugin.sidebar;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import work.crystalnet.plugin.CrystalNetwork;

public class DefaultSidebar implements SidebarStyle {

	private final CrystalNetwork plugin;
	private final Player target;

	public DefaultSidebar(CrystalNetwork plugin, Player target) {
		this.plugin = plugin;
		this.target = target;
	}

	@Override
	public String getTitle() {
		return "§e§lLobby";
	}

	@Override
	public void invoke(List<String> list) {
		list.add("§bcrystalnet.work");
		list.add("Coins: §6"
				+ NumberFormat.getNumberInstance().format(RandomUtils.nextInt(1145141919, Integer.MAX_VALUE)));
		list.add(StringUtils.repeat(" ", list.size()));
		list.add("Logins: §a" + plugin.getStatusRecord().getLoginCount(target));
		list.add("§8" + new SimpleDateFormat("MM/dd/yy").format(new Date()));
	}
}
