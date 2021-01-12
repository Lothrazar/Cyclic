package com.lothrazar.cyclic.item.endereye;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilWorld;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemEnderEyeReuse extends ItemBase {

  public ItemEnderEyeReuse(Properties properties) {
    super(properties.maxDamage(256));
  }

  //compat with Repurposed Structures Mod see #1517
  private static final String RS_MODID = "repurposed_structures";
  private static final String RS_STRONGHOLD_ID = "stronghold_stonebrick";
  private static final String RS_NETHER_STRONGHOLD_ID = "stronghold_nether";
  private static final ResourceLocation RS_RESOURCE_LOCATION = new ResourceLocation(RS_MODID, RS_STRONGHOLD_ID);
  private static final ResourceLocation RS_NETHER_RESOURCE_LOCATION = new ResourceLocation(RS_MODID, RS_NETHER_STRONGHOLD_ID);

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (!worldIn.isRemote && worldIn instanceof ServerWorld) {
      ServerWorld sw = (ServerWorld) worldIn;
      ChunkGenerator chunkGenerator = sw.getChunkProvider().getChunkGenerator();
      //compat with Repurposed Structures Mod see #1517
      Structure<?> vanillaStronghold = Structure.STRONGHOLD;
      Structure<?> rsStronghold;
      Structure<?> rsNetherStronghold;
      BlockPos closestBlockPos = chunkGenerator.func_235956_a_(sw, vanillaStronghold, new BlockPos(player.getPosition()), 100, false);
      BlockPos rsBlockPos;
      if (ModList.get().isLoaded(RS_MODID)) {
        if (ForgeRegistries.STRUCTURE_FEATURES.containsKey(RS_RESOURCE_LOCATION)) {
          rsStronghold = ForgeRegistries.STRUCTURE_FEATURES.getValue(RS_RESOURCE_LOCATION);
          rsBlockPos = chunkGenerator.func_235956_a_(sw, rsStronghold, new BlockPos(player.getPosition()), 100, false);
          closestBlockPos = returnClosest(player.getPosition(), closestBlockPos, rsBlockPos);
        }
        if (ForgeRegistries.STRUCTURE_FEATURES.containsKey(RS_NETHER_RESOURCE_LOCATION)) {
          rsNetherStronghold = ForgeRegistries.STRUCTURE_FEATURES.getValue(RS_NETHER_RESOURCE_LOCATION);
          rsBlockPos = chunkGenerator.func_235956_a_(sw, rsNetherStronghold, new BlockPos(player.getPosition()), 100, false);
          closestBlockPos = returnClosest(player.getPosition(), closestBlockPos, rsBlockPos);
        }
      }
      if (closestBlockPos != null) {
        double posX = player.getPosX();
        double posY = player.getPosY();
        double posZ = player.getPosZ();
        EyeOfEnderEntityNodrop eyeofenderentity = new EyeOfEnderEntityNodrop(worldIn, posX, posY + player.getHeight() / 2.0F, posZ);
        eyeofenderentity.moveTowards(closestBlockPos);
        worldIn.addEntity(eyeofenderentity);
        if (player instanceof ServerPlayerEntity) {
          CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayerEntity) player, closestBlockPos);
        }
        worldIn.playSound((PlayerEntity) null, posX, posY, posZ, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F,
            0.4F / (random.nextFloat() * 0.4F + 0.8F));
        worldIn.playEvent((PlayerEntity) null, 1003, new BlockPos(player.getPosition()), 0);
        UtilItemStack.damageItem(player, stack);
        player.addStat(Stats.ITEM_USED.get(this));
        player.getCooldownTracker().setCooldown(stack.getItem(), 10);
        return ActionResult.resultSuccess(player.getHeldItem(hand));
      }
    }
    return super.onItemRightClick(worldIn, player, hand);
  }

  private BlockPos returnClosest(BlockPos playerPos, @Nullable BlockPos pos1, @Nullable BlockPos pos2) {
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
