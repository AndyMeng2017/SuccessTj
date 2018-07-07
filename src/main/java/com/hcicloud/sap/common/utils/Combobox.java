package com.hcicloud.sap.common.utils;

import java.io.Serializable;

/**
 * @Title: Combobox.java
 * @Package com.sinovoice.hcicloud.model.comm 
 * @Description: easyui 下拉列表实体
 * @author wudong
 * @date 2015年2月28日 上午10:37:49
 */
public class Combobox implements Serializable{

	public Combobox(){
		super();
	}
	public Combobox(Object id, String text) {
		super();
		this.id = id.toString();
		this.text = text;
	}
	private static final long serialVersionUID = -9098485911081038020L;
	/**
	 * 下拉列表值
	 */
	private String id;
	/**
	 * 下拉列表文本
	 */
	private String text;
	/**
	 * 获取id  
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置id  
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
