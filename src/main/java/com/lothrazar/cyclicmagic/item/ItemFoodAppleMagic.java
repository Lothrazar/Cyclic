package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.util.Const; 
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFoodAppleMagic extends ItemFood implements IHasRecipe {
	private boolean							hasEffect	= false;
	private ArrayList<Potion>		potions;
	private ArrayList<Integer>	potionDurations;
	private ArrayList<Integer>	potionAmplifiers;
	private ItemStack recipeInput;

	public static boolean apple_emerald_enabled;
	public static boolean apple_diamond_enabled;
	public static boolean apple_ender_enabled;
	public static boolean apple_bone_enabled;
	public static boolean apple_lapis_enabled;
	public static boolean apple_chocolate_enabled;
	public static boolean apple_netherwart_enabled;
	public static boolean apple_prismarine_enabled;
	public static boolean apple_clownfish_enabled;
	public static boolean apple_chorus_enabled;
	/*
	public ItemFoodAppleMagic(int fillsHunger, boolean has_effect, ItemStack rec) {
		this(fillsHunger,has_effect,rec);
	}*/
	
	public ItemFoodAppleMagic(int fillsHunger, boolean has_effect, ItemStack rec
			,Potion potionId, int potionDuration, int potionAmplifier) {
		super(fillsHunger, false);// is not edible by wolf
		hasEffect = has_effect;// true gives it enchantment shine
		this.setAlwaysEdible(); // can eat even if full hunger
		this.setCreativeTab(ModMain.TAB);
		potions = new ArrayList<Potion>();
		potionDurations = new ArrayList<Integer>();
		potionAmplifiers = new ArrayList<Integer>();
		recipeInput = rec;
		
		this.addEffect(potionId, potionDuration, potionAmplifier);
	}
	

	public ItemFoodAppleMagic addEffect(Potion potionId, int potionDuration, int potionAmplifier) {

		//currently, items pretty much just have one potion. but keeping the arrays in case that changes later
		potions.add(potionId);
		potionDurations.add(potionDuration * Const.TICKS_PER_SEC);
		potionAmplifiers.add(potionAmplifier);

		return this;// to chain together
	}

	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
		addAllEffects(world, player);
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return hasEffect; 
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		if (hasEffect)
			return EnumRarity.EPIC; // dynamic text to match the two apple colours
		else
			return EnumRarity.RARE;
	}

	public void addRecipe() {
 
		GameRegistry.addRecipe(new ItemStack(this), "lll", "lal", "lll", 'l', recipeInput, 'a', Items.APPLE);
	 
	}

	@Override
	public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) {
		for (int i = 0; i < potions.size(); i++) {

			list.add(I18n.format(potions.get(i).getName()));
		}
	}

	public void addAllEffects(World world, EntityLivingBase player) {

		for (int i = 0; i < potions.size(); i++) {

			PotionRegistry.addOrMergePotionEffect(player, new PotionEffect(potions.get(i), potionDurations.get(i), potionAmplifiers.get(i)));
		}
	}

	public static void syncConfig(Configuration config) {

		String category = Const.ConfigCategory.items;

		apple_bone_enabled 		= config.getBoolean("AppleBone", 		category, true,"A magic apple that gives the glowing effect - like spectral arrows. (Works on any living entity)");
		apple_emerald_enabled 	= config.getBoolean("AppleEmerald", 	category, true,"A magic apple that gives health booost V");
		apple_diamond_enabled 	= config.getBoolean("AppleDiamond", 	category, true,"A magic apple that gives the resistance effect");
		apple_ender_enabled 	= config.getBoolean("AppleEnder", 		category, true,"A magic apple that gives a new custom ender aura effect negates ender pearl damage");
		apple_lapis_enabled 	= config.getBoolean("AppleLapis", 		category, true,"A magic apple that gives the haste effect. (Works on any living entity)");
		apple_chocolate_enabled = config.getBoolean("AppleChocolate", 	category, true,"A magic apple that gives the luck effect. (Works on any living entity)");
		apple_netherwart_enabled= config.getBoolean("AppleNetherwart",  category, true,"A magic apple that gives a new custom magnet potion effect that pulls all nearby items towards you. (Works on any living entity)");
		apple_prismarine_enabled= config.getBoolean("ApplePrismarine",  category, true,"A magic apple that gives a new custom waterwalking effect. (Works on any living entity)");
		apple_clownfish_enabled = config.getBoolean("AppleClownfish", 	category, true,"A magic apple that gives a new custom slowfall potion effect. (Works on any living entity)");
		apple_chorus_enabled    = config.getBoolean("AppleChorus", 		category, true,"A magic apple that gives the levitation effect - just like shulkers. (Works on any living entity)");
 
	}
}
