package me.lainad27.trollSmp;

import org.bukkit.plugin.java.JavaPlugin;

import me.lainad27.trollSmp.englishChat.EnglishChatter;
import me.lainad27.trollSmp.hideCommands.CommandHider;
import me.lainad27.trollSmp.leakCords.CordsLeaker;
import me.lainad27.trollSmp.permaTrolled.PermaTrolledConfig;
import me.lainad27.trollSmp.permaTrolled.PermaTroller;
import me.lainad27.trollSmp.replaceValubles.ValublesReplacer;

public class Main extends JavaPlugin{

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults();
        saveDefaultConfig();
        
        PermaTrolledConfig.setup();
        PermaTrolledConfig.get().options().copyDefaults(true);
        PermaTrolledConfig.save();
        
        
        PermaTroller permaTroller = new PermaTroller(this);
        this.getCommand("togglepermatroll").setExecutor(permaTroller);
        
        
        EnglishChatter englishChatter = new EnglishChatter(this);
        this.getCommand("togglehebrew").setExecutor(englishChatter);
        
		ValublesReplacer MainExec = new ValublesReplacer(this);
		this.getCommand("troll").setExecutor(MainExec);
		CordsLeaker MainExec2 = new CordsLeaker(this);
		this.getCommand("leak").setExecutor(MainExec2);
		
		new CommandHider(this);
	}
}
