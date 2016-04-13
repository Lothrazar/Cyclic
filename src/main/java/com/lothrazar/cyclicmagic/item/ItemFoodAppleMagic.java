package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList; 
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
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

public class ItemFoodAppleMagic extends ItemFood
{  
	private boolean hasEffect = false;
	private ArrayList<Integer> potionIds;
	private ArrayList<Integer> potionDurations;
	private ArrayList<Integer> potionAmplifiers;

	
	public ItemFoodAppleMagic(int fillsHunger,boolean has_effect)
	{  
		super(fillsHunger,false);//is not edible by wolf
		hasEffect = has_effect;//true gives it enchantment shine
 
		this.setAlwaysEdible(); //can eat even if full hunger
		this.setCreativeTab(ModMain.TAB);
		potionIds = new ArrayList<Integer>();
		potionDurations = new ArrayList<Integer>();
		potionAmplifiers = new ArrayList<Integer>();
	}
	 
	public ItemFoodAppleMagic addEffect(int potionId,int potionDuration,int potionAmplifier)
	{
		int TICKS_PER_SEC = 20;
		
		potionIds.add(potionId);
		potionDurations.add(potionDuration * TICKS_PER_SEC);
		potionAmplifiers.add(potionAmplifier);
		 
		return this;//to chain together
	}
	
	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player)
    {     
		addAllEffects(world, player);  
    }
	
	@Override
    public boolean hasEffect(ItemStack par1ItemStack)
    {
    	return hasEffect; //give it shimmer, depending on if this was set in constructor
    }
	 
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		 if(hasEffect)
			 return EnumRarity.EPIC; //dynamic text to match the two apple colours
		 else 
			 return EnumRarity.RARE;
	} 
	final static int smeltexp = 0;
	public static void addRecipe(ItemFoodAppleMagic apple, ItemStack ingredient, boolean isExpensive) 
	{
		int refund = 8;
		
		if(isExpensive)
		{ 
			GameRegistry.addRecipe(new ItemStack(apple)
				,"lll","lal","lll"  
				,'l', ingredient
				,'a', Items.apple); 
		}
		else
		{
			GameRegistry.addShapelessRecipe(new ItemStack(apple)
				,ingredient
				, Items.apple);
	 
			refund = 1; 
		}
		
		GameRegistry.addSmelting(apple, new ItemStack(ingredient.getItem(), refund, ingredient.getMetadata()),	smeltexp);
	} 
	 
	@Override
	public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) 
	{   
		Potion p;
		for(int i = 0; i < potionIds.size(); i++)  
  		{ 
  			p = Potion.potionRegistry.getObjectById(potionIds.get(i));//Potion.potionTypes[potionIds.get(i)];
  			 
  			list.add(I18n.translateToLocal(p.getName()));   
  		}   
	} 

	public void addAllEffects(World world, EntityLivingBase player) 
	{
		if(world.isRemote == false)  //false means serverside
	  		for(int i = 0; i < potionIds.size(); i++)  
	  		{ 
	  			addOrMergePotionEffect(player, new PotionEffect(
	  					Potion.potionRegistry.getObjectById(potionIds.get(i)) 
	  					,potionDurations.get(i)
	  					,potionAmplifiers.get(i)));
	  		}
	}
	
	public static void addOrMergePotionEffect(EntityLivingBase player, PotionEffect newp)
	{
		if(player.isPotionActive(newp.getPotion()))
		{
			//do not use built in 'combine' function, just add up duration myself
			PotionEffect p = player.getActivePotionEffect(newp.getPotion());
			
			int ampMax = Math.max(p.getAmplifier(), newp.getAmplifier());
		
			
			player.addPotionEffect(new PotionEffect(newp.getPotion()
					,newp.getDuration() + p.getDuration()
					,ampMax));
		}
		else
		{
			player.addPotionEffect(newp);
		}
	}

}
