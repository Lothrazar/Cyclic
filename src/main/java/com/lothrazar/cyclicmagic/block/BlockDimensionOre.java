package com.lothrazar.cyclicmagic.block;
import java.util.Random;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.module.WorldGenModule;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockDimensionOre extends BlockOre {
  private Item dropped;
  private int droppedMeta;
  private int randomMax;
  private int spawnChance = 0;
  private SpawnType spawn = null;
  public static enum SpawnType {
    ENDERMITE, SILVERFISH
  }
  public BlockDimensionOre(Item drop) {
    this(drop, 0);
  }
  public BlockDimensionOre(Item drop, int dmg) {
    this(drop, 0, 1);
  }
  public BlockDimensionOre(Item drop, int dmg, int max) {
    super();
    dropped = drop;
    droppedMeta = dmg;
    randomMax = max;
    this.setSoundType(SoundType.STONE);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setHarvestLevel(Const.ToolStrings.axe, 0);
    this.setHarvestLevel(Const.ToolStrings.shovel, 0);
  }
  public BlockDimensionOre setPickaxeHarvestLevel(int h) {
    this.setHarvestLevel(Const.ToolStrings.pickaxe, h);
    return this;
  }
  public void setSpawnType(SpawnType t, int chance) {
    this.spawn = t;
    this.spawnChance = chance;
  }
  public void registerSmeltingOutput(Item out) {
    this.registerSmeltingOutput(new ItemStack(out));
  }
  public void registerSmeltingOutput(ItemStack out) {
    GameRegistry.addSmelting(this, out, 1);
  }
  public void trySpawnTriggeredEntity(World world, BlockPos pos) {
    if (WorldGenModule.oreSpawns == false) { return; } //config has disabled spawning no matter what
    if (this.spawn != null) {
      int rand = world.rand.nextInt(100);
      if (rand < this.spawnChance) {
        Entity e = null;
        switch (this.spawn) {
          case ENDERMITE:
            e = new EntityEndermite(world);
          break;
          case SILVERFISH:
            e = new EntitySilverfish(world);
          break;
        }
        if (e != null) {
          e.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
          world.spawnEntity(e);
        }
      }
    }
  }
  public int damageDropped(IBlockState state) {
    return droppedMeta;
  }
  @Override
  public int quantityDroppedWithBonus(int fortune, Random random) {
    return super.quantityDroppedWithBonus(fortune, random);
  }
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return dropped;
  }
  public int quantityDropped(Random random) {
    if (randomMax == 1) { return 1; }
    return 1 + random.nextInt(randomMax);
  }
  @Override
  public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
    Random rand = world instanceof World ? ((World) world).rand : new Random();
    return MathHelper.getInt(rand, 2, 5);
  }
  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    return true;
  }
}
