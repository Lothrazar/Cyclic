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
      ModCyclic.logger.error(type);
      if (block.isToolEffective(type, world.getBlockState(pos)) == false) {
        return;
      }
    }
    //start at 1 because this one is already over
    int totalBroken = 1;//TODO: MAX!
    //TODO: recursion or loop
    totalBroken += this.harvestSurrounding(world, player, pos, block);
    UtilChat.addChatMessage(player, totalBroken + "!");
  }
  private int harvestSurrounding(World world, EntityPlayer player, BlockPos pos, Block block) {
    int hereBroken = 0;
    List<BlockPos> theFuture = this.getMatchingSurrounding(world, pos, block);
    for (BlockPos targetPos : theFuture) {
      //TODO: 
      IBlockState targetState = world.getBlockState(targetPos);
      if (player.canHarvestBlock(targetState) == false) {
        continue;
      }
      //TODO: steal some events or stuff from interaction manager. cant use directly => infinite loop
      //      if (player.interactionManager.tryHarvestBlock(targetPos)) {
      block.harvestBlock(world, player, targetPos, targetState, null, player.getHeldItem(EnumHand.MAIN_HAND));
      world.destroyBlock(targetPos, false);
      //        if( ) {
      //      player.canHarvestBlock(state)
      //      block.dropBlockAsItem(world, p, world.getBlockState(p), fort);
      //     block.dropXpOnBlockBreak(world, pos, block.getExpDrop(world.getBlockState(p), world, pos, fort));
      hereBroken++;
      //      }
    }
    return hereBroken;
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
