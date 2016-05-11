package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BaseItem extends Item{

	private String rawName;
	private boolean hideFromCreativeTab = false;

	public BaseItem setRawName(String s){
		this.rawName = s;
		return this;
	}
	public BaseItem setHidden(boolean hide){
		this.hideFromCreativeTab = hide;
		return this;
	}

	public void register(){
		
		this.setUnlocalizedName(rawName);
		
		GameRegistry.register(this, new ResourceLocation(Const.MODID, rawName));

		if (hideFromCreativeTab == false) {
			this.setCreativeTab(ModMain.TAB);
		}
	}
}
