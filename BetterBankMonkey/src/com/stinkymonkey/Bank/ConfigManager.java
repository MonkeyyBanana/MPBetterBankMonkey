package com.stinkymonkey.Bank;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ConfigManager 
{
	private Monkey monkey;

	//Config Generation and load
	public ConfigManager(Monkey monkey) {
		this.monkey = monkey;
		loadConfig();
	}
	
	public void loadConfig() {
		//Create MysqlPlayerDataBridge folder
		File pluginFolder = new File("plugins" + System.getProperty("file.separator") + Monkey.instance.getDescription().getName());
		if (pluginFolder.exists() == false) {
    		pluginFolder.mkdir();
    	}
		
		File configFile = new File("plugins" + System.getProperty("file.separator") + Monkey.instance.getDescription().getName() + System.getProperty("file.separator") + "config.yml");
		if (configFile.exists() == false) {
			System.out.println("No config file found! Creating new one...");
    		monkey.saveDefaultConfig();
		}
    	
    	try {
    		System.out.println("Loading the config file...");
    		monkey.getConfig().load(configFile);
    	} catch (Exception e) {
    		System.out.println("Could not load the config file! You need to regenerate the config! Error: " + e.getMessage());
			e.printStackTrace();
    	}
	}
	
	//Get config contents by strings
	public String getString(String key) {
		if (!monkey.getConfig().contains(key)) {
			monkey.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + Monkey.instance.getDescription().getName() + " folder! (Try generating a new one by deleting the current)");
			return "errorCouldNotLocateInConfigYml:" + key;
		} else {
			return monkey.getConfig().getString(key);
		}
	}
	
	public String getStringWithColor(String key) {
		if (!monkey.getConfig().contains(key)) {
			monkey.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + Monkey.instance.getDescription().getName() + " folder! (Try generating a new one by deleting the current)");
			return "errorCouldNotLocateInConfigYml:" + key;
		} else {
			return monkey.getConfig().getString(key).replaceAll("&", "§");
		}
	}
	
	public List<String> getStringList(String key) {
		if (!monkey.getConfig().contains(key)) {
			monkey.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + Monkey.instance.getDescription().getName() + " folder! (Try generating a new one by deleting the current)");
			return null;
		} else {
			return monkey.getConfig().getStringList(key);
		}
	}
	
	public Integer getInteger(String key) {
		if (!monkey.getConfig().contains(key)) {
			monkey.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + Monkey.instance.getDescription().getName() + " folder! (Try generating a new one by deleting the current)");
			return null;
		} else {
			return monkey.getConfig().getInt(key);
		}
	}
	
	public Boolean getBoolean(String key) {
		if (!monkey.getConfig().contains(key)) {
			monkey.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + Monkey.instance.getDescription().getName() + " folder! (Try generating a new one by deleting the current)");
			return null;
		} else {
			return monkey.getConfig().getBoolean(key);
		}
	}
	
	//Send player messages using the config predefined messages
	public void printMessage(Player player, String messageKey, String amount, Player player2, String player2Name) {
		if (monkey.getConfig().contains(messageKey)) {
			List<String> message = new ArrayList<String>();
			message.add(monkey.getConfig().getString(messageKey));
			
			//if message is set to '' cancel the message
			if (getString(messageKey).equals("")) {
				return;
			}
			
			DecimalFormat f = new DecimalFormat("#,##0.00");
			
			if (amount != null && !amount.equals("")) {
				Double amountDouble = Double.parseDouble(amount);
				if (amountDouble.toString().endsWith(".0")) {
					DecimalFormat fr = new DecimalFormat("#,##0");
					message.set(0, message.get(0).replaceAll("%amount", "" + fr.format(amountDouble)));
				} else {
				    message.set(0, message.get(0).replaceAll("%amount", "" + f.format(amountDouble)));
				}
			}
			
			if (player2 != null) {
				if (!player2Name.equals("")) {
					message.set(0, message.get(0).replaceAll("%player2", player2Name));
				}
				if (monkey.getMoneyDatabaseInterface().hasAccount(player2)) {
					if (monkey.getMoneyDatabaseInterface().getBalance(player2).toString().endsWith(".0")) {
						DecimalFormat fr = new DecimalFormat("#,##0");
						message.set(0, message.get(0).replaceAll("%balance", "" + fr.format(monkey.getMoneyDatabaseInterface().getBalance(player2))));
					} else  {
					    message.set(0, message.get(0).replaceAll("%balance", "" + f.format(monkey.getMoneyDatabaseInterface().getBalance(player2))));
					}
					
				}
			}
			
			message.set(0, message.get(0).replaceAll("%pocket", ""+ Monkey.eco.getBalance(player)));
			message.set(0, message.get(0).replaceAll("%player", player.getName()));
			//Message format
			player.sendMessage(parseFormattingCodes(getString("chatMessages.prefix")) + parseFormattingCodes(message.get(0)));
			for (int i = 1; i < message.size(); i++) {
				player.sendMessage(parseFormattingCodes(message.get(i)));
			}
			
		} else {
			monkey.getLogger().severe("Could not locate '"+messageKey+"' in the config.yml inside of the MysqlEconomyBank folder!");
			player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Could not locate '"+messageKey+"' in the config.yml inside of the MysqlEconomyBank folder!");
		}
	}
	
	//Color and Format codes support
	public String parseFormattingCodes(String message) {
		message = message.replaceAll("&0", ChatColor.BLACK + "");
		message = message.replaceAll("&1", ChatColor.DARK_BLUE + "");
		message = message.replaceAll("&2", ChatColor.DARK_GREEN + "");
		message = message.replaceAll("&3", ChatColor.DARK_AQUA + "");
		message = message.replaceAll("&4", ChatColor.DARK_RED + "");
		message = message.replaceAll("&5", ChatColor.DARK_PURPLE + "");
		message = message.replaceAll("&6", ChatColor.GOLD + "");
		message = message.replaceAll("&7", ChatColor.GRAY + "");
		message = message.replaceAll("&8", ChatColor.DARK_GRAY + "");
		message = message.replaceAll("&9", ChatColor.BLUE + "");
		message = message.replaceAll("&a", ChatColor.GREEN + "");
		message = message.replaceAll("&b", ChatColor.AQUA + "");
		message = message.replaceAll("&c", ChatColor.RED + "");
		message = message.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
		message = message.replaceAll("&e", ChatColor.YELLOW + "");
		message = message.replaceAll("&f", ChatColor.WHITE + "");
		message = message.replaceAll("&l", ChatColor.BOLD + "");
		message = message.replaceAll("&o", ChatColor.ITALIC + "");
		message = message.replaceAll("&m", ChatColor.STRIKETHROUGH + "");
		message = message.replaceAll("&n", ChatColor.UNDERLINE + "");
		message = message.replaceAll("&k", ChatColor.MAGIC + "");
		message = message.replaceAll("&r", ChatColor.RESET + "");
		return message;
	}
}
