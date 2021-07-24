package com.lothrazar.cyclic.block.spikes;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import cofh.lib.util.helpers.MathHelper;

public class TileDiamondSpikes extends TileEntityBase implements ITickableTileEntity {

  WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;

  public TileDiamondSpikes() {
    super(TileRegistry.spikes_diamond);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    if (uuid != null) {
      tag.putUniqueId("uuid", uuid);
    }
    return super.write(tag);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    if (tag.contains("uuid")) {
      uuid = tag.getUniqueId("uuid");
    }
    super.read(bs, tag);
  }

  @Override
  public void tick() {
    if (timer > 0) {
      timer--;
      return;
    }
    timer = MathHelper.nextInt(world.rand, 12, 36);
    if (fakePlayer == null && world instanceof ServerWorld) {
      if (uuid == null) {
        uuid = UUID.randomUUID();
      }
      fakePlayer = setupBeforeTrigger((ServerWorld) world, "spikes_diamond", uuid);
      if (fakePlayer.get().getHeldItem(Hand.MAIN_HAND).isEmpty()) {
        Map<Enchantment, Integer> map = Maps.newHashMap();
        map.put(Enchantments.BANE_OF_ARTHROPODS, 2);
        map.put(Enchantments.SWEEPING, 3);
        map.put(Enchantments.SHARPNESS, 1);
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        EnchantmentHelper.setEnchantments(map, sword);
        fakePlayer.get().setHeldItem(Hand.MAIN_HAND, sword);
      }
      if (world.rand.nextDouble() < 0.001F) {
        tryDumpFakePlayerInvo(fakePlayer, false);
      }
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
