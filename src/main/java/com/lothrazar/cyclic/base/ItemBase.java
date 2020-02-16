package com.lothrazar.cyclic.base;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBase extends Item {

  public ItemBase(Properties properties) {
    super(properties);
  }

  protected ItemStack findAmmo(PlayerEntity player, Item item) {
    for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
      ItemStack itemstack = player.inventory.getStackInSlot(i);
      if (itemstack.getItem() == item) {
        return itemstack;
      }
    }
    return ItemStack.EMPTY;
  }

  public void tryRepairWith(ItemStack stackToRepair, PlayerEntity player, Item target) {
    if (stackToRepair.isDamaged()) {
      ItemStack torches = this.findAmmo(player, target);
      if (!torches.isEmpty()) {
        torches.shrink(1);
        UtilItemStack.repairItem(stackToRepair);
      }
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    TranslationTextComponent t = new TranslationTextComponent(getTranslationKey() + ".tooltip");
    t.applyTextStyle(TextFormatting.GRAY);
    tooltip.add(t);
  }
}
