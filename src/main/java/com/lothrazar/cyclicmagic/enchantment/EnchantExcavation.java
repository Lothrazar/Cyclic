package com.lothrazar.cyclicmagic.enchantment;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantExcavation extends EnchantBase {
  private static final int XP_PER_LVL = 8;
  public EnchantExcavation() {
    super("excavation", Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    GuideRegistry.register(this, new ArrayList<String>());
  }
  @Override
  public int getMaxLevel() {
    return 3;
  }
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onBreakEvent(BreakEvent event) {
    World world = event.getWorld();
    EntityPlayer player = event.getPlayer();
    if (player.swingingHand == null) {
      return;
    }
    // ModCyclic.logger.error(player.swingingHand == null ? "NULL":"WAT");
    BlockPos pos = event.getPos();
    Block block = event.getState().getBlock();
    //is this item stack enchanted with ME?
    ItemStack stackHarvestingWith = player.getHeldItem(player.swingingHand);
    int level = this.getCurrentLevelTool(stackHarvestingWith);
    if (level <= 0) {
      return;
    }
    // if I am using an axe on stone or dirt, doesn't trigger
    for (String type : stackHarvestingWith.getItem().getToolClasses(stackHarvestingWith)) {
     
      if (block.isToolEffective(type, world.getBlockState(pos)) == false) {
        return;
      }
    }
    //starts at 1 for current one
    this.harvestSurrounding(world, player, pos, block, 1, level);
     
  }
  private int getHarvestMax(int level) {
    return level * 12;
  }
  /**
   * WARNING: RECURSIVE
   * 
   * @param world
   * @param player
   * @param posIn
   * @param block
   * @return
   */
  private void harvestSurrounding(final World world, final EntityPlayer player, final BlockPos posIn, final Block block, int totalBroken, final int level) {
    if (totalBroken > this.getHarvestMax(level)) {
      return;
    }
    List<BlockPos> theFuture = this.getMatchingSurrounding(world, posIn, block);
    List<BlockPos> wasHarvested = new ArrayList<BlockPos>();
    for (BlockPos targetPos : theFuture) {
      IBlockState targetState = world.getBlockState(targetPos);
      //check canHarvest every time -> permission or any other hooks
      if (world.isAirBlock(targetPos) ||  player.canHarvestBlock(targetState) == false) {
        continue;
      }
      block.harvestBlock(world, player, targetPos, targetState, null, player.getHeldItem(EnumHand.MAIN_HAND));
      world.destroyBlock(targetPos, false);
      wasHarvested.add(targetPos);
      totalBroken++;
      this.harvestSurrounding(world, player, targetPos, block, totalBroken, level);
    }
    //    return wasHarvested;
  }
  private List<BlockPos> getMatchingSurrounding(World world, BlockPos start, Block blockIn) {
    List<BlockPos> list = new ArrayList<BlockPos>();
    for (EnumFacing fac : EnumFacing.values()) {
      if (world.getBlockState(start.offset(fac)).getBlock() == blockIn) {
        list.add(start.offset(fac));
      }
    }
    return list;
  }
}
