package com.lothrazar.cyclicmagic.block.imbue;

import com.lothrazar.cyclicmagic.block.imbue.BlockImbue.ImbueFlavor;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public class TileEntityImbue extends TileEntityBaseMachineInvo implements ITickable {

  public static final int SLOT_TARGET = 0;

  public TileEntityImbue() {
    super(2);//one slot for bow, 2 for ingredients
  }

  @Override
  public void update() {
    // TODO apply thing to target
    if (this.isPowered()) {
      RecipeImbue found = findMatchingRecipe();
      ItemStack target = this.getStackInSlot(SLOT_TARGET);
      ItemStack fuel = this.getStackInSlot(SLOT_TARGET + 1);
      if (target.getItem() instanceof ItemBow && found != null) {
        // wait, does it already HAVE imbue? 
        ImbueFlavor current = BlockImbue.getImbueType(target);
        //f bow has a different kind, OR empty charges
        if (current == null || current != found.flavor
            || BlockImbue.getImbueCharge(target) == 0) {
          BlockImbue.setImbue(target, found);
          BlockImbue.setImbueCharge(target, 20);
          fuel.shrink(1);
          if (world.isRemote) {
            //client only ie fake, no damage
            EntityLightningBolt ball = new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false);
            world.spawnEntity(ball);
          }
          //drop it yaaaas
          UtilItemStack.dropItemStackInWorld(world, pos, target);
          setInventorySlotContents(SLOT_TARGET, ItemStack.EMPTY);
        }
        //dont overwrite same to same
      }
    }
  }

  private RecipeImbue findMatchingRecipe() {
    for (RecipeImbue irecipe : BlockImbue.recipes) {
      if (irecipe.matches(this.getStackInSlot(1))) {
        return irecipe;
      }
    }
    return null;
  }
  //  private void addImbue(ItemStack target) {
  //    UtilNBT.getItemStackNBT(target).setInteger(BlockImbue.NBT_IMBUE, ImbueFlavor.LEVITATION.ordinal());
  //  }
}
