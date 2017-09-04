package com.lothrazar.cyclicmagic.item.gear;
import com.google.common.collect.Multimap;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPowerSword extends ItemSword implements IHasRecipe {
  private static final int COOLDOWN = Const.TICKS_PER_SEC * 10;
  public enum SwordType {
    FROST, ENDER;
  }
  private SwordType type;
  public ItemPowerSword(SwordType t) {
    super(MaterialRegistry.powerToolMaterial);
    this.type = t;
    this.setMaxDamage(1);
  }
  @Override
  public IRecipe addRecipe() {
    ItemStack item = ItemStack.EMPTY;
    switch (type) {
      case ENDER:
        item = new ItemStack(Blocks.MYCELIUM);
      break;
      case FROST:
        item = new ItemStack(Blocks.PACKED_ICE);
      break;
    }
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "ded",
        "ded",
        "wsw",
        'w', item,
        'e', Items.NETHER_STAR,
        'd', Items.DRAGON_BREATH,
        's', "blockDiamond");
  }
  //overrides to disable item damage
  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    return true;
  }
  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
    return true;
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
    switch (type) {
      case ENDER:
        spawnLingeringPotion(player, PotionTypes.WEAKNESS);
      break;
      case FROST:
        spawnLingeringPotion(player, PotionTypes.SLOWNESS);
      break;
    }
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
  //  public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
  //    Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
  //    if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
  //   //    multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
  //     
  //      multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", VANILLA_SPEED * 4, 0));
  //    }
  //    return multimap;
  //  }
}
