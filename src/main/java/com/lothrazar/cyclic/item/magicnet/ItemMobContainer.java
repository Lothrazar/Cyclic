package com.lothrazar.cyclic.item.magicnet;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemMobContainer extends ItemBase {

  public ItemMobContainer(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (stack.hasTag()) {
      TranslationTextComponent t = new TranslationTextComponent(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID));
      t.applyTextStyle(TextFormatting.GRAY);
      tooltip.add(t);
    }
    else {
      super.addInformation(stack, worldIn, tooltip, flagIn);
    }
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
