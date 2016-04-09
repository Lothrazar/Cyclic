package com.lothrazar.cyclicmagic.event;

import org.lwjgl.input.Keyboard;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class EventFoodDetails{

	@SubscribeEvent
	public void onItemTooltipEvent(ItemTooltipEvent event){

		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
			// https://www.reddit.com/r/minecraftsuggestions/comments/3brh7v/when_hovering_over_a_food_it_shows_how_many_food/
			ItemStack itemStack = event.getItemStack();
			if(itemStack != null && itemStack.getItem() instanceof ItemFood){
				ItemFood food = (ItemFood) itemStack.getItem();
	
				int hunger = food.getHealAmount(itemStack);
				float satur = food.getSaturationModifier(itemStack);
	
				event.getToolTip().add(hunger + " (" + satur + ")");
			}
		}
	}
}
