package me.lainad27.manhunt;

import org.bukkit.plugin.java.JavaPlugin;

import me.lainad27.manhunt.listenerExec.ManhuntListenerExecutor;

public class Main extends JavaPlugin{

	@Override
	public void onEnable() {		
		ManhuntListenerExecutor MainExec = new ManhuntListenerExecutor(this);
		this.getCommand("hunter").setExecutor(MainExec);
		this.getCommand("speeder").setExecutor(MainExec);
		this.getCommand("start").setExecutor(MainExec);
		this.getCommand("togglesound").setExecutor(MainExec);
	}
}
