package com.spring.dbservice;

import com.event.BaseEvent;

public class CanneoGetConnectionEvent extends BaseEvent {

	public final static String CANNOT_GET_CONNECTION_EVENT = "CANNOT_GET_CONNECTION_EVENT";

	private DBTemplate db = null;
	
	private Exception e = null;


	public CanneoGetConnectionEvent(DBTemplate _db,Exception e) {
		super(CANNOT_GET_CONNECTION_EVENT);
		setDb(_db);
		setE(e);
	}

	public CanneoGetConnectionEvent(String _type) {
		super(_type);
	}

	public DBTemplate getDb() {
		return db;
	}

	public void setDb(DBTemplate db) {
		this.db = db;
	}

	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

}
