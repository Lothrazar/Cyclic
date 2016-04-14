package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFoodAppleMagic extends ItemFood implements IHasRecipe {
	private boolean							hasEffect	= false;
	private ArrayList<Potion>		potions;
	private ArrayList<Integer>	potionDurations;
	private ArrayList<Integer>	potionAmplifiers;
	private ItemStack recipeInput;
	private boolean isExpensive;
	public ItemFoodAppleMagic(int fillsHunger, boolean has_effect, ItemStack rec, boolean exp) {
		super(fillsHunger, false);// is not edible by wolf
		hasEffect = has_effect;// true gives it enchantment shine

		this.setAlwaysEdible(); // can eat even if full hunger
		this.setCreativeTab(ModMain.TAB);
		potions = new ArrayList<Potion>();
		potionDurations = new ArrayList<Integer>();
		potionAmplifiers = new ArrayList<Integer>();
		recipeInput = rec;
		isExpensive = exp;
	}

	public ItemFoodAppleMagic addEffect(Potion potionId, int potionDuration, int potionAmplifier) {

		potions.add(potionId);
		potionDurations.add(potionDuration * Const.TICKS_PER_SEC);
		potionAmplifiers.add(potionAmplifier);

		return this;// to chain together
	}

	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
		addAllEffects(world, player);
		
		if(par1ItemStack.getItem() == ItemRegistry.apple_diamond){
			
			UtilEntity.incrementMaxHealth(player, 2);
		}
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

		if (isExpensive) {
			GameRegistry.addRecipe(new ItemStack(this), "lll", "lal", "lll", 'l', recipeInput, 'a', Items.apple);
		}
		else {
			GameRegistry.addShapelessRecipe(new ItemStack(this), recipeInput, Items.apple);

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
