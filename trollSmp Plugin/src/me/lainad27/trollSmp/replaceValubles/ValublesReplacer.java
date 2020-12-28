package me.lainad27.trollSmp.replaceValubles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.lainad27.trollSmp.Main;

public class ValublesReplacer implements CommandExecutor, Listener{
	int TaskId;
	
	
	private Main plugin;
	
	
	public ValublesReplacer(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("troll") && sender.hasPermission("troll.use")) {
			for (String arg: args) {
				Player trolled = Bukkit.getServer().getPlayer(arg);
				new BukkitRunnable() {
				    @Override
				    public void run() {
						if (Bukkit.getServer().getPlayer(arg) == null) {
							this.cancel();
						}
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
				}.runTaskTimer(plugin, 10, 100);
				TaskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
					public void run() {
						if (Bukkit.getServer().getPlayer(arg) == null) {
							Bukkit.getScheduler().cancelTask(TaskId);
						}
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						      public void run() {
						    	  trolled.playSound(trolled.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 4, 1);
						      }
						    }, (long) (Math.random()*6000));
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						      public void run() {
						    	  trolled.playSound(trolled.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1, 1);
						      }
						    }, (long) (Math.random()*6000));
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						      public void run() {
						    	  trolled.playSound(trolled.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
						      }
						    }, (long) (Math.random()*6000));
					}
			    	}, 0, 6000);
			}
			return true;
		}
		return false;
	}

}
