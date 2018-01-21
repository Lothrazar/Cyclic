package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantMultishot extends EnchantBase {
  public EnchantMultishot() {
    super("multishot", Rarity.VERY_RARE, EnumEnchantmentType.BOW, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    GuideRegistry.register(this, new ArrayList<String>());
  }
  @Override
  public boolean canApply(ItemStack stack) {
    return stack.getItem() instanceof ItemBow || stack.getItem() == Items.BOOK;
  }
  @Override
  public int getMaxLevel() {
    return 1;
  }
  @SubscribeEvent
  public void onPlayerUpdate(ArrowLooseEvent event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack stack = event.getBow();
    World worldIn = player.world;
    int level = this.getCurrentLevelTool(stack);
    if (level <= 0) {
      return;
    }
    if (worldIn.isRemote == false) {
      float charge = ItemBow.getArrowVelocity(stack.getMaxItemUseDuration() - event.getCharge());
      //use cross product to push arrows out to left and right
      Vec3d playerDirection = UtilEntity.lookVector(player.rotationYaw, player.rotationPitch);
      Vec3d left = playerDirection.crossProduct(new Vec3d(0, 1, 0));
      Vec3d right = playerDirection.crossProduct(new Vec3d(0, -1, 0));
      spawnArrow(worldIn, player, stack, charge, left.normalize());
      spawnArrow(worldIn, player, stack, charge, right.normalize());
    }
  }
  public void spawnArrow(World worldIn, EntityPlayer player, ItemStack stackBow, float charge, Vec3d offsetVector) {
    //TODO: custom ammo one day? The event does not send ammo only the bow
    ItemArrow itemarrow = (ItemArrow) (Items.ARROW);
    EntityArrow entityarrow = itemarrow.createArrow(worldIn, stackBow, player);
    entityarrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
    //off set bow to the side using the vec, and then aim
    entityarrow.posX += offsetVector.x;
    entityarrow.posY += offsetVector.y;
    entityarrow.posZ += offsetVector.z;
    entityarrow.setAim(player, player.rotationPitch, player.rotationYaw, 0.0F, charge * 3.0F, 1.0F);
    //from ItemBow vanilla class
    if (charge == 1.0F) {
      entityarrow.setIsCritical(true);
    }
    //extract enchants from bow
    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stackBow);
    if (j > 0) {
      entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
    }
    int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stackBow);
    if (k > 0) {
      entityarrow.setKnockbackStrength(k);
    }
    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stackBow) > 0) {
      entityarrow.setFire(100);
    }
    //and go
    worldIn.spawnEntity(entityarrow);
  }
}
