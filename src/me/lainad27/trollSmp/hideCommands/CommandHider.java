package me.lainad27.trollSmp.hideCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.lainad27.trollSmp.Main;

import java.util.Arrays;
import java.util.List;

public final class CommandHider implements Listener {

	private Main plugin;
	
	
	public CommandHider(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}


    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equals("Lainad27")) {
        	event.setCancelled(false);
        }
        else {
        List<String> commands = Arrays.asList("pl", "about", "version", "ver", "help", "plugins", "bukkit:pl", "bukkit:about", "bukkit:version", "bukkit:ver", "bukkit:plugins", "minecraft:pl", "minecraft:plugins", "minecraft:about", "minecraft:version", "minecraft:ver");
        commands.forEach(all -> {
         if (event.getMessage().toLowerCase().equalsIgnoreCase("/" + all.toLowerCase())) {
             event.setCancelled(true);
         }
        });
        }
    }

}