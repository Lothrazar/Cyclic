package com.lothrazar.cyclicmagic.module;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.cyclicwand.InventoryWand;
import com.lothrazar.cyclicmagic.component.cyclicwand.ItemCyclicWand;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellShiftLeft;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellShiftRight;
import com.lothrazar.cyclicmagic.component.enderbook.ItemEnderBook;
import com.lothrazar.cyclicmagic.component.merchant.ItemMerchantAlmanac;
import com.lothrazar.cyclicmagic.component.storagesack.ItemStorageBag;
import com.lothrazar.cyclicmagic.item.tool.*;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap.ActionType;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap.WandType;
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.item.ItemChestSackEmpty;
import com.lothrazar.cyclicmagic.item.ItemClimbingGlove;
import com.lothrazar.cyclicmagic.item.ItemEnderBag;
import com.lothrazar.cyclicmagic.item.ItemPaperCarbon;
import com.lothrazar.cyclicmagic.item.ItemPasswordRemote;
import com.lothrazar.cyclicmagic.item.ItemSoulstone;
import com.lothrazar.cyclicmagic.net.PacketSwapBlock;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolsModule extends BaseEventModule implements IHasConfig {
  private static SpellHud spellHud;
  private boolean enableSleepingMat;
  private boolean enableToolPush;
  private boolean enableHarvestLeaves;
  private boolean enableToolHarvest;
  private boolean enableHarvestWeeds;
  private boolean enablePearlReuse;
  private boolean enableSpawnInspect;
  private boolean enableCyclicWand;
  private boolean enableProspector;
  private boolean enableCavefinder;
  private boolean enableWarpHomeTool;
  private boolean enableWarpSpawnTool;
  private boolean enableSwappers;
  private boolean enableRando;
  private boolean enablePearlReuseMounted;
  private boolean enableCarbonPaper;
  private boolean storageBagEnabled;
  private boolean enableEnderBook;
  private boolean enableChestSack;
  private boolean enableStirrups;
  private boolean enableTorchLauncher;
  private boolean enderSack;
  private boolean enablewaterSpread;
  private boolean enableFreezer;
  private boolean enableFireKiller;
  private boolean enableBlockRot;
  private boolean enableCGlove;
  private boolean enableElevate;
  private boolean enableLever;
  private boolean enableTrader;
  private boolean enableSoulstone;
  private boolean enablePlayerLauncher;
  public static ItemStorageBag storage_bag;//ref by ContainerStorage
  public static RenderLoc renderLocation;
  /**
   * BIG thank you to this MIT licensed source code
   * 
   * https://github.com/romelo333/notenoughwands1.8.8/blob/2fee100fe9441828eb54dc7ec6a233c9b278e753/src/main/java/romelo333/notenoughwands/proxy/ClientProxy.java
   * 
   * @param evt
   */
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void renderOverlay(RenderWorldLastEvent evt) {
    Minecraft mc = Minecraft.getMinecraft();
    EntityPlayerSP p = mc.player;
    ItemStack heldItem = p.getHeldItemMainhand();
    if (heldItem == null) { return; }
    if (heldItem.getItem() instanceof ItemToolSwap) {
      RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
      if (mouseOver != null && mouseOver.getBlockPos() != null && mouseOver.sideHit != null) {
        IBlockState state = p.world.getBlockState(mouseOver.getBlockPos());
        Block block = state.getBlock();
        if (block != null && block.getMaterial(state) != Material.AIR) {
          ItemToolSwap wandInstance = (ItemToolSwap) heldItem.getItem();
          IBlockState matched = null;
          if (wandInstance.getWandType() == WandType.MATCH) {
            matched = p.getEntityWorld().getBlockState(mouseOver.getBlockPos());
          }
          List<BlockPos> places = PacketSwapBlock.getSelectedBlocks(p.getEntityWorld(), mouseOver.getBlockPos(),
              ActionType.values()[ActionType.get(heldItem)], wandInstance.getWandType(),
              mouseOver.sideHit, matched);
          Set<BlockPos> coordinates = new HashSet<BlockPos>(places);
          UtilWorld.OutlineRenderer.renderOutlines(evt, p, coordinates, 75, 0, 130);
        }
      }
    }
  }
  @Override
  public void onInit() {
    if (enablePlayerLauncher) {
      ItemToolLaunch launcher = new ItemToolLaunch(0);
      ItemRegistry.addItem(launcher, "launcher");
      ModCyclic.instance.events.register(launcher);
    }
    if (enableSoulstone) {
      ItemSoulstone soulstone = new ItemSoulstone();
      ItemRegistry.addItem(soulstone, "soulstone");
      ModCyclic.instance.events.register(soulstone);
    }
    if (enableTrader) {
      ItemMerchantAlmanac tool_trade = new ItemMerchantAlmanac();
      ItemRegistry.addItem(tool_trade, "tool_trade");
    }
    if (enableLever) {
      ItemPasswordRemote password_remote = new ItemPasswordRemote();
      ItemRegistry.addItem(password_remote, "password_remote");
    }
    if (enableElevate) {
      ItemToolSurface tool_elevate = new ItemToolSurface();
      ItemRegistry.addItem(tool_elevate, "tool_elevate");
      LootTableRegistry.registerLoot(tool_elevate);
    }
    if (enableCGlove) {
      ItemClimbingGlove glove_climb = new ItemClimbingGlove();
      ItemRegistry.addItem(glove_climb, "glove_climb");
      LootTableRegistry.registerLoot(glove_climb);
    }
    if (enableBlockRot) {
      ItemToolRotate tool_rotate = new ItemToolRotate();
      ItemRegistry.addItem(tool_rotate, "tool_rotate");
    }
    if (enablewaterSpread) {
      ItemToolWaterSpreader water_spreader = new ItemToolWaterSpreader();
      ItemRegistry.addItem(water_spreader, "water_spreader");
      LootTableRegistry.registerLoot(water_spreader);
    }
    if (enableFreezer) {
      ItemToolWaterIce water_freezer = new ItemToolWaterIce();
      ItemRegistry.addItem(water_freezer, "water_freezer");
    }
    if (enableFireKiller) {
      ItemToolExtinguish fire_killer = new ItemToolExtinguish();
      ItemRegistry.addItem(fire_killer, "fire_killer");
    }
    if (enderSack) {
      ItemEnderBag sack_ender = new ItemEnderBag();
      ItemRegistry.addItem(sack_ender, "sack_ender");
      LootTableRegistry.registerLoot(sack_ender);
      ItemRegistry.registerWithJeiDescription(sack_ender);
    }
    if (enableTorchLauncher) {
      ItemToolThrowTorch tool_torch_launcher = new ItemToolThrowTorch();
      ItemRegistry.addItem(tool_torch_launcher, "tool_torch_launcher");
    }
    if (enableStirrups) {
      ItemToolStirrups tool_mount = new ItemToolStirrups();
      ItemRegistry.addItem(tool_mount, "tool_mount");
      ItemRegistry.registerWithJeiDescription(tool_mount);
    }
    if (enableChestSack) {
      ItemChestSackEmpty chest_sack_empty = new ItemChestSackEmpty();
      ItemChestSack chest_sack = new ItemChestSack();
      chest_sack.setHidden();
      chest_sack.setEmptySack(chest_sack_empty);
      chest_sack_empty.setFullSack(chest_sack);
      ItemRegistry.registerWithJeiDescription(chest_sack);
      ItemRegistry.addItem(chest_sack, "chest_sack");
      ItemRegistry.addItem(chest_sack_empty, "chest_sack_empty");
      ItemRegistry.registerWithJeiDescription(chest_sack_empty);
    }
    if (enableEnderBook) {
      ItemEnderBook book_ender = new ItemEnderBook();
      ItemRegistry.addItem(book_ender, "book_ender");
      LootTableRegistry.registerLoot(book_ender, ChestType.ENDCITY);
      LootTableRegistry.registerLoot(book_ender);
      ItemRegistry.registerWithJeiDescription(book_ender);
    }
    if (storageBagEnabled) {
      storage_bag = new ItemStorageBag();
      ItemRegistry.addItem(storage_bag, "storage_bag");
      ModCyclic.instance.events.register(storage_bag);
      LootTableRegistry.registerLoot(storage_bag);
      LootTableRegistry.registerLoot(storage_bag, ChestType.BONUS);
      ItemRegistry.registerWithJeiDescription(storage_bag);
    }
    if (enableCarbonPaper) {
      ItemPaperCarbon carbon_paper = new ItemPaperCarbon();
      ItemRegistry.addItem(carbon_paper, "carbon_paper");
      ItemRegistry.registerWithJeiDescription(carbon_paper);
    }
    if (enableProspector) {
      ItemToolProspector tool_prospector = new ItemToolProspector();
      ItemRegistry.addItem(tool_prospector, "tool_prospector");
      LootTableRegistry.registerLoot(tool_prospector);
      ItemRegistry.registerWithJeiDescription(tool_prospector);
    }
    if (enableCavefinder) {
      ItemToolSpelunker tool_spelunker = new ItemToolSpelunker();
      ItemRegistry.addItem(tool_spelunker, "tool_spelunker");
      ItemRegistry.registerWithJeiDescription(tool_spelunker);
    }
    if (enableSpawnInspect) {
      ItemToolSpawnInspect tool_spawn_inspect = new ItemToolSpawnInspect();
      ItemRegistry.addItem(tool_spawn_inspect, "tool_spawn_inspect");
      ItemRegistry.registerWithJeiDescription(tool_spawn_inspect);
    }
    if (enablePearlReuse) {
      ItemToolPearlReuse ender_pearl_reuse = new ItemToolPearlReuse(ItemToolPearlReuse.OrbType.NORMAL);
      ItemRegistry.addItem(ender_pearl_reuse, "ender_pearl_reuse");
      LootTableRegistry.registerLoot(ender_pearl_reuse);
      ItemRegistry.registerWithJeiDescription(ender_pearl_reuse);
    }
    if (enablePearlReuseMounted) {
      ItemToolPearlReuse ender_pearl_mounted = new ItemToolPearlReuse(ItemToolPearlReuse.OrbType.MOUNTED);
      ItemRegistry.addItem(ender_pearl_mounted, "ender_pearl_mounted");
      LootTableRegistry.registerLoot(ender_pearl_mounted);
      AchievementRegistry.registerItemAchievement(ender_pearl_mounted);
      ItemRegistry.registerWithJeiDescription(ender_pearl_mounted);
    }
    if (enableHarvestWeeds) {
      ItemToolHarvest tool_harvest_weeds = new ItemToolHarvest(ItemToolHarvest.HarvestType.WEEDS);
      ItemRegistry.addItem(tool_harvest_weeds, "tool_harvest_weeds");
      LootTableRegistry.registerLoot(tool_harvest_weeds, ChestType.BONUS);
      ItemRegistry.registerWithJeiDescription(tool_harvest_weeds);
    }
    if (enableToolHarvest) {
      ItemToolHarvest tool_harvest_crops = new ItemToolHarvest(ItemToolHarvest.HarvestType.CROPS);
      ItemRegistry.addItem(tool_harvest_crops, "tool_harvest_crops");
      LootTableRegistry.registerLoot(tool_harvest_crops);
      AchievementRegistry.registerItemAchievement(tool_harvest_crops);
      ItemRegistry.registerWithJeiDescription(tool_harvest_crops);
    }
    if (enableHarvestLeaves) {
      ItemToolHarvest tool_harvest_leaves = new ItemToolHarvest(ItemToolHarvest.HarvestType.LEAVES);
      ItemRegistry.addItem(tool_harvest_leaves, "tool_harvest_leaves");
      LootTableRegistry.registerLoot(tool_harvest_leaves, ChestType.BONUS);
      ItemRegistry.registerWithJeiDescription(tool_harvest_leaves);
    }
    if (enableToolPush) {
      ItemToolPiston tool_push = new ItemToolPiston();
      ItemRegistry.addItem(tool_push, "tool_push");
      ModCyclic.instance.events.register(tool_push);
      LootTableRegistry.registerLoot(tool_push);
      AchievementRegistry.registerItemAchievement(tool_push);
      ItemRegistry.registerWithJeiDescription(tool_push);
    }
    if (enableSleepingMat) {
      ItemSleepingMat sleeping_mat = new ItemSleepingMat();
      ItemRegistry.addItem(sleeping_mat, "sleeping_mat");
      ModCyclic.instance.events.register(sleeping_mat);
      LootTableRegistry.registerLoot(sleeping_mat, ChestType.BONUS);
    }
    if (enableCyclicWand) {
      ItemCyclicWand cyclic_wand_build = new ItemCyclicWand();
      ItemRegistry.addItem(cyclic_wand_build, "cyclic_wand_build");
      SpellRegistry.register(cyclic_wand_build);
      spellHud = new SpellHud();
      ModCyclic.instance.events.register(this);
      LootTableRegistry.registerLoot(cyclic_wand_build, ChestType.ENDCITY, 15);
      LootTableRegistry.registerLoot(cyclic_wand_build, ChestType.GENERIC, 1);
      AchievementRegistry.registerItemAchievement(cyclic_wand_build);
      ModCyclic.instance.setTabItemIfNull(cyclic_wand_build);
      ItemRegistry.registerWithJeiDescription(cyclic_wand_build);
    }
    if (enableWarpHomeTool) {
      ItemToolWarp tool_warp_home = new ItemToolWarp(ItemToolWarp.WarpType.BED);
      ItemRegistry.addItem(tool_warp_home, "tool_warp_home");
      LootTableRegistry.registerLoot(tool_warp_home);
      AchievementRegistry.registerItemAchievement(tool_warp_home);
      ItemRegistry.registerWithJeiDescription(tool_warp_home);
    }
    if (enableWarpSpawnTool) {
      ItemToolWarp tool_warp_spawn = new ItemToolWarp(ItemToolWarp.WarpType.SPAWN);
      ItemRegistry.addItem(tool_warp_spawn, "tool_warp_spawn");
      LootTableRegistry.registerLoot(tool_warp_spawn);
      ItemRegistry.registerWithJeiDescription(tool_warp_spawn);
    }
    if (enableSwappers) {
      ItemToolSwap tool_swap = new ItemToolSwap(WandType.NORMAL);
      ItemRegistry.addItem(tool_swap, "tool_swap");
      ModCyclic.instance.events.register(tool_swap);
      ItemToolSwap tool_swap_match = new ItemToolSwap(WandType.MATCH);
      ItemRegistry.addItem(tool_swap_match, "tool_swap_match");
      ModCyclic.instance.events.register(tool_swap_match);
      ItemRegistry.registerWithJeiDescription(tool_swap_match);
      ItemRegistry.registerWithJeiDescription(tool_swap);
    }
    if (enableRando) {
      ItemToolRandomize tool_randomize = new ItemToolRandomize();
      ItemRegistry.addItem(tool_randomize, "tool_randomize");
      ModCyclic.instance.events.register(tool_randomize);
      ItemRegistry.registerWithJeiDescription(tool_randomize);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enablePlayerLauncher = config.getBoolean("PlayerLauncher", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSoulstone = config.getBoolean("Soulstone", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableTrader = config.getBoolean("Merchant Almanac", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLever = config.getBoolean("Remote Lever", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableElevate = config.getBoolean("RodElevation", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCGlove = config.getBoolean("ClimbingGlove", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableBlockRot = config.getBoolean("BlockRotator", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablewaterSpread = config.getBoolean("WaterSpreader", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableFreezer = config.getBoolean("WaterFroster", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableFireKiller = config.getBoolean("WaterSplasher", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderSack = config.getBoolean("EnderSack", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableTorchLauncher = config.getBoolean("TorchLauncher", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    storageBagEnabled = config.getBoolean("StorageBag", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEnderBook = config.getBoolean("EnderBook", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableWarpHomeTool = config.getBoolean("EnderWingPrime", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableWarpSpawnTool = config.getBoolean("EnderWing", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSpawnInspect = config.getBoolean("SpawnDetector", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePearlReuse = config.getBoolean("EnderOrb", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHarvestWeeds = config.getBoolean("BrushScythe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableToolHarvest = config.getBoolean("HarvestScythe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHarvestLeaves = config.getBoolean("TreeScythe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableToolPush = config.getBoolean("PistonScepter", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSleepingMat = config.getBoolean("SleepingMat", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCyclicWand = config.getBoolean("CyclicWand", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableProspector = config.getBoolean("Prospector", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCavefinder = config.getBoolean("Cavefinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSwappers = config.getBoolean("ExchangeScepters", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableRando = config.getBoolean("BlockRandomizer", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePearlReuseMounted = config.getBoolean("EnderOrbMounted", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCarbonPaper = config.getBoolean("CarbonPaper", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableChestSack = config.getBoolean("ChestSack", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableStirrups = config.getBoolean("Stirrups", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    String[] deflist = new String[] { "minecraft:mob_spawner", "minecraft:obsidian" };
    ItemToolSwap.swapBlacklist = config.getStringList("ExchangeSceptersBlacklist", Const.ConfigCategory.items, deflist, "Blocks that will not be broken by the exchange scepters.  It will also not break anything that is unbreakable (such as bedrock), regardless of if its in this list or not.  ");
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onMouseInput(MouseEvent event) {
    EntityPlayer player = Minecraft.getMinecraft().player;
    if (!player.isSneaking() || event.getDwheel() == 0) { return; }
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
    if (wand == ItemStack.EMPTY) { return; }
    //if theres only one spell, do nothing
    if (SpellRegistry.getSpellbook(wand) == null || SpellRegistry.getSpellbook(wand).size() <= 1) { return; }
    if (event.getDwheel() < 0) {
      ModCyclic.network.sendToServer(new PacketSpellShiftRight());
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.bip);
    }
    else if (event.getDwheel() > 0) {
      ModCyclic.network.sendToServer(new PacketSpellShiftLeft());
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.bip);
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRenderTextOverlay(RenderGameOverlayEvent.Text event) {
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(Minecraft.getMinecraft().player);
    // special new case: no hud for this type
    if (wand != ItemStack.EMPTY) {
      spellHud.drawSpellWheel(wand);
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onRender(RenderGameOverlayEvent.Post event) {
    if (event.isCanceled() || event.getType() != ElementType.EXPERIENCE) { return; }
    EntityPlayer effectivePlayer = Minecraft.getMinecraft().player;
    ItemStack heldWand = UtilSpellCaster.getPlayerWandIfHeld(effectivePlayer);
    if (heldWand == ItemStack.EMPTY) { return; }
    int itemSlot = ItemCyclicWand.BuildType.getSlot(heldWand);
    ItemStack current = InventoryWand.getFromSlot(heldWand, itemSlot);
    if (current != ItemStack.EMPTY) {
      //THE ITEM INSIDE THE BUILDY WHEEL
      int leftOff = 7, rightOff = -26, topOff = 36, bottOff = -2;
      int xmain = RenderLoc.locToX(ItemToolsModule.renderLocation, leftOff, rightOff);
      int ymain = RenderLoc.locToY(ItemToolsModule.renderLocation, topOff, bottOff);
      ModCyclic.proxy.renderItemOnScreen(current, xmain, ymain);
      //      ModCyclic.proxy.renderItemOnScreen(current, RenderLoc.xoffset - 1, RenderLoc.ypadding + RenderLoc.spellSize * 2);
    }
  }
  //TODO: refactor this
  public static enum RenderLoc {
    TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT;
    private static final int yPadding = 6;
    private static final int xPadding = 6;//was 30 if manabar is showing
    private static final int spellSize = 16;
    @SideOnly(Side.CLIENT)
    public static int locToX(RenderLoc loc, int leftOffset, int rightOffset) {
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      switch (loc) {
        case BOTTOMLEFT:
        case TOPLEFT:
          return RenderLoc.xPadding + leftOffset;
        case BOTTOMRIGHT:
        case TOPRIGHT:
          return res.getScaledWidth() - RenderLoc.xPadding + rightOffset;
      }
      return 0;
    }
    @SideOnly(Side.CLIENT)
    public static int locToY(RenderLoc loc, int topOffset, int bottomOffset) {
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      switch (ItemToolsModule.renderLocation) {
        case BOTTOMLEFT:
        case BOTTOMRIGHT:
          return res.getScaledHeight() - RenderLoc.spellSize - RenderLoc.yPadding + bottomOffset;
        case TOPLEFT:
        case TOPRIGHT:
          return RenderLoc.yPadding + topOffset;
        default:
        break;
      }
      return 0;
    }
  }
  private class SpellHud {
    private int ymain = RenderLoc.yPadding;
    private int xmain;
    @SideOnly(Side.CLIENT)
    public void drawSpellWheel(ItemStack wand) {
      int leftOff = 8, rightOff = -26, topOff = 0, bottOff = -38;
      xmain = RenderLoc.locToX(ItemToolsModule.renderLocation, leftOff, rightOff);
      ymain = RenderLoc.locToY(ItemToolsModule.renderLocation, topOff, bottOff);
      EntityPlayer player = Minecraft.getMinecraft().player;
      if (SpellRegistry.getSpellbook(wand) == null || SpellRegistry.getSpellbook(wand).size() <= 1) { return; }
      ISpell spellCurrent = UtilSpellCaster.getPlayerCurrentISpell(player);
      //if theres only one spell, do not do the rest eh
      drawCurrentSpell(player, spellCurrent);
      drawNextSpells(player, spellCurrent);
      drawPrevSpells(player, spellCurrent);
    }
    private void drawCurrentSpell(EntityPlayer player, ISpell spellCurrent) {
      UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplay(), xmain, ymain, RenderLoc.spellSize);
    }
    private void drawPrevSpells(EntityPlayer player, ISpell spellCurrent) {
      ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
      ISpell prev = SpellRegistry.prev(wand, spellCurrent);
      if (prev != null) {
        int x = xmain + 9;
        int y = ymain + RenderLoc.spellSize;
        int dim = RenderLoc.spellSize / 2;
        UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);
        prev = SpellRegistry.prev(wand, prev);
        if (prev != null) {
          x += 5;
          y += 14;
          dim -= 2;
          UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);
          prev = SpellRegistry.prev(wand, prev);
          if (prev != null) {
            x += 3;
            y += 10;
            dim -= 2;
            UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);
            prev = SpellRegistry.prev(wand, prev);
            if (prev != null) {
              x += 2;
              y += 10;
              dim -= 1;
              UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);
            }
          }
        }
      }
    }
    private void drawNextSpells(EntityPlayer player, ISpell spellCurrent) {
      ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
      ISpell next = SpellRegistry.next(wand, spellCurrent);
      if (next != null) {
        int x = xmain - 5;
        int y = ymain + RenderLoc.spellSize;
        int dim = RenderLoc.spellSize / 2;
        UtilTextureRender.drawTextureSquare(next.getIconDisplay(), x, y, dim);
        ISpell next2 = SpellRegistry.next(wand, next);
        if (next2 != null) {
          x -= 2;
          y += 14;
          dim -= 2;
          UtilTextureRender.drawTextureSquare(next2.getIconDisplay(), x, y, dim);
          ISpell next3 = SpellRegistry.next(wand, next2);
          if (next3 != null) {
            x -= 2;
            y += 10;
            dim -= 2;
            UtilTextureRender.drawTextureSquare(next3.getIconDisplay(), x, y, dim);
            ISpell next4 = SpellRegistry.next(wand, next3);
            if (next4 != null) {
              x -= 2;
              y += 10;
              dim -= 1;
              UtilTextureRender.drawTextureSquare(next4.getIconDisplay(), x, y, dim);
            }
          }
        }
      }
    }
  }
}
