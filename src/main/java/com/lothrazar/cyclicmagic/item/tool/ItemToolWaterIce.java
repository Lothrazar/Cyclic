package com.lothrazar.cyclicmagic.item.tool;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolWaterIce extends BaseTool implements IHasRecipe {
  private static final int DURABILITY = 256;
  private static final int COOLDOWN = 10;
  private static final int RADIUS = 2;
  public ItemToolWaterIce() {
    super(DURABILITY);
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (pos == null) { return super.onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ); }
    if (side != null) {
      pos = pos.offset(side);
    }
    if (spreadWaterFromCenter(world, pos.offset(side))) {
      super.onUse(stack, player, world, hand);
    }
    return super.onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
    if (spreadWaterFromCenter(world, player.getPosition().offset(player.getHorizontalFacing()))) {
      super.onUse(stack, player, world, hand);
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
      //  player.getCooldownTracker().setCooldown(this, COOLDOWN);
      UtilSound.playSound(world, posCenter, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.BLOCKS);
    }
    return success;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this),
        "wdw",
        "iwi",
        " o ",
        'w', new ItemStack(Items.WATER_BUCKET),
        'd', new ItemStack(Items.REDSTONE),
        'o', new ItemStack(Blocks.OBSIDIAN),
        'i', new ItemStack(Blocks.ICE));
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("item.water_spreader.tooltip"));
  }
}
