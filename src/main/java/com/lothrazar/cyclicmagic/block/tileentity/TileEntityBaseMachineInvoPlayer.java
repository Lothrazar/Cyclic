package com.lothrazar.cyclicmagic.block.tileentity;
import java.lang.ref.WeakReference;
import java.util.UUID;
import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class TileEntityBaseMachineInvoPlayer extends TileEntityBaseMachineInvo {
  public static final GameProfile breakerProfile = new GameProfile(UUID.nameUUIDFromBytes("CyclicFakePlayer2".getBytes(Charsets.UTF_8)), "CyclicFakePlayer2");
  protected WeakReference<FakePlayer> fakePlayer;
  protected UUID uuid;
  protected void initFakePlayer() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
      IBlockState state = worldObj.getBlockState(this.pos);
      worldObj.notifyBlockUpdate(pos, state, state, 3);
    }
    fakePlayer = new WeakReference<FakePlayer>(FakePlayerFactory.get((WorldServer) worldObj, breakerProfile));
    fakePlayer.get().onGround = true;
    fakePlayer.get().connection = new NetHandlerPlayServer(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManager(EnumPacketDirection.SERVERBOUND), fakePlayer.get()) {
      @SuppressWarnings("rawtypes")
      @Override
      public void sendPacket(Packet packetIn) {
      }
    };
  }
}
