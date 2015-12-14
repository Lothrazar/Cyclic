package com.lothrazar.cyclicmagic;

import java.util.ArrayList; 
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.spell.*;  
import net.minecraft.entity.player.EntityPlayer; 
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class SpellRegistry
{ 
	public static void setup()
	{
		spellbook = new ArrayList<ISpell>();
		deposit = new SpellChestDeposit(); 
		chesttransp = new SpellChestTransport(); 
		ghost = new SpellGhost();
		jump = new SpellJump();
		phase = new SpellPhasing();
		slowfall = new SpellSlowfall();
		waterwalk = new SpellWaterwalk();
		haste = new SpellHaste();

		if(deposit.getExpCost() >= 0) spellbook.add(deposit); 
		if(chesttransp.getExpCost() >= 0)spellbook.add(chesttransp); 
		if(haste.getExpCost() >= 0)spellbook.add(haste);
		if(waterwalk.getExpCost() >= 0)spellbook.add(waterwalk );
		if(slowfall.getExpCost() >= 0)spellbook.add(slowfall );
		if(jump.getExpCost() >= 0)spellbook.add(jump );
		if(phase.getExpCost() >= 0)spellbook.add(phase );
		if(ghost.getExpCost() >= 0)spellbook.add(ghost);
	}

	public static ArrayList<ISpell> spellbook;
	public static ISpell deposit;
	public static ISpell chesttransp;
	public static ISpell ghost;
	public static ISpell jump;
	public static ISpell phase;
	public static ISpell slowfall;
	public static ISpell waterwalk;
	public static ISpell haste;
	 
	public static ISpell getDefaultSpell()
	{
		return spellbook.get(0);
	}
	public static final int SPELL_TOGGLE_HIDE = 0;
	public static final int SPELL_TOGGLE_SHOW = 1;
	public static final int SPELL_TIMER_MAX = 20;

	public static boolean canPlayerCastAnything(EntityPlayer player)
	{
		PlayerPowerups props = PlayerPowerups.get(player);
		return props.getSpellTimer() == 0;
	}
	
	public static void cast(ISpell spell, World world, EntityPlayer player,BlockPos pos)
	{
		System.out.println("SpellRegistry.cast");
		if(spell == null)
		{
			System.out.println("ERROR: cast null spell");
			return;
		}
		if(canPlayerCastAnything(player) == false)
		{
			System.out.println("ERROR: canPlayerCastAnything == false");
			return;
		}
	
		if(spell.canPlayerCast(world, player, pos))
		{
			System.out.println("cast " + spell.getSpellName());
			spell.cast(world, player, pos);
			spell.onCastSuccess(world, player, pos);
			startSpellTimer(player);
		}
		else
		{
			System.out.println("onCastFailure " + spell.getSpellName());
			spell.onCastFailure(world, player, pos);
		}
	}
	public static void cast(int spell_id, World world, EntityPlayer player,BlockPos pos)
	{
		//ISpell sp = SpellRegistry.getSpellFromType(spell_id); 
		ISpell sp = SpellRegistry.getSpellFromID(spell_id); 
		cast(sp,world,player,pos);
	}
	
	public static void shiftLeft(EntityPlayer player)
	{
		ISpell current = getPlayerCurrentISpell(player);

		if(current.left() != null)
		{
			setPlayerCurrentSpell(player,current.left().getSpellID());
			UtilSound.playSoundAt(player, "random.orb");
		}
	}

	public static void shiftRight(EntityPlayer player)
	{ 
		ISpell current = getPlayerCurrentISpell(player);

		if(current.right() != null)
		{
			setPlayerCurrentSpell(player,current.right().getSpellID()); 
			UtilSound.playSoundAt(player, "random.orb");
		}
	}
	
	private static void setPlayerCurrentSpell(EntityPlayer player,	int current_id)
	{
		PlayerPowerups props = PlayerPowerups.get(player);

		props.setSpellCurrent(current_id);
	}
	public static int getPlayerCurrentSpell(EntityPlayer player)
	{
		PlayerPowerups props = PlayerPowerups.get(player);
		
		return props.getSpellCurrent();
	}
	public static int getSpellTimer(EntityPlayer player)
	{
		PlayerPowerups props = PlayerPowerups.get(player);
		return props.getSpellTimer();
	}
	public static void startSpellTimer(EntityPlayer player)
	{
		PlayerPowerups props = PlayerPowerups.get(player);
		props.setSpellTimer(SPELL_TIMER_MAX);
	}
	public static void tickSpellTimer(EntityPlayer player)
	{
		PlayerPowerups props = PlayerPowerups.get(player);
		if(props.getSpellTimer() < 0)
			props.setSpellTimer(0);
		else if(props.getSpellTimer() > 0)
			props.setSpellTimer(props.getSpellTimer() - 1);
	}

	public static ISpell getPlayerCurrentISpell(EntityPlayer player)
	{
		int spell_id = getPlayerCurrentSpell(player);
 
		for(ISpell sp : spellbook)
		{ 
			//if(sp.getSpellName().equalsIgnoreCase(s))
			if(sp.getSpellID() == spell_id)
			{
				return sp;
			}
		} 
		//if current spell is null,default to the first one
 
		return SpellRegistry.getDefaultSpell();
	}
	public static ISpell getSpellFromID(int id)
	{
		if(id == 0){return null;}
		for(ISpell sp : spellbook)
		{ 
			if(sp.getSpellID() == id)
			{
				return sp;
			}
		} 
		
		return null;
	}/*
	public static ISpell getSpellFromType(String next)
	{
		if(next == null){return null;}
		for(ISpell sp : spellbook)
		{ 
			if(sp.getSpellName() == next)
			{
				return sp;
			}
		} 
		
		return null;
	}*/
}
