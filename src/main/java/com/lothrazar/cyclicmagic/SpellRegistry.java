package com.lothrazar.cyclicmagic;

import java.util.ArrayList; 
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import com.lothrazar.cyclicmagic.spell.*;  
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer; 
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpellRegistry
{ 
	public static void setup()
	{
		spellbook = new ArrayList<ISpell>();
		deposit = new SpellChestDeposit(); 
		//chesttransp = new SpellChestTransport(); 
		ghost = new SpellGhost();
		jump = new SpellJump();
		phase = new SpellPhasing();
		slowfall = new SpellSlowfall();
		waterwalk = new SpellWaterwalk();
		haste = new SpellHaste();

		if(deposit.getExpCost() >= 0) spellbook.add(deposit); 
		//if(chesttransp.getExpCost() >= 0)spellbook.add(chesttransp); 
		if(haste.getExpCost() >= 0)spellbook.add(haste);
		if(waterwalk.getExpCost() >= 0)spellbook.add(waterwalk );
		if(slowfall.getExpCost() >= 0)spellbook.add(slowfall );
		if(jump.getExpCost() >= 0)spellbook.add(jump );
		if(phase.getExpCost() >= 0)spellbook.add(phase );
		if(ghost.getExpCost() >= 0)spellbook.add(ghost);
	}

	public static ArrayList<ISpell> spellbook;
	public static ISpell deposit;
	//public static ISpell chesttransp;
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
	
	
	

	@SideOnly(Side.CLIENT)
	static void drawSpell(RenderGameOverlayEvent.Text event)
	{ 
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer; 

		ISpell spell = SpellRegistry.getPlayerCurrentISpell(player);

		if(Minecraft.getMinecraft().gameSettings.showDebugInfo)
		{
			event.left.add(StatCollector.translateToLocal("key.spell."+spell.getSpellName()));
		}
		else
		{
			int ymain = 12;
			int dim = 12;
				
			int x = 12, y = 2;
			
			//Item ptr = SpellRegistry.canPlayerCastAnything(player) ? ItemRegistry.exp_cost_dummy : ItemRegistry.exp_cost_empty_dummy;
			//spell.getIconDisplayHeader()
			//UtilTextureRender.renderItemAt(new ItemStack(ptr),x,y,dim);
			
			//TODO: use the empty spell one also
			UtilTextureRender.drawTextureSquare(spell.getIconDisplayHeader(),x,y,dim);
			//int ysmall = ymain - 3;
			int xmain = 10;
			ymain = 14;
			if(spell.getIconDisplay() != null)
			{
				x = xmain; 
				y = ymain;
				dim = 16;
				UtilTextureRender.drawTextureSquare(spell.getIconDisplay(),x,y,dim);
			}
			
			
			ISpell spellNext = spell.left();//SpellRegistry.getSpellFromType(spell.getSpellID().next());
			ISpell spellPrev = spell.right();//SpellRegistry.getSpellFromType(spell.getSpellID().prev());
			
			
			if(spellNext != null)// && spellNext.getIconDisplay() != null
			{
				x = xmain-3; 
				y = ymain + 16;
				dim = 16/2;
				UtilTextureRender.drawTextureSquare(spellNext.getIconDisplay(),x,y,dim);
				
				ISpell sLeftLeft = spellNext.left();//SpellRegistry.getSpellFromType(spellNext.getSpellID().next());

				if(sLeftLeft != null && sLeftLeft.getIconDisplay() != null)
				{
					x = xmain-3 - 1; 
					y = ymain + 16+14;
					dim = 16/2 - 2;
					UtilTextureRender.drawTextureSquare(sLeftLeft.getIconDisplay(),x,y,dim);
					
					ISpell another = sLeftLeft.left();
					if(another != null)
					{
						x = xmain-3 - 3; 
						y = ymain + 16+14+10;
						dim = 16/2 - 4;
						UtilTextureRender.drawTextureSquare(another.getIconDisplay(),x,y,dim);
					}
				}
			}
			if(spellPrev != null)// && spellPrev.getIconDisplay() != null
			{
				x = xmain+6; 
				y = ymain + 16;
				dim = 16/2;
				UtilTextureRender.drawTextureSquare(spellPrev.getIconDisplay(),x,y,dim);

				ISpell sRightRight = spellPrev.right();//SpellRegistry.getSpellFromType(spellPrev.getSpellID().prev());

				if(sRightRight != null && sRightRight.getIconDisplay() != null)
				{
					x = xmain+6 + 4; 
					y = ymain + 16+14;
					dim = 16/2 - 2;
					UtilTextureRender.drawTextureSquare(sRightRight.getIconDisplay(),x,y,dim);
					
					ISpell another = sRightRight.right();
					if(another != null)
					{
						x = xmain+6 +7; 
						y = ymain + 16+14+10;
						dim = 16/2 - 4;
						UtilTextureRender.drawTextureSquare(another.getIconDisplay(),x,y,dim);
					}
				}
			}
			
		
			
		}
	}
	
}


