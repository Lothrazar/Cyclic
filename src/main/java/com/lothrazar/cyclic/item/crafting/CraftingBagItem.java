package com.lothrazar.cyclic.item.crafting;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CraftingBagItem extends ItemBaseCyclic {

  public CraftingBagItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide() && !playerIn.isCrouching()) {
      int slot = handIn == InteractionHand.MAIN_HAND ? playerIn.getInventory().getSelectedSlot() : 40;
      playerIn.openMenu(new CraftingBagContainerProvider(slot), buf -> buf.writeInt(slot));
    }
    return super.use(worldIn, playerIn, handIn);
  }

}
