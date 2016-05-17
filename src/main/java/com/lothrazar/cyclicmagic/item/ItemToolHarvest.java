package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ItemToolHarvest extends BaseTool implements IHasRecipe, IHasConfig{

	private static int			range_main		= 6;
	 
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer p, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		UtilHarvestCrops.harvestArea(worldObj, p, pos, range_main);
		UtilHarvestCrops.harvestArea(worldObj, p, pos.up(), range_main);
		
		return super.onItemUse(stack, p, worldObj, pos, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public void syncConfig(Configuration config) {
		Property prop = config.get(Const.ConfigCategory.items, "ToolHarvest", true, "Tool that harvests grass, flowers, and fully grown crops from the nearby area");
		prop.setRequiresMcRestart(true); 
		ItemRegistry.setConfigMap(this,prop.getBoolean());
	}

	@Override
	public void addRecipe() { 

		System.out.println("REMINDER: needs recipe");
	}
	//TODO: CONFIG: 	ItemRegistry.setConfigMap(this,prop.getBoolean());
}
