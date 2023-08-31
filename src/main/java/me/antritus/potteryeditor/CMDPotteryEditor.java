package me.antritus.potteryeditor;

import me.antritus.potteryeditor.astrolminiapi.CoreCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class CMDPotteryEditor extends CoreCommand {
	private final PotteryEditor editor;
	protected CMDPotteryEditor(PotteryEditor editor) {
		super("potteryeditor");
		setPermission("potteryeditor.editor");
		this.editor = editor;
	}
	@Override
	public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
		if (commandSender instanceof Player player){
			if (args.length>0 && args[0].equalsIgnoreCase("-info")){
				ItemStack itemStack = player.getInventory().getItemInMainHand();
				if (!itemStack.hasItemMeta()){
					return true;
				}
				player.sendMessage(itemStack.getItemMeta().getPersistentDataContainer().toString());
			}
			editor.manager.get(player).open();
		}
		return false;
	}
}
