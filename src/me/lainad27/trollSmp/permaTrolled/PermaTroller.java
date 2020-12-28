package me.lainad27.trollSmp.permaTrolled;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import me.lainad27.trollSmp.Main;

public class PermaTroller implements CommandExecutor, Listener {

	
	
private Main plugin;
	
	
	public PermaTroller(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("togglepermatroll") && sender.hasPermission("togglepermatroll.use")) {
			for (String arg: args) {
				if (PermaTrolledConfig.get().contains(arg)) {
					PermaTrolledConfig.get().set(arg, !PermaTrolledConfig.get().getBoolean(arg));
				}
				else {
					PermaTrolledConfig.get().set(arg, true);
				}
				
				sender.sendMessage("toggle now set to " + PermaTrolledConfig.get().getBoolean(arg));
				
				PermaTrolledConfig.save();
				PermaTrolledConfig.reload();
				
			}
		return true;
		}
		return false;
	}
	@EventHandler
	public void onStart(PluginEnableEvent e) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
		    @Override
		    public void run() {
		    	for(Player trolled : Bukkit.getOnlinePlayers()){
		    	if (!PermaTrolledConfig.get().contains(trolled.getName())) {
		    		break;
		    	}
		    	if (PermaTrolledConfig.get().getBoolean(trolled.getName())){
		    	World world = trolled.getWorld();
				int getx = trolled.getLocation().getBlockX();
				int gety = trolled.getLocation().getBlockY();
				int getz = trolled.getLocation().getBlockZ();
				for(int x = getx-64; x < getx+50; x++) {
					for(int y = gety-10; y < gety+10; y++) {
						for(int z = getz-64; z < getz+64; z++) {
							Location loc = new Location(world, x,y,z);
							if (loc.getBlock().getType().equals(Material.DIAMOND_ORE)){
								loc.getBlock().setType(Material.STONE);
							}
							if (loc.getBlock().getType().equals(Material.ANCIENT_DEBRIS)){
								loc.getBlock().setType(Material.NETHERRACK);
							}
						}
					}
				}
		    	}
		    }
		    }
		}, 0L, 80L);
	}
	
}
