package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolLaunch extends BaseTool implements IHasRecipe {

	private static final int durability 	= 1000;
	private static final float	power		= 1.5F;
	private static final int cooldown 		= 21;
	int rotationPitch = 57;
 
	public ItemToolLaunch() {
		super(durability);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
		//if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return repair != null && repair.getItem() == Items.DIAMOND;
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
 
		UtilEntity.launch(player, rotationPitch, power);
		
		UtilParticle.spawnParticle(worldIn, EnumParticleTypes.CRIT_MAGIC, player.getPosition());
		UtilSound.playSound(player,  SoundRegistry.bwoaaap);

		player.getCooldownTracker().setCooldown(this, cooldown);
		
 		super.onUse(itemStackIn, player, worldIn, hand);
		
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
