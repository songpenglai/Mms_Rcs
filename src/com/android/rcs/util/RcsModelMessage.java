package com.android.rcs.util;

import android.content.ContentValues;

public class RcsModelMessage {
	ContentValues Values = new ContentValues();
	int thread_id;
	String address;

	long date;
	long date_sent;
	int protocol;
	int read;
	int status;

	int type;
	String subject;
	String body;
	String service_center;
	int locked;
	int sub_id;
	int error_code;
	int seen;

	// rcs
	String file_name;
	String file_type;
	String file_path;
	int file_size;
	int send_size;
	int recv_size;
	String imdn_string;
	int imdn_type;
	int notify_type;
	int abbreviative;
	public ContentValues getContentValues() {
		return Values;
	}
	
	public int getThread_id() {
		return thread_id;
	}
	public void setThread_id(int thread_id) {
		this.thread_id = thread_id;
		Values.put("thread_id", thread_id);
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
		Values.put("address", address);
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
		Values.put("date", date);
	}
	public long getDate_sent() {
		return date_sent;
	}
	public void setDate_sent(long date_sent) {
		this.date_sent = date_sent;
		Values.put("date_sent", date_sent);
	}
	public int getProtocol() {
		return protocol;
	}
	public void setProtocol(int protocol) {
		this.protocol = protocol;
		Values.put("protocol", protocol);
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
		Values.put("read", read);
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
		Values.put("status", status);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
		Values.put("type", type);
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
		Values.put("subject", subject);
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
		Values.put("body", body);
	}
	public String getService_center() {
		return service_center;
	}
	public void setService_center(String service_center) {
		this.service_center = service_center;
		Values.put("service_center", service_center);
	}
	public int getLocked() {
		return locked;
	}
	public void setLocked(int locked) {
		this.locked = locked;
		Values.put("locked", locked);
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
		Values.put("sub_id", sub_id);
	}
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
		Values.put("error_code", error_code);
	}
	public int getSeen() {
		return seen;
	}
	public void setSeen(int seen) {
		this.seen = seen;
		Values.put("seen", seen);
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
		Values.put("file_name", file_name);
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
		Values.put("file_type", file_type);
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
		Values.put("file_path", file_path);
	}
	public int getFile_size() {
		return file_size;
	}
	public void setFile_size(int file_size) {
		this.file_size = file_size;
		Values.put("file_size", file_size);
	}
	public int getSend_size() {
		return send_size;
	}
	public void setSend_size(int send_size) {
		this.send_size = send_size;
		Values.put("send_size", send_size);
	}
	public int getRecv_size() {
		return recv_size;
	}
	public void setRecv_size(int recv_size) {
		this.recv_size = recv_size;
		Values.put("recv_size", recv_size);
	}
	public String getImdn_string() {
		return imdn_string;
	}
	public void setImdn_string(String imdn_string) {
		this.imdn_string = imdn_string;
		Values.put("imdn_string", imdn_string);
	}
	public int getImdn_type() {
		return imdn_type;
	}
	public void setImdn_type(int imdn_type) {
		this.imdn_type = imdn_type;
		Values.put("imdn_type", imdn_type);
	}
	public int getNotify_type() {
		return notify_type;
	}
	public void setNotify_type(int notify_type) {
		this.notify_type = notify_type;
		Values.put("notify_type", notify_type);
	}
	public int getAbbreviative() {
		return abbreviative;
	}
	public void setAbbreviative(int abbreviative) {
		this.abbreviative = abbreviative;
		Values.put("abbreviative", abbreviative);
	}

}
