package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.block.conveyor.ConveyorItemRenderer;
import com.lothrazar.cyclic.event.ClientInputEvents;
import com.lothrazar.cyclic.event.EventRender;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistryCyclic {

  public static KeyMapping CAKE;

  public ClientRegistryCyclic() {
    //fired by mod constructor  DistExecutor.safeRunForDist
    MinecraftForge.EVENT_BUS.register(new ClientInputEvents());
    MinecraftForge.EVENT_BUS.register(new EventRender());
  }

  public static void setupClient(final FMLClientSetupEvent event) {
    for (BlockBase b : BlockRegistry.blocksClientRegistry) {
      b.registerClient();
    }
    for (ItemBase i : ItemRegistry.items) {
      i.registerClient();
    }
    initColours();
    initKeybindings();
  }

  private static void initKeybindings() {
    CAKE = new KeyMapping("key." + ModCyclic.MODID + ".cake", new IKeyConflictContext() {

      @Override
      public boolean isActive() {
        //client side cant know when active. stored on server player file 
        //maybe when no gui is open
        Player player = Minecraft.getInstance().player;
        ModCyclic.LOGGER.info("only active when this is null? " + player.containerMenu);
        return true;
      }

      @Override
      public boolean conflicts(IKeyConflictContext other) {
        return this == other || KeyConflictContext.IN_GAME == other;
      }
    }, InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_X), "key." + ModCyclic.MODID + ".category");
    ClientRegistry.registerKeyBinding(CAKE);
  }

  @OnlyIn(Dist.CLIENT)
  private static void initColours() {
    Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
      if (stack.getItem() == ItemRegistry.storage_bag) {
        // ok
        if (tintIndex == 0) { //layer zero is outline, ignore this 
          return 0xFFFFFFFF;
        }
        //layer 1 is overlay  
        int c = ItemStorageBag.getColour(stack);
        return c;
      }
      else if (stack.getItem() == ItemRegistry.mob_container) {
        if (stack.hasTag() && tintIndex > 0) {
          //what entity is inside
          EntityType<?> thing = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)));
          //pull the colours from the egg
          for (SpawnEggItem spawneggitem : SpawnEggItem.eggs()) {
            if (spawneggitem.getType(null) == thing) {
              return spawneggitem.getColor(tintIndex - 1);
            }
          }
        }
      }
      return -1;
    }, ItemRegistry.mob_container, ItemRegistry.storage_bag);
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void registerModels(FMLClientSetupEvent event) {
    // TODO: build list in EntityRegistry and loop it here since they are same SpriteRends
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.snowbolt, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.lightningbolt, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.boomerang_stun, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.boomerang_carry, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.boomerang_damage, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.NETBALL, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.torchbolt, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.DUNGEON, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.eye, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.fire_bolt, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.conveyor_item, render -> new ConveyorItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.stone_bolt, render -> new ThrownItemRenderer<>(render, Minecraft.getInstance().getItemRenderer()));
  }
}
