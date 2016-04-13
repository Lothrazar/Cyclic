package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFoodAppleMagic extends ItemFood {
	private boolean							hasEffect	= false;
	private ArrayList<Potion>		potions;
	private ArrayList<Integer>	potionDurations;
	private ArrayList<Integer>	potionAmplifiers;

	public ItemFoodAppleMagic(int fillsHunger, boolean has_effect) {
		super(fillsHunger, false);// is not edible by wolf
		hasEffect = has_effect;// true gives it enchantment shine

		this.setAlwaysEdible(); // can eat even if full hunger
		this.setCreativeTab(ModMain.TAB);
		potions = new ArrayList<Potion>();
		potionDurations = new ArrayList<Integer>();
		potionAmplifiers = new ArrayList<Integer>();
	}

	public ItemFoodAppleMagic addEffect(Potion potionId, int potionDuration, int potionAmplifier) {
		int TICKS_PER_SEC = 20;

		potions.add(potionId);
		potionDurations.add(potionDuration * TICKS_PER_SEC);
		potionAmplifiers.add(potionAmplifier);

		return this;// to chain together
	}

	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
		addAllEffects(world, player);
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return hasEffect; // give it shimmer, depending on if this was set in
		                  // constructor
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		if (hasEffect)
			return EnumRarity.EPIC; // dynamic text to match the two apple colours
		else
			return EnumRarity.RARE;
	}

	final static int smeltexp = 0;

	public static void addRecipe(ItemFoodAppleMagic apple, ItemStack ingredient, boolean isExpensive) {

		if (isExpensive) {
			GameRegistry.addRecipe(new ItemStack(apple), "lll", "lal", "lll", 'l', ingredient, 'a', Items.apple);
		}
		else {
			GameRegistry.addShapelessRecipe(new ItemStack(apple), ingredient, Items.apple);

		}

	}

	@Override
	public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) {
		for (int i = 0; i < potions.size(); i++) {

			//if(potions.get(i) != null)
				list.add(I18n.translateToLocal(potions.get(i).getName()));
		}
	}

	public void addAllEffects(World world, EntityLivingBase player) {

		for (int i = 0; i < potions.size(); i++) {

			PotionRegistry.addOrMergePotionEffect(player, new PotionEffect(potions.get(i), potionDurations.get(i), potionAmplifiers.get(i)));
		}
	}
}
