package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilItem;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorLaunch extends ItemArmor implements IHasRecipe {

	private static final int durability 	= 1000;
	private static final float	power		= 1.05F;
	private static final int rotationPitch = 75;
	private static final int cooldown = 35;
	private static final String NBT_USES = "uses";
	private final int maxuses;
 
	public ItemArmorLaunch(int max) {
		super(ItemRegistry.ARMOR_MATERIAL_EMERALD, 0, EntityEquipmentSlot.FEET);
		this.setMaxDamage(durability);
		maxuses = max;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		//if you are on the ground (or not airborne, should be same thing
		if((entityIn.isAirBorne == false || entityIn.onGround )&& 
				UtilNBT.getItemStackNBTVal(stack,NBT_USES) > 0){
			//you have landed on the ground, dont count previous jumps
			UtilNBT.setItemStackNBTVal(stack,NBT_USES,0);
		}
    }

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
		//if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return repair != null && repair.getItem() == Items.EMERALD;
    }

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		EntityPlayer p = Minecraft.getMinecraft().thePlayer;
		
		ItemStack feet = p.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		if(feet == null || feet.getItem() != this){
			return;
		}
		 
		//JUMP IS pressed and you are moving down
		if (FMLClientHandler.instance().getClient().gameSettings.keyBindJump.isPressed()
				&&  p.posY < p.lastTickPosY && p.isAirBorne) {

			if(p.getCooldownTracker().hasCooldown(this)){
				return;
			}
			
			int uses = UtilNBT.getItemStackNBTVal(feet,NBT_USES);
			
			//TODO: 1: current armor slot boots === this
			//TODO 2: well, this was POC... add second item that does this. so we
			//have corrupted elytra and then that is used to craft
			//the wearable version
			
			
			UtilEntity.launch(p,rotationPitch,power);
			p.fallDistance = 0;
			UtilParticle.spawnParticle(p.worldObj, EnumParticleTypes.CRIT_MAGIC, p.getPosition());
			UtilSound.playSound(p, SoundRegistry.bwoaaap);
			
			UtilItem.damageItem(p, feet);
			uses++;
			
			if(uses >= this.maxuses ){
				//now block useage for a while
				p.getCooldownTracker().setCooldown(this, cooldown );
				uses = 0;
			}

			UtilNBT.setItemStackNBTVal(feet,NBT_USES,uses);
		}
	}
	
	@Override
	public void addRecipe() {
		switch(this.maxuses){
		case 1:
			GameRegistry.addRecipe(new ItemStack(this), 
					"   ", 
					"ebe", 
					"gsg", 
					'b', ItemRegistry.emerald_boots, 
					'e', Items.DIAMOND, 
					'g', Items.GHAST_TEAR, 
					's', Blocks.SLIME_BLOCK);
			break;
		case 2:
			GameRegistry.addRecipe(new ItemStack(this), 
					"   ", 
					"ebe", 
					"gsg", 
					'b', ItemRegistry.boots_extrajump, 
					'e', Items.DIAMOND, 
					'g', Items.GHAST_TEAR, 
					's', Blocks.DIAMOND_BLOCK);
			break;
		case 3:
			GameRegistry.addRecipe(new ItemStack(this), 
					"   ", 
					"ebe", 
					"gsg", 
					'b', ItemRegistry.boots_doublejump, 
					'e', Items.EMERALD, 
					'g', Items.GHAST_TEAR, 
					's', Blocks.DIAMOND_BLOCK);
			break;
		}
	}
}
