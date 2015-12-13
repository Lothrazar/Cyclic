package com.lothrazar.samsmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import com.lothrazar.samsmagic.ItemRegistry;
import com.lothrazar.samsmagic.ModMain; 

public class SpellPhasing extends BaseSpellExp implements ISpell
{ 
	@Override
	public String getSpellName()
	{
		return "phase";
	}
	@Override
	public int getSpellID() 
	{
		return 6;
	}
	
	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos)
	{
		if(super.canPlayerCast(world, player, pos) == false) {return false;}
	
		BlockPos offs = getPosOffset(player,pos);
		
		if(pos != null && offs != null && world.isAirBlock(offs) && world.isAirBlock(offs.up()))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos)
	{
		if(pos == null){return;}//covered also by canPlayerCast
		
 
		BlockPos offs = getPosOffset(player,pos);//was .getOpposite()
		
		//not 2, depends on block pos?
		if(world.isAirBlock(offs) && world.isAirBlock(offs.up()))
		{
			player.setPositionAndUpdate(offs.getX(), offs.getY(), offs.getZ()); 
		}
	}
   
	private BlockPos getPosOffset(EntityPlayer player,BlockPos pos) 
	{
		if(pos == null){return pos;}

//this spell crashes servers its sideonly=client.  TODO: what to do?
		EnumFacing face = EnumFacing.getFacingFromVector(
				(float)player.getLookVec().xCoord
				, (float)player.getLookVec().yCoord
				, (float)player.getLookVec().zCoord);
		int dist = 1;
		if(face.getOpposite() == EnumFacing.DOWN)
		{
			 dist = 2;//only move two when going down - player is 2 tall
		}
		//was .getOpposite()
		BlockPos offs = pos.offset(face, dist);
		return offs;
	}
 
	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		world.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F);  
		ModMain.spawnParticle(world, EnumParticleTypes.PORTAL, pos);

		super.onCastSuccess(world, player, pos);
	}

	@Override
	public int getExpCost()
	{
		return ModMain.cfg.phase;
	}
	
	@Override
	public ItemStack getIconDisplay()
	{
		return new ItemStack(ItemRegistry.spell_dummy_phasing);
	}
}
