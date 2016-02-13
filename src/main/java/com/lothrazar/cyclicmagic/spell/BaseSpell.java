package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.UtilSound;

public abstract class BaseSpell implements ISpell {

	private ResourceLocation icon;
	private int ID;
	private String name;
	protected int cost;
	protected int cooldown;
	protected ResourceLocation header = new ResourceLocation(Const.MODID, "textures/spells/header_on.png");
	protected ResourceLocation header_empty = new ResourceLocation(Const.MODID, "textures/spells/header_off.png");

	protected void init(int id, String n){
		ID = id;
		name = n;
		cost = 5;
		cooldown = 3;
		
		icon = new ResourceLocation(Const.MODID, "textures/spells/" + name + ".png");
	}
	
	public String getName(){
		return StatCollector.translateToLocal("spell." + name + ".name");
	}
	public String getInfo(){
		return StatCollector.translateToLocal("spell." + name + ".info");
	}

	@Override
	public int getCastCooldown() {
		return cooldown;
	}
	
	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public void onCastFailure(World world, EntityPlayer player, BlockPos pos) {

		UtilSound.playSoundAt(player, UtilSound.Own.buzzp);
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
	public void payCost(World world, EntityPlayer player, BlockPos pos) {
		
		if(player.capabilities.isCreativeMode == false){
			ItemCyclicWand.Energy.drainBy(player.getHeldItem(), this.getCost());
		}
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
		
		if( player.getHeldItem() == null ||  player.getHeldItem().getItem() != ItemRegistry.cyclic_wand){
			return false;
		}
		
		if( this.getCost() > ItemCyclicWand.Energy.getCurrent(player.getHeldItem())){
			return false;
		}
		return true;
	}

	@Override
	public ResourceLocation getIconDisplay() {
		return icon;
	}
}
