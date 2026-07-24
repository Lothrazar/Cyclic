package com.lothrazar.cyclic.item.compass;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.util.LevelWorldUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import javax.annotation.Nullable;

public class GpsCompassItem extends ItemBaseCyclic {

  public GpsCompassItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      ((ServerPlayer) player).openMenu(new ContainerProviderGpsCompass(), player.blockPosition());
    }
    return InteractionResult.SUCCESS.heldItemTransformedTo(stack);
  }

  public static float getAngle(ItemStack stack, @Nullable LivingEntity entity) {
    if (entity == null) {
      return 0.0f;
    }
    CompoundTag data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    if (!data.contains(GpsCompassCapability.NBT_KEY)) {
      return Mth.positiveModulo(entity.tickCount / 20.0f, 1.0f);
    }
    var lookup = entity.level().registryAccess();
    ItemStackHandler handler = new ItemStackHandler(1);
    handler.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, lookup, data.getCompound(GpsCompassCapability.NBT_KEY)));
    ItemStack cardStack = handler.getStackInSlot(0);
    if (cardStack.isEmpty()) {
      return Mth.positiveModulo(entity.tickCount / 20.0f, 1.0f);
    }
    BlockPosDim target = LocationGpsCard.getPosition(cardStack);
    if (target == null) {
      return Mth.positiveModulo(entity.tickCount / 20.0f, 1.0f);
    }
    String currentDim = LevelWorldUtil.dimensionToString(entity.level());
    if (target.getDimension() == null || !currentDim.equalsIgnoreCase(target.getDimension())) {
      return Mth.positiveModulo(entity.tickCount / 20.0f, 1.0f);
    }
    double dx = target.getPos().getX() + 0.5 - entity.getX();
    double dz = target.getPos().getZ() + 0.5 - entity.getZ();
    double angle = Math.atan2(dz, dx);
    double playerYaw = Mth.positiveModulo(entity.getYRot() / 360.0, 1.0);
    double targetAngle = Mth.positiveModulo(angle / (Math.PI * 2.0), 1.0);
    return (float) Mth.positiveModulo(targetAngle - playerYaw + 0.25, 1.0);
  }
}
