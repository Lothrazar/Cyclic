package com.lothrazar.cyclicmagic.spell;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellInventory extends BaseSpell {
  public SpellInventory(int id, String n) {
    super.init(id, n);
  }
  @Override
  public boolean cast(World world, EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
    if (!world.isRemote) { // does the isRemote check actually matter
      player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_WAND, world, 0, 0, 0);
    }
    return true;
  }
  @Override
  public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
    UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);
  }
  @Override
  public void playSound(World world, EntityPlayer player, Block block, BlockPos pos) {
    UtilSound.playSound(player, pos, SoundEvents.ENTITY_GENERIC_DRINK);
  }
}
