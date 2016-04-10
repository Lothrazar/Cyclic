package com.lothrazar.cyclicmagic.item;

import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEnderPearlReuse extends ItemEnderPearl implements IHasRecipe {
	public static final String name = "ender_pearl_reuse";

	public ItemEnderPearlReuse() {
		this.maxStackSize = 1;
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

		worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.entity_enderpearl_throw, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		playerIn.getCooldownTracker().setCooldown(this, 20);

		if (!worldIn.isRemote) {
			EntityEnderPearl entityenderpearl = new EntityEnderPearl(worldIn, playerIn);
			entityenderpearl.func_184538_a(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.spawnEntityInWorld(entityenderpearl);
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapedRecipe(new ItemStack(this), "eee", "ese", "eee", 'e', new ItemStack(Items.ender_eye), 's', new ItemStack(Items.nether_star));
	}
}
