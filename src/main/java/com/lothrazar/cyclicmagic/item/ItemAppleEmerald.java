package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAppleEmerald  extends ItemFood implements IHasRecipe {

	public ItemAppleEmerald() {
		super(2, false);
		this.setAlwaysEdible();
	}

	@Override
	public void addRecipe() {
		GameRegistry.addRecipe(new ItemStack(this), 
				"lll", 
				"lal", 
				"lll", 
				'l', Items.EMERALD, 
				'a', Items.GOLDEN_APPLE
				);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	public boolean itemInteractionForEntity (ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
	        
		if(entity instanceof EntityZombie){
			
			EntityZombie zombie = ((EntityZombie)entity);

			//this is what we WANT to do, but the method is protected. we have to fake it by faking the interact event
           // ((EntityZombie)entity).startConversion(1200);
			
			if(zombie.isVillager()){

				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(MobEffects.WEAKNESS,10,0));
				zombie.processInteract(player, hand, new ItemStack(Items.GOLDEN_APPLE));
				
				if(player.worldObj.isRemote == false){
					itemstack.stackSize--;
					if(itemstack.stackSize==0){itemstack=null;}
				}
				//UtilInventory.decrStackSize(player, currentItem);
			}
		}
		
		return super.itemInteractionForEntity(itemstack, player, entity, hand);
	}

	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
		PotionRegistry.addOrMergePotionEffect(player, new PotionEffect(
				MobEffects.SATURATION,
				900, 
				PotionRegistry.I));
	}
}
