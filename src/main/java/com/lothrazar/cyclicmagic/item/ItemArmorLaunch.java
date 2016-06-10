package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorLaunch extends ItemArmor implements IHasRecipe {

	private static final int durability 	= 1000;
	private static final float	power		= 1.05F;
	int rotationPitch = 75;
 
	public ItemArmorLaunch() {
		super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.FEET);
		this.setMaxDamage(durability);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
		//if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return repair != null && repair.getItem() == Items.DIAMOND;
    }

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		EntityPlayer p = Minecraft.getMinecraft().thePlayer;

		//TODO: 1: current armor slot boots === this
		//TODO 2: well, this was POC... add second item that does this. so we
		//have corrupted elytra and then that is used to craft
		//the wearable version
		boolean isMovingDown = p.posY < p.lastTickPosY && p.isAirBorne;

		if (FMLClientHandler.instance().getClient().gameSettings.keyBindJump.isPressed()
				&& isMovingDown) {
			UtilEntity.launch(p,rotationPitch,power);
			
			UtilParticle.spawnParticle(p.worldObj, EnumParticleTypes.CRIT_MAGIC, p.getPosition());
			UtilSound.playSound(p, SoundRegistry.bwoaaap);
		}
	}
	
	@Override
	public void addRecipe() {
		BrewingRecipeRegistry.addRecipe(
				 new ItemStack(ItemRegistry.corrupted_elytra),
				 new ItemStack(Items.DIAMOND),
				 new ItemStack(this));
	}
}
