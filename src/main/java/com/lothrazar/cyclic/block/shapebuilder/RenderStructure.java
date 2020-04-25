package com.lothrazar.cyclic.block.shapebuilder;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.render.FakeBlockRenderTypes;
import com.lothrazar.cyclic.util.UtilRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class RenderStructure extends TileEntityRenderer<TileStructure> {

  public RenderStructure(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileStructure te, float v, MatrixStack matrix,
      IRenderTypeBuffer ibuffer, int partialTicks, int destroyStage) {
    IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (inv == null) {
      return;
    }
    boolean doRender = 1 == te.getField(TileStructure.Fields.RENDER.ordinal());
    ItemStack stack = inv.getStackInSlot(0);
    if (stack.isEmpty()) {
      renderOutline(te, matrix);
    }
    else {
      renderBlank(te, matrix, stack);
    }
  }

  private void renderBlank(TileStructure te, MatrixStack matrix, ItemStack stack) {
    World world = ModCyclic.proxy.getClientWorld();
    BlockState renderBlockState = Block.getBlockFromItem(stack.getItem()).getDefaultState();
    //render 
    Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
    //
    double range = 6F;
    BlockRayTraceResult lookingAt = (BlockRayTraceResult) ModCyclic.proxy.getClientPlayer().pick(range, 0F, false);
    if (world.isAirBlock(lookingAt.getPos())) {
      return;
    }
    Minecraft mc = Minecraft.getInstance();
    IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
    IVertexBuilder builder = buffer.getBuffer(FakeBlockRenderTypes.FAKE_BLOCK);//i guess?
    BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
    matrix.push();
    BlockPos playerPos = te.getPos();//mc.gameRenderer.getActiveRenderInfo().getProjectedView();
    matrix.translate(-playerPos.getX(), -playerPos.getY(), -playerPos.getZ());
    for (BlockPos coordinate : te.getShape()) {
      if (!world.isAirBlock(coordinate)) {
        continue;
      }
      float x = coordinate.getX();
      float y = coordinate.getY();
      float z = coordinate.getZ();
      //      ModCyclic.LOGGER.info("y value of build " + y);
      matrix.push();
      matrix.translate(x, y, z);
      //
      //shrink it up
      matrix.translate(-0.0005f, -0.0005f, -0.0005f);
      matrix.scale(1.001f, 1.001f, 1.001f);
      //
      //      UtilWorld.OutlineRenderer.renderHighLightedBlocksOutline(builder, x, y, z, r / 255.0f, g / 255.0f, b / 255.0f, 1.0f); // .02f
      IBakedModel ibakedmodel = dispatcher.getModelForState(renderBlockState);
      BlockColors blockColors = Minecraft.getInstance().getBlockColors();
      int color = blockColors.getColor(renderBlockState, world, coordinate, 0);
      float red = (color >> 16 & 255) / 255.0F;
      float green = (color >> 8 & 255) / 255.0F;
      float blue = (color & 255) / 255.0F;
      float alpha = 0.7F;
      if (renderBlockState.getRenderType() == BlockRenderType.MODEL) {
        for (Direction direction : Direction.values()) {
          UtilRender.renderModelBrightnessColorQuads(matrix.getLast(), builder, red, green, blue, alpha,
              ibakedmodel.getQuads(renderBlockState, direction, new Random(MathHelper.getPositionRandom(coordinate)), EmptyModelData.INSTANCE), 15728640, 655360 / 2);
        }
        UtilRender.renderModelBrightnessColorQuads(matrix.getLast(), builder, red, green, blue, alpha,
            ibakedmodel.getQuads(renderBlockState, null, new Random(MathHelper.getPositionRandom(coordinate)), EmptyModelData.INSTANCE),
            15728640, 655360);
      }
      matrix.pop();
    }
    ///
    matrix.pop();
  }

  private void renderOutline(TileStructure te, MatrixStack matrix) {
    //    IRenderTypeBuffer.getImpl(ibuffer);
    final Minecraft mc = Minecraft.getInstance();
    IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
    World world = ModCyclic.proxy.getClientWorld();
    List<BlockPos> coords = te.getShape();
    matrix.push();
    BlockPos view = te.getPos();//mc.gameRenderer.getActiveRenderInfo().getProjectedView();
    matrix.translate(-view.getX(), -view.getY(), -view.getZ());
    IVertexBuilder builder;
    builder = buffer.getBuffer(FakeBlockRenderTypes.SOLID_COLOUR);
    for (BlockPos e : coords) {
      if (!world.isAirBlock(e)) {
        continue;
      }
      matrix.push();
      matrix.translate(e.getX(), e.getY(), e.getZ());
      matrix.translate(-0.005f, -0.005f, -0.005f);
      float scale = 0.7F;
      matrix.scale(scale, scale, scale);
      matrix.rotate(Vector3f.YP.rotationDegrees(-90.0F));
      Matrix4f positionMatrix = matrix.getLast().getMatrix();
      Color color = Color.BLUE;
      UtilRender.renderCube(positionMatrix, builder, e, color);
      matrix.pop();
    }
    matrix.pop();
    //    RenderSystem.disableDepthTest();
    buffer.finish(FakeBlockRenderTypes.SOLID_COLOUR);
  }
}
