package com.lothrazar.cyclicmagic.enchantment;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
import com.lothrazar.cyclicmagic.util.UtilItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantHarvest extends Enchantment {
  private static final HarestCropsConfig confLow = new HarestCropsConfig();
  private static final HarestCropsConfig confMed = new HarestCropsConfig();
  private static final HarestCropsConfig confHi = new HarestCropsConfig();
  public EnchantHarvest() {
    super(Rarity.COMMON, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    this.setName("harvest");
    this.initHarvesters();
  }
  private void initHarvesters() {
    //level 1
    confLow.doesPumpkinBlocks = false;
    confLow.doesMelonBlocks = false;
    confLow.doesLeaves = false;
    confLow.doesCrops = true;
    confLow.doesFlowers = false;
    confLow.doesHarvestMushroom = false;
    confLow.doesHarvestSapling = false;
    confLow.doesHarvestTallgrass = false;
    //level 2
    confMed.doesPumpkinBlocks = false;
    confMed.doesMelonBlocks = false;
    confMed.doesLeaves = false;
    confMed.doesCrops = true;
    confMed.doesFlowers = false;
    confMed.doesHarvestMushroom = true;
    confMed.doesHarvestSapling = false;
    confMed.doesHarvestTallgrass = false;
    //level 3
    confHi.doesPumpkinBlocks = true;
    confHi.doesMelonBlocks = true;
    confHi.doesLeaves = false;
    confHi.doesCrops = true;
    confHi.doesFlowers = false;
    confHi.doesHarvestMushroom = false;
    confHi.doesHarvestSapling = false;
    confHi.doesHarvestTallgrass = false;
  }
  @Override
  public int getMaxLevel() {
    return 5;
  }
  @Override
  public boolean canApply(ItemStack stack) {
    boolean yes = stack.getItem() == Items.BOOK || (stack.getItem() instanceof ItemHoe);
    return yes;
  }
  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);//|| this.type.canEnchantItem(stack.getItem()); 	
  }
  @SubscribeEvent
  public void onPlayerInteract(PlayerInteractEvent event) {
    EntityPlayer entityPlayer = event.getEntityPlayer();
    BlockPos pos = event.getPos();
    World world = event.getWorld();
    if (pos == null) { return; }
    ItemStack held = entityPlayer.getHeldItem(event.getHand());
    IBlockState clicked = world.getBlockState(pos);
    if (clicked != null && clicked.getBlock() != Blocks.DIRT
        && held != null && EnchantmentHelper.getEnchantments(held).containsKey(this)) {
      int mainLevel = EnchantmentHelper.getEnchantments(held).get(this);
      //level 1 or 2
      if (mainLevel > 0) {
        int harvested = 0;
        if (mainLevel == 1) {
          harvested = UtilHarvestCrops.harvestSingle(world, pos, confLow) ? 1 : 0;
        }
        else if (mainLevel == 2 || mainLevel == 3) {
          //radius means it goes both ways from senter
          harvested = UtilHarvestCrops.harvestArea(world, pos, mainLevel, confMed);
        }
        else {// if(mainLevel == 3){
          harvested = UtilHarvestCrops.harvestArea(world, pos, mainLevel + 1, confHi);
        }
        if (harvested > 0) {
          UtilItem.damageItem(entityPlayer, held);
          //todo: harvest -> durability
          entityPlayer.swingArm(event.getHand());
        }
      }
    }
  }
}
