package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

public class ItemToolPush  extends BaseTool implements IHasRecipe,IHasConfig{

	@SuppressWarnings("unused")
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer p, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		BlockPos resultPosition = UtilPlaceBlocks.pushBlock(worldObj, p, pos, side);
 
		return super.onItemUse(stack, p, worldObj, pos, hand, side, hitX, hitY, hitZ);// EnumActionResult.PASS;
	}
	@Override
	public void syncConfig(Configuration config) {
		Property prop = config.get(Const.ConfigCategory.items, "ToolPush", true, "Tool that can push almost anything");
		prop.setRequiresMcRestart(true); 
		ItemRegistry.setConfigMap(this,prop.getBoolean());
	}
	@Override
	public void addRecipe() { 
		GameRegistry.addRecipe(new ItemStack(this, 8), "b  ", " b ", "  p", 
			'b',Items.blaze_rod, 
		    'p',Blocks.piston);  
	}
}
