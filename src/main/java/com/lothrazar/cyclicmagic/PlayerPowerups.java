package com.lothrazar.cyclicmagic;

import com.lothrazar.cyclicmagic.SpellRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerPowerups implements IExtendedEntityProperties
{
	private final static String EXT_PROP_NAME = "PlayerPowerups"+Const.MODID;
	private final EntityPlayer player;//we get one of these powerup classes for each player
	// TODO: DO NOT RESET TIMER IF CASTING FAILS
	private static final int SPELLMAIN_WATCHER = 22;
	private static final String NBT_SPELLMAIN = "samSpell"; 
	
	private static final int SPELLTOG_WATCHER = 24;
	private static final String NBT_SPELLTOG = "samSpellToggle"; 

	private static final int SPELLTIMER_WATCHER = 25;
	private static final String NBT_SPELLTIMER = "samSpellTimer"; 
 
	public PlayerPowerups(EntityPlayer player)
	{
		this.player = player;   
		this.player.getDataWatcher().addObject(SPELLMAIN_WATCHER, 0);   
		this.player.getDataWatcher().addObject(SPELLTOG_WATCHER, 0); 
		this.player.getDataWatcher().addObject(SPELLTIMER_WATCHER, 0); 
	}
	@Override
	public void init(Entity entity, World world) 
	{ 
	}	
	
	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(PlayerPowerups.EXT_PROP_NAME, new PlayerPowerups(player));
	}

	public static final PlayerPowerups get(EntityPlayer player)
	{
		return (PlayerPowerups) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) 
	{
		// We need to create a new tag compound that will save everything for our Extended Properties
		NBTTagCompound properties = new NBTTagCompound(); 
	 
		properties.setInteger(NBT_SPELLMAIN,   this.player.getDataWatcher().getWatchableObjectInt(SPELLMAIN_WATCHER)); 
		properties.setInteger(NBT_SPELLTOG,    this.player.getDataWatcher().getWatchableObjectInt(SPELLTOG_WATCHER) ); 
		properties.setInteger(NBT_SPELLTIMER,  this.player.getDataWatcher().getWatchableObjectInt(SPELLTIMER_WATCHER) ); 
		properties.setTag("items", new NBTTagCompound());
		compound.setTag(EXT_PROP_NAME, properties); 
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) 
	{ 
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		if(properties == null){ properties = new NBTTagCompound(); }
		
		this.player.getDataWatcher().updateObject(SPELLMAIN_WATCHER, properties.getInteger(NBT_SPELLMAIN));  
		this.player.getDataWatcher().updateObject(SPELLTOG_WATCHER,  properties.getInteger(NBT_SPELLTOG));   
		this.player.getDataWatcher().updateObject(SPELLTIMER_WATCHER,properties.getInteger(NBT_SPELLTIMER));   
 	}



	public final int getSpellCurrent()
	{
		int spell_id = 0;
		try{
		spell_id = this.player.getDataWatcher().getWatchableObjectInt(SPELLMAIN_WATCHER);
		}
		catch(java.lang.ClassCastException e)
		{
			System.out.println(e.getMessage());//do not quit, leave it as zero
		}
		if(spell_id == 0)//== null || spell.isEmpty())
		{
			spell_id = SpellRegistry.getDefaultSpell().getSpellID();
			setSpellCurrent(spell_id);
		}
		
		return spell_id;
	}
 
	public final void setSpellToggle(int current) 
	{
		this.player.getDataWatcher().updateObject(SPELLTOG_WATCHER, current);
	}
	public final int getSpellToggle() 
	{
		return this.player.getDataWatcher().getWatchableObjectInt(SPELLTOG_WATCHER);
	}
	public final int getSpellToggleNext() 
	{
		int current = getSpellToggle() ;

		switch(current)
		{
		case SpellRegistry.SPELL_TOGGLE_SHOW:
			return SpellRegistry.SPELL_TOGGLE_HIDE;
		case SpellRegistry.SPELL_TOGGLE_HIDE:
		default:
			return SpellRegistry.SPELL_TOGGLE_SHOW;
		}
	} 
	public final void setSpellTimer(int current) 
	{ 
		this.player.getDataWatcher().updateObject(SPELLTIMER_WATCHER, current);
	}
	public final int getSpellTimer() 
	{
		return this.player.getDataWatcher().getWatchableObjectInt(SPELLTIMER_WATCHER);
	}
	//http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571567-forge-1-6-4-1-8-eventhandler-and

	public void copy(PlayerPowerups props) 
	{
		//thanks for the help https://github.com/coolAlias/Tutorial-Demo/blob/master/src/main/java/tutorial/entity/ExtendedPlayer.java

		//set in the player 
		player.getDataWatcher().updateObject(SPELLMAIN_WATCHER, props.getSpellCurrent()); 
		player.getDataWatcher().updateObject(SPELLTOG_WATCHER, props.getSpellToggle()); 
		player.getDataWatcher().updateObject(SPELLTIMER_WATCHER, props.getSpellTimer()); 
		//set here  
		this.setSpellCurrent(props.getSpellCurrent());  
		this.setSpellToggle(props.getSpellToggle());   
		this.setSpellTimer(props.getSpellTimer()); 
	}
	public final void setSpellCurrent(int spell_id) 
	{
		this.player.getDataWatcher().updateObject(SPELLMAIN_WATCHER, spell_id); 
	}
}