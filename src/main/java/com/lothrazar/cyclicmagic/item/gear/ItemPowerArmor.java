package com.lothrazar.cyclicmagic.item.gear;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPowerArmor extends ItemArmor {
  public ItemPowerArmor(EntityEquipmentSlot armorType) {
    super(ArmorMaterial.LEATHER, 0, armorType);
  }
  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
    if (this.armorType == EntityEquipmentSlot.FEET) {
      BlockPos belowPos = player.getPosition().down();
      if (world.containsAnyLiquid(new AxisAlignedBB(belowPos)) && world.isAirBlock(player.getPosition()) && player.motionY < 0
          && !player.isSneaking()) {// let them slip down into it
        player.motionY = 0;// stop falling
        player.onGround = true; // act as if on solid ground
        //      player.setAIMoveSpeed(0.1F);// walking and not sprinting is this
      }
    }
    //else->in case i add other powers
  }
}
