package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class ShearsMaterial extends ShearsItem {

  public ShearsMaterial(Properties builder) {
    super(builder);
    DispenserBlock.registerBehavior(this, new ShearsDispenseItemBehavior());
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    Block block = state.getBlock();
    if (block == Blocks.MELON
        || block == Blocks.PUMPKIN
        || state.is(BlockTags.CARPETS)
        || state.is(BlockTags.WOOL)
        || state.is(BlockTags.LEAVES)) {
      return 15F;
    }
    return super.getDestroySpeed(stack, state);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPos blockPos = context.getClickedPos();
    BlockState blockState = context.getLevel().getBlockState(blockPos);
    Level worldIn = context.getLevel();
    Player player = context.getPlayer();
    if (blockState.getBlock() instanceof BeehiveBlock && blockState.getValue(BeehiveBlock.HONEY_LEVEL) >= 5) {
      //replicate the behavior of BeehiveBlock#onBlockActivated
      worldIn.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.NEUTRAL, 1.0F, 1.0F);
      BeehiveBlock.dropHoneycomb(worldIn, blockPos);
      worldIn.setBlockAndUpdate(blockPos, blockState.setValue(BeehiveBlock.HONEY_LEVEL, 0));
      UtilItemStack.damageItem(player, context.getItemInHand());
      return InteractionResult.SUCCESS;
    }
    return super.useOn(context);
  }
}
