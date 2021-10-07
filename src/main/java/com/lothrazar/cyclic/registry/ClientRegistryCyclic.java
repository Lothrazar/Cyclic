package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.block.conveyor.ConveyorItemRenderer;
import com.lothrazar.cyclic.block.peatfarm.RenderPeatFarm;
import com.lothrazar.cyclic.block.shapebuilder.RenderStructure;
import com.lothrazar.cyclic.event.ClientInputEvents;
import com.lothrazar.cyclic.event.EventRender;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
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

  @SubscribeEvent
  public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    //    import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
    BlockEntityRendererProvider.Context lol; //  is required in constructor
    event.registerBlockEntityRenderer(TileRegistry.PEAT_FARM, RenderPeatFarm::new);
    event.registerBlockEntityRenderer(TileRegistry.STRUCTURE, RenderStructure::new);
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
  public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(EntityRegistry.snowbolt, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.boomerang_stun, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.boomerang_carry, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.boomerang_damage, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.NETBALL, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.torchbolt, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.DUNGEON, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.eye, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.fire_bolt, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.stone_bolt, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.lightningbolt, ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.conveyor_item, ConveyorItemRenderer::new);
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void registerModels(FMLClientSetupEvent event) {
  }
}
