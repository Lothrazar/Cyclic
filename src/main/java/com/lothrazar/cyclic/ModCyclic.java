package com.lothrazar.cyclic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.event.EventHandler;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.setup.ClientProxy;
import com.lothrazar.cyclic.setup.ConfigHandler;
import com.lothrazar.cyclic.setup.IProxy;
import com.lothrazar.cyclic.setup.ServerProxy;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModCyclic.MODID)
public class ModCyclic {

  public static final String certificateFingerprint = "@FINGERPRINT@";
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
  public static final String MODID = "cyclic";
  public static final Logger LOGGER = LogManager.getLogger();

  public ModCyclic() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    //only for server starting
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new EventHandler());
    ConfigHandler.loadConfig(ConfigHandler.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
  }

  private void setup(final FMLCommonSetupEvent event) {
    //https://github.com/TheIllusiveC4/Curios/blob/fc77c876b630dc6e4a325cb9ac627b551749a19b/src/main/java/top/theillusivec4/curios/api/CurioTags.java
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("charm").setSize(2).setEnabled(true).setHidden(false));
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("necklace").setSize(1).setEnabled(true).setHidden(false));
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("hands").setSize(2).setEnabled(true).setHidden(false));
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("belt").setSize(2).setEnabled(true).setHidden(false));
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("ring").setSize(2).setEnabled(true).setHidden(false));
    //now all blocks/items exist
    PacketRegistry.init();
    proxy.init();
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    //TODO: LOOP 
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Blocks.soundproofing);
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Enchants.excavate);//y
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Enchants.experience_boost);//y
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Enchants.life_leech);//y
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Enchants.magnet);//y
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Enchants.multishot);//y
    //    MinecraftForge.EVENT_BUS.register(CyclicRegistry.smelting);//  ?
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Enchants.venom);//y ?
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Items.diamond_carrot_health);
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Items.redstone_carrot_speed);
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Items.emerald_carrot_jump);
    MinecraftForge.EVENT_BUS.register(CyclicRegistry.Items.lapis_carrot_variant);
    MinecraftForge.EVENT_BUS.register(this);
    scaffoldingListen = new ItemScaffolding[] { CyclicRegistry.Items.item_scaffold_fragile, CyclicRegistry.Items.item_scaffold_responsive, CyclicRegistry.Items.item_scaffold_replace };
    //lets go
    proxy.initColours();
  }

  ItemScaffolding[] scaffoldingListen = new ItemScaffolding[0];

  @SubscribeEvent
  public void onRightClickBlock(RightClickBlock event) {
    for (ItemScaffolding loop : scaffoldingListen)
      if (event.getItemStack() != null && event.getItemStack().getItem() == loop && event.getPlayer().isCrouching()) {
        Direction opp = event.getFace().getOpposite();
        BlockPos dest = UtilWorld.nextReplaceableInDirection(event.getWorld(), event.getPos(), opp, 16, loop.getBlock());
        event.getWorld().setBlockState(dest, Block.getBlockFromItem(loop).getDefaultState());
        ItemStack stac = event.getPlayer().getHeldItem(event.getHand());
        stac.shrink(1);
        event.setCanceled(true);
      }
  }

  @SubscribeEvent
  public void onServerStarting(FMLServerStartingEvent event) {
    //you probably will not need this
  }

  @SubscribeEvent
  public static void onFingerprintViolation(FMLFingerprintViolationEvent event) {
    // https://tutorials.darkhax.net/tutorials/jar_signing/
    String source = (event.getSource() == null) ? "" : event.getSource().getName() + " ";
    String msg = MODID + "Invalid fingerprint detected! The file " + source + "may have been tampered with. This version will NOT be supported by the author!";
    //    System.out.println(msg);
  }
}
