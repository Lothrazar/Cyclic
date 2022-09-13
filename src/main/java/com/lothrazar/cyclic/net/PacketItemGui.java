package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.item.crafting.CraftingBagContainer;
import com.lothrazar.cyclic.item.crafting.CraftingBagContainerProvider;
import com.lothrazar.cyclic.item.craftingsimple.CraftingStickContainer;
import com.lothrazar.cyclic.item.craftingsimple.CraftingStickContainerProvider;
import com.lothrazar.cyclic.item.storagebag.ContainerStorageBag;
import com.lothrazar.cyclic.item.storagebag.StorageBagContainerProvider;
import com.lothrazar.cyclic.registry.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public class PacketItemGui extends PacketBase {

  private int slot;
  private Item item;

  public PacketItemGui(int slot, Item item) {
    this.slot = slot;
    this.item = item;
  }

  public static void handle(PacketItemGui message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      if (message.item == ItemRegistry.storage_bag && (player.openContainer instanceof ContainerStorageBag) == false) {
        NetworkHooks.openGui(player, new StorageBagContainerProvider(), player.getPosition());
      }
      else if (message.item == ItemRegistry.crafting_bag && (player.openContainer instanceof CraftingBagContainer) == false) {
        NetworkHooks.openGui(player, new CraftingBagContainerProvider(message.slot), buf -> buf.writeInt(message.slot));
      }
      else if (message.item == ItemRegistry.crafting_stick && (player.openContainer instanceof CraftingStickContainer) == false) {
        NetworkHooks.openGui(player, new CraftingStickContainerProvider(), player.getPosition());
      }
      //
      //
    });
    message.done(ctx);
  }

  public static PacketItemGui decode(PacketBuffer buf) {
    PacketItemGui p = new PacketItemGui(buf.readInt(), buf.readItemStack().getItem());
    return p;
  }

  public static void encode(PacketItemGui msg, PacketBuffer buf) {
    buf.writeInt(msg.slot);
    buf.writeItemStack(new ItemStack(msg.item));
  }
}
