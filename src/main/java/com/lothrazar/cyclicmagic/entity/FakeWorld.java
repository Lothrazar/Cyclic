/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.entity;

import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author SkySom Not me. From
 *         https://github.com/BrassGoggledCoders/MoarCarts/blob/9c841d7b9345b6231a929e0b3c3f9746c3d020b1/src/main/java/xyz/brassgoggledcoders/moarcarts/entities/EntityMinecartBase.java Which is a repo
 *         using MIT License https://github.com/BrassGoggledCoders/MoarCarts/blob/9c841d7b9345b6231a929e0b3c3f9746c3d020b1/LICENSE after this I also made my own changes alternate version i also used
 *         for reference
 *         https://github.com/BrassGoggledCoders/OpenTransport/blob/cc2208684ddf1b3db13863722a484f9f41c255f4/src/main/java/xyz/brassgoggledcoders/opentransport/api/wrappers/world/WorldWrapper.java
 */
public class FakeWorld extends World {

  private BlockPos originPos = new BlockPos(0, 0, 0);
  private EntityGoldMinecartDispenser entityMinecartBase;

  public FakeWorld(EntityGoldMinecartDispenser entityMinecartBase) {
    this(entityMinecartBase.world, entityMinecartBase);
  }

  public FakeWorld(World world, EntityGoldMinecartDispenser entityMinecartBase) {
    super(world.getSaveHandler(), world.getWorldInfo(), world.provider, world.profiler, world.isRemote);
    this.setEntityMinecartBase(entityMinecartBase);
  }

  //MFR grabs TE's just a bit different than most
  @Override
  protected IChunkProvider createChunkProvider() {
    return chunkProvider;
  }

  @Override
  public boolean spawnEntity(Entity entity) {
    entity.posX = this.getPosX();
    entity.posY = this.getPosY() + 2;
    entity.posZ = this.getPosZ();
    entity.forceSpawn = true;
    return this.getCartWorld().spawnEntity(entity);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void spawnParticle(EnumParticleTypes particleType, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
    this.getCartWorld().spawnParticle(particleType, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
  }

  @Override
  public Entity getEntityByID(int id) {
    return this.getCartWorld().getEntityByID(id);
  }

  @Override
  public void addBlockEvent(@Nonnull BlockPos pos, Block blockIn, int eventID, int eventParam) {
    this.entityMinecartBase.getDisplayTile().onBlockEventReceived(this, pos, eventID, eventParam);
  }

  @Override
  public IBlockState getBlockState(BlockPos blockPos) {
    if (blockPos.equals(originPos) || blockPos.getY() < 0 ||
        blockPos.equals(this.getEntityMinecartBase().getPosition())) {
      return this.getEntityMinecartBase().getDisplayTile();
    }
    return Blocks.AIR.getDefaultState();
  }

  @Override
  public TileEntity getTileEntity(@Nonnull BlockPos blockPos) {
    if (blockPos.equals(originPos)) {
      return null;
    } //TODO? this.getBlockWrapper().getTileEntity();
    return null;
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

  public double getPosX() {
    return this.getEntityMinecartBase().posX;
  }

  public double getPosY() {
    return this.getEntityMinecartBase().posY;
  }

  public double getPosZ() {
    return this.getEntityMinecartBase().posZ;
  }

  public World getCartWorld() {
    return this.getEntityMinecartBase().world;
  }
}
