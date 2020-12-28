package me.lainad27.trollSmp.leakCords;

import org.bukkit.Bukkit;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.lainad27.trollSmp.Main;

public class CordsLeaker implements CommandExecutor, Listener{
	int TaskId;
	
	
	private Main plugin;
	
	
	public CordsLeaker(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("leak") && sender.hasPermission("leak.use")) {
			for (String arg: args) {
				if (Bukkit.getServer().getPlayer(arg) == null) {
					sender.sendMessage(arg + " not in game bro");
					return true;
				}
				Player trolled = Bukkit.getServer().getPlayer(arg);
				World world = trolled.getWorld();
				int getx = trolled.getLocation().getBlockX();
				int gety = trolled.getLocation().getBlockY();
				int getz = trolled.getLocation().getBlockZ();
				sender.sendMessage(trolled.getName() +" at " + getx +" " +gety + " "+ getz + " in dimension " + world.getEnvironment().toString());
			}
			return true;
		}
		return false;
	}

}
