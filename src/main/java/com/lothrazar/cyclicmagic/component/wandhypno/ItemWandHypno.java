package com.lothrazar.cyclicmagic.component.wandhypno;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * inspired by insanity spell (poppy) from Roots 1 by @elucent
 * 
 *
 */
public class ItemWandHypno extends BaseTool implements IHasRecipe {
  //private static final double TRIGGERODDS = 0.5;
  private static final double RANGE = 16.0;
  private static final int durability = 2000;
  private static final int cooldown = 10;
  public ItemWandHypno() {
    super(durability);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack held = player.getHeldItem(hand);
    if (!world.isRemote) {
      int x = player.getPosition().getX();
      int y = player.getPosition().getY();
      int z = player.getPosition().getZ();
      List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - RANGE, y - RANGE, z - RANGE, x + RANGE, y + RANGE, z + RANGE));
      ArrayList<EntityLivingBase> trimmedTargets = new ArrayList<EntityLivingBase>();
      for (int i = 0; i < targets.size(); i++) {
        if (targets.get(i).getUniqueID().compareTo(player.getUniqueID()) != 0
            && targets.get(i).isCreatureType(EnumCreatureType.MONSTER, false)) {
          trimmedTargets.add(targets.get(i));
        }
      }
      EntityLivingBase cur;
      EntityLivingBase curTarget;
      int targeted = 0;
      for (int i = 0; i < trimmedTargets.size(); i++) {
        cur = trimmedTargets.get(i);
        cur.setRevengeTarget(null);
        int j = world.rand.nextInt(trimmedTargets.size());
        if (j != i) {//&& world.rand.nextDouble() < TRIGGERODDS
          curTarget = trimmedTargets.get(j);
          //          cur.attackEntityFrom( DamageSource.causeMobDamage(curTarget), 0);
          cur.setRevengeTarget(curTarget);
          cur.setLastAttackedEntity(curTarget);
          net.minecraftforge.common.ForgeHooks.onLivingSetAttackTarget(cur, curTarget);
          UtilParticle.spawnParticlePacket(EnumParticleTypes.DRAGON_BREATH, cur.getPosition());
          targeted++;
        }
      }
      if (targeted == 0) {
        UtilChat.sendStatusMessage(player, "wand.result.notargets");
      }
    }
    player.getCooldownTracker().setCooldown(held.getItem(), cooldown);
    super.onUse(held, player, world, hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, held);
  }
  @Override
  public IRecipe addRecipe() {
    //    switch (orbType) {
    //      case MOUNTED:
    //        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
    //            "ere",
    //            "rsr",
    //            "ere",
    //            'e', new ItemStack(Items.ENDER_EYE),
    //            'r', "dyeBlue",
    //            's', "blockIron");
    //      case NORMAL:
    //        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
    //            "ere",
    //            "rsr",
    //            "ere",
    //            'e', new ItemStack(Items.ENDER_EYE),
    //            'r', "dustRedstone",
    //            's', "blockIron");
    //      default:
    //      break;
    //    }
    return null;
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return false;
  }
}
