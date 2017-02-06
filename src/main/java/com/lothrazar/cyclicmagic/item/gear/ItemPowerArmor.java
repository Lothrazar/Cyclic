package com.lothrazar.cyclicmagic.item.gear;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPowerArmor extends ItemArmor {
  private static final float speedfactor = 0.077F;
  public ItemPowerArmor(ArmorMaterial material, EntityEquipmentSlot armorType) {
    super(material, 0, armorType);
  }
  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
    if (this.armorType == EntityEquipmentSlot.FEET) {
      BlockPos belowPos = player.getPosition().down();
      if (world.containsAnyLiquid(new AxisAlignedBB(belowPos)) && world.isAirBlock(player.getPosition()) && player.motionY < 0
          && !player.isSneaking()) {// let them slip down into it when sneaking
        player.motionY = 0;// stop falling
        player.onGround = true; // act as if on solid ground
      }
    } //else->in case i add other powers
    else if (this.armorType == EntityEquipmentSlot.LEGS) {
      //      player.stepHeight = 1.0F;
      //TODO:above doesnt get moved down to norm on unequip. save that idea for another item/charm/potion/enchant/something?
      if (player.isSneaking() && player.moveForward > 0) {
        UtilEntity.speedupEntity(player, speedfactor);
      }
    }
  }
  @Override
  public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) {
    list.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }
}
