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
	public String getRawName() {
		return rawName;
	}

	public void register(){
		
		this.setUnlocalizedName(getRawName());
		
		GameRegistry.register(this, new ResourceLocation(Const.MODID, getRawName()));

		if (hideFromCreativeTab == false) {
			this.setCreativeTab(ModMain.TAB);
		}
	}
}
