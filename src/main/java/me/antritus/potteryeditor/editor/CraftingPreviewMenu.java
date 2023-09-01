package me.antritus.potteryeditor.editor;

import me.antritus.potteryeditor.astrolminiapi.ColorUtils;
import me.antritus.potteryeditor.astrolminiapi.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


public class CraftingPreviewMenu implements InventoryHolder {
	public static final ItemStack BUTTON_RETURN = new ItemStack(Material.BARRIER);
	static {
		ItemMeta meta = BUTTON_RETURN.getItemMeta();
		meta.displayName(ColorUtils.translateComp("<yellow>Return"));
		BUTTON_RETURN.setItemMeta(meta);
	}
	private final Inventory inventory;
	private final InventoryUtils.InventoryPattern pattern;
	private final AbstractEditorGUI editorGUI;
	public CraftingPreviewMenu(AbstractEditorGUI editorGUI){
		this.editorGUI = editorGUI;
		inventory = InventoryUtils.createInventory(this, "Pottery Editor > Preview", 3);
		pattern = new InventoryUtils.InventoryPattern(
				"xyayxxxxx",
				"xbycxxzxx",
				"xydyoxxxx"
				);
		pattern.setItem('x', AbstractEditorGUI.BACKGROUND);
		pattern.setItem('y', Material.AIR);
		pattern.setItem('o', BUTTON_RETURN);
		pattern.setItem('z', editorGUI.inventory.getItem(40));
		pattern.setItem('d', editorGUI.sherds.get(0) != null ? editorGUI.sherds.get(0) : Material.BRICK);
		pattern.setItem('b', editorGUI.sherds.get(1) != null ? editorGUI.sherds.get(1) : Material.BRICK);
		pattern.setItem('c', editorGUI.sherds.get(2) != null ? editorGUI.sherds.get(2) : Material.BRICK);
		pattern.setItem('a', editorGUI.sherds.get(3) != null ? editorGUI.sherds.get(3) : Material.BRICK);
		pattern.update(inventory);
	}
	public void updateInventory(){
		pattern.setItem('z', editorGUI.inventory.getItem(40));
		pattern.setItem('d', editorGUI.sherds.get(0) != null ? editorGUI.sherds.get(0) : Material.BRICK);
		pattern.setItem('b', editorGUI.sherds.get(1) != null ? editorGUI.sherds.get(1) : Material.BRICK);
		pattern.setItem('c', editorGUI.sherds.get(2) != null ? editorGUI.sherds.get(2) : Material.BRICK);
		pattern.setItem('a', editorGUI.sherds.get(3) != null ? editorGUI.sherds.get(3) : Material.BRICK);
		pattern.update(inventory);
	}
	@Override
	public @NotNull Inventory getInventory() {
		return inventory;
	}
}
