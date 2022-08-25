package com.stinkymonkey.Bank.Database;

import java.sql.Connection;

import com.stinkymonkey.Bank.Monkey;

public class DatabaseManagerFlatFile implements DatabaseManagerInterface
{
	@SuppressWarnings("unused")
	private Monkey monkey;

	public DatabaseManagerFlatFile(Monkey money) {
		this.monkey = money;
		
		setupDatabase();
	}

	@Override
	public boolean setupDatabase() {
		return true;
	}

	@Override
	public boolean closeDatabase() {
		return true;
	}
	
	@Override
	public Connection getConnection() {
		return null;
	}
}
