package com.lothrazar.cyclicmagic.entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;

/**
 * @author SkySom Not me. From
 *         https://github.com/BrassGoggledCoders/MoarCarts/blob/9c841d7b9345b6231a929e0b3c3f9746c3d020b1/src/main/java/xyz/brassgoggledcoders/moarcarts/entities/EntityMinecartBase.java
 *         Which is a repo using MIT License
 *         https://github.com/BrassGoggledCoders/MoarCarts/blob/9c841d7b9345b6231a929e0b3c3f9746c3d020b1/LICENSE
 * after this I also made my own changes
 * alternate version i also used for reference
 * https://github.com/BrassGoggledCoders/OpenTransport/blob/cc2208684ddf1b3db13863722a484f9f41c255f4/src/main/java/xyz/brassgoggledcoders/opentransport/api/wrappers/world/WorldWrapper.java
 */
public class FakeWorld extends World {
  private EntityGoldMinecartDispenser entityMinecartBase;
  public FakeWorld(EntityGoldMinecartDispenser entityMinecartBase) {
    this(entityMinecartBase.world, entityMinecartBase);
  }
  public FakeWorld(World world, EntityGoldMinecartDispenser entityMinecartBase) {
    super(world.getSaveHandler(), world.getWorldInfo(), world.provider, world.theProfiler, world.isRemote);
    this.setEntityMinecartBase(entityMinecartBase);
  }
  //MFR grabs TE's just a bit different than most
  @Override
  protected IChunkProvider createChunkProvider() {
    return chunkProvider;
  }
  @Override
  public boolean spawnEntity(Entity entityIn) {
//    ModCyclic.logger.info("spawnEntity fakeworld " + entityIn);
//    ModCyclic.logger.info("spawnEntity fakeworld " + entityIn.getPosition());
    entityIn.posY++;//dirty hack to fix selfcollission.... TOOD: better wah is horizontal offset, put it in FRONT of the thing
    
    return this.getCartWorld().spawnEntity(entityIn);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void spawnParticle(EnumParticleTypes particleType, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
    this.getCartWorld().spawnParticle(particleType, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
  }
  @Override
  public Entity getEntityByID(int id) {
    return this.getEntityMinecartBase().world.getEntityByID(id);
  }
  @Override
  public IBlockState getBlockState(BlockPos blockPos) {
    return this.getEntityMinecartBase().getDisplayTile();
  }
  @Override
  public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> entityClass, AxisAlignedBB axisAlignedBB) {
    axisAlignedBB.getAverageEdgeLength();
    return this.getCartWorld().getEntitiesWithinAABB(entityClass, axisAlignedBB);
  }
  @Override
  public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ) {
    return this.getCartWorld().getChunkFromChunkCoords(chunkX, chunkZ);
  }
  @Override
  protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
    return true;
  }
  @Override
  public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity) {}
  public EntityGoldMinecartDispenser getEntityMinecartBase() {
    return entityMinecartBase;
  }
  public void setEntityMinecartBase(EntityGoldMinecartDispenser entityMinecartBase) {
    this.entityMinecartBase = entityMinecartBase;
  }
  public World getCartWorld() {
    return this.getEntityMinecartBase().world;
  }
}
