package me.antritus.potteryeditor.astrolminiapi;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("ALL")
public class ColorUtils {
	public static String translate(String str){
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	public static Component translateComp(String msg){
		return MiniMessage.miniMessage().deserialize(msg).decoration(TextDecoration.ITALIC, false);
	}
	public static Component translateCompLegacy(String msg){
		return LegacyComponentSerializer.legacy('§').deserialize(msg);
	}
	public static String deseriazize(Component component){
		return PlainTextComponentSerializer.plainText().serialize(component);
	}
}
