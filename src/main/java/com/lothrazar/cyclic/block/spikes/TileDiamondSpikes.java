package com.lothrazar.cyclic.block.spikes;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.lang.ref.WeakReference;

public class TileDiamondSpikes extends TileBlockEntityCyclic {

  WeakReference<FakePlayer> fakePlayer;
  final static boolean dropItemsOnGround = true;

  public TileDiamondSpikes(BlockPos pos, BlockState state) {
    super(TileRegistry.SPIKES_DIAMOND.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileDiamondSpikes e) {
    e.tick();
  }

  public void tick() {
    if (timer > 0) {
      timer--;
      return;
    }
    timer = level.getRandom().nextInt(24) + 12;
    if (fakePlayer == null && level instanceof ServerLevel) {
      fakePlayer = setupBeforeTrigger((ServerLevel) level, "spikes_diamond");
      if (fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);

        fakePlayer.get().setItemInHand(InteractionHand.MAIN_HAND, sword);
      }
      if (level.getRandom().nextDouble() < 0.001F) {
        tryDumpFakePlayerInvo(fakePlayer, null, dropItemsOnGround);
      }
    }
  }

  @Override
  public void setField(int field, int value) {
  }

  @Override
  public int getField(int field) {
    return 0;
  }
}
