package work.crystalnet.plugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StatusRecord {

	private final Plugin plugin;
	private Connection connection;
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;

	public StatusRecord(Plugin plugin) {
		this.plugin = plugin;
		host = "localhost";
		port = 3306;
		database = "crystalnetwork";
		username = "root";
		password = "2213";
	}

	public void loginPlayer(Player target) {
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					openConnection();
				} catch (SQLException e) {
					return;
				}
				try (Statement statement = connection.createStatement()) {
					final String sql = "insert into user (time, name, uuid, ip) values (?, ?, ?, ?);";
					final long time = new Date().getTime();
					final String name = target.getName();
					final String uuid = target.getUniqueId().toString();
					final String ip = target.spigot().getRawAddress().getAddress().getHostAddress();
					try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
						preparedStatement.setLong(1, time);
						preparedStatement.setString(2, name);
						preparedStatement.setString(3, uuid);
						preparedStatement.setString(4, ip);
						preparedStatement.executeUpdate();
					}
				} catch (SQLException e) {
				}
			}
		}.runTaskAsynchronously(plugin);
	}

	public int getLoginCount(Player target) {
		try {
			openConnection();
		} catch (SQLException e) {
			return -1;
		}
		try (Statement statement = connection.createStatement()) {
			try (ResultSet result = statement.executeQuery(
					"select count(uuid='" + target.getUniqueId().toString() + "' or null) as count from user;")) {
				while (result.next())
					return result.getInt("count");
			}
		} catch (SQLException e) {
			return -1;
		}
		return -1;
	}

	private void openConnection() throws SQLException {
		if (connection != null && !connection.isClosed())
			return;
		synchronized (this) {
			if (connection != null && !connection.isClosed())
				return;
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username,
					password);
		}
	}
}
