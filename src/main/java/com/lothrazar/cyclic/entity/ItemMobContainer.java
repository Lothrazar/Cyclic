package com.lothrazar.cyclic.entity;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemMobContainer extends ItemBase {

  public ItemMobContainer(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    ItemStack stack = player.getHeldItem(context.getHand());
    if (stack.hasTag() == false) {
      return ActionResultType.PASS;
    }
    BlockPos pos = context.getPos();
    if (context.getFace() != null) {
      pos = pos.offset(context.getFace());
    }
    World world = context.getWorld();
    Entity entity = ForgeRegistries.ENTITIES.getValue(
        new ResourceLocation(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)))
        .create(world);
    //    entity.egg
    entity.read(stack.getTag());
    //  target.rotationYaw// TODO
    //  target.rotationPitch
    entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5);
    if (world.addEntity(entity)) {
      stack.setTag(null);
      stack.shrink(1);
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.PASS;
  }
}
