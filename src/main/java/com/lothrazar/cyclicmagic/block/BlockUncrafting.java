package com.lothrazar.cyclicmagic.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityUncrafting;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilUncraft;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockUncrafting extends Block implements IHasRecipe,IHasConfig {
	// dont use blockContainer !!
	// http://www.minecraftforge.net/forum/index.php?topic=31953.0
	private static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockUncrafting() {
		super(Material.IRON);
		this.setHardness(3.0F).setResistance(5.0F);
		this.setSoundType(SoundType.METAL);
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
 
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity == null || player.isSneaking()) { return false; }

		int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		player.openGui(ModMain.instance, ModGuiHandler.GUI_INDEX_UNCRAFTING, world, x, y, z);

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
		if (tile instanceof IInventory == false) { return; }

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
		return hasTileEntity();
	}

	@Override
	public void addRecipe() {
		GameRegistry.addRecipe(new ItemStack(this), " r ", "fdf", " o ", 
				'o', Blocks.OBSIDIAN, 
				'f', Blocks.FURNACE, 'r', Blocks.DROPPER, 'd', Blocks.DIAMOND_BLOCK);
	}

	@Override
	public void syncConfig(Configuration config) {

		
//		String category = Const.ConfigCategory.uncrafting;
		
//		UtilUncraft.TIMER_FULL = config.getInt("Speed", category, 75, 10, 99999, "How fast this can uncraft items and blocks.  Lower numbers are faster");

		// blockIfCannotDoit = config.getBoolean("auto_block_slots", category,
		// true,
		// "Automatically block items from entering the slots if we cannot find a
		// way to uncraft them. If this is set to false. then items like flowers and
		// such will be allowed in, but then spat right back out again.");

		String category = Const.ConfigCategory.modpacks;
		
		UtilUncraft.dictionaryFreedom = config.getBoolean("PickFirstMeta", category, true, "If you change this to true, then the uncrafting will just take the first of many options in any recipe that takes multiple input types.  For example, false means chests cannot be uncrafted, but true means chests will ALWAYS give oak wooden planks.");
		
		
		config.addCustomCategoryComment(category, "Here you can blacklist any thing, vanilla or modded.  Mostly for creating modpacks.  Input means you cannot uncraft it at all.  Output means it will not come out of a recipe.");

		// so when uncrafting cake, you do not get milk buckets back
		String def = "";
		String csv = config.getString("BlacklistInput", category, def, "Items that cannot be uncrafted; not allowed in the slots.  EXAMPLE : 'item.stick,tile.hayBlock,tile.chest'  ");
		// [item.stick, tile.cloth]
		UtilUncraft.blacklistInput = (List<String>) Arrays.asList(csv.split(","));
		if (UtilUncraft.blacklistInput == null){
			UtilUncraft.blacklistInput = new ArrayList<String>();
		}
		def = "item.milk";
		csv = config.getString("BlacklistOutput", category, def, "Comma seperated items that cannot come out of crafting recipes.  For example, if milk is in here, then cake is uncrafted you get all items except the milk buckets.  ");

		UtilUncraft.blacklistOutput = (List<String>) Arrays.asList(csv.split(","));
		if (UtilUncraft.blacklistOutput == null) {
			UtilUncraft.blacklistOutput = new ArrayList<String>();
		}
	}
}
