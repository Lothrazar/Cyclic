package com.lothrazar.cyclicmagic.block;
import java.util.Random;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * imported from https://github.com/PrinceOfAmber/SamsPowerups/blob/
 * 5083ec601e34bbe045d9a3d0ca091e3d44af562f/src/main/java/com/lothrazar/
 * samscontent/BlockRegistry.j a v a
 * 
 * @author Lothrazar
 *
 */
public class BlockScaffolding extends BlockBase implements IHasRecipe { 
  protected boolean dropBlock = true;
  public BlockScaffolding() {
    super(Material.GLASS);
    this.setTickRandomly(true);
    this.setHardness(0F);
    this.setResistance(0F);
    this.setTranslucent();
    SoundEvent crackle = SoundRegistry.crackle;
    this.setSoundType(new SoundType(0.2F, 1.0F, crackle, crackle, crackle, crackle, crackle));
  }
  @Override
  public void updateTick(World worldObj, BlockPos pos, IBlockState state, Random rand) {
    worldObj.destroyBlock(pos, dropBlock);
  }
  public int tickRate(World worldIn) {
    return 200;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 12), "s s", " s ", "s s", 's', new ItemStack(Items.STICK));
  }
}