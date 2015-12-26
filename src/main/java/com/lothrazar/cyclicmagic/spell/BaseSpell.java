package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

/**
 * phasing out ISpell interface. now every spell must extend this base class for
 * now at least, until we make a spell book or some other system to replace this
 * linked list setup
 * 
 * @author Sam Bassett (Lothrazar)
 *
 */
public class BaseSpell implements ISpell {

	private ResourceLocation icon;
	private int ID;
	private String name;
	protected int experience;
	protected int cooldown;
	private final static ResourceLocation header = new ResourceLocation(Const.MODID, "textures/spells/exp_cost_dummy.png");
	private final static ResourceLocation header_empty = new ResourceLocation(Const.MODID, "textures/spells/exp_cost_empty_dummy.png");

	public BaseSpell(int id, String n) {
		ID = id;
		name = n;
		experience = 1;
		cooldown = 20;
		
		icon = new ResourceLocation(Const.MODID, "textures/spells/"+name+".png");
	}
	
	public String getName(){
		return StatCollector.translateToLocal("spell."+name+".name");
	}

	@Override
	public int getCastCooldown() {
		return cooldown;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
		// never cast a base spell, always override this
		return false;
	}

	@Override
	public int getCost() {
		return experience;
	}

	@Override
	public void onCastFailure(World world, EntityPlayer player, BlockPos pos) {

		UtilSound.playSoundAt(player, UtilSound.fizz);
	}

	@Override
	public ResourceLocation getIconDisplayHeaderEnabled() {
		return header;
	}

	@Override
	public ResourceLocation getIconDisplayHeaderDisabled() {
		return header_empty;
	}

	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos) {
		
		if(pos == null){
			pos = player.getPosition();
		}
		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT, pos);

		System.out.println("cast success, draining cost exp "+this.getCost());
		if (this.getCost() > 0) {
			UtilExperience.drainExp(player, this.getCost());
		}
		/*
		if (this.getCostDurability() > 0 && player.getHeldItem() != null) {
			player.getHeldItem().damageItem(this.getCostDurability(), player);
		}
		*/
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos) {
		if (player.capabilities.isCreativeMode) {
			return true;//skips everything
		}
		
		if( player.getHeldItem() == null ||  player.getHeldItem().getItem() != ItemRegistry.master_wand){
			return false;
		}
		
		if(getCost() > 0 && getCost() > UtilExperience.getExpTotal(player)){
			System.out.println("cannot cast - need EXP "+getCost());
			return false;//not enough exp
		}
			
		return true;
	}

	@Override
	public ResourceLocation getIconDisplay() {
		return icon;
	}
}
