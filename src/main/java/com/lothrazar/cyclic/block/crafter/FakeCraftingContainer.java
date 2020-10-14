package com.lothrazar.cyclic.block.crafter;

import com.lothrazar.cyclic.data.Const;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.IWorldPosCallable;

public class FakeCraftingContainer extends RecipeBookContainer<CraftingInventory> {
  public final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
  public final CraftResultInventory craftResult = new CraftResultInventory();
  private final IWorldPosCallable worldPosCallable;
  private final PlayerEntity player;

  public FakeCraftingContainer(int id, PlayerInventory playerInventory) {
    this(id, playerInventory, IWorldPosCallable.DUMMY);
  }

  public FakeCraftingContainer(int id, PlayerInventory playerInventory, IWorldPosCallable iWorldPosCallable) {
    super(ContainerType.CRAFTING, id);
    this.worldPosCallable = iWorldPosCallable;
    this.player = playerInventory.player;
    this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 0, 0));
    for(int rowPos = 0; rowPos < 3; rowPos++) {
      for(int colPos = 0; colPos < 3; colPos++) {
        this.addSlot(new Slot(this.craftMatrix, colPos + rowPos * 3, Const.SQ + colPos * Const.SQ, Const.SQ + rowPos * Const.SQ));
      }
    }
  }


  @Override
  public void fillStackedContents(RecipeItemHelper itemHelperIn) {

  }

  @Override
  public void clear() {

  }

  @Override
  public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
    return false;
  }

  @Override
  public int getOutputSlot() {
    return 0;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public int getSize() {
    return 0;
  }

  @Override
  public RecipeBookCategory func_241850_m() {
    return null;
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return false;
  }
}
