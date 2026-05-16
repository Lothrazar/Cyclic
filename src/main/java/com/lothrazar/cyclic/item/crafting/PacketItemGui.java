package com.lothrazar.cyclic.item.crafting;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.crafting.simple.CraftingStickContainer;
import com.lothrazar.cyclic.item.crafting.simple.CraftingStickContainerProvider;
import com.lothrazar.cyclic.item.lunchbox.ContainerProviderLunchbox;
import com.lothrazar.cyclic.item.lunchbox.ItemLunchbox;
import com.lothrazar.cyclic.item.storagebag.ContainerStorageBag;
import com.lothrazar.cyclic.item.storagebag.StorageBagContainerProvider;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketItemGui implements CustomPacketPayload {

  public static final Type<PacketItemGui> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "packet_item_gui"));

  public static final StreamCodec<FriendlyByteBuf, PacketItemGui> STREAM_CODEC = StreamCodec.ofMember(
      PacketItemGui::write,
      PacketItemGui::new
  );

  public int slot;
  public Item item;

  public PacketItemGui(int slot, Item item) {
    this.slot = slot;
    this.item = item;
  }

  public PacketItemGui(FriendlyByteBuf buf) {
    this.slot = buf.readInt();
    ResourceLocation rl = buf.readResourceLocation();
    Item read = BuiltInRegistries.ITEM.get(rl);
    this.item = read == null ? Items.AIR : read;
  }

  public void write(FriendlyByteBuf buf) {
    buf.writeInt(slot);
    ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
    buf.writeResourceLocation(key);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return ID;
  }

  public static void handle(PacketItemGui message, IPayloadContext context) {
    context.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) context.player();
      if (player == null) {
        return;
      }
      if (message.item == ItemRegistry.LUNCHBOX.get()) {
        ItemStack itemFoodMouse = player.containerMenu.getCarried();
        if (itemFoodMouse.isEmpty()) {
          player.openMenu(new ContainerProviderLunchbox(), buf -> buf.writeInt(message.slot));
        }
        else if (itemFoodMouse.getFoodProperties(player) != null) {
          ItemStack lunchbox = player.getInventory().getItem(message.slot);
          ItemLunchbox.insertFoodIntoLunchbox(lunchbox, itemFoodMouse, player);
        }
      }
      else if (message.item == ItemRegistry.STORAGE_BAG.get() && !(player.containerMenu instanceof ContainerStorageBag)) {
        player.openMenu(new StorageBagContainerProvider(message.slot), buf -> buf.writeInt(message.slot));
      }
      else if (message.item == ItemRegistry.CRAFTING_BAG.get() && !(player.containerMenu instanceof CraftingBagContainer)) {
        player.openMenu(new CraftingBagContainerProvider(message.slot), buf -> buf.writeInt(message.slot));
      }
      else if (message.item == ItemRegistry.CRAFTING_STICK.get() && !(player.containerMenu instanceof CraftingStickContainer)) {
        player.openMenu(new CraftingStickContainerProvider(message.slot), buf -> buf.writeInt(message.slot));
      }
    });
  }
}
