package com.voyage.meta.db;

import java.sql.Timestamp;

public class User {

	private int id;
	private String username;
	private String password;
	private String nickname;
	private Timestamp createtime;
	private Timestamp lastmodifytime;
	private Timestamp lastlogintime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getLastmodifytime() {
		return lastmodifytime;
	}

	public void setLastmodifytime(Timestamp lastmodifytime) {
		this.lastmodifytime = lastmodifytime;
	}

	public Timestamp getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Timestamp lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

}
