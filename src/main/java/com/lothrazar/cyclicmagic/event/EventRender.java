package com.lothrazar.cyclicmagic.event;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.cyclicwand.InventoryWand;
import com.lothrazar.cyclicmagic.component.cyclicwand.ItemCyclicWand;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.ItemBuildSwapper;
import com.lothrazar.cyclicmagic.item.ItemBuildSwapper.ActionType;
import com.lothrazar.cyclicmagic.item.ItemBuildSwapper.WandType;
import com.lothrazar.cyclicmagic.item.ItemChorusGlowing;
import com.lothrazar.cyclicmagic.net.PacketSwapBlock;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilChat;
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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventRender {
  public static SpellHud spellHud;
  //TODO: loc should be a property of hud, not standalone
  public static RenderLoc renderLocation;
  public EventRender() {
    spellHud = new SpellHud();
  }
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
    if (heldItem.getItem() instanceof ItemBuildSwapper) {
      RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
      if (mouseOver != null && mouseOver.getBlockPos() != null && mouseOver.sideHit != null) {
        IBlockState state = p.world.getBlockState(mouseOver.getBlockPos());
        Block block = state.getBlock();
        if (block != null && block.getMaterial(state) != Material.AIR) {
          ItemBuildSwapper wandInstance = (ItemBuildSwapper) heldItem.getItem();
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
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRenderTextOverlay(RenderGameOverlayEvent.Text event) {
    EntityPlayer player = ModCyclic.proxy.getClientPlayer();
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    if (props != null && props.getTODO() != null && props.getTODO().length() > 0) {
      event.getRight().add(props.getTODO());
    }
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
    // special new case: no hud for this type
    if (!wand.isEmpty()) {
      spellHud.drawSpellWheel(wand);
    }
    int flyingTicks = props.getFlyingTimer();
    if (flyingTicks > 0) {
      int secs = flyingTicks / Const.TICKS_PER_SEC;
      String time = UtilChat.formatSecondsToMinutes(secs);
      event.getRight().add(UtilChat.lang("screentext.flying.seconds") + time);
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onRender(RenderGameOverlayEvent.Post event) {
    if (event.isCanceled() || event.getType() != ElementType.EXPERIENCE) { return; }
    EntityPlayer effectivePlayer = ModCyclic.proxy.getClientPlayer();
    ItemStack heldWand = UtilSpellCaster.getPlayerWandIfHeld(effectivePlayer);
    if (heldWand.isEmpty()) { return; }
    int itemSlot = ItemCyclicWand.BuildType.getSlot(heldWand);
    ItemStack current = InventoryWand.getFromSlot(heldWand, itemSlot);
    if (!current.isEmpty()) {
      //THE ITEM INSIDE THE BUILDY WHEEL
      int leftOff = 7, rightOff = -26, topOff = 36, bottOff = -2;
      int xmain = RenderLoc.locToX(renderLocation, leftOff, rightOff);
      int ymain = RenderLoc.locToY(renderLocation, topOff, bottOff);
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
      switch (renderLocation) {
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
      xmain = RenderLoc.locToX(renderLocation, leftOff, rightOff);
      ymain = RenderLoc.locToY(renderLocation, topOff, bottOff);
      EntityPlayer player = ModCyclic.proxy.getClientPlayer();
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
