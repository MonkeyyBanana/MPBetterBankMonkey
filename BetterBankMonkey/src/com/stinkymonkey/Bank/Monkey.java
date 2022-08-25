package com.stinkymonkey.Bank;

import java.io.File;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.stinkymonkey.Bank.Database.AccountDatabaseInterface;
import com.stinkymonkey.Bank.Database.DatabaseManagerFlatFile;
import com.stinkymonkey.Bank.Database.DatabaseManagerInterface;
import com.stinkymonkey.Bank.Database.MoneyFlatFileInterface;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Monkey extends JavaPlugin
{
	public static Monkey instance;
	private static ConfigManager configM;
	public static Permission perms = null;
	public static Economy eco;
	private AccountDatabaseInterface<Double> moneyDatabaseInterface;
	private DatabaseManagerInterface databaseManager;
	
	@Override
	public void onEnable() 
	{
		instance = this;
		if (!setupEconomy())
		{
			System.out.println(ChatColor.RED + "Vault or Economy is not installed! https://dev.bukkit.org/projects/vault");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		CommandHandler commandBanana = new CommandHandler(this);
		getCommand("bank").setExecutor(commandBanana);
		setupPermissions();
		configM = new ConfigManager(this);
		if (!new File("plugins"+System.getProperty("file.separator")+"MysqlEconomyBank"+System.getProperty("file.separator")+"Accounts").exists()) 
		{
    		(new File("plugins"+System.getProperty("file.separator")+"MysqlEconomyBank"+System.getProperty("file.separator")+"Accounts")).mkdir();
    	}
    	System.out.println(instance.getDescription().getName() + " loaded successfully!");
    	databaseManager = new DatabaseManagerFlatFile(this);
    	moneyDatabaseInterface = new MoneyFlatFileInterface(this);
		
	}
	
	@Override
	public void onDisable()
	{
		// Shutdown
		// Reloads
		// Plugin Reloads
	}
	
	
	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economy != null)
			eco = economy.getProvider();
		return (eco!= null);
	}
	
	private boolean setupPermissions() 
	{
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        System.out.println("Using permission plugin: " + rsp.getProvider().getName());
        return perms != null;
    }
	
	public ConfigManager getConfigManager() 
	{
			return configM;
	}

	public AccountDatabaseInterface<Double> getMoneyDatabaseInterface() 
	{
    	return moneyDatabaseInterface;
    }
}
