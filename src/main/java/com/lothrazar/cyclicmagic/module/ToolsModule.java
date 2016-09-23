package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.item.tool.*;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap.WandType;
import com.lothrazar.cyclicmagic.item.ItemSleepingMat;
import com.lothrazar.cyclicmagic.net.PacketSpellShiftLeft;
import com.lothrazar.cyclicmagic.net.PacketSpellShiftRight;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToolsModule extends BaseModule {
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
  @Override
  public void onInit() {
    if (enableProspector) {
      ItemRegistry.tool_prospector = new ItemToolProspector();
      ItemRegistry.addItem(ItemRegistry.tool_prospector, "tool_prospector");
    }
    if (enableCavefinder) {
      ItemRegistry.tool_spelunker = new ItemToolSpelunker();
      ItemRegistry.addItem(ItemRegistry.tool_spelunker, "tool_spelunker");
    }
    if (enableSpawnInspect) {
      ItemRegistry.tool_spawn_inspect = new ItemToolSpawnInspect();
      ItemRegistry.addItem(ItemRegistry.tool_spawn_inspect, "tool_spawn_inspect");
    }
    if (enablePearlReuse) {
      ItemRegistry.ender_pearl_reuse = new ItemToolPearlReuse();
      ItemRegistry.addItem(ItemRegistry.ender_pearl_reuse, "ender_pearl_reuse");
    }
    if (enableHarvestWeeds) {
      ItemRegistry.tool_harvest_weeds = new ItemToolHarvest(ItemToolHarvest.HarvestType.WEEDS);
      ItemRegistry.addItem(ItemRegistry.tool_harvest_weeds, "tool_harvest_weeds");
    }
    if (enableToolHarvest) {
      ItemRegistry.tool_harvest_crops = new ItemToolHarvest(ItemToolHarvest.HarvestType.CROPS);
      ItemRegistry.addItem(ItemRegistry.tool_harvest_crops, "tool_harvest_crops");
    }
    if (enableHarvestLeaves) {
      ItemRegistry.tool_harvest_leaves = new ItemToolHarvest(ItemToolHarvest.HarvestType.LEAVES);
      ItemRegistry.addItem(ItemRegistry.tool_harvest_leaves, "tool_harvest_leaves");
    }
    if (enableToolPush) {
      ItemRegistry.tool_push = new ItemToolPush();
      ItemRegistry.addItem(ItemRegistry.tool_push, "tool_push");
      ModMain.instance.events.addEvent(ItemRegistry.tool_push);
    }
    if (enableSleepingMat) {
      ItemRegistry.sleeping_mat = new ItemSleepingMat();
      ItemRegistry.addItem(ItemRegistry.sleeping_mat, "sleeping_mat");
      ModMain.instance.events.addEvent(ItemRegistry.sleeping_mat);
    }
    if (enableCyclicWand) {
      ItemRegistry.cyclic_wand_build = new ItemCyclicWand();
      ItemRegistry.addItem(ItemRegistry.cyclic_wand_build, "cyclic_wand_build");
      SpellRegistry.register();
      spellHud = new SpellHud();
      ModMain.instance.events.addEvent(this);
    }
    if (enableWarpHomeTool) {
      ItemRegistry.tool_warp_home = new ItemToolWarp(ItemToolWarp.WarpType.BED);
      ItemRegistry.addItem(ItemRegistry.tool_warp_home, "tool_warp_home");
    }
    if (enableWarpSpawnTool) {
      ItemRegistry.tool_warp_spawn = new ItemToolWarp(ItemToolWarp.WarpType.SPAWN);
      ItemRegistry.addItem(ItemRegistry.tool_warp_spawn, "tool_warp_spawn");
    }
    if (enableSwappers) {
      ItemToolSwap tool_swap = new ItemToolSwap(WandType.NORMAL);
      ItemRegistry.addItem(tool_swap, "tool_swap");
      ModMain.instance.events.addEvent(tool_swap);
      ItemToolSwap tool_swap_match = new ItemToolSwap(WandType.MATCH);
      ItemRegistry.addItem(tool_swap_match, "tool_swap_match");
      ModMain.instance.events.addEvent(tool_swap_match);
    }
    if (enableRando) {
      ItemToolRandomize tool_randomize = new ItemToolRandomize();
      ItemRegistry.addItem(tool_randomize, "tool_randomize");
      ModMain.instance.events.addEvent(tool_randomize);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    //TODO: this config should be INSIDE the item mat
    ItemSleepingMat.doPotions = config.getBoolean("SleepingMatPotions", Const.ConfigCategory.items, true, "False will disable the potion effects given by the Sleeping Mat");
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
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onMouseInput(MouseEvent event) {
    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    if (!player.isSneaking() || event.getDwheel() == 0) { return; }
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
    if (wand == null) { return; }
    //if theres only one spell, do nothing
    if (SpellRegistry.getSpellbook(wand) == null || SpellRegistry.getSpellbook(wand).size() <= 1) { return; }
    if (event.getDwheel() < 0) {
      ModMain.network.sendToServer(new PacketSpellShiftRight());
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.bip);
    }
    else if (event.getDwheel() > 0) {
      ModMain.network.sendToServer(new PacketSpellShiftLeft());
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.bip);
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRenderTextOverlay(RenderGameOverlayEvent.Text event) {
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(Minecraft.getMinecraft().thePlayer);
    // special new case: no hud for this type
    if (wand != null) {
      spellHud.drawSpellWheel(wand);
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onRender(RenderGameOverlayEvent.Post event) {
    if (event.isCanceled() || event.getType() != ElementType.EXPERIENCE) { return; }
    EntityPlayer effectivePlayer = Minecraft.getMinecraft().thePlayer;
    ItemStack heldWand = UtilSpellCaster.getPlayerWandIfHeld(effectivePlayer);
    if (heldWand == null) { return; }
    int itemSlot = ItemCyclicWand.BuildType.getSlot(heldWand);
    ItemStack current = InventoryWand.getFromSlot(heldWand, itemSlot);
    if (current != null) {
      ModMain.proxy.renderItemOnScreen(current, SpellHud.xoffset - 1, SpellHud.ymain + SpellHud.spellSize * 2);
    }
    //    RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    //    GlStateManager.color(1, 1, 1, 1);
    //    RenderHelper.enableStandardItemLighting();
    //    RenderHelper.enableGUIStandardItemLighting();
    //    if (current != null) {
    //      itemRender.renderItemAndEffectIntoGUI(current,
    //          SpellHud.xoffset - 1, SpellHud.ymain + SpellHud.spellSize * 2);
    //    }
    //    RenderHelper.disableStandardItemLighting();
  }
  private class SpellHud {
    private static final int xoffset = 14;//was 30 if manabar is showing
    private static final int ymain = 6;
    private static final int spellSize = 16;
    private int xmain;
    @SideOnly(Side.CLIENT)
    public void drawSpellWheel(ItemStack wand) {
      if (SpellRegistry.renderOnLeft) {
        xmain = xoffset;
      }
      else {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        xmain = res.getScaledWidth() - xoffset;
      }
      EntityPlayer player = Minecraft.getMinecraft().thePlayer;
      ISpell spellCurrent = UtilSpellCaster.getPlayerCurrentISpell(player);
      //if theres only one spell, do not do the rest eh
      if (SpellRegistry.getSpellbook(wand) == null || SpellRegistry.getSpellbook(wand).size() <= 1) { return; }
      drawCurrentSpell(player, spellCurrent);
      drawNextSpells(player, spellCurrent);
      drawPrevSpells(player, spellCurrent);
    }
    private void drawCurrentSpell(EntityPlayer player, ISpell spellCurrent) {
      UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplay(), xmain, ymain, spellSize);
    }
    private void drawPrevSpells(EntityPlayer player, ISpell spellCurrent) {
      ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
      ISpell prev = SpellRegistry.prev(wand, spellCurrent);
      if (prev != null) {
        int x = xmain + 9;
        int y = ymain + spellSize;
        int dim = spellSize / 2;
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
        int y = ymain + spellSize;
        int dim = spellSize / 2;
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
