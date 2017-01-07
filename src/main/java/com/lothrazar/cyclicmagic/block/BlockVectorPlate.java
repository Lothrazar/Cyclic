package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class BlockVectorPlate extends BlockBaseHasTile implements IHasRecipe, IHasConfig {
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.03125D, 1D);
  public BlockVectorPlate() {
    super(Material.IRON);//, 
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setGuiId(ModGuiHandler.GUI_INDEX_VECTOR);
    //    this.setTooltip("tile.harvester_block.tooltip");
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileVector();
  }
  @Override
  public void addRecipe() {
    //    GameRegistry.addRecipe(new ItemStack(this), "rsr", "gbg", "ooo",
    //        'o', Blocks.OBSIDIAN,
    //        'g', Items.QUARTZ,
    //        's', Blocks.DISPENSER,
    //        'r', Items.EMERALD,
    //        'b', Items.DIAMOND);
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    TileVector tile = (TileVector) worldIn.getTileEntity(pos);
    entity.motionX = 0;////stop motion first,s o if they re coming in from another conveyor . and stop their running/walking speed
    entity.motionY = 0;
    entity.motionZ = 0;
    entity.fallDistance = 0;
//    System.out.println("launch yaw"+tile.getYaw());
    UtilEntity.launch(entity, tile.getAngle(), tile.getYaw(), tile.getActualPower());
  }
  @Override
  public void syncConfig(Configuration config) {
    //    String category = Const.ConfigCategory.modpackMisc;
    //    TileMachineHarvester.TIMER_FULL = config.getInt("HarvesterTime", category, 80, 10, 9999, "Number of ticks it takes to run one time, so lower is faster");
  }
}
