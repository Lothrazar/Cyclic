package com.lothrazar.cyclicmagic.spell;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseSpellThrown extends BaseSpell {
  @Override
  public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
    UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);
  }
  @Override
  public void playSound(World world, EntityPlayer player, Block block, BlockPos pos) {
    UtilSound.playSound(player, pos, SoundRegistry.pew);
  }
}
