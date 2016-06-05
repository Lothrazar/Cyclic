package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldArmor extends ItemArmor implements IHasRecipe {

	public ItemEmeraldArmor(EntityEquipmentSlot armorType) {

		super(ItemRegistry.ARMOR_MATERIAL_EMERALD, 0, armorType);
	}

	@Override
	public void addRecipe() {

		switch (this.armorType) {
		case CHEST:
			GameRegistry.addShapedRecipe(new ItemStack(this), "e e", "eee", "eee", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD));
		break;
		case FEET:
			GameRegistry.addShapedRecipe(new ItemStack(this), "e e", "e e", "   ", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD));
			GameRegistry.addShapedRecipe(new ItemStack(this), "   ", "e e", "e e", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD));
		break;
		case HEAD:
			GameRegistry.addShapedRecipe(new ItemStack(this), "eee", "e e", "   ", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD));

			GameRegistry.addShapedRecipe(new ItemStack(this), "   ", "eee", "e e", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD));

		break;
		case LEGS:
			GameRegistry.addShapedRecipe(new ItemStack(this), "eee", "e e", "e e", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD));
		break;
		case MAINHAND:
		break;
		case OFFHAND:
		break;
		default:
		break;
		}
	}
}
