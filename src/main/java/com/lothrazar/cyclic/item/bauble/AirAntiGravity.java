package com.lothrazar.cyclic.item.bauble;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AirAntiGravity extends ItemBase implements IHasClickToggle {

  private static final int TICKS_FALLDIST_SYNC = 22;//tick every so often
  private static final double DOWNWARD_SPEED_SNEAKING = -0.32;

  public AirAntiGravity(Properties properties) {
    super(properties);
  }

  private boolean canUse(ItemStack stack) {
    return stack.getDamage() < stack.getMaxDamage();
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    if (!this.canUse(stack)) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    if (entity instanceof PlayerEntity == false) {
      return;
    }
    PlayerEntity player = (PlayerEntity) entity;
    BlockPos belowMe = player.getPosition().down();
    boolean isAirBorne = (world.isAirBlock(belowMe) //sneak on air, or a nonsolid block like a flower
        || world.isTopSolid(belowMe, player) == false);
    //
    if (isAirBorne && player.getMotion().y < 0) {//player.isSneaking() &&
      double y = (player.isCrouching()) ? DOWNWARD_SPEED_SNEAKING : 0;
      player.setMotion(player.getMotion().x, y, player.getMotion().z);
      player.isAirBorne = false;
      //if we set onGround->true all the time, it blocks fwd movement anywya
      player.setOnGround(true);// (player.motionX == 0 && player.motionZ == 0); //allow jump only if not walking
      if (player.getEntityWorld().rand.nextDouble() < 0.1) {
        //        super.damageCharm(player, stack);
        UtilItemStack.damageItem(player, stack);
      }
      if (world.isRemote && player.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
        PacketRegistry.INSTANCE.sendToServer(new PacketPlayerFalldamage());
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
  public void toggle(PlayerEntity player, ItemStack held) {
    CompoundNBT tag = held.getTag();
    if (tag == null) {
      tag = new CompoundNBT();
    }
    tag.putInt(NBT_STATUS, (tag.getInt(NBT_STATUS) + 1) % 2);
    held.setTag(tag);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return isOn(stack);
  }

  @Override
  public boolean isOn(ItemStack held) {
    return held.getOrCreateTag().getInt(NBT_STATUS) == 0;
  }
}
