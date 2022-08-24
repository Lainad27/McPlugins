package me.lainad27.manhunt.listenerExec;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.lainad27.manhunt.Main;

public class ManhuntListenerExecutor implements CommandExecutor, Listener{
	
	int Taskid;
	int speederTime = 10;
	boolean TaskInitialised = false;
	
	List<String> Hunters = new ArrayList<String>();
	List<String> Speeders = new ArrayList<String>();
	
	List<String> Names = new ArrayList<String>();
	List<Integer> Tracking = new ArrayList<Integer>();
	
	boolean close = false;

	ArrayList<ArrayList<Integer>> Closes =  new ArrayList<ArrayList<Integer>>(); 
	
	private Main plugin;
	
	/**
	 * creates a new ManhuntListenerExecutor
	 * @param plugin
	 */
	public ManhuntListenerExecutor(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	/**
	 * 
	 * @param e
	 */
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if (p.getInventory().getItemInMainHand().getType() == Material.COMPASS && !e.getAction().toString().equals("PHYSICAL")) { 
			int indexOfAsker = Names.indexOf(p.getName());
			if (e.getAction().toString().equals("RIGHT_CLICK_BLOCK") || e.getAction().toString().equals("RIGHT_CLICK_AIR")) {
				Tracking.set(indexOfAsker ,((Tracking.get(indexOfAsker))%Names.size()));
			}
			
			else if (e.getAction().toString().equals("LEFT_CLICK_BLOCK") || e.getAction().toString().equals("LEFT_CLICK_AIR")) {
				Tracking.set(indexOfAsker ,((Tracking.get(indexOfAsker) + 1)%Names.size())); 
			}
			
			while (Bukkit.getServer().getPlayer(Names.get(Tracking.get(indexOfAsker))) ==null) { // check if player is online goddamn it
				Tracking.set(indexOfAsker ,(Tracking.get(indexOfAsker) + 1)%Names.size());
			}
			
			Player Tracked = Bukkit.getServer().getPlayer(Names.get(Tracking.get(indexOfAsker)));
			
			if (p.getWorld() != Tracked.getWorld()) {
				p.sendMessage("Sry bro. " + Tracked.getName() + " is on a different dimension.");
				return;
			}
			
			if (p.getWorld().getEnvironment().toString().equals("NORMAL")) {
				ItemStack compass = p.getInventory().getItemInMainHand();
				CompassMeta meta = (CompassMeta) compass.getItemMeta();
				if (meta.isLodestoneTracked()) {
					p.getInventory().setItemInMainHand(new ItemStack(Material.COMPASS));
				}
				p.setCompassTarget(Tracked.getLocation());
				p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + ("Tracking " + Tracked.getName()));
			}
			
			else if (p.getWorld().getEnvironment().toString().equals("NETHER")) {
				ItemStack compass = p.getInventory().getItemInMainHand();
				Location loc = (new Location(p.getWorld(), Tracked.getLocation().getX(), (double) 0, Tracked.getLocation().getZ()));
				loc.getBlock().setType(Material.LODESTONE);
				CompassMeta meta = (CompassMeta) compass.getItemMeta();
				meta.setLodestone(loc);
				meta.setLodestoneTracked(true);
				compass.setItemMeta(meta);
				
				p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + ("Tracking " + Tracked.getName()));
			}
		}	
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Names.contains(e.getPlayer().getName())== false){
			Names.add(e.getPlayer().getName());
			Tracking.add(0);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Names.contains(p.getName())) {
			Tracking.remove(Names.indexOf(p.getName()));
			Names.remove(Names.indexOf(p.getName()));
		}
	}
	
	@EventHandler
	public void onStart(PluginEnableEvent e) {
		for(Player p : Bukkit.getOnlinePlayers()){
			Names.add(p.getName());
			Tracking.add(0);
		}
	}
	
	/*
	@EventHandler
	public void movement(PlayerMoveEvent e) {
		Player hunter=e.getPlayer();
		
		
		if(!Hunters.contains(hunter.getName())) {
			return;
		}
		for (String speederString : Speeders) {
			if (Bukkit.getServer().getPlayer(speederString) == null) {
				continue;
			}
			
			
			Player speeder = Bukkit.getServer().getPlayer(speederString);
		
			if (hunter.getWorld()!=speeder.getWorld()) {
				return;
			}
			
			Location loc1 = hunter.getLocation();
			Location loc2 = speeder.getLocation();
		
			if ((close==false) && loc1.distance(loc2)<50) {
				for (int i=0; i<5; i++) {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, (float) 5, (float) 1);
							speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, (float) 5, (float) 1);
						}
				    	}, 5*i);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, (float) 5, (float) 1);
							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, (float) 5, (float) 1);
						}
					}, 5*i);
				}
				close=true;
			}
		
			else if ((close==true) && loc1.distance(loc2)>50) {
				for (int i=0; i<5; i++) {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
						}
					}, 5*i);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
						}
					}, 5*i);
				}
				close=false;
			}
		}
    }
	*/

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p= e.getPlayer();
		
		if (Hunters.contains(p.getName())) {
			p.getInventory().addItem(new ItemStack(Material.COMPASS));
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 36000 * 20, 0, false, false));
                }
			}, (1));
			Bukkit.getServer().broadcastMessage("The hunter " + p.getName() + " died!");
			return;
		}
		
		if (Speeders.contains(p.getName())) {
			if (Speeders.size()==1 || speederTime==25) {
				Bukkit.getServer().broadcastMessage("Hunters Won!");
				return;
			}
			
			p.sendTitle("You died you fking loser", "wait " +speederTime + " minutes to enter survival mode", 20, 100, 20);
			Bukkit.getServer().broadcastMessage("The speedrunner " + p.getName() + " died!");
			p.setGameMode(GameMode.SPECTATOR);
			for(int i=1; i<speederTime+1; i++) {
				final int j=i;
				final int currentSpeederTime = speederTime;
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				      public void run() {
				    	  Bukkit.getServer().broadcastMessage("player " + p.getName() + " respawns in " + (currentSpeederTime-j) + " minutes.");
				      }
				    }, 1200*i);
			}
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			      public void run() {
			    	  p.sendTitle("Position yourself...", "about to make u survival mode.", 20, 1000, 20);
			      }
			    }, speederTime*60*20-1000);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			      public void run() {
					p.setGameMode(GameMode.SURVIVAL);
					p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 999999, 1, false, false));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5, 10, false, false));
					Bukkit.getServer().broadcastMessage("player " + p.getName() + " respawned!");
			      }
			    }, speederTime*60*20);
			speederTime+=5;
		}
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("hunter") && sender.hasPermission("hunter.use")) {
			for (String arg : args) {
			
				if (Bukkit.getServer().getPlayer(arg) == null) {
					continue;
				}
			
				Player p = Bukkit.getServer().getPlayer(arg);
			
				p.getInventory().clear();
				p.getInventory().addItem(new ItemStack(Material.COMPASS));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 0, false, false));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5, 10, false, false));
				p.setDisplayName("i am not gay");
				if (Hunters.contains(p.getName())){
					continue;
				}
				else {
					Hunters.add(p.getName());
					Closes.add(new ArrayList<Integer>());
					for (int i=0; i<Speeders.size();i++) {
						Closes.get(Closes.size()-1).add(0);
					}
				}
			}
		}
		
		if (cmd.getName().equals("speeder") && sender.hasPermission("speeder.use")) {
			for (String arg : args) {
				
				if (Bukkit.getServer().getPlayer(arg) == null) {
					continue;
				}
			
				Player p = Bukkit.getServer().getPlayer(arg);
				p.getInventory().clear();
				p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 999999, 1, false, false));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5, 10, false, false));
				p.setDisplayName("i am gay");
				if (Speeders.contains(p.getName())){
					return true;
				}
				else {
					Speeders.add(p.getName());
					for (ArrayList<Integer> SpeederList : Closes) {
						SpeederList.add(0);
					}
				}
				return true;
			}
		}
		
		if (cmd.getName().equals("start") && sender.hasPermission("start.use")) {
			for (String hunterString : Hunters) {
				Player hunterStart = Bukkit.getServer().getPlayer(hunterString);
				hunterStart.sendTitle("MANHUNT START", "Start moving in " + args[0] + " seconds", 10, 20 , 10);
				Location startPos = hunterStart.getLocation();
				for (int i=1; i<4*Integer.parseInt(args[0]); i++) {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					      public void run() {
					    	  hunterStart.teleport(startPos);
					      }
					    }, 5*i);
				}
			}
			for (String speederString : Speeders) {
				Bukkit.getServer().getPlayer(speederString).sendTitle("MANHUNT START", "Hunter will come in " + args[0] + " seconds", 10, 20 , 10);
			}
			for (int i=0; i<Integer.parseInt(args[0]); i++) {
				final int j=i;
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				      public void run() {
				    	  Bukkit.getServer().broadcastMessage("Hunters free in " + (Integer.parseInt(args[0])-j) + " seconds");
				      }
				    }, 20*i);
			}
			if (TaskInitialised) {
				Bukkit.getScheduler().cancelTask(Taskid);
				TaskInitialised=false;
			}
			
			Taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			    @Override
			    public void run() {
			        for(String hunterString : Hunters) {
			        	if (Bukkit.getServer().getPlayer(hunterString) == null) {
							continue;
						}
			        	
			        	Player hunter = Bukkit.getServer().getPlayer(hunterString);
			        	
			        	for (String speederString : Speeders) {
			    			if (Bukkit.getServer().getPlayer(speederString) == null) {
			    				continue;
			    			}
			    			
			    			
			    			Player speeder = Bukkit.getServer().getPlayer(speederString);
			    		
			    			if (hunter.getWorld()!=speeder.getWorld()) {
			    				if (Closes.get(Hunters.indexOf(hunterString)).get(Speeders.indexOf(speederString))==1) {
			    					for (int i=0; i<5; i++) {
				    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    						public void run() {
				    				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
				    				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
				    						}
				    					}, 5*i);
				    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    						public void run() {
				    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
				    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
				    						}
				    					}, 5*i);
				    				}
				    				hunter.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ("Speedrunner " + speederString + " is out of viewing distance!"));
				    				speeder.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ("Hunter " + hunterString + " is out of viewing distance!"));
				    				Closes.get(Hunters.indexOf(hunterString)).set(Speeders.indexOf(speederString), 0);
			    				}
			    				return;
			    			}
			    						    			
			    			Location loc1 = hunter.getLocation();
			    			Location loc2 = speeder.getLocation();
			    		
			    			if ((Closes.get(Hunters.indexOf(hunterString)).get(Speeders.indexOf(speederString))==0) && loc1.distance(loc2)<75) {
			    				for (int i=0; i<5; i++) {
			    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			    						public void run() {
			    							speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, (float) 5, (float) 1);
			    							speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, (float) 5, (float) 1);
			    						}
			    				    	}, 5*i);
			    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			    						public void run() {
			    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, (float) 5, (float) 1);
			    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, (float) 5, (float) 1);
			    						}
			    					}, 5*i);
			    				}
			    				hunter.sendMessage(ChatColor.RED + "" + ChatColor.BOLD +("Speedrunner " + speederString + " is within viewing distance!"));
			    				speeder.sendMessage(ChatColor.RED + "" + ChatColor.BOLD +("Hunter " + hunterString + " is within viewing distance!"));
			    				Closes.get(Hunters.indexOf(hunterString)).set(Speeders.indexOf(speederString), 1);
			    			}
			    		
			    			else if ((Closes.get(Hunters.indexOf(hunterString)).get(Speeders.indexOf(speederString))==1) && loc1.distance(loc2)>75) {
			    				for (int i=0; i<5; i++) {
			    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			    						public void run() {
			    				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
			    				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
			    						}
			    					}, 5*i);
			    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			    						public void run() {
			    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
			    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
			    						}
			    					}, 5*i);
			    				}
			    				hunter.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ("Speedrunner " + speederString + " is out of viewing distance!"));
			    				speeder.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ("Hunter " + hunterString + " is out of viewing distance!"));
			    				Closes.get(Hunters.indexOf(hunterString)).set(Speeders.indexOf(speederString), 0);
			    			}
			            
			    		}
			        }
			    }
			}, 0L, 20L);
			
			TaskInitialised=true;
			
			return false;
			
		}
		if (cmd.getName().equals("togglesound") && sender.hasPermission("togglesound.use")) {
			if (TaskInitialised) {
				Bukkit.getScheduler().cancelTask(Taskid);
				TaskInitialised=false;
			}
			else {
				Taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				    @Override
				    public void run() {
				        for(String hunterString : Hunters) {
				        	if (Bukkit.getServer().getPlayer(hunterString) == null) {
								continue;
							}
				        	
				        	Player hunter = Bukkit.getServer().getPlayer(hunterString);
				        	
				        	for (String speederString : Speeders) {
				    			if (Bukkit.getServer().getPlayer(speederString) == null) {
				    				continue;
				    			}
				    			
				    			
				    			Player speeder = Bukkit.getServer().getPlayer(speederString);
				    		
				    			if (hunter.getWorld()!=speeder.getWorld()) {
				    				if (Closes.get(Hunters.indexOf(hunterString)).get(Speeders.indexOf(speederString))==1) {
				    					for (int i=0; i<5; i++) {
					    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					    						public void run() {
					    				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
					    				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
					    						}
					    					}, 5*i);
					    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					    						public void run() {
					    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
					    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
					    						}
					    					}, 5*i);
					    				}
					    				hunter.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ("Speedrunner " + speederString + " is out of viewing distance!"));
					    				speeder.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ("Hunter " + hunterString + " is out of viewing distance!"));
					    				Closes.get(Hunters.indexOf(hunterString)).set(Speeders.indexOf(speederString), 0);
				    				}
				    				return;
				    			}
				    						    			
				    			Location loc1 = hunter.getLocation();
				    			Location loc2 = speeder.getLocation();
				    		
				    			if ((Closes.get(Hunters.indexOf(hunterString)).get(Speeders.indexOf(speederString))==0) && loc1.distance(loc2)<75) {
				    				for (int i=0; i<5; i++) {
				    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    						public void run() {
				    							speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, (float) 5, (float) 1);
				    							speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, (float) 5, (float) 1);
				    						}
				    				    	}, 5*i);
				    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    						public void run() {
				    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, (float) 5, (float) 1);
				    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, (float) 5, (float) 1);
				    						}
				    					}, 5*i);
				    				}
				    				hunter.sendMessage(ChatColor.RED + "" + ChatColor.BOLD +("Speedrunner " + speederString + " is within viewing distance!"));
				    				speeder.sendMessage(ChatColor.RED + "" + ChatColor.BOLD +("Hunter " + hunterString + " is within viewing distance!"));
				    				Closes.get(Hunters.indexOf(hunterString)).set(Speeders.indexOf(speederString), 1);
				    			}
				    		
				    			else if ((Closes.get(Hunters.indexOf(hunterString)).get(Speeders.indexOf(speederString))==1) && loc1.distance(loc2)>75) {
				    				for (int i=0; i<5; i++) {
				    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    						public void run() {
				    				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
				    				    	  speeder.playSound(speeder.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
				    						}
				    					}, 5*i);
				    					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    						public void run() {
				    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, (float) 5, (float) 1);
				    							hunter.playSound(hunter.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, (float) 5, (float) 1);
				    						}
				    					}, 5*i);
				    				}
				    				hunter.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ("Speedrunner " + speederString + " is out of viewing distance!"));
				    				speeder.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + ("Hunter " + hunterString + " is out of viewing distance!"));
				    				Closes.get(Hunters.indexOf(hunterString)).set(Speeders.indexOf(speederString), 0);
				    			}
				    		}
				        }
				    }
				}, 0L, 20L);
				TaskInitialised=true;
			}
			return false;
		}
		return false;
	}
}
