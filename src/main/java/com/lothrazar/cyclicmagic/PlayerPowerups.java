package com.lothrazar.cyclicmagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerPowerups implements IExtendedEntityProperties {
	private final static String EXT_PROP_NAME = "CyclicMagic" + Const.MODID;
	private final EntityPlayer player;// we get one of these powerup classes for

	private static final int TIMER_WATCHER = 30;
	private static final String NBT_TIMER = "samSpellTimer";

	public PlayerPowerups(EntityPlayer player) {
		this.player = player;
		this.player.getDataWatcher().addObject(TIMER_WATCHER, 0);
	}
	public EntityPlayer getPlayer(){
		return player;
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
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
 
	public final void setSpellTimer(int current) {
		this.player.getDataWatcher().updateObject(TIMER_WATCHER, current);
	}

	public final int getSpellTimer() {
		return this.player.getDataWatcher().getWatchableObjectInt(TIMER_WATCHER);
	}

	// http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571567-forge-1-6-4-1-8-eventhandler-and

	public void copy(PlayerPowerups props) {
		// thanks for the help
		// https://github.com/coolAlias/Tutorial-Demo/blob/master/src/main/java/tutorial/entity/ExtendedPlayer.java

		// set in the player
		player.getDataWatcher().updateObject(TIMER_WATCHER, props.getSpellTimer());
		// set here
		this.setSpellTimer(props.getSpellTimer());
	}
}