package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemFangs extends BaseTool implements IHasRecipe {
  private static final int COOLDOWN = 10;//ticks not seconds
  private static final int DURABILITY = 666;
  private static final int MAX_RANGE = 16;
  public ItemFangs() {
    super(DURABILITY);
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    if (player.getCooldownTracker().hasCooldown(this)) { return false; }
    summonFangRay(player, entity.posX, entity.posY, entity.posZ);
    UtilItemStack.damageItem(player, player.getHeldItem(hand));
    return true;
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (player.getCooldownTracker().hasCooldown(this)) { return EnumActionResult.PASS; }
    summonFangRay(player, pos.getX() + hitX, pos.getY() + hitY + 1, pos.getZ() + hitZ);
    UtilItemStack.damageItem(player, player.getHeldItem(hand));
    return EnumActionResult.SUCCESS;
  }
  /**
   * Summon a ray of fangs in the direction of these coordinates away from the
   * caster
   * 
   * @param caster
   * @param posX
   * @param posY
   * @param posZ
   */
  private void summonFangRay(EntityPlayer caster, double posX, double posY, double posZ) {
    double minY = Math.min(posY, caster.posY);
    //double d1 = Math.max(posY,caster.posY) ;
    float arctan = (float) MathHelper.atan2(posZ - caster.posZ, posX - caster.posX);
    for (int i = 0; i < MAX_RANGE; ++i) {
      double fract = 1.25D * (double) (i + 1);
      this.summonFangSingle(caster,
          caster.posX + (double) MathHelper.cos(arctan) * fract,
          minY,
          caster.posZ + (double) MathHelper.sin(arctan) * fract,
          arctan, i);
    }
    onCastSuccess(caster);
  }
  /**
   * cast a single fang from the caster towards the direction of the given
   * coordinates with delay
   * 
   * @param caster
   * @param x
   * @param y
   * @param z
   * @param yaw
   * @param delay
   */
  private void summonFangSingle(EntityPlayer caster, double x, double y, double z, float yaw, int delay) {
    EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(caster.world, x, y, z, yaw, delay, caster);
    caster.world.spawnEntity(entityevokerfangs);
  }
  private void onCastSuccess(EntityPlayer caster) {
    UtilSound.playSound(caster, SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK);
    caster.getCooldownTracker().setCooldown(this, COOLDOWN);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "wpc",
        " dp",
        "r w",
        'w', Blocks.WEB,
        'r', Blocks.END_ROD,
        'c', Items.END_CRYSTAL,
        'p', Blocks.ICE, //ore dict ice doesnt exist
        'd', "blockEmerald");
  }
}
