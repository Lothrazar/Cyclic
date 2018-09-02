/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic;

import java.io.File;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.block.BlockShears;
import com.lothrazar.cyclicmagic.block.BlockSoundSuppress;
import com.lothrazar.cyclicmagic.block.anvil.BlockAnvilAuto;
import com.lothrazar.cyclicmagic.block.anvilmagma.BlockAnvilMagma;
import com.lothrazar.cyclicmagic.block.anvilvoid.BlockVoidAnvil;
import com.lothrazar.cyclicmagic.block.applesprout.BlockAppleCrop;
import com.lothrazar.cyclicmagic.block.arrowtarget.BlockArrowTarget;
import com.lothrazar.cyclicmagic.block.autouser.BlockUser;
import com.lothrazar.cyclicmagic.block.battery.BlockBattery;
import com.lothrazar.cyclicmagic.block.batterycheat.BlockBatteryInfinite;
import com.lothrazar.cyclicmagic.block.beaconempty.BlockBeaconPowered;
import com.lothrazar.cyclicmagic.block.beaconpotion.BlockBeaconPotion;
import com.lothrazar.cyclicmagic.block.bean.BlockCropMagicBean;
import com.lothrazar.cyclicmagic.block.builderpattern.BlockPatternBuilder;
import com.lothrazar.cyclicmagic.block.buildershape.BlockStructureBuilder;
import com.lothrazar.cyclicmagic.block.buttondoorbell.BlockDoorbell;
import com.lothrazar.cyclicmagic.block.buttonflat.BlockButtonLarge;
import com.lothrazar.cyclicmagic.block.clockredstone.BlockRedstoneClock;
import com.lothrazar.cyclicmagic.block.collector.BlockVacuum;
import com.lothrazar.cyclicmagic.block.controlledminer.BlockMinerSmart;
import com.lothrazar.cyclicmagic.block.crafter.BlockCrafter;
import com.lothrazar.cyclicmagic.block.dehydrator.BlockDeHydrator;
import com.lothrazar.cyclicmagic.block.dice.BlockDice;
import com.lothrazar.cyclicmagic.block.disenchanter.BlockDisenchanter;
import com.lothrazar.cyclicmagic.block.dropper.BlockDropperExact;
import com.lothrazar.cyclicmagic.block.enchanter.BlockEnchanter;
import com.lothrazar.cyclicmagic.block.enchantlibrary.shelf.BlockLibrary;
import com.lothrazar.cyclicmagic.block.entitydetector.BlockDetector;
import com.lothrazar.cyclicmagic.block.exppylon.BlockXpPylon;
import com.lothrazar.cyclicmagic.block.fan.BlockFan;
import com.lothrazar.cyclicmagic.block.fishing.BlockFishing;
import com.lothrazar.cyclicmagic.block.fluidplacer.BlockFluidPlacer;
import com.lothrazar.cyclicmagic.block.forester.BlockForester;
import com.lothrazar.cyclicmagic.block.harvester.BlockHarvester;
import com.lothrazar.cyclicmagic.block.hydrator.BlockHydrator;
import com.lothrazar.cyclicmagic.block.imbue.BlockImbue;
import com.lothrazar.cyclicmagic.block.interdiction.BlockMagnetAnti;
import com.lothrazar.cyclicmagic.block.laser.BlockLaser;
import com.lothrazar.cyclicmagic.block.magnetitem.BlockMagnet;
import com.lothrazar.cyclicmagic.block.miner.BlockMiner;
import com.lothrazar.cyclicmagic.block.moondetector.BlockMoonDetector;
import com.lothrazar.cyclicmagic.block.packager.BlockPackager;
import com.lothrazar.cyclicmagic.block.password.BlockPassword;
import com.lothrazar.cyclicmagic.block.placer.BlockPlacer;
import com.lothrazar.cyclicmagic.block.screen.BlockScreen;
import com.lothrazar.cyclicmagic.block.sound.BlockSoundPlayer;
import com.lothrazar.cyclicmagic.block.sprinkler.BlockSprinkler;
import com.lothrazar.cyclicmagic.block.tank.BlockFluidTank;
import com.lothrazar.cyclicmagic.block.trash.BlockTrash;
import com.lothrazar.cyclicmagic.block.uncrafter.BlockUncrafting;
import com.lothrazar.cyclicmagic.block.vector.BlockVectorPlate;
import com.lothrazar.cyclicmagic.block.workbench.BlockWorkbench;
import com.lothrazar.cyclicmagic.capability.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.creativetab.CreativeTabCyclic;
import com.lothrazar.cyclicmagic.enchant.EnchantAutoSmelt;
import com.lothrazar.cyclicmagic.enchant.EnchantBeheading;
import com.lothrazar.cyclicmagic.enchant.EnchantExcavation;
import com.lothrazar.cyclicmagic.enchant.EnchantLaunch;
import com.lothrazar.cyclicmagic.enchant.EnchantLifeLeech;
import com.lothrazar.cyclicmagic.enchant.EnchantMagnet;
import com.lothrazar.cyclicmagic.enchant.EnchantMultishot;
import com.lothrazar.cyclicmagic.enchant.EnchantQuickdraw;
import com.lothrazar.cyclicmagic.enchant.EnchantReach;
import com.lothrazar.cyclicmagic.enchant.EnchantVenom;
import com.lothrazar.cyclicmagic.enchant.EnchantWaterwalking;
import com.lothrazar.cyclicmagic.enchant.EnchantXpBoost;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.item.ItemCaveFinder;
import com.lothrazar.cyclicmagic.item.ItemEnderBag;
import com.lothrazar.cyclicmagic.item.ItemEnderWing;
import com.lothrazar.cyclicmagic.item.ItemFangs;
import com.lothrazar.cyclicmagic.item.ItemFireExtinguish;
import com.lothrazar.cyclicmagic.item.ItemIceWand;
import com.lothrazar.cyclicmagic.item.ItemLeverRemote;
import com.lothrazar.cyclicmagic.item.ItemMattock;
import com.lothrazar.cyclicmagic.item.ItemObsShears;
import com.lothrazar.cyclicmagic.item.ItemPaperCarbon;
import com.lothrazar.cyclicmagic.item.ItemPistonWand;
import com.lothrazar.cyclicmagic.item.ItemPlayerLauncher;
import com.lothrazar.cyclicmagic.item.ItemProspector;
import com.lothrazar.cyclicmagic.item.ItemRotateBlock;
import com.lothrazar.cyclicmagic.item.ItemSoulstone;
import com.lothrazar.cyclicmagic.item.ItemSpawnInspect;
import com.lothrazar.cyclicmagic.item.ItemStirrups;
import com.lothrazar.cyclicmagic.item.ItemStirrupsReverse;
import com.lothrazar.cyclicmagic.item.ItemWandHypno;
import com.lothrazar.cyclicmagic.item.ItemWarpSurface;
import com.lothrazar.cyclicmagic.item.ItemWaterRemoval;
import com.lothrazar.cyclicmagic.item.ItemWaterSpreader;
import com.lothrazar.cyclicmagic.item.boomerang.ItemBoomerang;
import com.lothrazar.cyclicmagic.item.cannon.ItemProjectileCannon;
import com.lothrazar.cyclicmagic.item.cannon.ParticleEventManager;
import com.lothrazar.cyclicmagic.item.crashtestdummy.ItemCrashSpawner;
import com.lothrazar.cyclicmagic.item.cyclicwand.ItemCyclicWand;
import com.lothrazar.cyclicmagic.item.enderbook.ItemEnderBook;
import com.lothrazar.cyclicmagic.item.endereye.ItemEnderEyeReuse;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemAutoTorch;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmAir;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmAntidote;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmBoat;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmFire;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmSlowfall;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmSpeed;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmVoid;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmWater;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemGloveClimb;
import com.lothrazar.cyclicmagic.item.findspawner.ItemProjectileDungeon;
import com.lothrazar.cyclicmagic.item.homingmissile.ItemMagicMissile;
import com.lothrazar.cyclicmagic.item.lightningmagic.ItemProjectileLightning;
import com.lothrazar.cyclicmagic.item.merchant.ItemMerchantAlmanac;
import com.lothrazar.cyclicmagic.item.mobcapture.ItemProjectileMagicNet;
import com.lothrazar.cyclicmagic.item.mobs.ItemHorseTame;
import com.lothrazar.cyclicmagic.item.mobs.ItemVillagerMagic;
import com.lothrazar.cyclicmagic.item.random.ItemRandomizer;
import com.lothrazar.cyclicmagic.item.shears.ItemShearsRanged;
import com.lothrazar.cyclicmagic.item.signcolor.ItemSignEditor;
import com.lothrazar.cyclicmagic.item.sleep.ItemSleepingMat;
import com.lothrazar.cyclicmagic.item.slingshot.ItemProjectileSlingshot;
import com.lothrazar.cyclicmagic.item.snowmagic.ItemProjectileSnow;
import com.lothrazar.cyclicmagic.item.torchmagic.ItemProjectileTorch;
import com.lothrazar.cyclicmagic.item.torchmagic.ItemTorchThrower;
import com.lothrazar.cyclicmagic.liquid.milk.FluidMilk;
import com.lothrazar.cyclicmagic.liquid.poison.FluidPoison;
import com.lothrazar.cyclicmagic.log.ModLogger;
import com.lothrazar.cyclicmagic.module.DynamiteModule;
import com.lothrazar.cyclicmagic.module.ICyclicModule;
import com.lothrazar.cyclicmagic.playerupgrade.ItemAppleStep;
import com.lothrazar.cyclicmagic.playerupgrade.ItemCraftingUnlock;
import com.lothrazar.cyclicmagic.playerupgrade.ItemFlight;
import com.lothrazar.cyclicmagic.playerupgrade.ItemInventoryUnlock;
import com.lothrazar.cyclicmagic.playerupgrade.ItemNoclipGhost;
import com.lothrazar.cyclicmagic.potion.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.potion.PotionTypeRegistry;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.ConfigRegistry;
import com.lothrazar.cyclicmagic.registry.EnchantRegistry;
import com.lothrazar.cyclicmagic.registry.EventRegistry;
import com.lothrazar.cyclicmagic.registry.InterModCommsRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.ModuleRegistry;
import com.lothrazar.cyclicmagic.registry.PacketRegistry;
import com.lothrazar.cyclicmagic.registry.PermissionRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.registry.SkillRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.registry.VillagerProfRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilString;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Const.MODID, useMetadata = true, dependencies = "before:guideapi;after:jei;after:baubles,crafttweaker", canBeDeactivated = false, certificateFingerprint = "@FINGERPRINT@", updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12,)", guiFactory = "com.lothrazar." + Const.MODID + ".config.IngameConfigFactory")
public class ModCyclic {

  @Instance(value = Const.MODID)
  public static ModCyclic instance;
  @SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
  public static CommonProxy proxy;
  public static ModLogger logger;
  public EventRegistry events;
  private ArrayList<IContent> content;
  public static SimpleNetworkWrapper network;
  public final static CreativeTabCyclic TAB = new CreativeTabCyclic();
  @CapabilityInject(IPlayerExtendedProperties.class)
  public static final Capability<IPlayerExtendedProperties> CAPABILITYSTORAGE = null;
  static {
    FluidRegistry.enableUniversalBucket();//https://github.com/BluSunrize/ImmersiveEngineering/blob/c76e51998756a54c22dd40ac1877313bf95e8520/src/main/java/blusunrize/immersiveengineering/ImmersiveEngineering.java
  }

  @EventHandler
  public void onPreInit(FMLPreInitializationEvent event) {
    logger = new ModLogger(event.getModLog());
    ConfigRegistry.oreConfig = new Configuration(new File(event.getModConfigurationDirectory(), "cyclic_ores.cfg"));
    ConfigRegistry.init(new Configuration(event.getSuggestedConfigurationFile()));
    ConfigRegistry.register(logger);
    MinecraftForge.EVENT_BUS.register(new ParticleEventManager());
    network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
    PacketRegistry.register(network);
    SoundRegistry.register();
    CapabilityRegistry.register();
    ReflectionRegistry.register();
    MaterialRegistry.register();
    this.events = new EventRegistry();
    this.events.registerCoreEvents();
    ModuleRegistry.init();
    ModuleRegistry.registerAll();//create new instance of every module

    //create alll instances of "content" aka blocks/items
    //^^ maybe a map with <category.configKey , instance>
    // maybe re-use IHasConfigg or new-extended ??? IContent: enabled(): boolean; register(): void; //optional. do tileentity/etc
    //a "ModuleNewtype" will just be an iContent that has no physicality, no block or item to it 
    // ->> OR we have "PeatModule", a content block with multi-content
    // ->> consider: fire & fluids, content that depends on secondary flags, it comes in if previous are in
    // run config including this new content hook
    //after config sync, find all content where enabled()===true and init/register it
    //then Forge content registries finish the rest 
    content = new ArrayList<IContent>();
    content.add(new BlockLibrary());
    content.add(new FluidPoison());
    content.add(new FluidMilk());
    content.add(new EnchantAutoSmelt());
    content.add(new EnchantBeheading());
    content.add(new EnchantExcavation());
    content.add(new EnchantLaunch());
    content.add(new EnchantLifeLeech());
    content.add(new EnchantMagnet());
    content.add(new EnchantMultishot());
    content.add(new EnchantQuickdraw());
    content.add(new EnchantReach());
    content.add(new EnchantVenom());
    content.add(new EnchantWaterwalking());
    content.add(new EnchantXpBoost());
    content.add(new ItemCrashSpawner());
    content.add(new ItemProjectileCannon());
    content.add(new ItemMerchantAlmanac());
    content.add(new ItemSoulstone());
    content.add(new ItemPlayerLauncher());
    content.add(new ItemLeverRemote());
    content.add(new ItemWarpSurface());
    content.add(new ItemRotateBlock());
    content.add(new ItemFangs());
    content.add(new ItemWaterSpreader());
    content.add(new ItemEnderEyeReuse());
    content.add(new ItemGloveClimb());
    content.add(new ItemTorchThrower());
    content.add(new ItemIceWand());
    content.add(new ItemFireExtinguish());
    content.add(new ItemEnderBag());
    content.add(new ItemStirrups());
    content.add(new ItemProspector());
    content.add(new ItemEnderBook());
    content.add(new ItemPaperCarbon());
    content.add(new ItemCaveFinder());
    content.add(new ItemProjectileDungeon());
    content.add(new ItemProjectileLightning());
    content.add(new ItemCyclicWand());
    content.add(new ItemObsShears());
    content.add(new ItemEnderWing(ItemEnderWing.WarpType.BED));
    content.add(new ItemEnderWing(ItemEnderWing.WarpType.SPAWN));
    content.add(new ItemPistonWand());
    content.add(new ItemSleepingMat());
    content.add(new ItemSpawnInspect());
    content.add(new ItemMattock());
    content.add(new ItemWandHypno());
    content.add(new ItemRandomizer());
    content.add(new DynamiteModule());
    content.add(new ItemShearsRanged());
    content.add(new ItemProjectileTorch());
    content.add(new ItemProjectileMagicNet());
    content.add(new ItemVillagerMagic());
    content.add(new ItemNoclipGhost());
    content.add(new ItemFlight());
    content.add(new ItemProjectileSnow());
    content.add(new ItemWaterRemoval());
    content.add(new ItemAutoTorch());
    content.add(new ItemInventoryUnlock());
    content.add(new ItemCraftingUnlock());
    content.add(new ItemCharmAntidote());
    content.add(new ItemMagicMissile());
    content.add(new ItemCharmWater());
    content.add(new ItemSignEditor());
    content.add(new ItemCharmAir());
    content.add(new ItemCharmVoid());
    content.add(new ItemCharmBoat());
    content.add(new ItemCharmFire());
    content.add(new ItemCharmSpeed());
    content.add(new ItemCharmSlowfall());
    content.add(new ItemAppleStep());
    content.add(new ItemStirrupsReverse());
    content.add(new ItemHorseTame());
    content.add(new ItemBoomerang());
    content.add(new ItemProjectileSlingshot());
    content.add(new BlockVectorPlate());
    content.add(new BlockPatternBuilder());
    content.add(new BlockMagnet());
    content.add(new BlockMagnetAnti());
    content.add(new BlockDoorbell());
    content.add(new BlockDeHydrator());
    content.add(new BlockVacuum());
    content.add(new BlockBeaconPotion());
    content.add(new BlockButtonLarge());
    content.add(new BlockMoonDetector());
    content.add(new BlockRedstoneClock());
    content.add(new BlockCropMagicBean());
    content.add(new BlockArrowTarget());
    content.add(new BlockAppleCrop());
    content.add(new BlockBeaconPowered());
    content.add(new BlockSprinkler());
    content.add(new BlockForester());
    content.add(new BlockPackager());
    content.add(new BlockMiner());
    content.add(new BlockHydrator());
    content.add(new BlockAnvilMagma());
    content.add(new BlockAnvilAuto(Blocks.ENCHANTING_TABLE));
    content.add(new BlockHarvester());
    content.add(new BlockEnchanter());
    content.add(new BlockXpPylon());
    content.add(new BlockStructureBuilder());
    content.add(new BlockUncrafting());
    content.add(new BlockMinerSmart());
    content.add(new BlockPlacer());
    content.add(new BlockPassword());
    content.add(new BlockUser());
    content.add(new BlockBatteryInfinite());
    content.add(new BlockBattery());
    content.add(new BlockTrash());
    content.add(new BlockWorkbench());
    content.add(new BlockScreen());
    content.add(new BlockSoundSuppress());
    content.add(new BlockCrafter());
    content.add(new BlockDetector());
    content.add(new BlockFan());
    content.add(new BlockShears());
    content.add(new BlockFishing());
    content.add(new BlockDisenchanter());
    content.add(new BlockFluidTank());
    content.add(new BlockLaser());
    content.add(new BlockSoundPlayer());
    content.add(new BlockDice());
    content.add(new BlockImbue());
    content.add(new BlockVoidAnvil());
    content.add(new BlockDropperExact());
    content.add(new BlockFluidPlacer());
    for (IContent cont : content) {
      ConfigRegistry.register(cont);
    }
    ConfigRegistry.syncAllConfig();
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onPreInit();
    }
    for (IContent cont : content) {
      if (cont.enabled()) {
        cont.register();
      }
    }
    proxy.preInit();
    //fluids still does the old way ( FluidRegistry.addBucketForFluid)
    //    MinecraftForge.EVENT_BUS.register(FluidsRegistry.class);
    MinecraftForge.EVENT_BUS.register(ItemRegistry.class);
    MinecraftForge.EVENT_BUS.register(BlockRegistry.class);
    MinecraftForge.EVENT_BUS.register(RecipeRegistry.class);
    MinecraftForge.EVENT_BUS.register(SoundRegistry.class);
    MinecraftForge.EVENT_BUS.register(PotionEffectRegistry.class);
    MinecraftForge.EVENT_BUS.register(PotionTypeRegistry.class);
    MinecraftForge.EVENT_BUS.register(EnchantRegistry.class);
    MinecraftForge.EVENT_BUS.register(VillagerProfRegistry.class);
  }

  @EventHandler
  public void onInit(FMLInitializationEvent event) {
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onInit();
    }
    proxy.init();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new ForgeGuiHandler());
    ConfigRegistry.syncAllConfig(); //fixes things , stuff was added to items and content that has config
    this.events.registerAll(); //important: register events AFTER modules onInit, since modules add events in this phase.
    PermissionRegistry.register();
    InterModCommsRegistry.register();
  }

  @EventHandler
  public void onPostInit(FMLPostInitializationEvent event) {
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onPostInit();
    }
    SkillRegistry.register();
    /**
     * TODO: unit test module with a post init to do this stuff
     */
    if (logger.runUnitTests()) {
      UtilString.unitTests();
    }
  }

  @EventHandler
  public void onServerStarting(FMLServerStartingEvent event) {
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onServerStarting(event);
    }
  }

  @EventHandler
  public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
    // https://tutorials.darkhax.net/tutorials/jar_signing/
    String source = (event.getSource() == null) ? "" : event.getSource().getName() + " ";
    String msg = "CYCLIC: Invalid fingerprint detected! The file " + source + "may have been tampered with. This version will NOT be supported by the author!";
    if (logger == null) {
      System.out.println(msg);
    }
    else {
      ModCyclic.logger.error(msg);
    }
  }
}
