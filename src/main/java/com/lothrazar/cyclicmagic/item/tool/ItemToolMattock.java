package com.lothrazar.cyclicmagic.item.tool;
import java.util.List;
import java.util.Set;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolMattock extends ItemTool implements IHasRecipe {
  final static int RADIUS = 1;//radius 2 is 5x5 area square
  private Set<Material> mats;
  public ItemToolMattock(float attackDamageIn, float attackSpeedIn, Item.ToolMaterial materialIn, Set<Block> effectiveBlocksIn, Set<Material> mats) {
    super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
    this.mats = mats;
    this.setMaxDamage(9000);
  }
  @Override
  public Set<String> getToolClasses(ItemStack stack) {
    return com.google.common.collect.ImmutableSet.of(Const.ToolStrings.shovel, Const.ToolStrings.pickaxe);
  }
  @Override
  public boolean canHarvestBlock(IBlockState state) {
    Block block = state.getBlock();//    super.canHarvestBlock(blockIn)
    return block == Blocks.OBSIDIAN ? this.toolMaterial.getHarvestLevel() == 3 : (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE ? (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK ? (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE ? (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE ? (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE ? (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE ? (state.getMaterial() == Material.ROCK ? true : (state.getMaterial() == Material.IRON ? true : state.getMaterial() == Material.ANVIL)) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2);
  }
  @Override
  public float getStrVsBlock(ItemStack stack, IBlockState state) {
    return state.getMaterial() != Material.IRON && state.getMaterial() != Material.ANVIL && state.getMaterial() != Material.ROCK ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
  }
  /**
   * <<<< made with some help from Tinkers Construct and Spark's Hammers
   * https://github.com/thebrightspark/SparksHammers/blob/b84bd178fe2bbe47b13a89ef9435b20f09e429a4/src/main/java/com/brightspark/sparkshammers/util/CommonUtils.java
   * and https://github.com/SlimeKnights/TinkersConstruct
   */
  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos posHit, EntityPlayer player) {
    RayTraceResult ray = rayTrace(player.getEntityWorld(), player, false);
    if (ray == null) { return super.onBlockStartBreak(stack, posHit, player); }
    EnumFacing sideHit = ray.sideHit;
    World world = player.getEntityWorld();
    //use the shape builder to get region
    List<BlockPos> shape;
    if (sideHit == EnumFacing.UP || sideHit == EnumFacing.DOWN) {
      shape = UtilShape.squareHorizontalHollow(posHit, RADIUS);
    }
    else if (sideHit == EnumFacing.EAST || sideHit == EnumFacing.WEST) {
      shape = UtilShape.squareVerticalZ(posHit, RADIUS);
    }
    else {//has to be NORTHSOUTH
      shape = UtilShape.squareVerticalX(posHit, RADIUS);
    }
    for (BlockPos posCurrent : shape) {
      //first we validate
      if (posHit.equals(posCurrent)) {
        continue;
      }
      if (super.onBlockStartBreak(stack, new BlockPos(posCurrent), player)) {
        continue;
      }
      IBlockState bsCurrent = world.getBlockState(posCurrent);
      if (world.isAirBlock(posCurrent)) {
        continue;
      }
      if (!mats.contains(bsCurrent.getMaterial())) {
        continue;
      }
      Block blockCurrent = bsCurrent.getBlock();
      if (!ForgeHooks.canHarvestBlock(blockCurrent, player, world, posCurrent)) {
        continue;
      }
      //then we destroy
      stack.onBlockDestroyed(world, bsCurrent, posCurrent, player);
      if (world.isRemote) {//C
        world.playEvent(2001, posCurrent, Block.getStateId(bsCurrent));
        if (blockCurrent.removedByPlayer(bsCurrent, world, posCurrent, player, true)) {
          blockCurrent.onBlockDestroyedByPlayer(world, posCurrent, bsCurrent);
        }
        stack.onBlockDestroyed(world, bsCurrent, posCurrent, player);//update tool damage
        if (stack.stackSize == 0 && stack == player.getHeldItemMainhand()) {
          ForgeEventFactory.onPlayerDestroyItem(player, stack, EnumHand.MAIN_HAND);
          player.setHeldItem(EnumHand.MAIN_HAND, null);
        }
        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posCurrent, Minecraft.getMinecraft().objectMouseOver.sideHit));
      }
      else if (player instanceof EntityPlayerMP) {//Server side, so this works
        EntityPlayerMP mp = (EntityPlayerMP) player;
        int xpGivenOnDrop = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, posCurrent);
        if (xpGivenOnDrop >= 0) {
          if (blockCurrent.removedByPlayer(bsCurrent, world, posCurrent, player, true)) {
            TileEntity tile = world.getTileEntity(posCurrent);
            blockCurrent.onBlockDestroyedByPlayer(world, posCurrent, bsCurrent);
            blockCurrent.harvestBlock(world, player, posCurrent, bsCurrent, tile, stack);
            blockCurrent.dropXpOnBlockBreak(world, posCurrent, xpGivenOnDrop);
          }
          mp.connection.sendPacket(new SPacketBlockChange(world, posCurrent));
        }
      }
    }
    return super.onBlockStartBreak(stack, posHit, player);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "ede", "oso", " s ",
        'e', Items.EMERALD,
        'o', Blocks.OBSIDIAN,
        'd', Items.DIAMOND,
        's', Items.BONE);
    GameRegistry.addRecipe(new ItemStack(this),
        "ede", "oso", " s ",
        'e', Blocks.QUARTZ_BLOCK,
        'o', Blocks.OBSIDIAN,
        'd', Blocks.DIAMOND_BLOCK,
        's', Items.BONE);
  }
}
