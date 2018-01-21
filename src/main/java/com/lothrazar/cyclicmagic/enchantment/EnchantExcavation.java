package com.lothrazar.cyclicmagic.enchantment;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantExcavation extends EnchantBase implements IHasConfig {
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
    return levelToMaxBreak[level];
  }
  /**
   * WARNING: RECURSIVE function to break all blocks connected up to the maximum total
   */
  private void harvestSurrounding(final World world, final EntityPlayer player, final BlockPos posIn, final Block block, int totalBroken, final int level) {
    if (totalBroken > this.getHarvestMax(level) 
        ||  player.getHeldItem(player.swingingHand).isEmpty()) {
      return;
    }
    int fortuneXp = 0;//even if tool has fortune, ignore just to unbalance a bit
    List<BlockPos> theFuture = this.getMatchingSurrounding(world, posIn, block);
    List<BlockPos> wasHarvested = new ArrayList<BlockPos>();
    for (BlockPos targetPos : theFuture) {
      IBlockState targetState = world.getBlockState(targetPos);
      //check canHarvest every time -> permission or any other hooks
      if (world.isAirBlock(targetPos) || player.canHarvestBlock(targetState) == false) {
        continue;
      }
      block.harvestBlock(world, player, targetPos, targetState, null, player.getHeldItem(EnumHand.MAIN_HAND));
      block.dropXpOnBlockBreak(world, targetPos, block.getExpDrop(targetState, world, targetPos, fortuneXp));
      world.destroyBlock(targetPos, false);
      wasHarvested.add(targetPos);
      //damage but also respect the unbreaking chant
      player.getHeldItem(player.swingingHand).attemptDamageItem(1, world.rand, null);
//      UtilItemStack.damageItem(player, player.getHeldItem(player.swingingHand) );
      totalBroken++;
      this.harvestSurrounding(world, player, targetPos, block, totalBroken, level);
    }
  }
  private List<BlockPos> getMatchingSurrounding(World world, BlockPos start, Block blockIn) {
    List<BlockPos> list = new ArrayList<BlockPos>();
    // TODO: DIAGONAL! 
    List<EnumFacing> targetFaces = Arrays.asList(EnumFacing.values());
    Collections.shuffle(targetFaces);
    for (EnumFacing fac : targetFaces) {
      if (world.getBlockState(start.offset(fac)).getBlock() == blockIn) {
        list.add(start.offset(fac));
      }
    }
    return list;
  }
  int[] levelToMaxBreak;
  @Override
  public void syncConfig(Configuration config) {
    // level starts at 1 so just ignore index 0 of array always, 0 means non enchanted
    levelToMaxBreak = new int[this.getMaxLevel() + 1];
    levelToMaxBreak[0] = 0;
    for (int i = 1; i <= this.getMaxLevel(); i++) {
      levelToMaxBreak[i] = config.getInt("EnchantExcavationBreak" + i, Const.ConfigCategory.modpackMisc, 10 + i * 12, 1, 128, "Max blocks broken by this enchantment at level " + i);
    }
  }
}
