package com.lothrazar.cyclicmagic.item.gear;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPowerSword extends ItemSword implements IHasRecipe {
  private static final int COOLDOWN = Const.TICKS_PER_SEC;
  public enum SwordType {
    SLOW, WEAK, ENDER;
  }
  private SwordType type;
  public ItemPowerSword(SwordType t) {
    super(MaterialRegistry.powerToolMaterial);
    this.type = t;
    this.setMaxDamage(1);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
    super.addInformation(stack, player, tooltip, advanced);
  }
  @Override
  public IRecipe addRecipe() {
    ItemStack item = ItemStack.EMPTY;
    switch (type) {
      case WEAK:
        item = new ItemStack(Blocks.MYCELIUM);
      break;
      case SLOW:
        item = new ItemStack(Blocks.PACKED_ICE);
      break;
      case ENDER:
        item = new ItemStack(Items.CHORUS_FRUIT);
      break;
    }
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "ded",
        "ded",
        "wsw",
        'w', item,
        'e', Items.NETHER_STAR,
        'd', Items.DRAGON_BREATH,
        's', "blockEmerald");
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    switch (type) {
      case WEAK:
        spawnLingeringPotion(player, PotionTypes.WEAKNESS);
      break;
      case SLOW:
        spawnLingeringPotion(player, PotionTypes.SLOWNESS);
      break;
      case ENDER:
        player.addPotionEffect(new PotionEffect(PotionEffectRegistry.ENDER, 200, 0));
        if (!world.isRemote) {
          EntityEnderPearl entityenderpearl = new EntityEnderPearl(world, player);
          entityenderpearl.setHeadingFromThrower(player, player.rotationPitch - 20, player.rotationYaw, 0.0F, 1.6F, 1.0F);
          world.spawnEntity(entityenderpearl);
        }
    }
    UtilSound.playSound(player, SoundEvents.ENTITY_ENDERPEARL_THROW);
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
  }
  private void spawnLingeringPotion(EntityPlayer player, PotionType ptype) {
    World world = player.getEntityWorld();
    ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), ptype);
    EntityPotion entitypotion = new EntityPotion(world, player, potion);
    entitypotion.setHeadingFromThrower(player, player.rotationPitch - 20, player.rotationYaw, 0, 1.6F, 0.5F);
    if (world.isRemote == false) {
      world.spawnEntity(entitypotion);
    }
  }
  //overrides to disable item damage
  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    return true;
  }
  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
    return true;
  }
}
