package me.antritus.potteryeditor.editor;

import me.antritus.potteryeditor.astrolminiapi.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

public abstract class AbstractEditorGUI {
	public static final ItemStack BACKGROUND = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
	public static final ItemStack BACKGROUND_SHERD = new ItemStack(Material.BRICK);
	public static final ItemStack BACKGROUND_POTTERY = new ItemStack(Material.DECORATED_POT);
	public static final ItemStack BUTTON_CLEAR = new ItemStack(Material.RED_DYE);
	public static final ItemStack BUTTON_BUILD = new ItemStack(Material.LIME_DYE);
	public static final ItemStack BUTTON_PREVIEW_CRAFT = new ItemStack(Material.CRAFTING_TABLE);
	public static final List<ItemStack> SHERDS = new ArrayList<>();
	protected static final List<Component> SHERD_LORE = new ArrayList<>();

	static {
		SHERD_LORE.add(ColorUtils.translateComp("<gray>Set as <green>front</green> side:</gray> <green>LEFT"));
		SHERD_LORE.add(ColorUtils.translateComp("<gray>Set as <green>left</green> side:</gray> <green>RIGHT"));
		SHERD_LORE.add(ColorUtils.translateComp("<gray>Set as <green>right</green> side:</gray> <green>SHIFT<gray>+<green>LEFT"));
		SHERD_LORE.add(ColorUtils.translateComp("<gray>Set as <green>back</green> side:</gray> <green>SHIFT<gray>+<green>RIGHT"));

		Arrays.stream(Material.values()).filter(material->material.name().toUpperCase().endsWith("_POTTERY_SHERD")).forEachOrdered(material-> {
					ItemStack itemStack = new ItemStack(material);
					ItemMeta meta = itemStack.getItemMeta();
					Component name = meta.hasDisplayName() ? meta.displayName() : ColorUtils.translateComp("<gold>").append(Component.translatable(itemStack.translationKey()));
					meta.displayName(name);
					meta.lore(SHERD_LORE);
					meta.addItemFlags(ItemFlag.values());
					itemStack.setItemMeta(meta);
					SHERDS.add(itemStack);
				}
		);
		ItemMeta meta = BACKGROUND.getItemMeta();
		meta.displayName(ColorUtils.translateComp(""));
		meta.addItemFlags(ItemFlag.values());
		editorItem(meta);
		BACKGROUND.setItemMeta(meta);
		meta = BACKGROUND_SHERD.getItemMeta();
		meta.displayName(ColorUtils.translateComp("<red>Clear Sherd"));
		meta.lore(List.of(
				ColorUtils.translateComp("<gray>This will be a clear side in the pottery.")
		));
		meta.addItemFlags(ItemFlag.values());
		editorItem(meta);
		BACKGROUND_SHERD.setItemMeta(meta);
		meta = BACKGROUND_POTTERY.getItemMeta();
		meta.displayName(ColorUtils.translateComp("<green>Decorated Pot"));
		meta.addItemFlags(ItemFlag.values());
		meta.removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
		editorItem(meta);
		BACKGROUND_POTTERY.setItemMeta(meta);


		meta = BUTTON_CLEAR.getItemMeta();
		List<Component> lore = new ArrayList<>();
		Component name = ColorUtils.translateComp("<red>Clear Editor (<underlined>NO GOING BACK</underlined>)");
		meta.displayName(name);
		lore.add(ColorUtils.translateComp("<gray>Click to clear the sherd slots of the editor."));
		meta.lore(lore);
		meta.addItemFlags(ItemFlag.values());
		BUTTON_CLEAR.setItemMeta(meta);

		meta = BUTTON_PREVIEW_CRAFT.getItemMeta();
		name = ColorUtils.translateComp("<yellow>Preview Recipe");
		meta.displayName(name);
		meta.lore(List.of(
				ColorUtils.translateComp("<gray>Click to preview crafting recipe.")
		));
		meta.addItemFlags(ItemFlag.values());
		BUTTON_PREVIEW_CRAFT.setItemMeta(meta);
	}
	static {
		BUTTON_BUILD.editMeta((meta)->{
			meta.displayName(ColorUtils.translateComp("<green>Build"));
			meta.lore(List.of(ColorUtils.translateComp("<gray>Gives you build of the pottery.")));
			meta.addItemFlags(ItemFlag.values());
		});
	}
	protected static void addPotterPreview(List<Component> lore){
		lore.add(ColorUtils.translateComp("<gray>Shift click to see crafting recipe."));
	}
	protected HashMap<Integer, Material> sherds = new HashMap<>();
	protected Inventory inventory;
	private UUID uuid;
	protected int clicks = 0;
	protected long lastCountStart = 0L;

	public AbstractEditorGUI(Player player){
		uuid = player.getUniqueId();
		updateInventory();
		sherds.put(0, null);
		sherds.put(1, null);
		sherds.put(2, null);
		sherds.put(3, null);
	}
	public abstract void updateInventory();
	public abstract void open();
	@NotNull
	public Player getPlayer(){
		Player player = Bukkit.getPlayer(uuid);
		if (player == null){
			throw new RuntimeException("Could not find player for uuid: "+uuid);
		}
		return player;
	}

	public static void editorItem(ItemMeta meta){
		if (meta == null){
			return;
		}
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		dataContainer.set(EditorManager.KEY_EDITOR_ITEM, PersistentDataType.BOOLEAN, true);
	}
	public static boolean isEditorItem(ItemMeta meta){
		if (meta == null){
			return false;
		}
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		return dataContainer.has(EditorManager.KEY_EDITOR_ITEM, PersistentDataType.BOOLEAN);
	}
	@Nullable
	public Material getSherd(@Range(from=0, to=3) int i){
		return sherds.get(i);
	}
	@NotNull
	public Map<Integer, Material> getSherds(){
		return sherds;
	}
}
