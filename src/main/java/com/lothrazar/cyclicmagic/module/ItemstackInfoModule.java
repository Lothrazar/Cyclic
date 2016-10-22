package com.lothrazar.cyclicmagic.module;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemstackInfoModule extends BaseEventModule implements IHasConfig {
  private boolean foodDetails;
  private boolean fuelDetails;
  @SubscribeEvent
  public void onItemTooltipEvent(ItemTooltipEvent event) {
    if (event.getItemStack() == null) { return; }
    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
      // https://www.reddit.com/r/minecraftsuggestions/comments/3brh7v/when_hovering_over_a_food_it_shows_how_many_food/
      ItemStack itemStack = event.getItemStack();
      if (foodDetails && itemStack != null && itemStack.getItem() instanceof ItemFood) {
        ItemFood food = (ItemFood) itemStack.getItem();
        int hunger = food.getHealAmount(itemStack);
        float satur = food.getSaturationModifier(itemStack);
        if (hunger > 0 || satur > 0) {
          event.getToolTip().add(hunger + " (" + satur + ")");
        }
      }
      if (fuelDetails) {
        int burnTime = TileEntityFurnace.getItemBurnTime(itemStack);
        if (burnTime > 0) {
          event.getToolTip().add(I18n.format("tooltip.burntime") + burnTime);
        }
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.items;
    config.addCustomCategoryComment(category, "Tweaks to new and existing items");
    foodDetails = config.getBoolean("Food Details", category, true, "Add food value and saturation to items info (hold shift)");
    fuelDetails = config.getBoolean("Fuel Details", category, true, "Add fuel burn time to items info (hold shift)");
  }
}
