package work.crystalnet.plugin.sidebar;

import java.util.List;

public interface SidebarStyle {

	String getTitle();

	void invoke(List<String> list);
}
