package com.lothrazar.cyclicmagic.item.armor;
 
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
 

public class ItemEmeraldArmor extends ItemArmor 
{
	public String textureName;
	public ItemEmeraldArmor(EntityEquipmentSlot armorType) 
	{
		//int renderIndex = 0;
		super(ItemRegistry.ARMOR_MATERIAL_EMERALD, 0, armorType);
		textureName = "emerald_armor";
		this.setCreativeTab(ItemRegistry.tabSamsContent); 
	}
	/*
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
	    return ModArmor.TEXTURE_LOCATION + "textures/models/armor/" + this.textureName + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
	}
	*/
}
