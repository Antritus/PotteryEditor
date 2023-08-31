package me.antritus.potteryeditor;

import me.antritus.potteryeditor.astrolminiapi.CoreCommand;
import me.antritus.potteryeditor.editor.EditorManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PotteryEditor extends JavaPlugin {
	public EditorManager manager;
	public PotteryEditor(){

	}
	@Override
	public void onEnable(){
		manager = new EditorManager(this);
		getServer().getPluginManager().registerEvents(manager, this);
		CoreCommand.registerCommand(this, new CMDPotteryEditor(this));
	}
}
