package com.lothrazar.cyclicmagic.entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.world.World;

public class EntityGoldMinecartDispenser extends EntityMinecartChest {
  public EntityGoldMinecartDispenser(World worldIn) {
    super(worldIn);
  }
  public EntityGoldMinecartDispenser(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  public int getSizeInventory() {
    return 9;
  }
  public IBlockState getDefaultDisplayTile() {
    return Blocks.DISPENSER.getDefaultState();//.withProperty(BlockChest.FACING, EnumFacing.NORTH);
  }
  public String getGuiID() {
    return "minecraft:dispenser";
  }
  @Override
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    this.addLoot(playerIn);
    return new ContainerDispenser(playerInventory, this);
  }
}
