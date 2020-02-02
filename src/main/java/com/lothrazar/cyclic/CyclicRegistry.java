package com.lothrazar.cyclic;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.BlockDarkGlass;
import com.lothrazar.cyclic.block.BlockPeat;
import com.lothrazar.cyclic.block.BlockPeatFuel;
import com.lothrazar.cyclic.block.battery.ContainerBattery;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.block.breaker.TileBreaker;
import com.lothrazar.cyclic.block.cable.energy.TileCableEnergy;
import com.lothrazar.cyclic.block.expcollect.BlockExpPylon;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.fan.TileFan;
import com.lothrazar.cyclic.block.generator.BlockPeatGenerator;
import com.lothrazar.cyclic.block.generator.ContainerGenerator;
import com.lothrazar.cyclic.block.generator.TilePeatGenerator;
import com.lothrazar.cyclic.block.harvester.BlockHarvester;
import com.lothrazar.cyclic.block.harvester.ContainerHarvester;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.block.itemcollect.BlockCollector;
import com.lothrazar.cyclic.block.itemcollect.ContainerCollector;
import com.lothrazar.cyclic.block.itemcollect.TileCollector;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffolding;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.block.tank.BlockFluidTank;
import com.lothrazar.cyclic.block.tank.TileTank;
import com.lothrazar.cyclic.block.trash.BlockTrash;
import com.lothrazar.cyclic.block.trash.TileTrash;
import com.lothrazar.cyclic.enchant.EnchantExcavation;
import com.lothrazar.cyclic.enchant.EnchantLifeLeech;
import com.lothrazar.cyclic.enchant.EnchantMagnet;
import com.lothrazar.cyclic.enchant.EnchantMultishot;
import com.lothrazar.cyclic.enchant.EnchantVenom;
import com.lothrazar.cyclic.enchant.EnchantXp;
import com.lothrazar.cyclic.entity.EntityMagicNetEmpty;
import com.lothrazar.cyclic.entity.EntityTorchBolt;
import com.lothrazar.cyclic.entity.ItemMobContainer;
import com.lothrazar.cyclic.entity.ItemTorchThrower;
import com.lothrazar.cyclic.item.PeatItem;
import com.lothrazar.cyclic.item.tool.WrenchItem;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;

public class CyclicRegistry {

  public static ItemGroup itemGroup = new ItemGroup(ModCyclic.MODID) {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(Blocks.trash);
    }
  };

  public static class Textures {

    public static final ResourceLocation GUIINVENTORY = new ResourceLocation(ModCyclic.MODID, "textures/gui/inventory_wand.png");
    public static final ResourceLocation GUI = new ResourceLocation(ModCyclic.MODID, "textures/gui/peat_generator.png");
    public static final ResourceLocation SLOT = new ResourceLocation(ModCyclic.MODID, "textures/gui/inventory_slot.png");
    public static final ResourceLocation ENERGY_CTR = new ResourceLocation(ModCyclic.MODID, "textures/gui/energy_ctr.png");
    public static final ResourceLocation ENERGY_INNER = new ResourceLocation(ModCyclic.MODID, "textures/gui/energy_inner.png");
    public static final ResourceLocation WIDGETS = new ResourceLocation(ModCyclic.MODID, "textures/gui/enderio-publicdomain-widgetsv2.png");
  }

  public static class Entities {

    @ObjectHolder(ModCyclic.MODID + ":magic_net")
    public static EntityType<EntityMagicNetEmpty> netball;
    @ObjectHolder(ModCyclic.MODID + ":torch_bolt")
    public static EntityType<EntityTorchBolt> torchbolt;
    //    
    //    @ObjectHolder(ModCyclic.MODID + ":boomerang")
    //    public static EntityType<BoomerangEntity> boomerang;
  }

  public static class Enchants {
    //  @ObjectHolder(ModCyclic.MODID + ":quickshot")
    //  public static Enchantment quickshot;

    @ObjectHolder(ModCyclic.MODID + ":excavate")
    public static EnchantExcavation excavate;
    @ObjectHolder(ModCyclic.MODID + ":experience_boost")
    public static EnchantXp experience_boost;
    @ObjectHolder(ModCyclic.MODID + ":life_leech")
    public static EnchantLifeLeech life_leech;
    //  @ObjectHolder(ModCyclic.MODID + ":launch")
    //  public static Enchantment launch;
    @ObjectHolder(ModCyclic.MODID + ":magnet")
    public static EnchantMagnet magnet;
    @ObjectHolder(ModCyclic.MODID + ":multishot")
    public static EnchantMultishot multishot;
    //  @ObjectHolder(ModCyclic.MODID + ":quickshot")
    //  public static Enchantment quickshot;
    //  @ObjectHolder(ModCyclic.MODID + ":smelting")
    //  public static EnchantAutoSmelt smelting;
    @ObjectHolder(ModCyclic.MODID + ":venom")
    public static EnchantVenom venom;
    @ObjectHolder(ModCyclic.MODID + ":lava_walking")
    public static Enchantment lava_walking;
  }

  public static class Items {

    @ObjectHolder(ModCyclic.MODID + ":redstone_carrot_speed")
    public static Item redstone_carrot_speed;
    @ObjectHolder(ModCyclic.MODID + ":diamond_carrot_health")
    public static Item diamond_carrot_health;
    @ObjectHolder(ModCyclic.MODID + ":torch_launcher")
    public static ItemTorchThrower torch_launcher;
    @ObjectHolder(ModCyclic.MODID + ":scaffold_replace")
    public static ItemScaffolding item_scaffold_replace;
    @ObjectHolder(ModCyclic.MODID + ":scaffold_fragile")
    public static ItemScaffolding item_scaffold_fragile;
    @ObjectHolder(ModCyclic.MODID + ":scaffold_responsive")
    public static ItemScaffolding item_scaffold_responsive;
    @ObjectHolder(ModCyclic.MODID + ":mob_container")
    public static ItemMobContainer mob_container;
    @ObjectHolder(ModCyclic.MODID + ":wooden_wrench")
    public static WrenchItem wrench;
    @ObjectHolder(ModCyclic.MODID + ":peat_fuel")
    public static PeatItem peat_fuel;
    @ObjectHolder(ModCyclic.MODID + ":experience_food")
    public static Item experience_food;
    @ObjectHolder(ModCyclic.MODID + ":magic_net")
    public static Item magic_net;
    @ObjectHolder(ModCyclic.MODID + ":tile_transporter")
    public static Item tile_transporter;
    @ObjectHolder(ModCyclic.MODID + ":tile_transporter_empty")
    public static Item tile_transporterempty;
  }

  public static class Tiles {

    @ObjectHolder(ModCyclic.MODID + ":tank")
    public static TileEntityType<TileTank> tank;
    @ObjectHolder(ModCyclic.MODID + ":battery")
    public static TileEntityType<TileBattery> batterytile;
    @ObjectHolder(ModCyclic.MODID + ":energy_pipe")
    public static TileEntityType<TileCableEnergy> energy_pipeTile;
    @ObjectHolder(ModCyclic.MODID + ":collector")
    public static TileEntityType<TileCollector> collectortile;
    @ObjectHolder(ModCyclic.MODID + ":trash")
    public static TileEntityType<TileTrash> trashtile;
    @ObjectHolder(ModCyclic.MODID + ":peat_generator")
    public static TileEntityType<TilePeatGenerator> peat_generatorTile;
    @ObjectHolder(ModCyclic.MODID + ":harvester")
    public static TileEntityType<TileHarvester> harvesterTile;
    @ObjectHolder(ModCyclic.MODID + ":breaker")
    public static TileEntityType<TileBreaker> breakerTile;
    @ObjectHolder(ModCyclic.MODID + ":fan")
    public static TileEntityType<TileFan> fantile;
    @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
    public static TileEntityType<TileExpPylon> experience_pylontile;
  }

  public static class ContainerScreens {

    @ObjectHolder(ModCyclic.MODID + ":battery")
    public static ContainerType<ContainerBattery> batteryCont;
    @ObjectHolder(ModCyclic.MODID + ":collector")
    public static ContainerType<ContainerCollector> collectortileContainer;
    @ObjectHolder(ModCyclic.MODID + ":peat_generator")
    public static ContainerType<ContainerGenerator> generatorCont;
    @ObjectHolder(ModCyclic.MODID + ":harvester")
    public static ContainerType<ContainerHarvester> harvester;
  }

  public static class Blocks {

    //not populated in the most ideal way 
    public static List<BlockBase> blocks = new ArrayList<>();
    //now the auto binding
    @ObjectHolder(ModCyclic.MODID + ":tank")
    public static BlockFluidTank tank;
    @ObjectHolder(ModCyclic.MODID + ":scaffold_replace")
    public static BlockScaffolding scaffold_replace;
    @ObjectHolder(ModCyclic.MODID + ":scaffold_responsive")
    public static BlockScaffolding scaffold_responsive;
    @ObjectHolder(ModCyclic.MODID + ":scaffold_fragile")
    public static BlockScaffolding scaffold_fragile;
    @ObjectHolder(ModCyclic.MODID + ":harvester")
    public static BlockHarvester harvester;
    @ObjectHolder(ModCyclic.MODID + ":peat_generator")
    public static BlockPeatGenerator peat_generator;
    @ObjectHolder(ModCyclic.MODID + ":peat_unbaked")
    public static BlockPeat peat_unbaked;
    @ObjectHolder(ModCyclic.MODID + ":peat_baked")
    public static BlockPeatFuel peat_baked;
    @ObjectHolder(ModCyclic.MODID + ":breaker")
    public static Block breaker;
    @ObjectHolder(ModCyclic.MODID + ":fan")
    public static Block fan;
    @ObjectHolder(ModCyclic.MODID + ":soundproofing")
    public static Block soundproofing;
    @ObjectHolder(ModCyclic.MODID + ":dark_glass")
    public static BlockDarkGlass dark_glass;
    @ObjectHolder(ModCyclic.MODID + ":trash")
    public static BlockTrash trash;
    @ObjectHolder(ModCyclic.MODID + ":experience_pylon")
    public static BlockExpPylon experience_pylon;
    @ObjectHolder(ModCyclic.MODID + ":collector")
    public static BlockCollector collector;
    @ObjectHolder(ModCyclic.MODID + ":battery")
    public static Block battery;
    @ObjectHolder(ModCyclic.MODID + ":energy_pipe")
    public static Block energy_pipe;
    @ObjectHolder(ModCyclic.MODID + ":spikes_iron")
    public static Block spikes_iron;
    @ObjectHolder(ModCyclic.MODID + ":spikes_curse")
    public static Block spikes_curse;
    @ObjectHolder(ModCyclic.MODID + ":spikes_fire")
    public static Block spikes_fire;
    @ObjectHolder(ModCyclic.MODID + ":fluid_pipe")
    public static Block fluid_pipe;
    @ObjectHolder(ModCyclic.MODID + ":item_pipe")
    public static Block item_pipe;
  }
}
