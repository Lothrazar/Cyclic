package com.lothrazar.cyclic.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
// import com.lothrazar.cyclic.block.antipotion.RenderBeaconAnti;
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
import com.lothrazar.cyclic.block.facade.RenderCableFacade;
import com.lothrazar.cyclic.block.facade.light.RenderLightFacade;
import com.lothrazar.cyclic.block.facade.soundmuff.SoundmuffRenderFacade;
import com.lothrazar.cyclic.block.fan.RenderFan;
import com.lothrazar.cyclic.block.fishing.RenderFisher;
import com.lothrazar.cyclic.block.forester.RenderForester;
import com.lothrazar.cyclic.block.harvester.RenderHarvester;
import com.lothrazar.cyclic.block.laser.RenderLaser;
import com.lothrazar.cyclic.block.melter.RenderMelter;
import com.lothrazar.cyclic.block.miner.RenderMiner;
import com.lothrazar.cyclic.block.peatfarm.RenderPeatFarm;
import com.lothrazar.cyclic.block.screen.RenderScreentext;
import com.lothrazar.cyclic.block.shapebuilder.RenderStructure;
import com.lothrazar.cyclic.block.shapedata.RenderShapedata;
import com.lothrazar.cyclic.block.solidifier.RenderSolidifier;
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


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = ModCyclic.MODID)
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
//  public static final IGuiOverlay HUD_MANA = (gui, poseStack, partialTicks, width, height) -> {
//    //cancel if turned off
//    if (!FeatureRegistry.PLAYER_RENDER_CAPS) {
//      return;
//    }
//    //ok go
//    if (Minecraft.getInstance().player.getMainHandItem().is(ItemRegistry.BATTERY_INFINITE.get())) {
//      final String toDisplay = "P:" + ClientDataManager.getPlayerMana() + " CH:" + ClientDataManager.getChunkMana();
//      int x = 10; // ManaConfig.MANA_HUD_X.get();
//      int y = 10; // ManaConfig.MANA_HUD_Y.get(); //TODO: client-config
//      if (x >= 0 && y >= 0) {
//        poseStack.drawString(gui.getFont(), toDisplay, x, y, 0xFF0000);
//        //        gui.getFont().draw(poseStack, toDisplay, x, y, 0xFF0000); // client config color
//      }
//    }
//  };

  public ClientRegistryCyclic() {
    //fired by mod constructor  DistExecutor.safeRunForDist
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
    event.registerBlockEntityRenderer(TileRegistry.FAN.get(), RenderFan::new);
    event.registerBlockEntityRenderer(TileRegistry.ENDER_SHELF.get(), EnderShelfRenderer::new);
    event.registerBlockEntityRenderer(TileRegistry.FISHER.get(), RenderFisher::new);
    event.registerBlockEntityRenderer(TileRegistry.FORESTER.get(), RenderForester::new);
    event.registerBlockEntityRenderer(TileRegistry.HARVESTER.get(), RenderHarvester::new);
    event.registerBlockEntityRenderer(TileRegistry.LASER.get(), RenderLaser::new);
    event.registerBlockEntityRenderer(TileRegistry.LIGHT_CAMO.get(), RenderLightFacade::new);
    event.registerBlockEntityRenderer(TileRegistry.MELTER.get(), RenderMelter::new);
    event.registerBlockEntityRenderer(TileRegistry.MINER.get(), RenderMiner::new);
    event.registerBlockEntityRenderer(TileRegistry.SCREEN.get(), RenderScreentext::new);
    event.registerBlockEntityRenderer(TileRegistry.COMPUTER_SHAPE.get(), RenderShapedata::new);
    event.registerBlockEntityRenderer(TileRegistry.SOLIDIFIER.get(), RenderSolidifier::new);
    event.registerBlockEntityRenderer(TileRegistry.SOUNDPROOFING_GHOST.get(), SoundmuffRenderFacade::new);
    event.registerBlockEntityRenderer(TileRegistry.SPRINKLER.get(), RenderSprinkler::new);
    event.registerBlockEntityRenderer(TileRegistry.TANK.get(), RenderTank::new);
    event.registerBlockEntityRenderer(TileRegistry.WIRELESS_TRANSMITTER.get(), RenderTransmit::new);
    event.registerBlockEntityRenderer(TileRegistry.BEACON.get(), RenderBeaconPotion::new);
//    event.registerBlockEntityRenderer(TileRegistry.ANTI_BEACON.get(), RenderBeaconAnti::new);
    event.registerBlockEntityRenderer(TileRegistry.BEACON_REDSTONE.get(), RenderBeaconRedstone::new);
    //cable renderers
    event.registerBlockEntityRenderer(TileRegistry.ENERGY_PIPE.get(), RenderCableFacade::new);
    event.registerBlockEntityRenderer(TileRegistry.ITEM_PIPE.get(), RenderCableFacade::new);
    event.registerBlockEntityRenderer(TileRegistry.FLUID_PIPE.get(), RenderCableFacade::new);
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
//  @SubscribeEvent
//  public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
//    event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), ModCyclic.MODID, HUD_MANA);
//  }

  @SubscribeEvent
  public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
    //    net.neoforged.neoforge.client.ClientRegistry.registerKeyBinding(CAKE);
    event.register(CAKE);
  }

  @SubscribeEvent
  public static void onRegisterMenuScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
    event.register(MenuTypeRegistry.STORAGE_BAG.get(), com.lothrazar.cyclic.item.storagebag.ScreenStorageBag::new);
    event.register(MenuTypeRegistry.CRAFTING_BAG.get(), com.lothrazar.cyclic.item.crafting.CraftingBagScreen::new);
    event.register(MenuTypeRegistry.CRAFTING_STICK.get(), com.lothrazar.cyclic.item.crafting.simple.CraftingStickScreen::new);
    event.register(MenuTypeRegistry.FILTER_DATA.get(), com.lothrazar.cyclic.item.datacard.filter.ScreenFilterCard::new);
    event.register(MenuTypeRegistry.DROPPER.get(), com.lothrazar.cyclic.block.dropper.ScreenDropper::new);
    event.register(MenuTypeRegistry.FISHER.get(), com.lothrazar.cyclic.block.fishing.ScreenFisher::new);
    event.register(MenuTypeRegistry.DETECTOR_ITEM.get(), com.lothrazar.cyclic.block.detectoritem.ScreenDetectorItem::new);
    event.register(MenuTypeRegistry.GENERATOR_DROPS.get(), com.lothrazar.cyclic.block.generatoritem.ScreenGeneratorDrops::new);
    event.register(MenuTypeRegistry.BREAKER.get(), com.lothrazar.cyclic.block.breaker.ScreenBreaker::new);
    event.register(MenuTypeRegistry.GENERATOR_FOOD.get(), com.lothrazar.cyclic.block.generatorfood.ScreenGeneratorFood::new);
    event.register(MenuTypeRegistry.SOLIDIFIER.get(), com.lothrazar.cyclic.block.solidifier.ScreenSolidifier::new);
    event.register(MenuTypeRegistry.HARVESTER.get(), com.lothrazar.cyclic.block.harvester.ScreenHarvester::new);
    event.register(MenuTypeRegistry.DETECTOR_ENTITY.get(), com.lothrazar.cyclic.block.detectorentity.ScreenDetector::new);
    event.register(MenuTypeRegistry.STRUCTURE.get(), com.lothrazar.cyclic.block.shapebuilder.ScreenStructure::new);
    event.register(MenuTypeRegistry.CRATE_MINI.get(), com.lothrazar.cyclic.block.cratemini.ScreenCrateMini::new);
    event.register(MenuTypeRegistry.COLLECTOR_FLUID.get(), com.lothrazar.cyclic.block.collectfluid.ScreenFluidCollect::new);
    event.register(MenuTypeRegistry.CRUSHER.get(), com.lothrazar.cyclic.block.crusher.ScreenCrusher::new);
    event.register(MenuTypeRegistry.PLACER.get(), com.lothrazar.cyclic.block.placer.ScreenPlacer::new);
    event.register(MenuTypeRegistry.MELTER.get(), com.lothrazar.cyclic.block.melter.ScreenMelter::new);
    event.register(MenuTypeRegistry.ANVIL_VOID.get(), com.lothrazar.cyclic.block.anvilvoid.ScreenAnvilVoid::new);
    event.register(MenuTypeRegistry.TELEPORT.get(), com.lothrazar.cyclic.block.tp.ScreenTeleport::new);
    event.register(MenuTypeRegistry.USER.get(), com.lothrazar.cyclic.block.user.ScreenUser::new);
    event.register(MenuTypeRegistry.EXPERIENCE_PYLON.get(), com.lothrazar.cyclic.block.expcollect.ScreenExpPylon::new);
    event.register(MenuTypeRegistry.UNCRAFTER.get(), com.lothrazar.cyclic.block.uncrafter.ScreenUncraft::new);
    event.register(MenuTypeRegistry.GENERATOR_FLUID.get(), com.lothrazar.cyclic.block.generatorfluid.ScreenGeneratorFluid::new);
    event.register(MenuTypeRegistry.DISENCHANTER.get(), com.lothrazar.cyclic.block.disenchant.ScreenDisenchant::new);
    event.register(MenuTypeRegistry.LASER.get(), com.lothrazar.cyclic.block.laser.ScreenLaser::new);
    event.register(MenuTypeRegistry.GENERATOR_FUEL.get(), com.lothrazar.cyclic.block.generatorfuel.ScreenGeneratorFuel::new);
    event.register(MenuTypeRegistry.CRATE.get(), com.lothrazar.cyclic.block.crate.ScreenCrate::new);
    event.register(MenuTypeRegistry.WORKBENCH.get(), com.lothrazar.cyclic.block.workbench.ScreenWorkbench::new);
    event.register(MenuTypeRegistry.SOUND_RECORDER.get(), com.lothrazar.cyclic.block.soundrecord.ScreenSoundRecorder::new);
    event.register(MenuTypeRegistry.FLUID_PIPE.get(), com.lothrazar.cyclic.block.cable.fluid.ScreenCableFluid::new);
    event.register(MenuTypeRegistry.ITEM_PIPE.get(), com.lothrazar.cyclic.block.cable.item.ScreenCableItem::new);
    event.register(MenuTypeRegistry.WIRELESS_ENERGY.get(), com.lothrazar.cyclic.block.wireless.energy.ScreenWirelessEnergy::new);
    event.register(MenuTypeRegistry.WIRELESS_TRANSMITTER.get(), com.lothrazar.cyclic.block.wireless.redstone.ScreenTransmit::new);
    event.register(MenuTypeRegistry.WIRELESS_FLUID.get(), com.lothrazar.cyclic.block.wireless.fluid.ScreenWirelessFluid::new);
    event.register(MenuTypeRegistry.WIRELESS_ITEM.get(), com.lothrazar.cyclic.block.wireless.item.ScreenWirelessItem::new);
    event.register(MenuTypeRegistry.SCREEN.get(), com.lothrazar.cyclic.block.screen.ScreenScreentext::new);
    event.register(MenuTypeRegistry.PACKAGER.get(), com.lothrazar.cyclic.block.packager.ScreenPackager::new);
    event.register(MenuTypeRegistry.FORESTER.get(), com.lothrazar.cyclic.block.forester.ScreenForester::new);
    event.register(MenuTypeRegistry.FAN.get(), com.lothrazar.cyclic.block.fan.ScreenFan::new);
    event.register(MenuTypeRegistry.ANVIL_MAGMA.get(), com.lothrazar.cyclic.block.anvilmagma.ScreenAnvilMagma::new);
    event.register(MenuTypeRegistry.CRAFTER.get(), com.lothrazar.cyclic.block.crafter.ScreenCrafter::new);
    event.register(MenuTypeRegistry.BATTERY.get(), com.lothrazar.cyclic.block.battery.ScreenBattery::new);
    event.register(MenuTypeRegistry.COLLECTOR.get(), com.lothrazar.cyclic.block.collectitem.ScreenItemCollector::new);
    event.register(MenuTypeRegistry.BEACON.get(), com.lothrazar.cyclic.block.beaconpotion.ScreenPotion::new);
    event.register(MenuTypeRegistry.ANVIL.get(), com.lothrazar.cyclic.block.anvil.ScreenAnvil::new);
    event.register(MenuTypeRegistry.PEAT_FARM.get(), com.lothrazar.cyclic.block.peatfarm.ScreenPeatFarm::new);
    event.register(MenuTypeRegistry.PLACER_FLUID.get(), com.lothrazar.cyclic.block.placerfluid.ScreenPlacerFluid::new);
    event.register(MenuTypeRegistry.BATTERY_CLAY.get(), com.lothrazar.cyclic.block.batteryclay.ScreenClayBattery::new);
    event.register(MenuTypeRegistry.CLOCK.get(), com.lothrazar.cyclic.block.clock.ScreenClock::new);
    event.register(MenuTypeRegistry.GENERATOR_SOLAR.get(), com.lothrazar.cyclic.block.generatorsolar.ScreenGeneratorSolar::new);
    event.register(MenuTypeRegistry.SOUND_PLAYER.get(), com.lothrazar.cyclic.block.soundplay.ScreenSoundPlayer::new);
    event.register(MenuTypeRegistry.COMPUTER_SHAPE.get(), com.lothrazar.cyclic.block.shapedata.ScreenShapedata::new);
    event.register(MenuTypeRegistry.MINER.get(), com.lothrazar.cyclic.block.miner.ScreenMiner::new);
  }

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
      if (stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA) && tintIndex > 0) {
        //what entity is inside
        EntityType<?> thing = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)));
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
    event.registerEntityRenderer(EntityRegistry.LIGHTNING_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.MAGIC_MISSILE.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.CONVEYOR_ITEM.get(), ConveyorItemRenderer::new);
  }
}
