package com.lothrazar.cyclicmagic.component.crafter;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.miner.BlockMiner.MinerType;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.IItemHandler;
 
public class TileEntityCrafter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  public TileEntityCrafter() {
    super(0);
  }
  IRecipe recipe;
  private int needsRedstone = 1;
  public static enum Fields {
    REDSTONE
  }
  @Override
  public void update() {
    if (isRunning()) {
      this.spawnParticlesAbove();
    }

    //TODO: first see i we have mats in inventory, 
    //and need some way to set recipe from GUI
    setRecipeInput();
    
    IRecipe r = getRecipe();
    //does it match
    if(r!=null&& r.matches(crafter, world)){
// pay the cost  
      final ItemStack craftingResult = recipe.getCraftingResult((InventoryCrafting)this.crafter);
      
      //confirmed this test does actually et the outut: 4x planks 
      System.out.println("OUT"+craftingResult);
    }
  }
 Container DUMMY_CONTAINER = new Container() {
    public boolean canInteractWith(@Nonnull final EntityPlayer playerIn) {
        return false;
    }
};
  InventoryCrafting crafter = new InventoryCrafting(DUMMY_CONTAINER,3,3);
  private IRecipe getRecipe() {
    if (this.recipe != null) {
        return this.recipe;
    }
 
    final List<IRecipe> recipes = (List<IRecipe>)CraftingManager.getInstance().getRecipeList();
    for (final IRecipe recipe : recipes) {
        try {
            if (recipe.getRecipeSize() <= 9 && recipe.matches((InventoryCrafting)this.crafter, this.world)) {
                return this.recipe = recipe;
            }
            continue;
        }
        catch (Exception err) {
            throw new RuntimeException("Caught exception while querying recipe " , err);
        }
    }
    return this.recipe;
}
  private void setRecipeInput() {
    this.crafter.setInventorySlotContents(0, new ItemStack(Blocks.LOG));
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] {};
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case REDSTONE:
      return this.needsRedstone;
      }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case REDSTONE:
      this.needsRedstone = value;
      break;
      }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
