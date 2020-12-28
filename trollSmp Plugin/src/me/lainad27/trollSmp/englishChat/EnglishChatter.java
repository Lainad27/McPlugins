package me.lainad27.trollSmp.englishChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.lainad27.trollSmp.Main;

public class EnglishChatter implements CommandExecutor,Listener{
private Main plugin;
	
	boolean on = true;
	
	public EnglishChatter(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (on && e.getMessage().matches(".*[א-ת]+.*")){
			e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Message did not send. Only English in Chat!");
			e.setCancelled(true);
		}
	}



	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("togglehebrew") && sender.hasPermission("togglehebrew.use")) {
			on = !on;
			sender.sendMessage("hebrew in chat is now toggled " + on);
		}
		return false;
	}
	
	
}
