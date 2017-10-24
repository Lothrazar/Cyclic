package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLaunch extends BlockBasePressurePlate implements IHasRecipe {
  private final static float ANGLE = 90;
  private final static int RECIPE_OUT = 6;
  public static enum LaunchType {
    SMALL, MEDIUM, LARGE;
  }
  public static boolean sneakPlayerAvoid;
  private LaunchType type;
  private float power;
  private SoundEvent sound;
  public BlockLaunch(LaunchType t, SoundEvent s) {
    super(Material.IRON, MapColor.GRASS);//same as BlockSlime
    this.setSoundType(SoundType.SLIME);
    this.setHardness(2.0F).setResistance(2.0F);
    sound = s;
    type = t;
    switch (type) {
      case LARGE:
        this.power = 1.8F;
      break;
      case MEDIUM:
        this.power = 1.3F;
      break;
      case SMALL:
        this.power = 0.8F;
      break;
      default:
      break;
    }
  }
  @Override
  protected void playClickOnSound(World worldIn, BlockPos pos) {
    worldIn.playSound((EntityPlayer) null, pos, this.sound, SoundCategory.BLOCKS, 0.3F, 0.5F);
  }
  @Override
  protected void playClickOffSound(World worldIn, BlockPos pos) {}
  @Override
  protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
    return 0;
  }
  @Override
  protected int getRedstoneStrength(IBlockState state) {
    return 0;
  }
  @Override
  protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
    return null;
  }
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    if (sneakPlayerAvoid && entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) {
      return;
    }
    UtilEntity.launch(entity, ANGLE, power);
    this.playClickOnSound(worldIn, pos);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    int fakePower = (int) Math.round(this.power * 10); //  String.format("%.1f", this.power))
    tooltip.add(UtilChat.lang("tile.plate_launch.tooltip" + fakePower));
  }
  @Override
  public IRecipe addRecipe() {
    switch (type) {
      case LARGE:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUT),
            "sss", "ggg", "iii",
            's', "slimeball",
            'g', Blocks.STONE_PRESSURE_PLATE,
            'i', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
      break;
      case MEDIUM:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUT),
            "sss", "ggg", "iii",
            's', "slimeball",
            'g', Blocks.WOODEN_PRESSURE_PLATE,
            'i', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
      break;
      case SMALL:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUT),
            "sss", "ggg", "iii",
            's', "slimeball",
            'g', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
            'i', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
      break;
    }
    return null;
  }
}
