package com.lothrazar.cyclic.item.endereye;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemEnderEyeReuse extends ItemBase {

  public ItemEnderEyeReuse(Properties properties) {
    super(properties.durability(256));
  }

  //compat with Repurposed Structures Mod see #1517
  private static final String RS_MODID = "repurposed_structures";
  private static final String RS_STRONGHOLD_ID = "stronghold_stonebrick";
  private static final String RS_NETHER_STRONGHOLD_ID = "stronghold_nether";
  private static final ResourceLocation RS_RESOURCE_LOCATION = new ResourceLocation(RS_MODID, RS_STRONGHOLD_ID);
  private static final ResourceLocation RS_NETHER_RESOURCE_LOCATION = new ResourceLocation(RS_MODID, RS_NETHER_STRONGHOLD_ID);

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!worldIn.isClientSide && worldIn instanceof ServerLevel) {
      ServerLevel sw = (ServerLevel) worldIn;
      ChunkGenerator chunkGenerator = sw.getChunkSource().getGenerator();
      //compat with Repurposed Structures Mod see #1517
      StructureFeature<?> vanillaStronghold = StructureFeature.STRONGHOLD;
      StructureFeature<?> rsStronghold;
      StructureFeature<?> rsNetherStronghold;
      BlockPos closestBlockPos = chunkGenerator.findNearestMapFeature(sw, vanillaStronghold, new BlockPos(player.blockPosition()), 100, false);
      BlockPos rsBlockPos;
      if (ModList.get().isLoaded(RS_MODID)) {
        if (ForgeRegistries.STRUCTURE_FEATURES.containsKey(RS_RESOURCE_LOCATION)) {
          rsStronghold = ForgeRegistries.STRUCTURE_FEATURES.getValue(RS_RESOURCE_LOCATION);
          rsBlockPos = chunkGenerator.findNearestMapFeature(sw, rsStronghold, new BlockPos(player.blockPosition()), 100, false);
          closestBlockPos = returnClosest(player.blockPosition(), closestBlockPos, rsBlockPos);
        }
        if (ForgeRegistries.STRUCTURE_FEATURES.containsKey(RS_NETHER_RESOURCE_LOCATION)) {
          rsNetherStronghold = ForgeRegistries.STRUCTURE_FEATURES.getValue(RS_NETHER_RESOURCE_LOCATION);
          rsBlockPos = chunkGenerator.findNearestMapFeature(sw, rsNetherStronghold, new BlockPos(player.blockPosition()), 100, false);
          closestBlockPos = returnClosest(player.blockPosition(), closestBlockPos, rsBlockPos);
        }
      }
      if (closestBlockPos != null) {
        double posX = player.getX();
        double posY = player.getY();
        double posZ = player.getZ();
        EyeOfEnderEntityNodrop eyeofenderentity = new EyeOfEnderEntityNodrop(worldIn, posX, posY + player.getBbHeight() / 2.0F, posZ);
        eyeofenderentity.signalTo(closestBlockPos);
        worldIn.addFreshEntity(eyeofenderentity);
        if (player instanceof ServerPlayer) {
          CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer) player, closestBlockPos);
        }
        worldIn.playSound((Player) null, posX, posY, posZ, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F,
            0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
        worldIn.levelEvent((Player) null, 1003, new BlockPos(player.blockPosition()), 0);
        UtilItemStack.damageItem(player, stack);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.getCooldowns().addCooldown(stack.getItem(), 10);
        return InteractionResultHolder.success(player.getItemInHand(hand));
      }
    }
    return super.use(worldIn, player, hand);
  }

  private BlockPos returnClosest(BlockPos playerPos, BlockPos pos1, BlockPos pos2) {
    if (pos1 == null && pos2 == null) {
      return null;
    }
    else if (pos1 == null) {
      return pos2;
    }
    else if (pos2 == null) {
      return pos1;
    }
    else if (UtilWorld.distanceBetweenHorizontal(playerPos, pos1) <= UtilWorld.distanceBetweenHorizontal(playerPos, pos2)) {
      return pos1;
    }
    return pos2;
  }
}
