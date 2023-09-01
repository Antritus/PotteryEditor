package me.antritus.potteryeditor.editor;

import me.antritus.potteryeditor.PotteryEditor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.DecoratedPot;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class EditorManager implements Listener {
	private final HashMap<UUID, AbstractEditorGUI> guis = new HashMap<>();
	public static NamespacedKey KEY_EDITOR_ITEM;
	public EditorManager(PotteryEditor editor)
	{
		KEY_EDITOR_ITEM = new NamespacedKey(editor, "editorItem");
	}
	@NotNull
	public AbstractEditorGUI get(Player player){
		guis.putIfAbsent(player.getUniqueId(), new EditorGUI(player));
		return guis.get(player.getUniqueId());
	}
	@Nullable
	public AbstractEditorGUI get(Inventory inventory){
		AtomicReference<AbstractEditorGUI> gui = new AtomicReference<>();
		guis.forEach((uniqueId, guiInstance)->{
			if (guiInstance.inventory.equals(inventory)){
				gui.set(guiInstance);
			}
		});
		return gui.get();
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		guis.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onInteract(InventoryClickEvent event){
		Inventory inventory = event.getClickedInventory();
		if (inventory == null){
			return;
		}
		Player player = (Player) event.getWhoClicked();
		if (inventory.getHolder() instanceof EditorGUI gui) {
			event.setCancelled(true);
			event.setResult(Event.Result.DENY);
			ItemStack itemStack = event.getCurrentItem();
			if (itemStack == null) {
				return;
			}
			if (event.getSlot()==30){
				if (!event.getWhoClicked().hasPermission("potteryeditor.build")){
					event.getWhoClicked().sendRichMessage("<red>You do not have enough permissions to build decorated pots!");
					return;
				}
				ItemStack item = new ItemStack(Material.DECORATED_POT);
				BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
				DecoratedPot clone = (DecoratedPot) ((BlockStateMeta) (event.getInventory().getItem(40).getItemMeta())).getBlockState();
				meta.setBlockState(clone);
				item.setItemMeta(meta);
				player.getInventory().addItem(item);
				return;
			}
			if (event.getSlot()==32){
				gui.sherds.put(0, null);
				gui.sherds.put(1, null);
				gui.sherds.put(2, null);
				gui.sherds.put(3, null);
				gui.updateInventory();
				return;
			}

			if (event.getSlot()==38
					||event.getSlot()==39
					||event.getSlot()==41
					||event.getSlot()==42) {
				int slot = switch (event.getSlot()) {
					case 39 -> 1;
					case 41 -> 2;
					case 42 -> 3;
					default -> 0;
				};
				gui.sherds.put(slot, null);
				gui.updateInventory();
				return;
			}
			if (event.getSlot()==49){
				if (!event.getWhoClicked().hasPermission("potteryeditor.preview")){
					event.getWhoClicked().sendRichMessage("<red>You do not have enough permissions to preview this decorated pot's crafting recipe!");
					return;
				}
				gui.previewMenu.updateInventory();
				player.openInventory(gui.previewMenu.getInventory());
				return;
			}
			if (itemStack.getType().name().endsWith("_POTTERY_SHERD")){
				if (event.getSlot()<=26){
					//if (event.getClick()==ClickType.NUMBER_KEY){
					//	int hotbarButton = event.getHotbarButton();
					//	if (hotbarButton>3){
					//		return;
					//	}
					//	gui.sherds.put(hotbarButton, itemStack.getType());
					//	gui.updateInventory();
					//}else {
					//	int slot = -1;
					//	if (gui.sherds.get(0) == null)
					//		slot = 0;
					//	else if (gui.sherds.get(1) == null)
					//		slot = 1;
					//	else if (gui.sherds.get(2) == null)
					//		slot = 2;
					//	else if (gui.sherds.get(3) == null)
					//		slot = 3;
					//	gui.sherds.put(slot, itemStack.getType());
					//	gui.updateInventory();
					//}
					int slot = switch (event.getClick()){
						case LEFT -> 0;
						case RIGHT -> 1;
						case SHIFT_LEFT -> 2;
						case SHIFT_RIGHT -> 3;
						default -> -1;
					};
					if (slot==-1){
						return;
					}
					gui.sherds.put(slot, itemStack.getType());
					gui.updateInventory();
					return;
				}
			}
		} else if (event.getInventory().getHolder() instanceof CraftingPreviewMenu menu){
			event.setCancelled(true);
			if (event.getSlot()==22){
				event.getWhoClicked().openInventory(guis.get(player.getUniqueId()).inventory);
			}
		}
	}
}