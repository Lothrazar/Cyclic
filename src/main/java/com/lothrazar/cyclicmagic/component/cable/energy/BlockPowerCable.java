package com.lothrazar.cyclicmagic.component.cable.energy;
import com.lothrazar.cyclicmagic.IHasRecipe; 
import com.lothrazar.cyclicmagic.component.cable.BlockCableBase;
import com.lothrazar.cyclicmagic.component.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockPowerCable extends BlockCableBase implements IHasRecipe {
  public BlockPowerCable() {
    //    super(Material.CLAY);
    this.setPowerTransport();
  }
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileEntityCablePower();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 8),
        "sis",
        "i i",
        "sis",
        's', Blocks.BRICK_STAIRS,
        'i', "dustRedstone");
  }
}
