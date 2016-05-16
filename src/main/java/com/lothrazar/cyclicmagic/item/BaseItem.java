package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BaseItem extends Item{
 
	private boolean hideFromCreativeTab = false;
 
	public BaseItem setHidden(){
		this.hideFromCreativeTab = true;
		return this;
	}
 
	public void register(String name){
		
		ItemRegistry.registerItem(this, name, this.hideFromCreativeTab);
	}
}
