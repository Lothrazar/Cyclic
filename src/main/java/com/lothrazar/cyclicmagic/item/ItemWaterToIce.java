package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWaterToIce extends BaseTool implements IHasRecipe {
  private static final int DURABILITY = 256;
  //  private static final int COOLDOWN = 10;
  private static final int RADIUS = 2;
  public ItemWaterToIce() {
    super(DURABILITY);
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    if (pos == null) { return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ); }
    if (side != null) {
      pos = pos.offset(side);
    }
    if (spreadWaterFromCenter(world, pos.offset(side))) {
      super.onUse(stack, player, world, hand);
    }
    return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (spreadWaterFromCenter(world, player.getPosition().offset(player.getHorizontalFacing()))) {
      super.onUse(stack, player, world, hand); //player.getCooldownTracker().setCooldown(this, COOLDOWN);
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
  }
  private boolean spreadWaterFromCenter(World world, BlockPos posCenter) {
    int count = 0;
    List<BlockPos> water = UtilWorld.findBlocks(world, posCenter, Blocks.WATER, RADIUS);
    water.addAll(UtilWorld.findBlocks(world, posCenter, Blocks.FLOWING_WATER, RADIUS));
    for (BlockPos pos : water) {
      world.setBlockState(pos, Blocks.ICE.getDefaultState(), 3);
      //       world.markChunkDirty(pos, null);
      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, pos);
      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, pos.up());
      count++;
    }
    boolean success = count > 0;
    if (success) {//particles are on each location, sound is just once
      UtilSound.playSound(world, posCenter, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.BLOCKS);
    }
    return success;
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "wdw",
        "iwi",
        " o ",
        'w', new ItemStack(Items.WATER_BUCKET),
        'd', new ItemStack(Items.NETHERBRICK),
        'o', new ItemStack(Blocks.OBSIDIAN),
        'i', new ItemStack(Blocks.ICE));
  }
}
