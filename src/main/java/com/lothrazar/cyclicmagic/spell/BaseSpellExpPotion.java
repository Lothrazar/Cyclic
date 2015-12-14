package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilSound;

public abstract class BaseSpellExpPotion extends  BaseSpellExp implements ISpell{

	private int potionId;
	private int potionDuration;
	private int potionAmp;

	public void setPotion(int id, int effectDuration, int effectAmplifier){
		potionId = id;
		potionDuration = effectDuration;
		potionAmp = effectAmplifier;
	}

	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos)
	{
		ModMain.addOrMergePotionEffect(player,new PotionEffect(potionId,potionDuration,potionAmp));
	}
	
	

	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		UtilSound.playSoundAt(player, "random.drink");

		super.onCastSuccess(world, player, pos);
	}
	/*
	 //needed for IF not abstract
	public BaseSpellExpPotion(int id, int effectDuration, int effectAmplifier){
		this.setPotion(id, effectDuration, effectAmplifier);
	}
	
	
	
	
	@Override
	public String getSpellName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getSpellID() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public ResourceLocation getIconDisplay() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getExpCost() {
		// TODO Auto-generated method stub
		return 0;
	}*/
}
