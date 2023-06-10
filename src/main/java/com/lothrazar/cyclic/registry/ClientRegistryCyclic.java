package com.lothrazar.cyclic.registry;

import org.lwjgl.glfw.GLFW;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.antipotion.RenderBeaconAnti;
import com.lothrazar.cyclic.block.beaconpotion.RenderBeaconPotion;
import com.lothrazar.cyclic.block.beaconredstone.RenderBeaconRedstone;
import com.lothrazar.cyclic.block.collectfluid.RenderFluidCollect;
import com.lothrazar.cyclic.block.collectitem.RenderItemCollect;
import com.lothrazar.cyclic.block.conveyor.ConveyorItemRenderer;
import com.lothrazar.cyclic.block.detectorentity.RenderDetector;
import com.lothrazar.cyclic.block.detectoritem.RenderDetectorItem;
import com.lothrazar.cyclic.block.dropper.RenderDropper;
import com.lothrazar.cyclic.block.enderitemshelf.ItemShelfRenderer;
import com.lothrazar.cyclic.block.endershelf.EnderShelfRenderer;
import com.lothrazar.cyclic.block.fishing.RenderFisher;
import com.lothrazar.cyclic.block.forester.RenderForester;
import com.lothrazar.cyclic.block.harvester.RenderHarvester;
import com.lothrazar.cyclic.block.laser.RenderLaser;
import com.lothrazar.cyclic.block.lightcompr.RenderLightCamo;
import com.lothrazar.cyclic.block.melter.RenderMelter;
import com.lothrazar.cyclic.block.miner.RenderMiner;
import com.lothrazar.cyclic.block.peatfarm.RenderPeatFarm;
import com.lothrazar.cyclic.block.screen.RenderScreentext;
import com.lothrazar.cyclic.block.shapebuilder.RenderStructure;
import com.lothrazar.cyclic.block.shapedata.RenderShapedata;
import com.lothrazar.cyclic.block.solidifier.RenderSolidifier;
import com.lothrazar.cyclic.block.soundmuff.ghost.SoundmuffRender;
import com.lothrazar.cyclic.block.sprinkler.RenderSprinkler;
import com.lothrazar.cyclic.block.tank.RenderTank;
import com.lothrazar.cyclic.block.wireless.redstone.RenderTransmit;
import com.lothrazar.cyclic.capabilities.ClientDataManager;
import com.lothrazar.cyclic.event.ClientInputEvents;
import com.lothrazar.cyclic.event.EventRender;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.equipment.ShieldCyclicItem;
import com.lothrazar.cyclic.item.lunchbox.ItemLunchbox;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistryCyclic {

  //TODO: refactor split into keyboard registry, overlay registry, other renderers below 
  public static final KeyMapping CAKE = new KeyMapping("key." + ModCyclic.MODID + ".cake", new IKeyConflictContext() {

    @Override
    public boolean isActive() {
      //client side cant know when active. stored on server player file 
      //maybe when no gui is open 
      return true;
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
      return this == other || KeyConflictContext.IN_GAME == other;
    }
  }, InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_X), "key." + ModCyclic.MODID + ".category");
  //IIngameOverlay
  public static final IGuiOverlay HUD_MANA = (gui, poseStack, partialTicks, width, height) -> {
    //cancel if turned off
    if (!FeatureRegistry.PLAYER_RENDER_CAPS) {
      return;
    }
    //ok go
    if (Minecraft.getInstance().player.getMainHandItem().is(ItemRegistry.BATTERY_INFINITE.get())) {
      final String toDisplay = "P:" + ClientDataManager.getPlayerMana() + " CH:" + ClientDataManager.getChunkMana();
      int x = 10; // ManaConfig.MANA_HUD_X.get();
      int y = 10; // ManaConfig.MANA_HUD_Y.get(); //TODO: client-config
      if (x >= 0 && y >= 0) {
        gui.getFont().draw(poseStack, toDisplay, x, y, 0xFF0000); // client config color
      }
    }
  };

  public ClientRegistryCyclic() {
    //fired by mod constructor  DistExecutor.safeRunForDist
    MinecraftForge.EVENT_BUS.register(new ClientInputEvents());
    MinecraftForge.EVENT_BUS.register(new EventRender());
  }

  public static void setupClient(final FMLClientSetupEvent event) {
    for (BlockCyclic b : BlockRegistry.BLOCKSCLIENTREGISTRY) {
      b.registerClient();
    }
    for (ItemBaseCyclic i : ItemRegistry.ITEMSFIXME) {
      i.registerClient();
    }
    initShields();
  }
  // see /resources/assets/cyclic/assets/
  //  @SubscribeEvent
  //  public static void onStitch(TextureStitchEvent.Pre event) {
  //    SpriteSourceProvider bitch;
  //    if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_WOOD.texture());
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_WOOD_NOPATTERN.texture());
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_LEATHER.texture());
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_LEATHER_NOPATTERN.texture());
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_FLINT.texture());
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_FLINT_NOPATTERN.texture());
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_BONE.texture());
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_BONE_NOPATTERN.texture());
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_OBSIDIAN.texture());
  //      event.addSprite(MaterialShieldRegistry.SHIELD_BASE_OBSIDIAN_NOPATTERN.texture());
  //    }
  //  }

  @SubscribeEvent
  public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(TileRegistry.PEAT_FARM.get(), RenderPeatFarm::new);
    event.registerBlockEntityRenderer(TileRegistry.STRUCTURE.get(), RenderStructure::new);
    event.registerBlockEntityRenderer(TileRegistry.COLLECTOR_FLUID.get(), RenderFluidCollect::new);
    event.registerBlockEntityRenderer(TileRegistry.COLLECTOR.get(), RenderItemCollect::new);
    event.registerBlockEntityRenderer(TileRegistry.DETECTOR_ENTITY.get(), RenderDetector::new);
    event.registerBlockEntityRenderer(TileRegistry.DETECTOR_ITEM.get(), RenderDetectorItem::new);
    event.registerBlockEntityRenderer(TileRegistry.DROPPER.get(), RenderDropper::new);
    event.registerBlockEntityRenderer(TileRegistry.ENDER_ITEM_SHELF.get(), ItemShelfRenderer::new);
    event.registerBlockEntityRenderer(TileRegistry.ENDER_SHELF.get(), EnderShelfRenderer::new);
    event.registerBlockEntityRenderer(TileRegistry.FISHER.get(), RenderFisher::new);
    event.registerBlockEntityRenderer(TileRegistry.FORESTER.get(), RenderForester::new);
    event.registerBlockEntityRenderer(TileRegistry.HARVESTER.get(), RenderHarvester::new);
    event.registerBlockEntityRenderer(TileRegistry.LASER.get(), RenderLaser::new);
    event.registerBlockEntityRenderer(TileRegistry.LIGHT_CAMO.get(), RenderLightCamo::new);
    event.registerBlockEntityRenderer(TileRegistry.MELTER.get(), RenderMelter::new);
    event.registerBlockEntityRenderer(TileRegistry.MINER.get(), RenderMiner::new);
    event.registerBlockEntityRenderer(TileRegistry.SCREEN.get(), RenderScreentext::new);
    event.registerBlockEntityRenderer(TileRegistry.COMPUTER_SHAPE.get(), RenderShapedata::new);
    event.registerBlockEntityRenderer(TileRegistry.SOLIDIFIER.get(), RenderSolidifier::new);
    event.registerBlockEntityRenderer(TileRegistry.SOUNDPROOFING_GHOST.get(), SoundmuffRender::new);
    event.registerBlockEntityRenderer(TileRegistry.SPRINKLER.get(), RenderSprinkler::new);
    event.registerBlockEntityRenderer(TileRegistry.TANK.get(), RenderTank::new);
    event.registerBlockEntityRenderer(TileRegistry.WIRELESS_TRANSMITTER.get(), RenderTransmit::new);
    event.registerBlockEntityRenderer(TileRegistry.BEACON.get(), RenderBeaconPotion::new);
    event.registerBlockEntityRenderer(TileRegistry.ANTI_BEACON.get(), RenderBeaconAnti::new);
    event.registerBlockEntityRenderer(TileRegistry.BEACON_REDSTONE.get(), RenderBeaconRedstone::new);
  }

  @SuppressWarnings("deprecation") //shield itemproperty
  private static void initShields() {
    //this matches up with ShieldCyclicItem where it calls startUsingItem() inside of use()
    net.minecraft.client.renderer.item.ItemPropertyFunction blockFn = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
    ItemProperties.register(ItemRegistry.SHIELD_WOOD.get(), ShieldCyclicItem.BLOCKING, blockFn);
    ItemProperties.register(ItemRegistry.SHIELD_LEATHER.get(), ShieldCyclicItem.BLOCKING, blockFn);
    ItemProperties.register(ItemRegistry.SHIELD_FLINT.get(), ShieldCyclicItem.BLOCKING, blockFn);
    ItemProperties.register(ItemRegistry.SHIELD_BONE.get(), ShieldCyclicItem.BLOCKING, blockFn);
    ItemProperties.register(ItemRegistry.SHIELD_OBSIDIAN.get(), ShieldCyclicItem.BLOCKING, blockFn);
  }

  //    OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "data", HUD_MANA);
  @SubscribeEvent
  public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
    event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), ModCyclic.MODID, HUD_MANA);
  }

  @SubscribeEvent
  public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
    //    net.minecraftforge.client.ClientRegistry.registerKeyBinding(CAKE);
    event.register(CAKE);
  }
  //  @OnlyIn(Dist.CLIENT)
  //  @SubscribeEvent
  //  public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
  //    List<Block> blocks = new ArrayList<>();
  //    event.register((state, worldIn, pos, tintIndex) -> {
  //      return 0;
  //    }, blocks.toArray(new Block[0]));
  //  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
    event.register((stack, tintIndex) -> {
      if (tintIndex == 0) { //layer zero is outline, ignore this 
        return 0xFFFFFFFF;
      }
      //layer 1 is overlay  
      return ItemLunchbox.getColour(stack);
    }, ItemRegistry.LUNCHBOX.get());
    //
    event.register((stack, tintIndex) -> {
      if (tintIndex == 0) { //layer zero is outline, ignore this 
        return 0xFFFFFFFF;
      }
      return ItemStorageBag.getColour(stack);
    }, ItemRegistry.STORAGE_BAG.get());
    //
    event.register((stack, tintIndex) -> {
      if (stack.hasTag() && tintIndex > 0) {
        //what entity is inside
        EntityType<?> thing = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)));
        //pull the colours from the egg
        for (SpawnEggItem spawneggitem : SpawnEggItem.eggs()) {
          if (spawneggitem.getType(null) == thing) {
            return spawneggitem.getColor(tintIndex - 1);
          }
        }
      }
      return 0xFFFFFFFF;
    }, ItemRegistry.MOB_CONTAINER.get());
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(EntityRegistry.SNOW_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.BOOMERANG_STUN.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.BOOMERANG_CARRY.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.BOOMERANG_DAMAGE.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.MAGIC_NET.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.TORCH_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.DUNGEON.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.EYE.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.FIRE_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.DARKFIRE_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.ENDER_FISHING.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.STONE_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.LASER_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.LIGHTNING_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.MAGIC_MISSILE.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.CONVEYOR_ITEM.get(), ConveyorItemRenderer::new);
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void registerModels(FMLClientSetupEvent event) {}
}
