package com.lothrazar.cyclicmagic.component.bucketstorage;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBucketStorage extends BlockBase implements ITileEntityProvider, IHasRecipe {
  private Item bucketItem;
  public BlockBucketStorage(Item bucketIn) {
    super(Material.IRON);
    this.setHardness(7F);
    this.setResistance(7F);
    this.setSoundType(SoundType.GLASS);
    this.setHarvestLevel("pickaxe", 1);
    bucketItem = bucketIn;
  }
  public static final String NBTBUCKETS = "buckets";
  public static int getBucketsStored(ItemStack item) {
    if (item.getItem() == Item.getItemFromBlock(BlockRegistry.block_storeempty))
      return 0;
    if (item.getTagCompound() == null) {
      item.setTagCompound(new NBTTagCompound());
    }
    return item.getTagCompound().getInteger(NBTBUCKETS) + 1;
  }
  public static int getItemStackBucketNBT(ItemStack item) {
    if (item.getTagCompound() == null) {
      item.setTagCompound(new NBTTagCompound());
    }
    return item.getTagCompound().getInteger(NBTBUCKETS);
  }
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      int b = BlockBucketStorage.getItemStackBucketNBT(stack);
      TileEntityBucketStorage container = (TileEntityBucketStorage) worldIn.getTileEntity(pos);
      container.setBuckets(b);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT; // http://www.minecraftforge.net/forum/index.php?topic=18754.0
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) { // http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
    return false;
  }
  @Override
  public boolean hasComparatorInputOverride(IBlockState state) {
    return true;
  }
  @Override
  public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
    TileEntityBucketStorage container = (TileEntityBucketStorage) world.getTileEntity(pos);
    return container.getBuckets();
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityBucketStorage(meta);
  }
  //start of 'fixing getDrops to not have null tile entity', using pattern from forge BlockFlowerPot patch
  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    if (willHarvest) return true; //If it will harvest, delay deletion of the block until after getDrops
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }
  @Override
  public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack tool) {
    super.harvestBlock(world, player, pos, state, te, tool);
    world.setBlockToAir(pos);
  }
  //end of 'fixing getDrops to not have null tile entity'
  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    //?? TE null? http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677315-solved-tileentity-returning-null
    //http://www.minecraftforge.net/forum/index.php?/topic/38048-19-solved-blockgetdrops-and-tileentity/
    List<ItemStack> ret = new ArrayList<ItemStack>();
    Item item = Item.getItemFromBlock(this);//this.getItemDropped(state, rand, fortune);
    TileEntity ent = world.getTileEntity(pos);
    ItemStack stack = new ItemStack(item);
    if (ent != null && ent instanceof TileEntityBucketStorage) {
      TileEntityBucketStorage t = (TileEntityBucketStorage) ent;
      UtilNBT.setItemStackNBTVal(stack, BlockBucketStorage.NBTBUCKETS, t.getBuckets());
      t.setBuckets(0);
    }
    ret.add(stack);
    return ret;
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (hand != EnumHand.MAIN_HAND) { return false; }
    ItemStack held = entityPlayer.getHeldItem(hand);
    if (!held.isEmpty()) { return false; }
    Block blockClicked = state.getBlock();
    if ((blockClicked instanceof BlockBucketStorage) == false) { return false; }
    BlockBucketStorage block = (BlockBucketStorage) blockClicked;
    TileEntityBucketStorage container = (TileEntityBucketStorage) world.getTileEntity(pos);
    if (block.bucketItem != null && block.bucketItem == this.bucketItem) {
      if (world.isRemote == false) {
        if (container.getBuckets() > 0) {
          removeBucket(entityPlayer, world, container, block.bucketItem);
        }
        else { // it is also empty
          removeBucket(entityPlayer, world, container, block.bucketItem);
          world.setBlockState(pos, BlockRegistry.block_storeempty.getDefaultState());
        }
        world.updateComparatorOutputLevel(pos, blockClicked);
      }
      UtilSound.playSound(world, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS);
      spawnMyParticle(world, block.bucketItem, pos);// .offset(face)
    }
    return super.onBlockActivated(world, pos, state, entityPlayer, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer entityPlayer) {
    EnumHand hand = entityPlayer.getActiveHand();
    if (hand == null) {
      hand = EnumHand.MAIN_HAND;
    }
    ItemStack held = entityPlayer.getHeldItem(hand);
    if (pos == null) { return; }
    IBlockState bstate = world.getBlockState(pos);
    if (bstate == null) { return; }
    Block blockClicked = bstate.getBlock();
    if (blockClicked == null || blockClicked == Blocks.AIR) { return; }
    if ((blockClicked instanceof BlockBucketStorage) == false) { return; }
    BlockBucketStorage block = (BlockBucketStorage) blockClicked;
    TileEntityBucketStorage container = (TileEntityBucketStorage) world.getTileEntity(pos);
    if (entityPlayer.isSneaking() && world.isRemote == false) {
      int inside;
      if (block.bucketItem == null)
        inside = 0;
      else
        inside = container.getBuckets() + 1;// yess its messed up?
      UtilChat.addChatMessage(entityPlayer, new TextComponentTranslation(inside + ""));
      return;// no sounds just tell us how much
    }
    if (held.isEmpty()) { return; }
    // before we add the bucket, wait and should we set the block first?
    if (block.bucketItem == null) {
      IBlockState state = null;
      if (held.getItem() == Items.LAVA_BUCKET) {
        state = BlockRegistry.block_storelava.getDefaultState();
      }
      else if (held.getItem() == Items.WATER_BUCKET) {
        state = BlockRegistry.block_storewater.getDefaultState();
      }
      if (held.getItem() == Items.MILK_BUCKET) {
        state = BlockRegistry.block_storemilk.getDefaultState();
      }
      if (state != null) {
        if (world.isRemote == false) {
          world.setBlockState(pos, state);
          container.addBucket();
          entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
          world.updateComparatorOutputLevel(pos, blockClicked);
        }
        UtilSound.playSound(world, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS);
        spawnMyParticle(world, held.getItem(), pos);
      }
      return;
    }
    else if (held != null && held.getItem() == block.bucketItem) {
      if (world.isRemote == false) {
        container.addBucket();
        entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
        world.updateComparatorOutputLevel(pos, blockClicked);
      }
      UtilSound.playSound(world, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS);
      spawnMyParticle(world, block.bucketItem, pos);
      return;
    }
    super.onBlockClicked(world, pos, entityPlayer);
  }
  private void spawnMyParticle(World world, Item item, BlockPos pos) {
    if (item == Items.MILK_BUCKET)
      UtilParticle.spawnParticle(world, EnumParticleTypes.SNOW_SHOVEL, pos);
    else if (item == Items.LAVA_BUCKET)
      UtilParticle.spawnParticle(world, EnumParticleTypes.LAVA, pos);
    else if (item == Items.WATER_BUCKET)
      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, pos);
  }
  private void removeBucket(EntityPlayer entityPlayer, World world, TileEntityBucketStorage storage, Item bucketItem) {
    storage.removeBucket();
    entityPlayer.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(bucketItem));
  }
  @Override
  public IRecipe addRecipe() {
    if (this.bucketItem == null) { return RecipeRegistry.addShapedRecipe(new ItemStack(BlockRegistry.block_storeempty),
        "i i",
        " o ",
        "i i",
        'o', "obsidian", 'i', "ingotIron"); }
    return null;
  }
}
