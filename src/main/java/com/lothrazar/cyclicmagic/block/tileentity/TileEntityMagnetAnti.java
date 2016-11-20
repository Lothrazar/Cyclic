package com.lothrazar.cyclicmagic.block.tileentity;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;// net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

public class TileEntityMagnetAnti extends TileEntity implements ITickable {
  private int timer;
  private static final String NBT_TIMER = "Timer";
  public final static int TIMER_FULL = 20;
  public final static int ITEM_VRADIUS = 3;
  public final static int ITEM_HRADIUS = 32;
  public TileEntityMagnetAnti() {
    this.timer = TIMER_FULL;
  }
  @Override
  public ITextComponent getDisplayName() {
    return null;
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    timer = tagCompound.getInteger(NBT_TIMER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    return super.writeToNBT(tagCompound);
  }
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    // getDescriptionPacket()
    // Gathers data into a packet (S35PacketUpdateTileEntity) that is to be
    // sent to the client. Called on server only.
    NBTTagCompound syncData = new NBTTagCompound();
    this.writeToNBT(syncData);
    return new SPacketUpdateTileEntity(this.pos, 1, syncData);
  }
  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    // Extracts data from a packet (S35PacketUpdateTileEntity) that was sent
    // from the server. Called on client only.
    this.readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }
  public boolean isBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  @Override
  public void update() {
    boolean trigger = false;
    timer -= this.getSpeed();
    if (timer <= 0) {
      timer = TIMER_FULL;
      trigger = true;
    }
    // center of the block
    double x = this.getPos().getX() + 0.5;
    double y = this.getPos().getY() + 0.7;
    double z = this.getPos().getZ() + 0.5;
    if (trigger) {
      UtilEntity.moveEntityLivingNonplayers(this.getWorld(), x, y, z, ITEM_HRADIUS, ITEM_VRADIUS, false);
      timer = TIMER_FULL;//harvest worked!
      spawnParticles();
    }
  }
  protected void spawnParticles() {
    if (this.getWorld().isRemote) {
      double x = this.getPos().getX() + 0.5; //center of the block;
      double y = this.getPos().getY() + 0.5;
      double z = this.getPos().getZ() + 0.5;
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, x, y, z);
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, x, y + 1, z);
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, x, y + 2, z);
      UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, x, y + 3, z);
    }
  }
  private int getSpeed() {
    return 1;
  }
}
