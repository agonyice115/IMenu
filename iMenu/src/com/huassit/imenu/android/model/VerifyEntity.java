package com.huassit.imenu.android.model;

import java.io.Serializable;

public class VerifyEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String verifyCode;
	public String menuCode;
	
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	
	
}
