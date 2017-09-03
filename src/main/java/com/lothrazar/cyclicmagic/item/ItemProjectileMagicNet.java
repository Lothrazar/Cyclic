package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.component.wandtorch.EntityTorchBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetEmpty;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetFull;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.base.BaseItemProjectile;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemProjectileMagicNet extends BaseItemProjectile implements IHasRecipe {
  public static final String NBT_ENTITYID = "id";
  public ItemProjectileMagicNet(){
    super();
  }
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityTorchBolt(world, x, y, z);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 1),
        "lal",
        "qiq",
        "lal",
        'i', "ingotIron",
        'a', new ItemStack(Blocks.TALLGRASS, 1, OreDictionary.WILDCARD_VALUE),
        'l', "dyeCyan",
        'q', new ItemStack(Items.SNOWBALL));
  }
  public boolean hasEntity(ItemStack held) {
    return held.getTagCompound() != null && held.getTagCompound().hasKey(NBT_ENTITYID);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return hasEntity(stack);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    if (hasEffect(stack)) {
      tooltip.add(stack.getTagCompound().getString(NBT_ENTITYID));
    }
    else {
      super.addInformation(stack, playerIn, tooltip, advanced);
    }
  }
  @Override
  public  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    if (hasEntity(held)) {
      this.doThrow(world, player, hand, new EntityMagicNetFull(world, player, held.copy()));
      held.getTagCompound().removeTag(NBT_ENTITYID);
      held.setTagCompound(null);
    }
    else {
      this.doThrow(world, player, hand, new EntityMagicNetEmpty(world, player));
    
    }
    UtilPlayer.decrStackSize(player, hand);
  }
  @Override
  public SoundEvent getSound() {
    // TODO Auto-generated method stub
    return SoundEvents.ENTITY_EGG_THROW;
  }
}
