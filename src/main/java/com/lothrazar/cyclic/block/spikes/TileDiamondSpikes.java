package com.lothrazar.cyclic.block.spikes;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;

public class TileDiamondSpikes extends TileEntityBase implements TickableBlockEntity {

  WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;

  public TileDiamondSpikes() {
    super(TileRegistry.spikes_diamond);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    if (uuid != null) {
      tag.putUUID("uuid", uuid);
    }
    return super.save(tag);
  }

  @Override
  public void load(BlockState bs, CompoundTag tag) {
    if (tag.contains("uuid")) {
      uuid = tag.getUUID("uuid");
    }
    super.load(bs, tag);
  }

  @Override
  public void tick() {
    if (timer > 0) {
      timer--;
      return;
    }
    timer = level.random.nextInt(24) + 12;
    if (fakePlayer == null && level instanceof ServerLevel) {
      if (uuid == null) {
        uuid = UUID.randomUUID();
      }
      fakePlayer = setupBeforeTrigger((ServerLevel) level, "spikes_diamond", uuid);
      if (fakePlayer.get().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
        Map<Enchantment, Integer> map = Maps.newHashMap();
        map.put(Enchantments.BANE_OF_ARTHROPODS, 2);
        map.put(Enchantments.SWEEPING_EDGE, 3);
        map.put(Enchantments.SHARPNESS, 1);
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        EnchantmentHelper.setEnchantments(map, sword);
        fakePlayer.get().setItemInHand(InteractionHand.MAIN_HAND, sword);
      }
      if (level.random.nextDouble() < 0.001F) {
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
