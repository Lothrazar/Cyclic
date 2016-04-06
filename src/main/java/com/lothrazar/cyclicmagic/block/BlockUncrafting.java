package com.lothrazar.cyclicmagic.block;

import java.util.Random;

import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection; 
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer; 
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity; 
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUncrafting extends Block {
	// dont use blockContainer !!
	// http://www.minecraftforge.net/forum/index.php?topic=31953.0
	private static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockUncrafting() {
		super(Material.iron);
		this.setHardness(3.0F).setResistance(5.0F).setCreativeTab(CreativeTabs.tabTools);
		this.setStepSound(SoundType.METAL);
		this.setTickRandomly(true);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getHorizontal(meta);
		return this.getDefaultState().withProperty(PROPERTYFACING, facing);
	}

	public EnumFacing getFacingFromState(IBlockState state) {
		return (EnumFacing) state.getValue(PROPERTYFACING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumFacing facing = (EnumFacing) state.getValue(PROPERTYFACING);

		int facingbits = facing.getHorizontalIndex();
		return facingbits;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PROPERTYFACING });
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		// find the quadrant the player is facing
		EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);

		return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random rand) {
		//was randomDisplayTick in 1.8.x
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1431325-1-5-1-forge-spawning-fire-particle
		if (world.getTileEntity(pos) == null) {
			return;
		}
		TileEntityUncrafting tile = (TileEntityUncrafting) world.getTileEntity(pos);
		if (tile == null) {
			return;
		}

		if (tile.isBurning()) {

			// first we center everything, but vertiically up a but more than
			// the others
			double dx = (float) pos.getX() + world.rand.nextFloat(); // 0.5F +
			double dy = (float) pos.getY() + world.rand.nextFloat(); // 0.7F +
			double dz = (float) pos.getZ() + world.rand.nextFloat(); // 0.5F +

			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, dx, dy, dz, 0, 0, 0);

		}

		/*	private static String SOUND_SUCCESS = "random.break" ==>> entity.arrow.shoot;// http://minecraft.gamepedia.com/Sounds.json
	private static String SOUND_REJECTED = "random.bow";*/
		String playSound = tile.getAndClearSound();
		if (playSound != null && playSound.isEmpty() == false) {
			SoundEvent sound = (SoundEvent)SoundEvent.soundEventRegistry.getObject(new ResourceLocation(playSound));
			if(sound != null){		
				world.playSound(pos.getX(), pos.getY(), pos.getZ(),  sound, SoundCategory.BLOCKS,  1, 1, false);
			} 
			else{
				System.out.println("dead sound"+playSound);
			}
		}

		super.randomTick(world, pos, state, rand);
	}

	// onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,EnumHand hand, ItemStack heldItem,EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity == null || player.isSneaking()) {
			return false;
		}

		int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		player.openGui(ModMain.instance, ModMain.guiId, world, x, y, z);

		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		this.dropItems(world, pos, state);
		super.breakBlock(world, pos, state);
	}

	private void dropItems(World world, BlockPos pos, IBlockState state) {
		Random rand = world.rand;

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IInventory == false) {
			return;
		}

		int x = pos.getX(), y = pos.getY(), z = pos.getZ();

		IInventory inventory = (IInventory) tile;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float f = 0.05F;
				entityItem.motionX = rand.nextGaussian() * f;
				entityItem.motionY = rand.nextGaussian() * f + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * f;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}

	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new TileEntityUncrafting();
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
}
