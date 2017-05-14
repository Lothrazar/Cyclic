package com.lothrazar.cyclicmagic.item.minecart;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.EntityMinecartTurret;
import com.lothrazar.cyclicmagic.item.BaseItemMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemTurretMinecart extends BaseItemMinecart implements IHasRecipe {
  public ItemTurretMinecart() {
    super();
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        "bdb",
        "gmg",
        "ggg",
        'd', Blocks.BONE_BLOCK,
        'b', Items.BOW,
        'g', Items.GOLD_INGOT,
        'm', Blocks.OBSERVER);
  }
  @Override
  public EntityMinecart summonMinecart(World world) {
    return new EntityMinecartTurret(world);
  }
}
