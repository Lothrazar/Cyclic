package com.lothrazar.cyclicmagic.entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.world.World;

public class EntityGoldMinecartDropper extends EntityMinecartChest {
  public EntityGoldMinecartDropper(World worldIn) {
    super(worldIn);
  }
  public EntityGoldMinecartDropper(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  public int getSizeInventory() {
    return 9;
  }
  public IBlockState getDefaultDisplayTile() {
    return Blocks.DROPPER.getDefaultState();//.withProperty(BlockChest.FACING, EnumFacing.NORTH);
  }
  public String getGuiID() {
    //minecraft:dropper
    return "minecraft:dropper";
  }
  @Override
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    this.addLoot(playerIn);
    return new ContainerDispenser(playerInventory, this);
  }
}
