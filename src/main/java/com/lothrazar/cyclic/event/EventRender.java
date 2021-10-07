package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.data.RelativeShape;
import com.lothrazar.cyclic.item.OreProspector;
import com.lothrazar.cyclic.item.builder.BuildStyle;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.builder.PacketSwapBlock;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.item.random.RandomizerItem;
import com.lothrazar.cyclic.util.UtilPlayer;
import com.lothrazar.cyclic.util.UtilRender;
import com.lothrazar.cyclic.util.UtilWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventRender {

  @SubscribeEvent
  public void overlay(RenderGameOverlayEvent.Post event) {
    //Build scepter feature : render selected blockstate in cross hair
    Player player = Minecraft.getInstance().player;
    Minecraft mc = Minecraft.getInstance();
    System.out.println("TESTME : ElementType.CROSSHAIRS is gone deleted");
    if (event.getType() == ElementType.ALL) {
      ItemStack itemStackHeld = BuilderItem.getIfHeld(player);
      if (itemStackHeld.getItem() instanceof BuilderItem) {
        //
        BlockState targetState = BuilderActionType.getBlockState(itemStackHeld);
        if (targetState != null) {
          //ok still 
          drawStack(new ItemStack(targetState.getBlock()));
          int slot = UtilPlayer.getFirstSlotWithBlock(player, targetState);
          if (slot < 0) {
            //nothing found
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();
            drawString(event.getMatrixStack(), "" + 0, width / 2 + 16, height / 2 + 12);
          }
        }
      }
    }
    else if (event.getType() == ElementType.TEXT) {
      int height = mc.getWindow().getGuiScaledHeight();
      //      int width = mc.getMainWindow().getScaledWidth();
      //      //
      //      // 
      //      int seconds = (int) (player.world.getDayTime() / 20);
      //      int minutes = seconds / 60;
      //      int hours = minutes / 60;
      //      //8pm = 20000
      //      //noon = 12000
      //      //8am = 8000
      //      drawString(event.getMatrixStack(), "" + player.getPosition().toString(), width - 50, height - 30);
      //      drawString(event.getMatrixStack(), "" + player.world.getDayTime(), width - 50, height - 60);
      // now files
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
      if (datFile.flyTicks > 0) {
        int sec = datFile.flyTicks / 20;
        drawString(event.getMatrixStack(), "flight " + sec, 10, height - 30);
      }
      if (datFile.spectatorTicks > 0) {
        int sec = datFile.spectatorTicks / 20;
        drawString(event.getMatrixStack(), "noClip " + sec, 10, height - 10);
      }
    }
  }

  public static void drawString(PoseStack ms, String str, int x, int y) {
    Minecraft mc = Minecraft.getInstance();
    mc.font.draw(ms, str, x, y, 0xFFFFFF);
  }

  public static void drawStack(ItemStack stack) {
    Minecraft mc = Minecraft.getInstance();
    int width = mc.getWindow().getGuiScaledWidth();
    int height = mc.getWindow().getGuiScaledHeight();
    mc.getItemRenderer().renderAndDecorateItem(stack, width / 2, height / 2);
  }
  //  @SubscribeEvent
  //  public void addCustomButtonToInventory(GuiScreenEvent.InitGuiEvent.Post event) {
  //   // TODO: invo cake
  //    PlayerEntity player = Minecraft.getInstance().player;
  //    if (event.getGui() instanceof HorseInventoryScreen
  //        && player != null
  //        && player.getRidingEntity() != null) {
  //      Entity liv = player.getRidingEntity();
  //      if (liv.getPersistentData().contains(ItemHorseEnder.NBT_KEYACTIVE)
  //          && liv.getPersistentData().getInt(ItemHorseEnder.NBT_KEYACTIVE) > 0) {
  //        //
  //        int ct = liv.getPersistentData().getInt(ItemHorseEnder.NBT_KEYACTIVE);
  //        ExtendedButton bt2 = new ExtendedButton(event.getGui().width / 2 + 68,
  //            event.getGui().height / 2 - 80,
  //            //cyclic.carrot_ender.charges
  //            18, 14, new StringTextComponent("" + ct), b -> {
  //              //              if(event.i)
  //              UtilChat.addChatMessage(player, "item.cyclic.carrot_ender.tooltip");
  //              //                  test);
  //            });
  //        event.addWidget(bt2);
  //      }
  //    }
  //  }

  ///////////////////// asdfasdf TODO REFACTOR THIS 
  @SubscribeEvent
  public void renderOverlay(RenderWorldLastEvent event) {
    Player player = Minecraft.getInstance().player;
    if (player == null) {
      return;
    }
    Level world = player.level;
    double range = 6F;
    float alpha = 0.125F * 2;
    Map<BlockPos, Color> renderCubes = new HashMap<>();
    // could refactor here, three cases
    ///////////////////// BuilderItem
    ItemStack stack = BuilderItem.getIfHeld(player);
    if (stack.getItem() instanceof BuilderItem) {
      BlockHitResult lookingAt = (BlockHitResult) player.pick(range, 0F, false);
      if (!world.isEmptyBlock(lookingAt.getBlockPos())) {
        BlockPos pos = lookingAt.getBlockPos();
        BuildStyle buildStyle = ((BuilderItem) stack.getItem()).style;
        if (buildStyle.isOffset() && lookingAt.getDirection() != null) {
          pos = pos.relative(lookingAt.getDirection());
        }
        alpha = 0.4F;
        //now the item has a build area
        List<BlockPos> coordinates = PacketSwapBlock.getSelectedBlocks(world, pos, BuilderItem.getActionType(stack), lookingAt.getDirection(), buildStyle);
        for (BlockPos coordinate : coordinates) {
          renderCubes.put(coordinate, ClientConfigCyclic.getColor(stack));
        }
      }
    }
    ///////////////////// RandomizerItem
    stack = RandomizerItem.getIfHeld(player);
    if (stack.getItem() instanceof RandomizerItem) {
      BlockHitResult lookingAt = UtilRender.getLookingAt(player, (int) range);
      if (player.level.getBlockState(lookingAt.getBlockPos()) == Blocks.AIR.defaultBlockState()) {
        return;
      }
      List<BlockPos> coords = RandomizerItem.getPlaces(lookingAt.getBlockPos(), lookingAt.getDirection());
      for (BlockPos e : coords) {
        renderCubes.put(e, RandomizerItem.canMove(player.level.getBlockState(e), player.level, e) ? ClientConfigCyclic.getColor(stack) : Color.RED);
      }
    }
    stack = OreProspector.getIfHeld(player);
    if (stack.getItem() instanceof OreProspector) {
      List<BlockPosDim> coords = OreProspector.getPosition(stack);
      for (BlockPosDim loc : coords) {
        if (loc != null) {
          if (loc.getDimension() == null ||
              loc.getDimension().equalsIgnoreCase(UtilWorld.dimensionToString(world))) {
            UtilRender.createBox(event.getMatrixStack(), loc.getPos());
          }
        }
      }
    }
    ///////////////////// LocationGpsItem
    stack = player.getMainHandItem();
    if (stack.getItem() instanceof LocationGpsCard) {
      BlockPosDim loc = LocationGpsCard.getPosition(stack);
      if (loc != null) {
        if (loc.getDimension() == null ||
            loc.getDimension().equalsIgnoreCase(UtilWorld.dimensionToString(world))) {
          renderCubes.put(loc.getPos(), ClientConfigCyclic.getColor(stack));
        }
      }
    }
    ///////////////////////////////////////ShapeCard
    if (stack.getItem() instanceof ShapeCard) {
      RelativeShape shape = RelativeShape.read(stack);
      if (shape != null) {
        BlockPos here = player.blockPosition();
        //TODO: offsetTo
        //        shape.offsetTo(pos)
        for (BlockPos s : shape.getShape()) {
          renderCubes.put(here.offset(s), ClientConfigCyclic.getColor(stack));
        }
      }
    }
    // other items added here
    //
    //render the pos->colour map
    if (renderCubes.keySet().size() > 0) {
      UtilRender.renderColourCubes(event, renderCubes, alpha);
    }
  }
}
