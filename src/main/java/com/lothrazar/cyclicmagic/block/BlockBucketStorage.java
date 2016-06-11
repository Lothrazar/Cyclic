package com.lothrazar.cyclicmagic.block;

import java.util.Random;

import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBucketStorage;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBucketStorage extends Block implements ITileEntityProvider {

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

	// http://www.minecraftforge.net/forum/index.php?topic=18754.0
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {

		return BlockRenderLayer.TRANSLUCENT;// ;// EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {

		// http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
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

		return new TileEntityBucketStorage(worldIn, meta);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		return null;
		// return Item.getItemFromBlock(BlockRegistry.block_storeempty);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

		// if(world.isRemote == false){System.out.println("Server.Right");}
		ItemStack held = entityPlayer.getHeldItem(hand);

		Block blockClicked = state.getBlock();
		if ((blockClicked instanceof BlockBucketStorage) == false) { return false; }
		BlockBucketStorage block = (BlockBucketStorage) blockClicked;
		TileEntityBucketStorage container = (TileEntityBucketStorage) world.getTileEntity(pos);

		long timeSince = world.getTotalWorldTime() - container.getTimeLast();
		if (timeSince < TileEntityBucketStorage.TIMEOUT) {
			// System.out.println("SKIP"+timeSince);
			return false;
		}

		if (held == null && block.bucketItem != null && block.bucketItem == this.bucketItem) {

			if (world.isRemote == false) {
				// server only

				if (container.getBuckets() > 0) {
					removeBucket(entityPlayer, world, container, block.bucketItem);
				}
				else {
					// it is also empty
					removeBucket(entityPlayer, world, container, block.bucketItem);
					world.setBlockState(pos, BlockRegistry.block_storeempty.getDefaultState());

				}

				container.setTimeLast(world.getTotalWorldTime());
				world.updateComparatorOutputLevel(pos, blockClicked);
			}
			// both sides
			

			UtilSound.playSound(world,pos,SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS);

			spawnMyParticle(world, block.bucketItem, pos);// .offset(face)

		}

		return super.onBlockActivated(world, pos, state, entityPlayer, hand, heldItem, side, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer entityPlayer) {

		// only left click
		// if(world.isRemote == false){System.out.println("Server.Left");}

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

		if (entityPlayer.isSneaking() && world.isRemote == false) { // server has
		                                                            // accurate
		                                                            // number
			int inside;
			if (blockClicked == BlockRegistry.block_storeempty)
				inside = 0;
			else
				inside = container.getBuckets() + 1;// yess its messed up?

			entityPlayer.addChatMessage(new TextComponentTranslation(inside + ""));

			return;// no sounds just tell us how much
		}

		if (held == null) { return; }

		// before we add the bucket, wait and should we set the block first?
		if (blockClicked == BlockRegistry.block_storeempty && block.bucketItem == null) {
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
					// System.out.println("addBucket to EMPTY BLOCK");
					// server only
					world.setBlockState(pos, state);
					container.addBucket();
					// entityPlayer.destroyCurrentEquippedItem();
					entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);

					world.updateComparatorOutputLevel(pos, blockClicked);
				}
				// both sides
				

				UtilSound.playSound(world,pos,SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS);
			
				spawnMyParticle(world, held.getItem(), pos);// .offset(face)
			}

			return;
		}
		else if (held != null && held.getItem() == block.bucketItem) {

			if (world.isRemote == false) {
				// System.out.println("addBucket to EXISTING BLOCK"+world.isRemote);
				// server only
				container.addBucket();
				// entityPlayer.destroyCurrentEquippedItem();
				entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);

				world.updateComparatorOutputLevel(pos, blockClicked);
			}

			// both sides
			UtilSound.playSound(world,pos,SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS);
	
			spawnMyParticle(world, block.bucketItem, pos);// .offset(face)
			return;
		}

		super.onBlockClicked(world, pos, entityPlayer);
	}

	/*
	 * @SubscribeEvent public void onPlayerInteract(PlayerInteractEvent event) {
	 * EntityPlayer
	 * entityPlayer = event.getEntityPlayer(); BlockPos pos = event.getPos();
	 * World world =
	 * event.getWorld(); EnumFacing face = event.getFace(); EnumHand hand =
	 * entityPlayer.getActiveHand(); ItemStack held =
	 * entityPlayer.getHeldItem(hand);
	 * 
	 * if (pos == null) { return; } IBlockState bstate = world.getBlockState(pos);
	 * if (bstate ==
	 * null) { return; }
	 * 
	 * Block blockClicked = bstate.getBlock();
	 * 
	 * if (blockClicked == null || blockClicked == Blocks.air) { return; } if
	 * ((blockClicked
	 * instanceof BlockBucketStorage) == false) { return; }
	 * 
	 * }
	 */
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

		UtilEntity.dropItemStackInWorld(world, entityPlayer.getPosition(), new ItemStack(bucketItem));
	}

	public void addRecipe() {
		if(this == BlockRegistry.block_storeempty)
			GameRegistry.addRecipe(new ItemStack(this), "i i", " o ", "i i", 'o', Blocks.OBSIDIAN, 'i', Blocks.IRON_BLOCK);
	
		// the filled ones are not crafted, only obtained when filled and then harvested
	}
	

	@SubscribeEvent
	public void onBreakEvent(BreakEvent event) {

		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState state = event.getState();
		TileEntity ent = world.getTileEntity(pos);

		// TODO; check tool/pickaxe? if notHarvestable or whatever, drop the
		// buckets and the ..glass?

		if (ent != null && ent instanceof TileEntityBucketStorage) {
			TileEntityBucketStorage t = (TileEntityBucketStorage) ent;
			ItemStack stack = new ItemStack(state.getBlock());

			UtilNBT.setItemStackNBTVal(stack, BlockBucketStorage.NBTBUCKETS, t.getBuckets());

			UtilEntity.dropItemStackInWorld(world, pos, stack);

			t.setBuckets(0);
		}
	}
}
