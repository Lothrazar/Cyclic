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

public class ItemToolPull  extends BaseTool implements  IHasRecipe, IHasConfig{

private static final int durability = 5000;
	
	public ItemToolPull(){
		super(durability);
	}
	
	@SuppressWarnings("unused")
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

		BlockPos resultPosition = UtilPlaceBlocks.pullBlock(worldObj, player, pos, side);

		super.onUse(stack, player, worldObj, hand);
		return super.onItemUse(stack, player, worldObj, pos, hand, side, hitX, hitY, hitZ); 

	}
	@Override
	public void syncConfig(Configuration config) {
		Property prop = config.get(Const.ConfigCategory.items, "ToolPull", true, "Tool that can pull almost anything");
		prop.setRequiresMcRestart(true); 
		ItemRegistry.setConfigMap(this,prop.getBoolean());
	}
	@Override
	public void addRecipe() { 
		GameRegistry.addRecipe(new ItemStack(this, 8), "b  ", " b ", "  p", 
			'b',Items.blaze_rod, 
		    'p',Blocks.sticky_piston);  
	}
}
