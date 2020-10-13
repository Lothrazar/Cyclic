package com.lothrazar.cyclic.item.bauble;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FlippersItem extends ItemBase implements IHasClickToggle {

  private static final float speedfactor = 0.11F * 3.5F;

  public FlippersItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    //so
    if (!this.isOn(stack)) {
      return;
    }
    if (entityIn instanceof LivingEntity) {
      LivingEntity entity = (LivingEntity) entityIn;
      if (entity.isInWater()) {
        UtilEntity.speedupEntityIfMoving(entity, speedfactor);
      }
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    TranslationTextComponent t = new TranslationTextComponent("item.cyclic.bauble.on." + this.isOn(stack));
    t.mergeStyle(TextFormatting.DARK_GRAY);
    tooltip.add(t);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return isOn(stack);
  }

  @Override
  public void toggle(PlayerEntity player, ItemStack held) {
    CompoundNBT tag = held.getTag();
    if (tag == null) {
      tag = new CompoundNBT();
    }
    tag.putInt(NBT_STATUS, (tag.getInt(NBT_STATUS) + 1) % 2);
    held.setTag(tag);
  }

  @Override
  public boolean isOn(ItemStack held) {
    return held.getOrCreateTag().getInt(NBT_STATUS) == 0;
  }
}
