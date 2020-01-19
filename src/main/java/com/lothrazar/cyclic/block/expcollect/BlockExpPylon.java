package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.item.ExpItemGain;
import com.lothrazar.cyclic.util.UtilStuff;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockExpPylon extends BlockBase {

  public BlockExpPylon(Properties properties) {
    super(properties.hardnessAndResistance(1.8F).sound(SoundType.GLASS));
  }

  @Override
  public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
    if (!world.isRemote && hand == Hand.MAIN_HAND) {
      ItemStack held = player.getHeldItem(hand);
      if (held.isEmpty() || player.isCrouching()) {
        TileExpPylon tile = (TileExpPylon) world.getTileEntity(pos);
        UtilStuff.messageStatus(player, "" + tile.getStoredXp());
        return ActionResultType.SUCCESS;
      }
      if (held.getItem() == Items.SUGAR) {
        TileExpPylon tile = (TileExpPylon) world.getTileEntity(pos);
        if (tile.drainStoredXp(ExpItemGain.EXP_PER_FOOD)) {
          //do it
          held.shrink(1);
          player.dropItem(new ItemStack(CyclicRegistry.Items.experience_food), true);
          return ActionResultType.SUCCESS;
        }
        else {
          UtilStuff.messageStatus(player, getTranslationKey() + "notenough");
        }
      }
    }
    return ActionResultType.PASS;
  }

  @Override
  public BlockRenderType getRenderType(BlockState bs) {
    return BlockRenderType.INVISIBLE;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileExpPylon();
  }
}
