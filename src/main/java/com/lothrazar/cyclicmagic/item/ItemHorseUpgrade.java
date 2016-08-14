package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.util.Const.HorseMeta;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHorseUpgrade extends BaseItem implements IHasRecipe {
  public static int HEARTS_MAX;
  public static int SPEED_MAX;
  public static int JUMP_MAX;
  private static double JUMP_SCALE = 1.02; // %age
  private static double SPEED_SCALE = 1.05; // %age
  private ItemStack recipeItem;
  private HorseUpgradeType upgradeType;
  public ItemHorseUpgrade(HorseUpgradeType type, ItemStack rec) {
    super();
    recipeItem = rec;
    upgradeType = type;
  }
  @SuppressWarnings("unchecked")
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, @SuppressWarnings("rawtypes") List tooltip, boolean advanced) {
    if (stack == null || stack.getItem() == null) { return; } // just being safe
    Item carrot = stack.getItem();
    tooltip.add(I18n.format(carrot.getUnlocalizedName(stack) + ".effect"));
  }
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this), Items.CARROT, recipeItem);
  }
  public static void onHorseInteract(EntityHorse horse, EntityPlayer player, ItemHorseUpgrade heldItem) {
    boolean success = false;
    switch (heldItem.upgradeType) {
    case HEALTH:
      float mh = (float) horse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
      if (mh < 2 * HEARTS_MAX) { // 20 hearts == 40 health points
        horse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(mh + 2);
        success = true;
      }
      break;
    case JUMP:
      if (ReflectionRegistry.horseJumpStrength != null) {
        double jump = horse.getEntityAttribute(ReflectionRegistry.horseJumpStrength).getAttributeValue();// horse.getHorseJumpStrength()
        double newjump = jump * JUMP_SCALE;
        // double jumpHeight = getJumpTranslated(horse.getHorseJumpStrength());
        if (UtilEntity.getJumpTranslated(newjump) < JUMP_MAX) {
          horse.getEntityAttribute(ReflectionRegistry.horseJumpStrength).setBaseValue(newjump);
          success = true;
        }
      }
      break;
    case SPEED:
      double speed = horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
      double newSpeed = speed * SPEED_SCALE;
      if (UtilEntity.getSpeedTranslated(newSpeed) < SPEED_MAX) {
        horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(newSpeed);
        success = true;
      }
      break;
    case TYPE:
      switch (horse.getType()) {
      case HORSE:
        horse.setType(HorseType.ZOMBIE);
        success = true;
        break;
      case ZOMBIE:
        horse.setType(HorseType.SKELETON);
        success = true;
        break;
      case SKELETON:
        horse.setType(HorseType.HORSE);
        success = true;
        break;
      case DONKEY:// donkey and mule ignored by design
        break;
      case MULE:
        break;
      default:
        break;
      }
      break;
    case VARIANT:
      int var = horse.getHorseVariant();
      int var_reduced = 0;
      int var_new = 0;
      while (var - 256 > 0) {
        var_reduced += 256;// this could be done with modulo % arithmetic too,
        // but meh
        // doesnt matter either way
        var -= 256;
      } // invalid numbers make horse invisible, but this is somehow safe. and
      // easier than
      // doing bitwise ops
      switch (var) {
      case HorseMeta.variant_black:
        var_new = HorseMeta.variant_brown;
        break;
      case HorseMeta.variant_brown:
        var_new = HorseMeta.variant_brown_dark;
        break;
      case HorseMeta.variant_brown_dark:
        var_new = HorseMeta.variant_chestnut;
        break;
      case HorseMeta.variant_chestnut:
        var_new = HorseMeta.variant_creamy;
        break;
      case HorseMeta.variant_creamy:
        var_new = HorseMeta.variant_gray;
        break;
      case HorseMeta.variant_gray:
        var_new = HorseMeta.variant_white;
        break;
      case HorseMeta.variant_white:
        var_new = HorseMeta.variant_black;
        break;
      }
      var_new += var_reduced;
      horse.setHorseVariant(var_new);
      success = true;
      break;
    default:
      break;
    }
    if (success) {
      if (player.capabilities.isCreativeMode == false) {
        player.inventory.decrStackSize(player.inventory.currentItem, 1);
      }
      double x = horse.getPosition().getX();
      double y = horse.getPosition().getY();
      double z = horse.getPosition().getZ();
      for (int countparticles = 0; countparticles <= 10; ++countparticles) {
        horse.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + (horse.worldObj.rand.nextDouble() - 0.5D) * (double) 0.8, y + horse.worldObj.rand.nextDouble() * (double) 1.5 - (double) 0.1, z + (horse.worldObj.rand.nextDouble() - 0.5D) * (double) 0.8, 0.0D, 0.0D, 0.0D);
      }
      UtilSound.playSound(player, horse.getPosition(), SoundEvents.ENTITY_HORSE_EAT, SoundCategory.NEUTRAL);
      horse.setEatingHaystack(true); // makes horse animate and bend down to eat
    }
  }
  public static enum HorseUpgradeType {
    HEALTH, JUMP, SPEED, TYPE, VARIANT
  }
}
