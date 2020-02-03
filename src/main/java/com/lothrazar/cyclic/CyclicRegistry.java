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
import com.lothrazar.cyclic.item.boomerang.BoomerangEntity;
import com.lothrazar.cyclic.item.tool.WrenchItem;
import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.potion.effect.StunEffect;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.ObjectHolder;

public class CyclicRegistry {

  public static ItemGroup itemGroup = new ItemGroup(ModCyclic.MODID) {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(Blocks.trash);
    }
  };

  public static class MaterialArmor {

    private static final String EMERALDID = ModCyclic.MODID + ":emerald";
    private static final String CRYSTALID = ModCyclic.MODID + ":crystal";
    public static final IArmorMaterial EMERALD = new IArmorMaterial() {

      @Override
      public int getDurability(EquipmentSlotType slotIn) {
        return ArmorMaterial.DIAMOND.getDurability(slotIn) + ArmorMaterial.IRON.getDurability(slotIn);
      }

      @Override
      public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return ArmorMaterial.DIAMOND.getDamageReductionAmount(slotIn) + ArmorMaterial.IRON.getDamageReductionAmount(slotIn);
      }

      @Override
      public int getEnchantability() {
        return ArmorMaterial.GOLD.getEnchantability();
      }

      @Override
      public SoundEvent getSoundEvent() {
        return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(net.minecraft.item.Items.EMERALD));
      }

      @Override
      public String getName() {
        return EMERALDID;
      }

      @Override
      public float getToughness() {
        return ArmorMaterial.DIAMOND.getToughness() * 1.5F;
      }
    };
    public static final IArmorMaterial GEMOBSIDIAN = new IArmorMaterial() {

      @Override
      public int getDurability(EquipmentSlotType slotIn) {
        return ArmorMaterial.DIAMOND.getDurability(slotIn) * 4;
      }

      @Override
      public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return ArmorMaterial.DIAMOND.getDamageReductionAmount(slotIn) * 3;
      }

      @Override
      public int getEnchantability() {
        return ArmorMaterial.GOLD.getEnchantability() + 3;
      }

      @Override
      public SoundEvent getSoundEvent() {
        return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(Items.gem_obsidian));
      }

      @Override
      public String getName() {
        return CRYSTALID;
      }

      @Override
      public float getToughness() {
        return ArmorMaterial.DIAMOND.getToughness() * 3F;
      }
    };
  }

  public static class MaterialTools {

    public static final IItemTier GEMOBSIDIAN = new IItemTier() {

      @Override
      public int getMaxUses() {
        return ItemTier.DIAMOND.getMaxUses() * 4;
      }

      @Override
      public float getEfficiency() {
        return ItemTier.DIAMOND.getEfficiency() * 4;
      }

      @Override
      public float getAttackDamage() {
        return ItemTier.DIAMOND.getAttackDamage() * 3.5F;
      }

      @Override
      public int getHarvestLevel() {
        return ItemTier.DIAMOND.getHarvestLevel() + 1;
      }

      @Override
      public int getEnchantability() {
        return ItemTier.GOLD.getEnchantability() + 1;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(Items.gem_obsidian));
      }
    };
    public static final IItemTier EMERALD = new IItemTier() {

      @Override
      public int getMaxUses() {
        return ItemTier.DIAMOND.getMaxUses() + ItemTier.GOLD.getMaxUses();
      }

      @Override
      public float getEfficiency() {
        return ItemTier.DIAMOND.getEfficiency() * 2;
      }

      @Override
      public float getAttackDamage() {
        return ItemTier.DIAMOND.getAttackDamage() * 1.5F;
      }

      @Override
      public int getHarvestLevel() {
        return ItemTier.DIAMOND.getHarvestLevel();
      }

      @Override
      public int getEnchantability() {
        return ItemTier.GOLD.getEnchantability() + 1;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(net.minecraft.item.Items.EMERALD));
      }
    };
    public static final IItemTier SANDSTONE = new IItemTier() {

      @Override
      public int getMaxUses() {
        return ItemTier.STONE.getMaxUses() - 2;
      }

      @Override
      public float getEfficiency() {
        return ItemTier.STONE.getEfficiency();
      }

      @Override
      public float getAttackDamage() {
        return (ItemTier.WOOD.getAttackDamage() + ItemTier.STONE.getAttackDamage()) / 2;
      }

      @Override
      public int getHarvestLevel() {
        return ItemTier.STONE.getHarvestLevel();
      }

      @Override
      public int getEnchantability() {
        return (ItemTier.WOOD.getEnchantability() + ItemTier.STONE.getEnchantability()) / 2;
      }

      @Override
      public Ingredient getRepairMaterial() {
        //        Ingredient.fromTag(Tag.)
        return Ingredient.fromStacks(new ItemStack(net.minecraft.block.Blocks.SANDSTONE));
      }
    };
    public static final IItemTier NETHERBRICK = new IItemTier() {

      @Override
      public int getMaxUses() {
        return (ItemTier.IRON.getMaxUses() + ItemTier.STONE.getMaxUses()) / 2;
      }

      @Override
      public float getEfficiency() {
        return (ItemTier.IRON.getEfficiency() + ItemTier.STONE.getEfficiency()) / 2;
      }

      @Override
      public float getAttackDamage() {
        return (ItemTier.IRON.getAttackDamage() + ItemTier.STONE.getAttackDamage()) / 2;
      }

      @Override
      public int getHarvestLevel() {
        return ItemTier.IRON.getHarvestLevel();
      }

      @Override
      public int getEnchantability() {
        return (ItemTier.IRON.getEnchantability() + ItemTier.STONE.getEnchantability()) / 2;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(net.minecraft.block.Blocks.NETHER_BRICKS));
      }
    };
  }

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
    @ObjectHolder(ModCyclic.MODID + ":boomerang_stun")
    public static EntityType<BoomerangEntity> boomerang_stun;
    @ObjectHolder(ModCyclic.MODID + ":boomerang_carry")
    public static EntityType<BoomerangEntity> boomerang_carry;
    @ObjectHolder(ModCyclic.MODID + ":boomerang_damage")
    public static EntityType<BoomerangEntity> boomerang_damage;
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

    @ObjectHolder(ModCyclic.MODID + ":gem_obsidian")
    protected static Item gem_obsidian;
    @ObjectHolder(ModCyclic.MODID + ":lapis_carrot_variant")
    public static Item lapis_carrot_variant;
    @ObjectHolder(ModCyclic.MODID + ":boomerang_damage")
    public static Item boomerang_damage;
    @ObjectHolder(ModCyclic.MODID + ":boomerang_carry")
    public static Item boomerang_carry;
    @ObjectHolder(ModCyclic.MODID + ":boomerang_stun")
    public static Item boomerang_stun;
    @ObjectHolder(ModCyclic.MODID + ":emerald_carrot_jump")
    public static Item emerald_carrot_jump;
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
    @ObjectHolder(ModCyclic.MODID + ":toxic_carrot")
    public static Item toxic_carrot;
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

  public static class PotionEffects {

    public static final List<TickableEffect> effects = new ArrayList<TickableEffect>();
    @ObjectHolder(ModCyclic.MODID + ":stun")
    public static StunEffect stun;
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
