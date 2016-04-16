package com.lothrazar.cyclicmagic.item;

import java.util.Set;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemMultiTool extends ItemTool implements IHasRecipe {
	public static final String			name									= "multitool";

	// copied from tool base classes not imported since private
	private static final Set<Block>	AXE_EFFECTIVE_ON			= Sets.newHashSet(new Block[] { Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder });
	private static final Set<Block>	PICKAXE_EFFECTIVE_ON	= Sets.newHashSet(new Block[] { Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone, Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore,
	    Blocks.lit_redstone_ore, Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone, Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab });
	private static final Set<Block>	SPADE_EFFECTIVE_ON		= Sets.newHashSet(new Block[] { Blocks.clay, Blocks.dirt, Blocks.farmland, Blocks.grass, Blocks.gravel, Blocks.mycelium, Blocks.sand, Blocks.snow, Blocks.snow_layer, Blocks.soul_sand });

	private static final Set<Block>	EFFECTIVE_ON					= Sets.newHashSet(Iterables.concat(AXE_EFFECTIVE_ON, PICKAXE_EFFECTIVE_ON, SPADE_EFFECTIVE_ON));

	protected static float					damageVsEntity				= 1;
	protected static float					attackSpeed						= -2.8F;

	public ItemMultiTool() {

		super(damageVsEntity, attackSpeed, ToolMaterial.DIAMOND, EFFECTIVE_ON);

		this.setMaxDamage(500000);// unbreakable

	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {

		stack.addEnchantment(Enchantments.efficiency, 5);

		super.onCreated(stack, worldIn, playerIn);
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {

		return this.toolMaterial.getHarvestLevel();
	}

	/*
	 * @Override
	 * public float getDigSpeed(ItemStack stack,
	 * net.minecraft.block.state.IBlockState state){
	 * 
	 * float digSpeed = ToolMaterial.EMERALD.getEfficiencyOnProperMaterial();
	 * if(state != null && state.getBlock() == Blocks.obsidian){
	 * digSpeed = digSpeed * 3; // from 8 to 24
	 * }
	 * return digSpeed;
	 * }
	 */
	@Override
	public Set<String> getToolClasses(ItemStack stack) {

		return Sets.newHashSet(new String[] { "pickaxe", "axe", "shovel" });
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {

		return ToolMaterial.GOLD.getEnchantability();
	}

	@Override
	public void addRecipe() {

		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegistry.diamondCarrot), 
				Items.diamond_pickaxe, Items.diamond_axe, Items.diamond_shovel, Items.diamond_hoe,
				Items.chorus_fruit, Items.emerald, Items.blaze_rod, Items.ghast_tear,
				Items.prismarine_crystals
		);
	}

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		}
		else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (playerIn.isSneaking() && facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up()) 
					&& (block == Blocks.grass || block == Blocks.grass_path || block == Blocks.dirt )) {

				//sneaking: do what a hoe does to till dirt

				if (!worldIn.isRemote) {
					worldIn.setBlockState(pos, Blocks.farmland.getDefaultState(), 11);
					//stack.damageItem(1, playerIn);
				}

				UtilSound.playSound(worldIn,  pos, SoundEvents.item_hoe_till, SoundCategory.BLOCKS);
				
				
				return EnumActionResult.SUCCESS;
			}
			else if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.air && block == Blocks.grass) {
				// do what a shovel does to make a path
	
				UtilSound.playSound(worldIn,  pos, SoundEvents.item_shovel_flatten, SoundCategory.BLOCKS);
				//worldIn.playSound(playerIn, pos, SoundEvents.item_shovel_flatten, SoundCategory.BLOCKS, 1.0F, 1.0F);

				if (!worldIn.isRemote) {
					worldIn.setBlockState(pos, Blocks.grass_path.getDefaultState(), 11);
				}

				return EnumActionResult.SUCCESS;
			}
			else {
				return EnumActionResult.PASS;
			}
		}
	}
}
