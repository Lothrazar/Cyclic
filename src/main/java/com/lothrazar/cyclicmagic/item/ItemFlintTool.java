package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFlintTool extends ItemAxe implements IHasRecipe {

	/*FOR DAMAGE AND SPEED: looks like diamond uses the final param
	 *  private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};
    */
	public ItemFlintTool() {
		// protected ItemAxe(Item.ToolMaterial material, int damage, int speed)
		super(ToolMaterial.WOOD,1,-3);
		this.setMaxDamage(10);
	}

	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe(new ItemStack(this), 
				" f", 
				"s ", 
				'f',new ItemStack(Items.FLINT), 
				's', new ItemStack(Items.STICK));


		GameRegistry.addShapedRecipe(new ItemStack(this), 
				"f ", 
				" s", 
				'f',new ItemStack(Items.FLINT), 
				's', new ItemStack(Items.STICK));	}
}
