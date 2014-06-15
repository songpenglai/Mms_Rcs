package com.android.rcs.util;

import android.content.ContentValues;

public class RcsModelGroups {

	public static final String[] GROUPS_ARRAY = {
		"name",					// 0
		"type",				    // 1
		"organicer_phone",		// 2
		"begin_time",			// 3
		"end_time",				// 4
		"duration",				// 5
		"thread_id",			// 6
		"group_id",				// 7
		"session_idertity",		// 8
		"group_chat_id",		// 9
		"group_member"			// 10
	};
	
	
	ContentValues Values = new ContentValues();
	String name;
	int type;
	String organicer_phone;
	long begin_time;
	long end_time;
	String duration;
	int thread_id;
	String group_id;
	String session_idertity;
	String group_chat_id;
	String group_member;
	
	
	public ContentValues getValues() {
		return Values;
	}
	public void setValues(ContentValues values) {
		Values = values;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		Values.put("name", name);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
		Values.put("type", type);
	}
	public String getOrganicer_phone() {
		return organicer_phone;
	}
	public void setOrganicer_phone(String organicer_phone) {
		this.organicer_phone = organicer_phone;
		Values.put("organicer_phone", organicer_phone);
	}
	public long getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(long begin_time) {
		this.begin_time = begin_time;
		Values.put("begin_time", begin_time);
	}
	public long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(long end_time) {
		this.end_time = end_time;
		Values.put("end_time", end_time);
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
		Values.put("duration", duration);
	}
	public int getThread_id() {
		return thread_id;
	}
	public void setThread_id(int thread_id) {
		this.thread_id = thread_id;
		Values.put("thread_id", thread_id);
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
		Values.put("group_id", group_id);
	}
	public String getSession_idertity() {
		return session_idertity;
	}
	public void setSession_idertity(String session_idertity) {
		this.session_idertity = session_idertity;
		Values.put("session_idertity", session_idertity);
	}
	public String getGroup_chat_id() {
		return group_chat_id;
	}
	public void setGroup_chat_id(String group_chat_id) {
		this.group_chat_id = group_chat_id;
		Values.put("group_chat_id", group_chat_id);
	}
	public String getGroup_member() {
		return group_member;
	}
	public void setGroup_member(String group_member) {
		this.group_member = group_member;
		Values.put("group_member", group_member);
	}


}
