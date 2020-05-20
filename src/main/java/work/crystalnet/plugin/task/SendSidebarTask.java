package work.crystalnet.plugin.task;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import work.crystalnet.plugin.sidebar.SidebarStyle;

public class SendSidebarTask implements Runnable {

	private final Player target;
	private final SidebarStyle style;

	public SendSidebarTask(Player target, SidebarStyle style) {
		this.target = target;
		this.style = style;
	}

	@Override
	public void run() {
		final String objectiveName = "style";

		Scoreboard scoreboard = target.getScoreboard();
		if (scoreboard == null || scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			target.setScoreboard(scoreboard);
		}

		Objective obj = scoreboard.getObjective(objectiveName);
		if (obj != null) {
			obj.unregister();
		}

		obj = scoreboard.registerNewObjective(objectiveName, "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		obj.setDisplayName(style.getTitle());
		List<String> list = new ArrayList<>();
		style.invoke(list);
		for (int i = 0; i < list.size(); i++) {
			obj.getScore(list.get(i)).setScore(i);
		}

		Team team = scoreboard.getTeam("team");
		if (team == null) {
			team = scoreboard.registerNewTeam("team");
			team.addEntry(target.getName());
		}
		team.setPrefix("§6[PREFIX]§r");
	}
}
