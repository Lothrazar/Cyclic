package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemInventoryStorage extends Item {

	public ItemInventoryStorage() {

		this.setMaxStackSize(1);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {

		return 1; // Without this method, your inventory will NOT work!!!
	}
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {

		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

		System.out.println("onItemRightClick");
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		/*
		// so this only happens IF either onItemUse did not fire at all, or it
		// fired and casting failed
		boolean success = UtilSpellCaster.tryCastCurrent(worldIn, playerIn, null, null);

		if (success) {
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
		else {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
		}
*/
		// return super.onItemRightClick(itemStackIn, worldIn, playerIn,hand);
	}
}
