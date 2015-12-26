package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.Const;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemRuneBase  extends Item {
	public ItemRuneBase() {
		super();
		this.setMaxStackSize(1);
	}

	private final static String NBT_MODE = "mode";
	protected final static int MODE_ON = 1;
	protected final static int MODE_OFF = 2;

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (this.getMode(stack) == MODE_ON && worldIn.getWorldTime() % Const.TICKS_PER_SEC == 0) {
			
			// tick once per second, if its turned on

			if(trigger(worldIn, entityIn )){
				//TODO: drain mana/ particle sound/ ??
			}
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
 
		String base = "rune.mode.";
		if(this.getMode(stack) == MODE_ON){
			tooltip.add( EnumChatFormatting.GREEN + StatCollector.translateToLocal(base + "on"));
		}
		else{
			tooltip.add( EnumChatFormatting.RED + StatCollector.translateToLocal(base + "off"));
		}
		 
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	//each one must implement this differently
	protected abstract boolean trigger(World world,Entity entityIn );

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn) {

		if (worldIn.isRemote == false) {
			this.toggleMode(stack);
			// playerIn.addChatComponentMessage(new ChatComponentText(this.getModeName(this.getMode(stack))));
		}

		return stack;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{ 
		return EnumRarity.UNCOMMON;  
	}
	 
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return (this.getMode(stack) == MODE_ON);
	}

	protected int getMode(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if (!stack.getTagCompound().hasKey(NBT_MODE)) {
			return MODE_OFF;// not set, dont return zero
		}
		else {
			return stack.getTagCompound().getInteger(NBT_MODE);
		}
	}

	protected void setMode(ItemStack stack, int mode) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		stack.getTagCompound().setInteger(NBT_MODE, mode);
	}
	protected void toggleMode(ItemStack stack) {
		int next = this.getMode(stack) == MODE_ON ? MODE_OFF : MODE_ON;

		this.setMode(stack, next);
	}
}
