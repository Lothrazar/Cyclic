package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.Const; 
import com.lothrazar.cyclicmagic.PotionRegistry;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCyclicWand extends Item {

	private static final String NBT_MANA = "mana";
	private static final String NBT_SPELL = "spell_id";
	private static final String NBT_UNLOCKS = "unlock_";
	public ItemCyclicWand() {
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabAllSearch);//TODO: remove this for release
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
		if(!slotChanged){
			return false;//only item data has changed, so do not animate
		}
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn){
		//default to all unlocked
		Spells.setUnlockDefault(stack);
    }
	
	@Override
    public String getUnlocalizedName(ItemStack stack)
    {
		String name = super.getUnlocalizedName() + "_" + Variants.getVariantFromMeta(stack).name().toLowerCase();

        return name;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        for (int i = 0; i < Variants.values().length; i++){
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }
     
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {

		ISpell spell = SpellRegistry.getSpellFromID(Spells.getSpellCurrent(stack));

		int MAX = ItemCyclicWand.Energy.getMaximum(stack);
		//StatCollector.translateToLocal("spell.cost")
		tooltip.add(spell.getName() + " "+EnumChatFormatting.DARK_BLUE + spell.getCost());

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(Energy.getCurrent(stack) + "/" + MAX);	
			tooltip.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("wand.regen") + EnumChatFormatting.DARK_BLUE + Energy.getRegen(stack));
			
			tooltip.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("wand.gui.info"));
			tooltip.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("wand.recharge.info"));
			tooltip.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("wand.wheel.info"));
		}
		else {
			tooltip.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("item.shift"));
		}
		
		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.EPIC;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {

		// so this only happens IF either onItemUse did not fire at all, or it
		// fired and casting failed
		SpellRegistry.caster.tryCastCurrent(worldIn, playerIn, null, null);
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// if held by something not a player? such as custom npc/zombie/etc
		if (entityIn instanceof EntityPlayer == false) {
			return;
		}

		EntityPlayer p = (EntityPlayer) entityIn;
		if (worldIn.isRemote == false && worldIn.getWorldTime() % Const.TICKS_PER_SEC == 0){

			Energy.rechargeBy(stack, Energy.getRegen(stack));
			//Mana.setMana(stack, Mana.getMana(stack) + Mana.getRegen(stack));
		}
		
		switch(Variants.getVariantFromMeta(stack)){
			case QUARTZ:
				break;
			case GOLD:
				
				break;
			case DIAMOND:
				
				break;
			case EMERALD:
	//TODO: balance and crafting
				//Passives.triggerFalling( p);
				//Passives.triggerBreathing( p);
				//Passives.triggerBurning( p);
				//Passives.triggerProtect( p);
				break;
			default:break;
		}
		
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		// If onItemUse returns false onItemRightClick will be called.
		// http://www.minecraftforge.net/forum/index.php?topic=31966.0
		// so if this casts and succeeds, the right click is cancelled
		return SpellRegistry.caster.tryCastCurrent(worldIn, playerIn, pos, side);
	}
	
    private static NBTTagCompound getNBT(ItemStack stack){
    	return stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    }

	public static class Spells{
	
		private static void unlockSpell(ItemStack stack, ISpell spell){
			NBTTagCompound nbt = getNBT(stack);
			nbt.setBoolean(NBT_UNLOCKS + spell.getID(), true);
			stack.setTagCompound(nbt);
		}/*
		private static void lockSpell(ItemStack stack, ISpell spell){
			NBTTagCompound nbt = getStackNBT(stack);
			nbt.setBoolean(NBT_UNLOCKS + spell.getID(), true);
			stack.setTagCompound(nbt);
		}*/
		public static void toggleSpell(ItemStack stack, int spell_id){
			NBTTagCompound nbt = getNBT(stack);
			String key = NBT_UNLOCKS + spell_id;
			nbt.setBoolean(key, !nbt.getBoolean(key));
			stack.setTagCompound(nbt);
		}
		public static boolean isSpellUnlocked(ItemStack stack, ISpell spell){
			return isSpellUnlocked(stack,spell.getID());
		}
		public static boolean isSpellUnlocked(ItemStack stack, int spell_id){
			NBTTagCompound nbt = getNBT(stack);
			return nbt.getBoolean(NBT_UNLOCKS + spell_id);
		}
		private static void setUnlockDefault(ItemStack stack){
	
			NBTTagCompound nbt = getNBT(stack);
			
			for(ISpell s : SpellRegistry.getSpellbook()){
				unlockSpell(stack,s);
			}
			stack.setTagCompound(nbt);
		}

		public static int nextId(ItemStack stack,int spell_id ) {
			return nextId(stack,spell_id,0);
		}
		public static int nextId(ItemStack stack,int spell_id,int infbreaker ) {
		//	byte[] spells = this.getUnlocksFromString();
	
			
			int next;
			
			if (spell_id >= SpellRegistry.getSpellbook().size() - 1)
				next = 0;// (int)spells[0];
			else
				next = spell_id + 1;// (int)spells[spell_id+1];
			
			//dont infloop
			if(isSpellUnlocked(stack,next) == false && infbreaker < 100){
		
				infbreaker++;
				return nextId(stack,next,infbreaker);
			}
			
			infbreaker = 0;
			//this.setUnlocksFromByte(spells);
			return next;
		}

		public static int prevId(ItemStack stack,int spell_id ) {
			return prevId(stack,spell_id,0);
		}
		public static int prevId(ItemStack stack,int spell_id,int infbreaker ) {
		
			int prev;
	
			if (spell_id == 0)
				prev = SpellRegistry.getSpellbook().size() - 1; 
			else
				prev = spell_id - 1; 
			//dont infloop
			if(isSpellUnlocked(stack,prev) == false && infbreaker < 100){
		
				infbreaker++;
				return prevId(stack,prev,infbreaker);
			}
			
			infbreaker = 0;
			
			return prev;
		}
		
		public static int getSpellCurrent(ItemStack stack) {
			
			return getNBT(stack).getInteger(NBT_SPELL);
		}
		
		public static void setSpellCurrent(ItemStack stack,int spell_id) {
			NBTTagCompound tags = getNBT(stack);
			
			tags.setInteger(NBT_SPELL, spell_id);
			
			stack.setTagCompound(tags);
		}
	}


	public static class Energy{
		private static int getRegen(ItemStack stack){
			switch(Variants.getVariantFromMeta(stack)){
			case QUARTZ:
				return 1;
			case GOLD:
				return 2;
			case DIAMOND:
				return 3;
			case EMERALD:
				return 5;
			}
			return 0;
		}
		
		public static int getMaximum(ItemStack stack){
			switch(Variants.getVariantFromMeta(stack)){
			case QUARTZ:
				return 500;
			case GOLD:
				return 600;
			case DIAMOND:
				return 800;
			case EMERALD:
				return 1000;
			}
			return 0;
		}

		public static int getCurrent(ItemStack stack) {
			return getNBT(stack).getInteger(NBT_MANA);
		}
		
		public static void setCurrent(ItemStack stack,int m) {
			if(m < 0){m = 0;}
			int MAX = ItemCyclicWand.Energy.getMaximum(stack);
			int filled = (int) Math.min(m, MAX);

			getNBT(stack).setInteger(NBT_MANA, filled);
		}
		public static void drainBy(ItemStack stack,int m) {
			Energy.setCurrent(stack, Energy.getCurrent(stack) - m);
		}
		public static void rechargeBy(ItemStack stack,int m) {
			Energy.setCurrent(stack, Energy.getCurrent(stack) + m);
		}
	}
	

	public enum Variants{
		QUARTZ,
		GOLD,
		DIAMOND,
		EMERALD;
		
		public int getMetadata() {
			return ordinal();
		}

		public String getResource() {
			
			return Const.TEXTURE_LOCATION + "cyclic_wand_" + this.name().toLowerCase();
		}
	    static Variants getVariantFromMeta(ItemStack stack){
	    	try{
				return Variants.values()[stack.getMetadata()];
			}
			catch(Exception e){
				//System.out.println("INVALID META::"+stack.getMetadata());
				return Variants.QUARTZ;//this is damage zero anyway
			}
	    }
	}
}
