package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.util.UtilStuff;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GloveItem extends Item implements IHasClickToggle {

  public static final int TICKS_FALLDIST_SYNC = 22;//tick every so often
  private static final double CLIMB_SPEED = 0.288D;
  private final static String NBT_STATUS = "onoff";

  public GloveItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    //so
    if (!this.isOn(stack)) {
      return;
    }
    if (entityIn instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entityIn;
      if (player.collidedHorizontally) {
        World world = player.getEntityWorld();
        UtilStuff.tryMakeEntityClimb(world, player, CLIMB_SPEED);
        //        stack.damageItem(1, player);
        if (world.isRemote &&
            player.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
          //SOUND DOES WORK I JSUT dont like it anymore lol
          //  UtilStuff.playSound(player, SoundEvents.BLOCK_LADDER_STEP);
        }
      }
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return false;//isOn(stack);
  }

  @Override
  public void toggle(PlayerEntity player, ItemStack held) {
    CompoundNBT tag = held.getTag();
    if (tag == null) {
      tag = new CompoundNBT();
    }
    tag.putInt(NBT_STATUS, (tag.getInt(NBT_STATUS) + 1) % 2);
    held.setTag(tag);
    //    ModCyclic.LOGGER.info("after toggle " + held.getTag().getInt(NBT_STATUS));
  }

  @Override
  public boolean isOn(ItemStack held) {
    return held.getOrCreateTag().getInt(NBT_STATUS) == 0;
  }
}
