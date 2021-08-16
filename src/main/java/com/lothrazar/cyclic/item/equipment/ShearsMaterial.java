package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.BeehiveDispenseBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShearsMaterial extends ShearsItem {

  public ShearsMaterial(Properties builder) {
    super(builder);
    DispenserBlock.registerDispenseBehavior(this, new BeehiveDispenseBehavior());
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    Block block = state.getBlock();
    if (block == Blocks.MELON
        || block == Blocks.PUMPKIN
        || block.isIn(BlockTags.CARPETS)
        || block.isIn(BlockTags.WOOL)
        || state.isIn(BlockTags.LEAVES)) {
      return 15F;
    }
    return super.getDestroySpeed(stack, state);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    BlockPos blockPos = context.getPos();
    BlockState blockState = context.getWorld().getBlockState(blockPos);
    World worldIn = context.getWorld();
    PlayerEntity player = context.getPlayer();
    if (blockState.getBlock() instanceof BeehiveBlock && blockState.get(BeehiveBlock.HONEY_LEVEL) >= 5) {
      //replicate the behavior of BeehiveBlock#onBlockActivated
      worldIn.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.NEUTRAL, 1.0F, 1.0F);
      BeehiveBlock.dropHoneyComb(worldIn, blockPos);
      worldIn.setBlockState(blockPos, blockState.with(BeehiveBlock.HONEY_LEVEL, 0));
      UtilItemStack.damageItem(player, context.getItem());
      return ActionResultType.SUCCESS;
    }
    return super.onItemUse(context);
  }
}
