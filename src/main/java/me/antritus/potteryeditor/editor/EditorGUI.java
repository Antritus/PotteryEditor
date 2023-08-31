package me.antritus.potteryeditor.editor;

import me.antritus.potteryeditor.astrolminiapi.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.DecoratedPot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;


public class EditorGUI extends AbstractEditorGUI{
	public EditorGUI(Player player) {
		super(player);
	}

	public int convert(DecoratedPot.Side side){
		if (side.ordinal()==1||side.ordinal()==2){
			return side.ordinal();
		}
		return side.ordinal()-3<0?3:0;
	}
	public DecoratedPot.Side convert(int mySide){
		if (mySide==1||mySide==2){
			return DecoratedPot.Side.values()[mySide];
		}
		return mySide==0 ? DecoratedPot.Side.FRONT:DecoratedPot.Side.BACK;
	}
	@Override
	public void updateInventory() {
		int invSize = 54;
		if (inventory == null) {
			inventory = Bukkit.createInventory(getPlayer(), invSize, ColorUtils.translateComp("Pottery Editor"));
			SHERDS.forEach(inventory::addItem);
			for (int i = 27; i < invSize; i++) {
				inventory.setItem(i, BACKGROUND);
			}

			inventory.setItem(30, BUTTON_BUILD);
			inventory.setItem(32, BUTTON_CLEAR);
		}

		sherds.putIfAbsent(0, null);
		sherds.putIfAbsent(1, null);
		sherds.putIfAbsent(2, null);
		sherds.putIfAbsent(3, null);
		ItemStack clone = BACKGROUND_POTTERY.clone();
		BlockStateMeta meta = (BlockStateMeta) clone.getItemMeta();
		org.bukkit.block.DecoratedPot pot = (DecoratedPot) meta.getBlockState();
		sherds.forEach((key, material)->{
			DecoratedPot.Side side = convert(key);
			pot.setSherd(side, material);
		});
		meta.setBlockState(pot);
		clone.setItemMeta(meta);

		inventory.setItem(40, clone);

		ItemStack item = sherds.get(0) != null && !sherds.get(0).isAir() ? generateSherd(sherds.get(0)) : BACKGROUND_SHERD;
		inventory.setItem(38, item);
		item = sherds.get(1) != null && !sherds.get(1).isAir() ? generateSherd(sherds.get(1)) : BACKGROUND_SHERD;
		inventory.setItem(39, item);
		item = sherds.get(2) != null && !sherds.get(2).isAir() ? generateSherd(sherds.get(2)) : BACKGROUND_SHERD;
		inventory.setItem(41, item);
		item = sherds.get(3) != null && !sherds.get(3).isAir() ? generateSherd(sherds.get(3)) : BACKGROUND_SHERD;
		inventory.setItem(42, item);
	}

	private ItemStack generateSherd(Material material){
		return new ItemStack(material);
	}
	private ItemStack generatePottery(){
		return BACKGROUND_POTTERY.clone();
	}
	private ItemStack generatePreview(Material material){
		return new ItemStack(material);
	}

	@Override
	public void open() {
		getPlayer().openInventory(inventory);
	}
}
