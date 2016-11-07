package com.lothrazar.cyclicmagic.spell;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseSpellRange extends BaseSpell {
  public static int maxRange = 64;
  @Override
  public void spawnParticle(World world, EntityPlayer p, BlockPos pos) {
    if (SpellRegistry.doParticles) {
      UtilParticle.spawnParticleBeam(p.worldObj, EnumParticleTypes.SPELL_WITCH, p.getPosition(), pos, 2);
    }
  }
  @Override
  public void playSound(World world, EntityPlayer player, Block block, BlockPos pos) {
    UtilSound.playSoundPlaceBlock(player, pos, block);
  }
  @Override
  public boolean canPlayerCast(World world, EntityPlayer p, BlockPos pos) {
    return p.capabilities.allowEdit;
  }
}
