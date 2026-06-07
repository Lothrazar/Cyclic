package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.util.RegistryHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import com.lothrazar.cyclic.render.SpinModelRenderer;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.minecraft.client.gui.LayeredDraw;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import com.lothrazar.cyclic.ModCyclic;
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
import com.lothrazar.cyclic.block.expfountain.RenderExperienceFountain;
import com.lothrazar.cyclic.block.sprinkler.RenderSprinkler;
import com.lothrazar.cyclic.block.tank.RenderTank;
import com.lothrazar.cyclic.block.wireless.redstone.RenderTransmit;
import com.lothrazar.cyclic.item.equipment.ShieldCyclicItem;
import com.lothrazar.cyclic.item.lunchbox.ScreenLunchbox;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import com.lothrazar.cyclic.render.ShieldBlockEntityWithoutLevelRenderer;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.fluid.FluidSlimeHolder;
import com.lothrazar.cyclic.fluid.FluidWaxHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.block.anvil.ScreenAnvil;
import com.lothrazar.cyclic.block.anvilmagma.ScreenAnvilMagma;
import com.lothrazar.cyclic.block.anvilvoid.ScreenAnvilVoid;
import com.lothrazar.cyclic.block.battery.ScreenBattery;
import com.lothrazar.cyclic.block.batteryclay.ScreenClayBattery;
import com.lothrazar.cyclic.block.beaconpotion.ScreenPotion;
import com.lothrazar.cyclic.block.breaker.ScreenBreaker;
import com.lothrazar.cyclic.block.cable.fluid.ScreenCableFluid;
import com.lothrazar.cyclic.block.cable.item.ScreenCableItem;
import com.lothrazar.cyclic.block.clock.ScreenClock;
import com.lothrazar.cyclic.block.collectfluid.ScreenFluidCollect;
import com.lothrazar.cyclic.block.collectitem.ScreenItemCollector;
import com.lothrazar.cyclic.block.magnet.ScreenMagnet;
import com.lothrazar.cyclic.block.crafter.ScreenCrafter;
import com.lothrazar.cyclic.block.crate.ScreenCrate;
import com.lothrazar.cyclic.block.cratemini.ScreenCrateMini;
import com.lothrazar.cyclic.block.crusher.ScreenCrusher;
import com.lothrazar.cyclic.block.detectorentity.ScreenDetector;
import com.lothrazar.cyclic.block.detectoritem.ScreenDetectorItem;
import com.lothrazar.cyclic.block.disenchant.ScreenDisenchant;
import com.lothrazar.cyclic.block.dropper.ScreenDropper;
import com.lothrazar.cyclic.block.expcollect.ScreenExpPylon;
import com.lothrazar.cyclic.block.fan.ScreenFan;
import com.lothrazar.cyclic.block.fishing.ScreenFisher;
import com.lothrazar.cyclic.block.forester.ScreenForester;
import com.lothrazar.cyclic.block.generatorfluid.ScreenGeneratorFluid;
import com.lothrazar.cyclic.block.generatorfood.ScreenGeneratorFood;
import com.lothrazar.cyclic.block.generatorfuel.ScreenGeneratorFuel;
import com.lothrazar.cyclic.block.generatoritem.ScreenGeneratorDrops;
import com.lothrazar.cyclic.block.generatorsolar.ScreenGeneratorSolar;
import com.lothrazar.cyclic.block.harvester.ScreenHarvester;
import com.lothrazar.cyclic.block.laser.ScreenLaser;
import com.lothrazar.cyclic.block.melter.ScreenMelter;
import com.lothrazar.cyclic.block.miner.ScreenMiner;
import com.lothrazar.cyclic.block.packager.ScreenPackager;
import com.lothrazar.cyclic.block.peatfarm.ScreenPeatFarm;
import com.lothrazar.cyclic.block.placer.ScreenPlacer;
import com.lothrazar.cyclic.block.placerfluid.ScreenPlacerFluid;
import com.lothrazar.cyclic.block.screen.ScreenScreentext;
import com.lothrazar.cyclic.block.shapebuilder.ScreenStructure;
import com.lothrazar.cyclic.block.shapedata.ScreenShapedata;
import com.lothrazar.cyclic.block.solidifier.ScreenSolidifier;
import com.lothrazar.cyclic.block.soundplay.ScreenSoundPlayer;
import com.lothrazar.cyclic.block.soundrecord.ScreenSoundRecorder;
import com.lothrazar.cyclic.block.tp.ScreenTeleport;
import com.lothrazar.cyclic.block.uncrafter.ScreenUncraft;
import com.lothrazar.cyclic.block.user.ScreenUser;
import com.lothrazar.cyclic.block.wireless.energy.ScreenWirelessEnergy;
import com.lothrazar.cyclic.block.wireless.fluid.ScreenWirelessFluid;
import com.lothrazar.cyclic.block.wireless.item.ScreenWirelessItem;
import com.lothrazar.cyclic.block.wireless.redstone.ScreenTransmit;
import com.lothrazar.cyclic.block.workbench.ScreenWorkbench;
import com.lothrazar.cyclic.item.crafting.CraftingBagScreen;
import com.lothrazar.cyclic.item.enderbook.EnderBookScreen;
import com.lothrazar.cyclic.item.crafting.simple.CraftingStickScreen;
import com.lothrazar.cyclic.item.datacard.filter.ScreenFilterCard;
import com.lothrazar.cyclic.item.storagebag.ScreenStorageBag;

@EventBusSubscriber(modid = ModCyclic.MODID, value = Dist.CLIENT)
public class ClientRegistryCyclic {

  @SubscribeEvent
  public static void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
    event.registerAbove(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID,"energy_hud"), ENERGY_HUD_LAYER);
  }
  /**
   *
   */
  public static final LayeredDraw.Layer ENERGY_HUD_LAYER = (guiGraphics, deltaTracker) -> {
    if (!ClientConfigCyclic.ENERGY_HUD.get()) {
      return;
    }
    LocalPlayer player = Minecraft.getInstance().player;
    var e = CapabilityUtil.energy(player.getMainHandItem());
    if(e != null) {

      final String toDisplay = e.getEnergyStored() + "/" + e.getMaxEnergyStored();
      int x = 10;
      int y = 10;
      int colour = 0xFF0000;
//      if (x >= 0 && y >= 0) {
        guiGraphics.drawString(Minecraft.getInstance().font, toDisplay, x, y, colour);
//      }
    }
  };

  @SubscribeEvent
  public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
    event.register(SpinModelRenderer.SPRINKLER_SPIN);
    event.register(SpinModelRenderer.FOUNTAIN_SPIN);
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
    event.registerBlockEntityRenderer(TileRegistry.SHELF.get(), ItemShelfRenderer::new);
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
    event.registerBlockEntityRenderer(TileRegistry.EXPERIENCE_FOUNTAIN.get(), RenderExperienceFountain::new);
    event.registerBlockEntityRenderer(TileRegistry.TANK.get(), RenderTank::new);
    event.registerBlockEntityRenderer(TileRegistry.WIRELESS_TRANSMITTER.get(), RenderTransmit::new);
    event.registerBlockEntityRenderer(TileRegistry.BEACON.get(), RenderBeaconPotion::new);
    event.registerBlockEntityRenderer(TileRegistry.BEACON_SPONGE.get(), RenderBeaconAnti::new);
    event.registerBlockEntityRenderer(TileRegistry.BEACON_REDSTONE.get(), RenderBeaconRedstone::new);
    //cable renderers
    event.registerBlockEntityRenderer(TileRegistry.ENERGY_PIPE.get(), RenderCableFacade::new);
    event.registerBlockEntityRenderer(TileRegistry.ITEM_PIPE.get(), RenderCableFacade::new);
    event.registerBlockEntityRenderer(TileRegistry.FLUID_PIPE.get(), RenderCableFacade::new);
  }

  public static void setupClient(final FMLClientSetupEvent event) {
    initShields();
    //provide a client-side HolderLookup.Provider for item caps that serialize component-aware
    //payloads (FluidStack, ItemStack handlers) so tooltips work over dedicated-server connections.
    initClientRegistries();
  }

  public static void initClientRegistries() {
    RegistryHolder.CLIENT_LOOKUP = () -> {
      var mc = net.minecraft.client.Minecraft.getInstance();
      if (mc.level != null) {
        return mc.level.registryAccess();
      }
      var conn = mc.getConnection();
      if (conn != null) {
        return conn.registryAccess();
      }
      return null;
    };
  }
  @SuppressWarnings("deprecation")
  private static void initShields() {
    //this matches up with ShieldCyclicItem where it calls startUsingItem() inside of use()
    ItemPropertyFunction blockFn = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
    ItemProperties.register(ItemRegistry.SHIELD_WOOD.get(), ShieldCyclicItem.BLOCKING, blockFn);
    ItemProperties.register(ItemRegistry.SHIELD_LEATHER.get(), ShieldCyclicItem.BLOCKING, blockFn);
    ItemProperties.register(ItemRegistry.SHIELD_FLINT.get(), ShieldCyclicItem.BLOCKING, blockFn);
    ItemProperties.register(ItemRegistry.SHIELD_BONE.get(), ShieldCyclicItem.BLOCKING, blockFn);
    ItemProperties.register(ItemRegistry.SHIELD_OBSIDIAN.get(), ShieldCyclicItem.BLOCKING, blockFn);
  }

  // the | 0xFF000000 is to force max alpha
  @SubscribeEvent
  public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
    event.registerFluidType(new IClientFluidTypeExtensions() {
      @Override public ResourceLocation getStillTexture() { return FluidXpJuiceHolder.FLUID_STILL; }
      @Override public ResourceLocation getFlowingTexture() { return FluidXpJuiceHolder.FLUID_FLOW; }
      @Override public int getTintColor() { return FluidXpJuiceHolder.COLOR | 0xFF000000; }
    }, FluidXpJuiceHolder.TYPE.get());
    event.registerFluidType(new IClientFluidTypeExtensions() {
      @Override public ResourceLocation getStillTexture() { return FluidMagmaHolder.FLUID_STILL; }
      @Override public ResourceLocation getFlowingTexture() { return FluidMagmaHolder.FLUID_FLOW; }
      @Override public int getTintColor() { return FluidMagmaHolder.COLOR | 0xFF000000; }
    }, FluidMagmaHolder.TYPE.get());
    event.registerFluidType(new IClientFluidTypeExtensions() {
      @Override public ResourceLocation getStillTexture() { return FluidSlimeHolder.FLUID_STILL; }
      @Override public ResourceLocation getFlowingTexture() { return FluidSlimeHolder.FLUID_FLOW; }
      @Override public int getTintColor() { return FluidSlimeHolder.COLOR | 0xFF000000; }
    }, FluidSlimeHolder.TYPE.get());
    event.registerFluidType(new IClientFluidTypeExtensions() {
      @Override public ResourceLocation getStillTexture() { return FluidWaxHolder.FLUID_STILL; }
      @Override public ResourceLocation getFlowingTexture() { return FluidWaxHolder.FLUID_FLOW; }
      @Override public int getTintColor() { return FluidWaxHolder.COLOR | 0xFF000000; }
    }, FluidWaxHolder.TYPE.get());
    event.registerFluidType(new IClientFluidTypeExtensions() {
      @Override public ResourceLocation getStillTexture() { return FluidBiomassHolder.FLUID_STILL; }
      @Override public ResourceLocation getFlowingTexture() { return FluidBiomassHolder.FLUID_FLOW; }
      @Override public int getTintColor() { return FluidBiomassHolder.COLOR | 0xFF000000; }
    }, FluidBiomassHolder.TYPE.get());
    event.registerFluidType(new IClientFluidTypeExtensions() {
      @Override public ResourceLocation getStillTexture() { return FluidHoneyHolder.FLUID_STILL; }
      @Override public ResourceLocation getFlowingTexture() { return FluidHoneyHolder.FLUID_FLOW; }
      @Override public int getTintColor() { return FluidHoneyHolder.COLOR | 0xFF000000; }
    }, FluidHoneyHolder.TYPE.get());
    IClientItemExtensions shieldExt = new IClientItemExtensions() {
      @Override
      public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return ShieldBlockEntityWithoutLevelRenderer.instance;
      }
    };
    event.registerItem(shieldExt, ItemRegistry.SHIELD_WOOD.get(), ItemRegistry.SHIELD_LEATHER.get(),
        ItemRegistry.SHIELD_FLINT.get(), ItemRegistry.SHIELD_BONE.get(), ItemRegistry.SHIELD_OBSIDIAN.get());
  }


  @SubscribeEvent
  public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
    event.register(MenuTypeRegistry.STORAGE_BAG.get(), ScreenStorageBag::new);
    event.register(MenuTypeRegistry.CRAFTING_BAG.get(), CraftingBagScreen::new);
    event.register(MenuTypeRegistry.ENDER_BOOK.get(), EnderBookScreen::new);
    event.register(MenuTypeRegistry.CRAFTING_STICK.get(), CraftingStickScreen::new);
    event.register(MenuTypeRegistry.FILTER_DATA.get(), ScreenFilterCard::new);
    event.register(MenuTypeRegistry.DROPPER.get(), ScreenDropper::new);
    event.register(MenuTypeRegistry.FISHER.get(), ScreenFisher::new);
    event.register(MenuTypeRegistry.DETECTOR_ITEM.get(), ScreenDetectorItem::new);
    event.register(MenuTypeRegistry.GENERATOR_DROPS.get(), ScreenGeneratorDrops::new);
    event.register(MenuTypeRegistry.BREAKER.get(), ScreenBreaker::new);
    event.register(MenuTypeRegistry.GENERATOR_FOOD.get(), ScreenGeneratorFood::new);
    event.register(MenuTypeRegistry.SOLIDIFIER.get(), ScreenSolidifier::new);
    event.register(MenuTypeRegistry.HARVESTER.get(), ScreenHarvester::new);
    event.register(MenuTypeRegistry.DETECTOR_ENTITY.get(), ScreenDetector::new);
    event.register(MenuTypeRegistry.STRUCTURE.get(), ScreenStructure::new);
    event.register(MenuTypeRegistry.CRATE_MINI.get(), ScreenCrateMini::new);
    event.register(MenuTypeRegistry.COLLECTOR_FLUID.get(), ScreenFluidCollect::new);
    event.register(MenuTypeRegistry.CRUSHER.get(), ScreenCrusher::new);
    event.register(MenuTypeRegistry.PLACER.get(), ScreenPlacer::new);
    event.register(MenuTypeRegistry.MELTER.get(), ScreenMelter::new);
    event.register(MenuTypeRegistry.ANVIL_VOID.get(), ScreenAnvilVoid::new);
    event.register(MenuTypeRegistry.TELEPORT.get(), ScreenTeleport::new);
    event.register(MenuTypeRegistry.USER.get(), ScreenUser::new);
    event.register(MenuTypeRegistry.EXPERIENCE_PYLON.get(), ScreenExpPylon::new);
    event.register(MenuTypeRegistry.UNCRAFTER.get(), ScreenUncraft::new);
    event.register(MenuTypeRegistry.GENERATOR_FLUID.get(), ScreenGeneratorFluid::new);
    event.register(MenuTypeRegistry.DISENCHANTER.get(), ScreenDisenchant::new);
    event.register(MenuTypeRegistry.LASER.get(), ScreenLaser::new);
    event.register(MenuTypeRegistry.GENERATOR_FUEL.get(), ScreenGeneratorFuel::new);
    event.register(MenuTypeRegistry.CRATE.get(), ScreenCrate::new);
    event.register(MenuTypeRegistry.WORKBENCH.get(), ScreenWorkbench::new);
    event.register(MenuTypeRegistry.SOUND_RECORDER.get(), ScreenSoundRecorder::new);
    event.register(MenuTypeRegistry.FLUID_PIPE.get(), ScreenCableFluid::new);
    event.register(MenuTypeRegistry.ITEM_PIPE.get(), ScreenCableItem::new);
    event.register(MenuTypeRegistry.WIRELESS_ENERGY.get(), ScreenWirelessEnergy::new);
    event.register(MenuTypeRegistry.WIRELESS_TRANSMITTER.get(), ScreenTransmit::new);
    event.register(MenuTypeRegistry.WIRELESS_FLUID.get(), ScreenWirelessFluid::new);
    event.register(MenuTypeRegistry.WIRELESS_ITEM.get(), ScreenWirelessItem::new);
    event.register(MenuTypeRegistry.SCREEN.get(), ScreenScreentext::new);
    event.register(MenuTypeRegistry.PACKAGER.get(), ScreenPackager::new);
    event.register(MenuTypeRegistry.FORESTER.get(), ScreenForester::new);
    event.register(MenuTypeRegistry.FAN.get(), ScreenFan::new);
    event.register(MenuTypeRegistry.ANVIL_MAGMA.get(), ScreenAnvilMagma::new);
    event.register(MenuTypeRegistry.CRAFTER.get(), ScreenCrafter::new);
    event.register(MenuTypeRegistry.BATTERY.get(), ScreenBattery::new);
    event.register(MenuTypeRegistry.COLLECTOR.get(), ScreenItemCollector::new);
    event.register(MenuTypeRegistry.MAGNET.get(), ScreenMagnet::new);
    event.register(MenuTypeRegistry.BEACON.get(), ScreenPotion::new);
    event.register(MenuTypeRegistry.ANVIL.get(), ScreenAnvil::new);
    event.register(MenuTypeRegistry.PEAT_FARM.get(), ScreenPeatFarm::new);
    event.register(MenuTypeRegistry.PLACER_FLUID.get(), ScreenPlacerFluid::new);
    event.register(MenuTypeRegistry.BATTERY_CLAY.get(), ScreenClayBattery::new);
    event.register(MenuTypeRegistry.CLOCK.get(), ScreenClock::new);
    event.register(MenuTypeRegistry.GENERATOR_SOLAR.get(), ScreenGeneratorSolar::new);
    event.register(MenuTypeRegistry.SOUND_PLAYER.get(), ScreenSoundPlayer::new);
    event.register(MenuTypeRegistry.COMPUTER_SHAPE.get(), ScreenShapedata::new);
    event.register(MenuTypeRegistry.MINER.get(), ScreenMiner::new);
    event.register(MenuTypeRegistry.LUNCHBOX.get(), ScreenLunchbox::new);
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
    event.register((stack, tintIndex) -> {
      if (tintIndex == 0) { //layer zero is outline, ignore this 
        return 0xFFFFFFFF;
      }
      //layer 1 is overlay  
      return ScreenLunchbox.getColour(stack);
    }, ItemRegistry.LUNCHBOX.get());
    //
    event.register((stack, tintIndex) -> {
      //layer 0 = outline, layer 2 = string overlay; only tint layer 1 (bag body)
      if (tintIndex != 1) {
        return 0xFFFFFFFF;
      }
      return ItemStorageBag.getColour(stack);
    }, ItemRegistry.STORAGE_BAG.get());
    //
    event.register((stack, tintIndex) -> {
      if (stack.has(DataComponents.CUSTOM_DATA) && tintIndex > 0) {
        //what entity is inside
        EntityType<?> thing = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)));
        //pull the colours from the egg
        for (SpawnEggItem spawneggitem : SpawnEggItem.eggs()) {
          if (spawneggitem.getType(spawneggitem.getDefaultInstance()) == thing) {
            return 0xFF000000 | spawneggitem.getColor(tintIndex - 1);
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
    event.registerEntityRenderer(EntityRegistry.MOB_CONTAINER_EMPTY.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.TORCH_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.DUNGEON.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.EYE.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.FIRE_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.ENDER_FISHING.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.STONE_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.LIGHTNING_BOLT.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.MAGIC_MISSILE.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(EntityRegistry.CONVEYOR_ITEM.get(), ConveyorItemRenderer::new);
  }
}
