package com.lothrazar.cyclicmagic.registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.context.BlockPosContext;

public class PermissionRegistry {
  public static final String MODIFYBLOCKS = "cyclic.player.modify";
  public static void register() {
    PermissionAPI.registerNode(MODIFYBLOCKS, DefaultPermissionLevel.ALL, "Check if the player modify blocks here");
  }
  public static boolean hasPermissionHere(EntityPlayer player, BlockPos pos) {
    return PermissionAPI.hasPermission(player.getGameProfile(), MODIFYBLOCKS, new BlockPosContext(player, pos, null, null));
  }
}
