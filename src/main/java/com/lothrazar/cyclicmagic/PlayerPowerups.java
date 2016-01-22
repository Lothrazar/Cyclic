package com.lothrazar.cyclicmagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerPowerups implements IExtendedEntityProperties {
	private final static String EXT_PROP_NAME = "CyclicMagic" + Const.MODID;
	private final EntityPlayer player;// we get one of these powerup classes for

	private static final int UNLOCKS_WATCHER = 27;
	private static final String NBT_UNLOCKS = "unlocks";

	private static final int MANA_WATCHER = 29;
	private static final String NBT_MANA = "samMana";
	
	private static final int TIMER_WATCHER = 30;
	private static final String NBT_TIMER = "samSpellTimer";

	public PlayerPowerups(EntityPlayer player) {
		this.player = player;
		this.player.getDataWatcher().addObject(TIMER_WATCHER, 0);
		this.player.getDataWatcher().addObject(MANA_WATCHER, 5);
		String spells = "";
		for (int i = 0; i < SpellRegistry.getSpellbook().size(); i++)
			spells += "1";

		this.player.getDataWatcher().addObject(UNLOCKS_WATCHER, spells);
	}
	private void setUnlockDefault(){
		String spells = "";
		for (int i = 0; i < SpellRegistry.getSpellbook().size(); i++)
			spells += "1";

		this.setSpellUnlocks(spells);
	}

	//this is probably super innefficeint BUT i have no choice
	//data watcher limits me to only 12 slots [20-32] and has
	//NO data arrays at all
	//would use int/byte array if i could. NBT supports them but not watchers
	private byte[] getUnlocksFromString(){
		String s = this.getSpellUnlocks();	
		if(s.length() < SpellRegistry.getSpellbook().size()){
			setUnlockDefault();
		}
		s = this.getSpellUnlocks();	
		byte[] ret = new byte[s.length()];
		for(int i = 0; i < s.length(); i++){
			if(s.charAt(i) == '0')
				ret[i] = 0;
			else if(s.charAt(i) == '1')
				ret[i] = 1;
		}
		return ret;
	}
	
	private void setUnlocksFromByte(byte[] ret){
		
		String spells = "";//new byte[SpellRegistry.getSpellbook().size()];
		for (int i = 0; i < ret.length; i++){
			if(ret[i] == 1)
				spells += "1";
			else if(ret[i] == 0)
				spells += "0";
		}
		this.setSpellUnlocks(spells);
	}
	
	@Override
	public void init(Entity entity, World world) {
	}

	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(PlayerPowerups.EXT_PROP_NAME, new PlayerPowerups(player));
	}

	public static final PlayerPowerups get(EntityPlayer player) {
		return (PlayerPowerups) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();

		properties.setInteger(NBT_TIMER, this.player.getDataWatcher().getWatchableObjectInt(TIMER_WATCHER));
		properties.setString(NBT_UNLOCKS, this.player.getDataWatcher().getWatchableObjectString(UNLOCKS_WATCHER));
		properties.setInteger(NBT_MANA, this.player.getDataWatcher().getWatchableObjectInt(MANA_WATCHER));
	
		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		try{
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		if (properties == null) {
			properties = new NBTTagCompound();
		}

		this.player.getDataWatcher().updateObject(TIMER_WATCHER, properties.getInteger(NBT_TIMER));
		this.player.getDataWatcher().updateObject(MANA_WATCHER, properties.getInteger(NBT_MANA));

		this.player.getDataWatcher().updateObject(UNLOCKS_WATCHER, properties.getString(NBT_UNLOCKS));
		}
		catch(Exception e){
			System.out.println("load nbt");
			System.out.println(e.getMessage());
		}
	}
 
	public void toggleOneSpell(int spell_id) {

		byte[] spells = this.getUnlocksFromString();
		// if it was zero, it beomes 1. if it was 1, it becomes zero
		spells[spell_id] = (byte) (1 - spells[spell_id]);
		
		this.setUnlocksFromByte(spells);
	}

	private int infbreaker = 0;
	public int nextId(int spell_id) {
		byte[] spells = this.getUnlocksFromString();

		//   check spells[abc] to see if its enabled for player, and
		// otherwise skip
		
		int next;
		
		if (spell_id >= SpellRegistry.getSpellbook().size() - 1)
			next = 0;// (int)spells[0];
		else
			next = spell_id + 1;// (int)spells[spell_id+1];
		
		//dont infloop
		if(this.isSpellUnlocked(next) == false && infbreaker < 100){
	
			infbreaker++;
			return nextId(next);
		}
		
		infbreaker = 0;
		this.setUnlocksFromByte(spells);
		return next;
	}

	public int prevId(int spell_id) {
		byte[] spells = this.getUnlocksFromString();
		
		int prev;

		if (spell_id == 0)
			prev = SpellRegistry.getSpellbook().size() - 1; 
		else
			prev = spell_id - 1; 
		//dont infloop
		if(this.isSpellUnlocked(prev) == false && infbreaker < 100){
	
			infbreaker++;
			return prevId(prev);
		}
		
		infbreaker = 0;
		

		this.setUnlocksFromByte(spells);
		return prev;
	}

	public boolean isSpellUnlocked(int spell_id) {
		byte[] spells = this.getUnlocksFromString();

		return (spells[spell_id] == 1);
	}
	
	public final void setSpellTimer(int current) {
		this.player.getDataWatcher().updateObject(TIMER_WATCHER, current);
	}

	public final int getSpellTimer() {
		return this.player.getDataWatcher().getWatchableObjectInt(TIMER_WATCHER);
	}

	public String getSpellUnlocks( ) {
		return this.player.getDataWatcher().getWatchableObjectString(UNLOCKS_WATCHER);
	}
	public void setSpellUnlocks(String s) {
		this.player.getDataWatcher().updateObject(UNLOCKS_WATCHER, s);
	}

	public final int getMana() {
		return this.player.getDataWatcher().getWatchableObjectInt(MANA_WATCHER);
	}
	
	public final void setMana(int m) {
		if(m < 0){m = 0;}
		int filled = (int) Math.min(m, SpellRegistry.caster.MAXMANA);
		
		this.player.getDataWatcher().updateObject(MANA_WATCHER, filled);
	}
	public final void drainManaBy(int m) {
		this.setMana(this.getMana() - m);
	}
	public void rechargeManaBy(int m) {
		this.setMana(this.getMana() + m);
	}
	// http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571567-forge-1-6-4-1-8-eventhandler-and

	public void copy(PlayerPowerups props) {
		// thanks for the help
		// https://github.com/coolAlias/Tutorial-Demo/blob/master/src/main/java/tutorial/entity/ExtendedPlayer.java

		// set in the player
		player.getDataWatcher().updateObject(TIMER_WATCHER, props.getSpellTimer());
		player.getDataWatcher().updateObject(UNLOCKS_WATCHER, props.getSpellUnlocks());
		player.getDataWatcher().updateObject(MANA_WATCHER, props.getMana());
		// set here
		this.setSpellTimer(props.getSpellTimer());
		this.setSpellUnlocks(props.getSpellUnlocks());
		this.setMana(props.getMana());
	}
}