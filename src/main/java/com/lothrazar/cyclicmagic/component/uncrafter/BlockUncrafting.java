package com.lothrazar.cyclicmagic.component.uncrafter;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.base.MachineTESR;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilUncraft;
import com.lothrazar.cyclicmagic.util.UtilUncraft.BlacklistType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUncrafting extends BlockBaseFacingInventory implements IHasRecipe, IHasConfig, IBlockHasTESR {
  // http://www.minecraftforge.net/forum/index.php?topic=31953.0
  public BlockUncrafting() {
    super(Material.IRON, ForgeGuiHandler.GUI_INDEX_UNCRAFTING);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
  }
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityUncrafter.class, new MachineTESR(this, 0));
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityUncrafter();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "d d",
        "frf",
        "ooo",
        'r', Blocks.DROPPER,
        'o', "obsidian",
        'f', Blocks.FURNACE,
        'd', "gemDiamond");
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.uncrafter;
    UtilUncraft.dictionaryFreedom = config.getBoolean("PickFirstMeta", category, true, "If you change this to true, then the uncrafting will just take the first of many options in any recipe that takes multiple input types.  For example, false means chests cannot be uncrafted, but true means chests will ALWAYS give oak wooden planks.");
    UtilUncraft.resetBlacklists();
    config.addCustomCategoryComment(category, "Blacklists and other tweaks for the Uncrafting Grinder.   (Use F3+H to see the details, it is always 'modid:item')");
    //INPUT
    String[] deflist = new String[] {
        "minecraft:end_crystal",
        "minecraft:magma",
        "progressiveautomation:WitherDiamond",
        "progressiveautomation:WitherGold",
        "progressiveautomation:WitherIron",
        "progressiveautomation:WitherStone",
        "progressiveautomation:WitherWood",
        "minecraft:elytra", "techreborn:uumatter",
        "spectrite:spectrite_arrow","spectrite:spectrite_arrow_special"
    };
    String[] blacklist = config.getStringList("BlacklistInput", category, deflist, "Items that cannot be uncrafted.  ");
    UtilUncraft.setBlacklist(blacklist, BlacklistType.INPUT);
    //OUTPUT
    deflist = new String[] { "minecraft:milk_bucket", "minecraft:water_bucket", "minecraft:lava_bucket", "botania:manaTablet",
        "harvestcraft:juicerItem", "harvestcraft:mixingbowlItem", "harvestcraft:mortarandpestleItem",
        "harvestcraft:bakewareItem", "harvestcraft:saucepanItem", "harvestcraft:skilletItem", "harvestcraft:potItem", "harvestcraft:cuttingboardItem",
        "mysticalagriculture:infusion_crystal", "mysticalagriculture:master_infusion_crystal", "minecraft:nether_star", "minecraft:elytra", "techreborn:uumatter"
    };
    blacklist = config.getStringList("BlacklistOutput", category, deflist, "Items that cannot come out of crafting recipes.  For example, if milk is in here, then cake can be uncrafted, but you get all items except the milk buckets.  ");
    UtilUncraft.setBlacklist(blacklist, BlacklistType.OUTPUT);
    //MODNAME
    deflist = new String[] { "projecte", "resourcefulcrops" , "spectrite"};
    blacklist = config.getStringList("BlacklistMod", category, deflist, "If a mod id is in this list, then nothing from that mod will be uncrafted ");
    UtilUncraft.setBlacklist(blacklist, BlacklistType.MODNAME);
    //CONTAINS
    //    deflist = new String[] { "botania:manaTablet","projecte:pe_philosophers_stone" };//bot mana tablet
    //    blacklist = config.getStringList("BlacklistIfIngredient", category, deflist, "If something contains one of these items as output, uncrafting will be blocked.  For example, if you put 'minecraft:iron_ingot' here, you will not be able to uncraft pistons or iron swords or anything that uses iron at all.");
    //    UtilUncraft.setBlacklist(blacklist, BlacklistType.CONTAINS);
  }
}
