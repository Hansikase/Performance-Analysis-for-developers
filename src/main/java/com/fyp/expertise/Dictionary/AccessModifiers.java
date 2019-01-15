package com.fyp.expertise.Dictionary;

public enum AccessModifiers {
	PRIVATE("private"),PUBLIC("public"),PROTECTED("protected");
	
	private String mod;
	
	private AccessModifiers(final String mod){
		this.mod = mod;
	}
	
	public String getModifier(){
		return mod;
	}
}
