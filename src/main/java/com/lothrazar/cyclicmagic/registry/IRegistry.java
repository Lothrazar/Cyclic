package com.lothrazar.cyclicmagic.registry;


public interface IRegistry{

	public boolean doPreInitRegister();//does register happen early or as normal?
	
	public void register();
	
	public void syncConfig();
	
	public void registerPackets();
	
	
}
