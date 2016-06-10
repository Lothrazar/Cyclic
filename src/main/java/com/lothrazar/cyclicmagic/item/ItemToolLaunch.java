package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolLaunch extends ItemArmor implements IHasRecipe {

	private static final int durability 	= 1000;
	private static final float	power		= 1.5F;
	private static final int cooldown 		= 21;
	int rotationPitch = 57;
 
	public ItemToolLaunch() {
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
		}
	}
	
    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
    	if(isSelected){
    		entityIn.fallDistance = 0;
    		Entity ridingEntity = entityIn.getRidingEntity();

    		if (ridingEntity != null) {
    			ridingEntity.fallDistance = 0;
    		}
    	}
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player, EnumHand hand) {

	//	rotationYaw -89.250336
		// rotationPitch  :  57.14997
		
		//fixed vertical launch angle//player.rotationPitch ;
		 

		UtilEntity.launch(player, rotationPitch, power);
		
		UtilParticle.spawnParticle(worldIn, EnumParticleTypes.CRIT_MAGIC, player.getPosition());
		UtilSound.playSound(player, player.getPosition(), SoundRegistry.bwoaaap);

		player.getCooldownTracker().setCooldown(this, cooldown);
		
//		super.onUse(itemStackIn, player, worldIn, hand);
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void addRecipe() {
		BrewingRecipeRegistry.addRecipe(
				 new ItemStack(Items.ELYTRA),
				 new ItemStack(Items.NETHER_STAR),
				 new ItemStack(this));
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack){
	    return true;
	}
}
