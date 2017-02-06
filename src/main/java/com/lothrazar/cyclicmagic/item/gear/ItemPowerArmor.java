package com.lothrazar.cyclicmagic.item.gear;
import java.util.List;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPowerArmor extends ItemArmor {
  private static final float speedfactor = 0.077F;
  public static final String toggleFlag = Const.MODID + "_armortag";
  public static final String toggleStepFlag = Const.MODID + "_steptag";
  public ItemPowerArmor(ArmorMaterial material, EntityEquipmentSlot armorType) {
    super(material, 0, armorType);
  }
  @SuppressWarnings("incomplete-switch")
  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

    switch (this.armorType) {
      case CHEST:
        if (player.isSneaking() && player.moveForward > 0) {
          UtilEntity.speedupEntity(player, speedfactor);
        }
      break;
      case FEET:
        BlockPos belowPos = player.getPosition().down();
        if (world.containsAnyLiquid(new AxisAlignedBB(belowPos)) && world.isAirBlock(player.getPosition()) && player.motionY < 0
            && !player.isSneaking()) {// let them slip down into it when sneaking
          player.motionY = 0;// stop falling
          player.onGround = true; // act as if on solid ground
        }
      break;
      case HEAD:
        setGlowing(player, true);
        
      break;
      case LEGS:
        setStepHeight(player, true);
      break;
    }
  }
  public static void setStepHeight(EntityPlayer player, boolean on) {
    player.stepHeight = (on) ? 1.0F : 0.5F;
    player.getEntityData().setBoolean(toggleStepFlag, on);
  }
  public static void checkIfLegsOff(EntityPlayer player) {
    Item itemInSlot = UtilPlayer.getItemArmorSlot(player,EntityEquipmentSlot.LEGS);
    if (player.getEntityData().getBoolean(ItemPowerArmor.toggleStepFlag) &&
        (itemInSlot == null || !(itemInSlot instanceof ItemPowerArmor))) {
      //turn it off once, from the message
      setStepHeight(player, false);
    }
  }
  public static void checkIfHelmOff(EntityPlayer player) {
    Item itemInSlot = UtilPlayer.getItemArmorSlot(player,EntityEquipmentSlot.HEAD);
    if (player.getEntityData().getBoolean(ItemPowerArmor.toggleFlag) &&
        (itemInSlot == null || !(itemInSlot instanceof ItemPowerArmor))) {
      //turn it off once, from the message
      setGlowing(player, false);
    }
  }
  public static void setGlowing(EntityPlayer player, boolean hidden) {
    player.setGlowing(hidden);//hidden means dont render
    //flag it so we know the purple glow was from this item, not something else
    player.getEntityData().setBoolean(toggleFlag, hidden);
  }
  @Override
  public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) {
    list.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }
}
