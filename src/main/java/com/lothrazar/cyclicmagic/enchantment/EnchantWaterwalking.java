package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantWaterwalking extends EnchantBase {
  public EnchantWaterwalking() {
    super("waterwalking", Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
    GuideRegistry.register(this, new ArrayList<String>(Arrays.asList("")));
  }
  @Override
  public int getMaxLevel() {
    return 1;
  }
  @Override
  public boolean canApply(ItemStack stack) {
    //anything that goes on your feet
    boolean yes = stack.getItem() == Items.BOOK ||
        (stack.getItem() instanceof ItemArmor)
            && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.FEET;
    return yes;
  }
  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof EntityPlayer) {
      EntityPlayer p = (EntityPlayer) event.getEntity();
      ItemStack feet = p.getItemStackFromSlot(EntityEquipmentSlot.FEET);
      if (feet.isEmpty()) { return; }
      setLiquidWalk(p);
    }
  }
  private void setLiquidWalk(EntityPlayer player) {
    BlockPos belowPos = player.getPosition().down();
    if (player.world.containsAnyLiquid(new AxisAlignedBB(belowPos)) && player.world.isAirBlock(player.getPosition()) && player.motionY < 0
        && !player.isSneaking()) {// let them slip down into it when sneaking
      player.motionY = 0;// stop falling
      player.onGround = true; // act as if on solid ground
    }
  }
}
