package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.SoundEvent;

public class BlockLaunch extends BlockBasePressurePlate {
  private final static float ANGLE = 90;
  private float power;
  private SoundEvent sound;
  public BlockLaunch(float p, SoundEvent s) {
    super(Material.CLAY, MapColor.GRASS);//same as BlockSlime
    this.setSoundType(SoundType.SLIME);
    power = p;
    sound = s;
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
    UtilEntity.launch(entity, ANGLE, power);
    this.playClickOnSound(worldIn, pos);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    int fakePower = (int) Math.round(this.power * 10); //  String.format("%.1f", this.power))
    tooltip.add(UtilChat.lang("tile.plate_launch.tooltip" + fakePower));
  }
}
