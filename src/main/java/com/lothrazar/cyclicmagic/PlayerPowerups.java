package com.lothrazar.cyclicmagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerPowerups implements IExtendedEntityProperties {
	private final static String EXT_PROP_NAME = "PlayerPowerups" + Const.MODID;
	private final EntityPlayer player;// we get one of these powerup classes for
	private byte[] spells = null;

	private static final String NBT_UNLOCKS = "unlocks";

	private static final int SPELLMAIN_WATCHER = 22;
	private static final String NBT_SPELLMAIN = "samSpell";

	private static final int SPELLTIMER_WATCHER = 25;
	private static final String NBT_SPELLTIMER = "samSpellTimer";

	public PlayerPowerups(EntityPlayer player) {
		this.player = player;
		this.player.getDataWatcher().addObject(SPELLMAIN_WATCHER, 0);
		this.player.getDataWatcher().addObject(SPELLTIMER_WATCHER, 0);
		spells = new byte[SpellRegistry.getSpellbook().size()];
		for (int i = 0; i < spells.length; i++)
			spells[i] = 1;
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

		properties.setInteger(NBT_SPELLMAIN, this.player.getDataWatcher().getWatchableObjectInt(SPELLMAIN_WATCHER));
		properties.setInteger(NBT_SPELLTIMER, this.player.getDataWatcher().getWatchableObjectInt(SPELLTIMER_WATCHER));

		properties.setByteArray(NBT_UNLOCKS, spells);

		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		if (properties == null) {
			properties = new NBTTagCompound();
		}

		this.player.getDataWatcher().updateObject(SPELLMAIN_WATCHER, properties.getInteger(NBT_SPELLMAIN));
		this.player.getDataWatcher().updateObject(SPELLTIMER_WATCHER, properties.getInteger(NBT_SPELLTIMER));

		spells = properties.getByteArray(NBT_UNLOCKS);
	}
 
	public void toggleOneSpell(int spell_id) {

		// if it was zero, it beomes 1. if it was 1, it becomes zero
		spells[spell_id] = (byte) (1 - spells[spell_id]);
	}

	public int nextId(int spell_id) {

		// TODO: check spells[abc] to see if its enabled for player, and
		// otherwise skip
		if (spell_id >= spells.length)
			return 0;// (int)spells[0];
		else
			return spell_id + 1;// (int)spells[spell_id+1];
	}

	public int prevId(int spell_id) {

		if (spell_id == 0)
			return spells.length - 1;// (int)spells[0];
		else
			return spell_id - 1;// (int)spells[spell_id-1];
	}

	public boolean isSpellUnlocked(int spell_id) {

		return (spells[spell_id] == 1);
	}

	public final int getSpellCurrent() {
		int spell_id = 0;
		try {
			spell_id = this.player.getDataWatcher().getWatchableObjectInt(SPELLMAIN_WATCHER);
		}
		catch (java.lang.ClassCastException e) {
			System.out.println(e.getMessage());// do not quit, leave it as zero
		}/*
		if (spell_id == 0)// == null || spell.isEmpty())
		{
			spell_id = SpellRegistry.getDefaultSpell().getID();
			setSpellCurrent(spell_id);
		}*/

		return spell_id;
	}

	public final void setSpellTimer(int current) {
		this.player.getDataWatcher().updateObject(SPELLTIMER_WATCHER, current);
	}

	public final int getSpellTimer() {
		return this.player.getDataWatcher().getWatchableObjectInt(SPELLTIMER_WATCHER);
	}

	public final void setSpellCurrent(int spell_id) {
		this.player.getDataWatcher().updateObject(SPELLMAIN_WATCHER, spell_id);
	}

	// http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571567-forge-1-6-4-1-8-eventhandler-and

	public void copy(PlayerPowerups props) {
		// thanks for the help
		// https://github.com/coolAlias/Tutorial-Demo/blob/master/src/main/java/tutorial/entity/ExtendedPlayer.java

		// set in the player
		player.getDataWatcher().updateObject(SPELLMAIN_WATCHER, props.getSpellCurrent());
		player.getDataWatcher().updateObject(SPELLTIMER_WATCHER, props.getSpellTimer());
		// set here
		this.setSpellCurrent(props.getSpellCurrent());
		this.setSpellTimer(props.getSpellTimer());
	}
}