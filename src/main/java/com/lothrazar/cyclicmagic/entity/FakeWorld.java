package com.lothrazar.cyclicmagic.entity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
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
 */
public class FakeWorld extends World {
  private EntityGoldMinecartDispenser entityMinecartBase;
  public FakeWorld(EntityGoldMinecartDispenser entityMinecartBase) {
    this(entityMinecartBase.world, entityMinecartBase);
  }
  public FakeWorld(World world, EntityGoldMinecartDispenser entityMinecartBase) {
    super(world.getSaveHandler(), world.getWorldInfo(), world.provider, world.theProfiler, world.isRemote);
    this.setEntityMinecartBase(entityMinecartBase);
    //    if(entityMinecartBase instanceof EntityGoldMinecartDispenser)
    //    {
    //      this.setEntityMinecartTEBase((EntityGoldMinecartDispenser)entityMinecartBase);
    //    }
  }
  //MFR grabs TE's just a bit different than most
  @Override
  protected IChunkProvider createChunkProvider() {
    return chunkProvider;
  }
  @Override
  public boolean spawnEntity(Entity entityIn) {
    return this.getCartWorld().spawnEntity(entityIn);
  }
  @Override

  @SideOnly(Side.CLIENT)
  public void spawnParticle(EnumParticleTypes particleType, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
  {
    this.getCartWorld().spawnParticle(particleType, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
  }
  //Pretty sure this for IE's blocks originally though other use it.
  //  @Override
  //  public void markBlockForUpdate(BlockPos blockPos)
  //  {
  //    if(this.getEntityMinecartTEBase() != null)
  //    {
  //      this.getEntityMinecartTEBase().setDirty(true);
  //    }
  //  }
  //Enderchest use this for open and close
  //  @Override
  //  public void addBlockEvent(BlockPos blockPos, Block block, int metadata, int p_14745) {}
  //
  //  @Override
  //  public boolean isSideSolid(BlockPos blockPos, EnumFacing blockSide)
  //  {
  //    return false;
  //  }
  //
  //  @Override
  //  public TileEntity getTileEntity(BlockPos blockPos)
  //  {
  //    if(blockPos.equals(this.getEntityMinecartBase().ORIGIN_POS)) {
  //      if(this.getEntityMinecartTEBase() != null)
  //      {
  //        return this.getEntityMinecartTEBase().getTileEntity();
  //      }
  //    }
  //    return null;
  //  }
  //
  //  //Really hope this doesn't break anything. Welp.
  //  @Override
  //  protected int getRenderDistanceChunks()
  //  {
  //    return 0;
  //  }
  @Override
  public Entity getEntityByID(int id) {
    return this.getEntityMinecartBase().world.getEntityByID(id);
  }
  //Most Blocks use this.
  @Override
  public IBlockState getBlockState(BlockPos blockPos) {
    //    if(blockPos.equals(this.getEntityMinecartBase().ORIGIN_POS)) {
    ModCyclic.logger.info("getBlockState fakeworld " + this.getEntityMinecartBase().getDisplayTile());
    return this.getEntityMinecartBase().getDisplayTile();
    //    }
    //    ModCyclic.logger.info("getBlockState defaulyt ");
    //    return this.getEntityMinecartBase().getDefaultDisplayTile();
  }
  //  Infinitato tries to get Entities and add potion effects
  @Override
  public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> entityClass, AxisAlignedBB axisAlignedBB) {
    axisAlignedBB.getAverageEdgeLength();
    return this.getCartWorld().getEntitiesWithinAABB(entityClass, axisAlignedBB);
  }
  //  //Infinitato creates explosions when it lands
  //  @Override
  //  public Explosion createExplosion(Entity entity, double posX, double posY, double posZ, float size, boolean damage)
  //  {
  //    return this.getCartWorld().createExplosion(entity, this.getCartX(), this.getCartY(), this.getCartZ(), size, damage);
  //  }
  //  @Override
  //  public void playSoundEffect(double posX, double posY, double posZ, String sound, float noice, float soundTimes)
  //  {
  //    this.getCartWorld().playSoundAtEntity(this.getEntityMinecartBase(), sound, noice, soundTimes);
  //  }
  @Override
  public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ) {
    return this.getCartWorld().getChunkFromChunkCoords(chunkX, chunkZ);
  }
  @Override
  protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
    return true;
  }
  @Override
  public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity) {
    //    if(this.getEntityMinecartTEBase() != null)
    //    {
    //      this.getEntityMinecartTEBase().markDirty();
    //    }
  }
  //  public EntityMinecartTEBase getEntityMinecartTEBase()
  //  {
  //    if(entityMinecartTEBase == null && entityMinecartBase != null)
  //    {
  //      this.setEntityMinecartTEBase((EntityMinecartTEBase)entityMinecartBase);
  //    }
  //    return entityMinecartTEBase;
  //  }
  //
  //  public void setEntityMinecartTEBase(EntityMinecartTEBase entityMinecartTEBase)
  //  {
  //    this.entityMinecartTEBase = entityMinecartTEBase;
  //  }
  public EntityGoldMinecartDispenser getEntityMinecartBase() {
    //    if(entityMinecartBase == null && entityMinecartTEBase != null)
    //    {
    //        this.setEntityMinecartBase(entityMinecartTEBase);
    //    }
    return entityMinecartBase;
  }
  public void setEntityMinecartBase(EntityGoldMinecartDispenser entityMinecartBase) {
    this.entityMinecartBase = entityMinecartBase;
  }
  //  public double getCartX()
  //  {
  //    return this.getEntityMinecartBase().posX;
  //  }
  //
  //  public double getCartY()
  //  {
  //    return this.getEntityMinecartBase().posY;
  //  }
  //
  //  public double getCartZ()
  //  {
  //    return this.getEntityMinecartBase().posZ;
  //  }
  public World getCartWorld() {
    return this.getEntityMinecartBase().world;
  }
}
