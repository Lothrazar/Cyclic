package com.lothrazar.cyclicmagic.item;

import java.util.List;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PlayerPowerups;
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

	private static final String NBT_SPELL = "spell_id";
	private static final String NBT_UNLOCKS = "unlock_";
	public ItemCyclicWand() {
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabAllSearch);//TODO: remove this for release
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
		String name = super.getUnlocalizedName() + "_" + getVariantFromMeta(stack).name().toLowerCase();

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

		PlayerPowerups props = PlayerPowerups.get(playerIn);
		ISpell spell = SpellRegistry.getSpellFromID(Spells.getSpellCurrent(stack));

		tooltip.add(spell.getName());
		tooltip.add(StatCollector.translateToLocal("spell.cost") + spell.getCost());
		int max = (int) SpellRegistry.caster.MAXMANA;
		tooltip.add(props.getMana() + "/" + max);

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
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

	private static final int REGEN = 1;
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// if held by something not a player? such as custom npc/zombie/etc
		if (entityIn instanceof EntityPlayer == false) {
			return;
		}

		EntityPlayer p = (EntityPlayer) entityIn;
		if (worldIn.isRemote == false && worldIn.getWorldTime() % Const.TICKS_PER_SEC == 0) {

			PlayerPowerups props = PlayerPowerups.get(p);
			props.setMana(props.getMana() + REGEN);
		}
		
		triggerRuneEffect(stack,worldIn, p );
		
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	

	private void triggerRuneEffect(ItemStack stack,World world,EntityPlayer entityIn){
	
		switch(this.getVariantFromMeta(stack)){
		case QUARTZ:
			break;
		case GOLD:
			
			break;
		case DIAMOND:
			
			break;
		case EMERALD:
//TODO: balance and crafting
			Passives.triggerFalling( entityIn);
			Passives.triggerBreathing( entityIn);
			Passives.triggerBurning( entityIn);
			Passives.triggerProtect( entityIn);
			break;
		default:break;
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		// If onItemUse returns false onItemRightClick will be called.
		// http://www.minecraftforge.net/forum/index.php?topic=31966.0
		// so if this casts and succeeds, the right click is cancelled
		return SpellRegistry.caster.tryCastCurrent(worldIn, playerIn, pos, side);
	}
	
    private static NBTTagCompound getStackNBT(ItemStack stack){
    	return stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    }
    
    private Variants getVariantFromMeta(ItemStack stack){
    	try{
			return Variants.values()[stack.getMetadata()];
		}
		catch(Exception e){
			//System.out.println("INVALID META::"+stack.getMetadata());
			return Variants.QUARTZ;//this is damage zero anyway
		}
    }
    
	private static class Passives{

		private final static float HEALTHLIMIT = 10;//1 heart = 2 health
		private final static int SECONDS = 30;
		private final static float FALLDISTANCE = 3;
		private final static float AIRLIMIT = 150;// 300 is a full bar
		
		private static void triggerProtect(EntityPlayer entity){
			if(entity.getHealth() <= HEALTHLIMIT && entity.isPotionActive(Potion.absorption.id) == false){
				
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.absorption.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.V));
			
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.resistance.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.I));
			}
		}
		
		private static void triggerBurning(EntityPlayer entity){
			if(entity.isBurning() && entity.isPotionActive(Potion.fireResistance.id) == false){
	
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.fireResistance.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.V));
				
			}
			if(entity.ridingEntity != null && entity.ridingEntity.fallDistance >= FALLDISTANCE 
					&& entity.ridingEntity instanceof EntityLivingBase){
				EntityLivingBase maybeHorse = (EntityLivingBase)entity.ridingEntity;
				
				if(maybeHorse.isPotionActive(PotionRegistry.slowfall) == false){
	
					PotionRegistry.addOrMergePotionEffect(maybeHorse, new PotionEffect(PotionRegistry.slowfall.id,SECONDS * Const.TICKS_PER_SEC));
	
				}
			}
		}
		
		private static void triggerBreathing(EntityPlayer entity){
			if(entity.getAir() <= AIRLIMIT){
				
				if(entity.isPotionActive(Potion.waterBreathing) == false){
					PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.waterBreathing.id,SECONDS * Const.TICKS_PER_SEC));
	
				}
			}
		}
		
		private static void triggerFalling(EntityPlayer entity){
			if(entity.fallDistance >= FALLDISTANCE){
				
				if(entity.isPotionActive(PotionRegistry.slowfall) == false){
					PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(PotionRegistry.slowfall.id,SECONDS * Const.TICKS_PER_SEC));
	
				}
			}
			
			if(entity.getPosition().getY() < -10){
				entity.setPositionAndUpdate(entity.getPosition().getX(), entity.getPosition().getY() + 256, entity.getPosition().getZ());;
	
			}
		}
	}

	public static class Spells{
	
		private static void unlockSpell(ItemStack stack, ISpell spell){
			NBTTagCompound nbt = getStackNBT(stack);
			nbt.setBoolean(NBT_UNLOCKS + spell.getID(), true);
			stack.setTagCompound(nbt);
		}/*
		private static void lockSpell(ItemStack stack, ISpell spell){
			NBTTagCompound nbt = getStackNBT(stack);
			nbt.setBoolean(NBT_UNLOCKS + spell.getID(), true);
			stack.setTagCompound(nbt);
		}*/
		public static void toggleSpell(ItemStack stack, int spell_id){
			NBTTagCompound nbt = getStackNBT(stack);
			String key = NBT_UNLOCKS + spell_id;
			nbt.setBoolean(key, !nbt.getBoolean(key));
			stack.setTagCompound(nbt);
		}
		public static boolean isSpellUnlocked(ItemStack stack, ISpell spell){
			return isSpellUnlocked(stack,spell.getID());
		}
		public static boolean isSpellUnlocked(ItemStack stack, int spell_id){
			NBTTagCompound nbt = getStackNBT(stack);
			return nbt.getBoolean(NBT_UNLOCKS + spell_id);
		}
		private static void setUnlockDefault(ItemStack stack){
	
			NBTTagCompound nbt = getStackNBT(stack);
			
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
			
			return getStackNBT(stack).getInteger(NBT_SPELL);
		}
		
		public static void setSpellCurrent(ItemStack stack,int spell_id) {
			NBTTagCompound tags = getStackNBT(stack);
			
			tags.setInteger(NBT_SPELL, spell_id);
			
			stack.setTagCompound(tags);
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
	}
}
