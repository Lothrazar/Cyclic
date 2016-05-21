package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;

import net.minecraft.entity.player.EntityPlayer; 
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolHarvest extends BaseTool implements IHasRecipe, IHasConfig{

	private static final int range = 6;
	private static final int durability = 1000;
	
	public ItemToolHarvest(){
		super(durability);
	}
	 
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		HarestCropsConfig conf = new HarestCropsConfig(); 
		UtilHarvestCrops.harvestArea(worldObj, player, pos, range,conf);
		UtilHarvestCrops.harvestArea(worldObj, player, pos.up(), range,conf);

		super.onUse(stack, player, worldObj, hand);
		return super.onItemUse(stack, player, worldObj, pos, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public void syncConfig(Configuration config) {
		Property prop = config.get(Const.ConfigCategory.items, "ToolHarvest", true, "Tool that harvests grass, flowers, and fully grown crops from the nearby area");
		prop.setRequiresMcRestart(true); 
		ItemRegistry.setConfigMap(this,prop.getBoolean());
	}

	@Override
	public void addRecipe() { 

		GameRegistry.addRecipe(new ItemStack(this), 
				" gs", 
				" bg", 
				"b  ", 
			'b',Items.BLAZE_ROD, 
			'g',Items.GHAST_TEAR, 
		    's',Items.SHEARS); 
	}
}
