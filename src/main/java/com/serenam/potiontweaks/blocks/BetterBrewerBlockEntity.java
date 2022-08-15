package com.serenam.potiontweaks.blocks;

import com.serenam.potiontweaks.gui.BetterBrewerScreenHandler;
import com.serenam.potiontweaks.registry.ModBlockEntities;
import com.serenam.potiontweaks.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BetterBrewerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory
{
    private static final int[] ALL_SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);

    private int brewTime;
    private int fuelBlaze;
    private int fuelEmerald;
    private int slotsLocked;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate()
    {
        @Override
        public int get(int index)
        {
            return switch (index)
            {
                case 0 -> brewTime;
                case 1 -> fuelBlaze;
                case 2 -> fuelEmerald;
                case 3 -> slotsLocked;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value)
        {
            switch (index)
            {
                case 0:
                    brewTime = value;
                    break;
                case 1:
                    fuelBlaze = value;
                    break;
                case 2:
                    fuelEmerald = value;
                    break;
                case 3:
                    slotsLocked = value;
                    break;
            }
        }

        @Override
        public int size()
        {
            return 4;
        }
    };

    public BetterBrewerBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.BETTER_BREWER, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems()
    {
        return inventory;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
    {
        return new BetterBrewerScreenHandler(syncId, inv, this, propertyDelegate);
    }

    @Override
    public Text getDisplayName()
    {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    public static void tick(World world, BlockPos pos, BlockState state, BetterBrewerBlockEntity be)
    {
        be.slotsLocked = world.isReceivingRedstonePower(pos) ? 1 : 0;

        ItemStack blazePowder = be.inventory.get(0);
        if (be.fuelBlaze <= 0 && blazePowder.isOf(Items.BLAZE_POWDER))
        {
            be.fuelBlaze = 20;
            blazePowder.decrement(1);
            markDirty(world, pos, state);
        }

        ItemStack emerald = be.inventory.get(1);
        if (be.fuelEmerald <= 0 && emerald.isOf(Items.EMERALD))
        {
            be.fuelEmerald = 20;
            emerald.decrement(1);
            markDirty(world, pos, state);
        }

        boolean craftable = canCraft(be.slotsLocked != 0, be.inventory);
        boolean brewing = be.brewTime > 0;
        if (brewing)
        {
            be.brewTime--;
            boolean doneBrewing = be.brewTime <= 0;
            if (craftable && doneBrewing)
            {
                craft(world, pos, be.slotsLocked != 0, be.inventory);
                be.fuelBlaze--;
                be.fuelEmerald -= 4;
                markDirty(world, pos, state);
            }
            else if (!craftable)
            {
                be.brewTime = 0;
                markDirty(world, pos, state);
            }
        }
        else if (craftable && be.fuelBlaze > 0 && be.fuelEmerald > 0)
        {
            be.brewTime = 600;
            markDirty(world, pos, state);
        }
    }

    private static ItemStack getCraftingOutput(boolean recipeLocked, DefaultedList<ItemStack> slots)
    {
        List<ItemStack> ingredients = new ArrayList<>();
        for (int i = 3; i < 8; i++)
        {
            ItemStack stack = slots.get(i);
            if (recipeLocked && stack.getCount() == 1)
            {
                return ItemStack.EMPTY;
            }

            if (!stack.isEmpty())
            {
                ingredients.add(stack);
            }
        }

        if (ingredients.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        ItemStack potion = new ItemStack(Items.POTION, 1);
        NbtCompound potionNbt = potion.getOrCreateNbt();
        potionNbt.putString("Potion", "minecraft:water");
        potion.setNbt(potionNbt);

        for (ItemStack item : ingredients)
        {
            if (!BrewingRecipeRegistry.hasRecipe(potion, item))
            {
                return ItemStack.EMPTY;
            }

            potion = BrewingRecipeRegistry.craft(item, potion);
        }

        return potion;
    }

    private static boolean canCraft(boolean slotsLocked, DefaultedList<ItemStack> slots)
    {
        ItemStack bottles = slots.get(2);
        if (bottles.isEmpty())
        {
            return false;
        }

        ItemStack out1 = slots.get(8);
        ItemStack out2 = slots.get(9);
        ItemStack out3 = slots.get(10);
        if (!out1.isEmpty() && !out2.isEmpty() && !out3.isEmpty())
        {
            return false;
        }

        ItemStack potion = getCraftingOutput(slotsLocked, slots);
        return !potion.isEmpty();
    }

    private static void craft(World world, BlockPos pos, boolean slotsLocked, DefaultedList<ItemStack> slots)
    {
        ItemStack potion = getCraftingOutput(slotsLocked, slots);
        for (int i = 8; i < 11; i++)
        {
            if (slots.get(i).isEmpty())
            {
                slots.set(i, potion.copy());

                ItemStack bottles = slots.get(2);
                bottles.decrement(1);
                slots.set(2, bottles);
                if (slots.get(2).isEmpty())
                {
                    break;
                }
            }
        }

        for (int i = 3; i < 8; i++)
        {
            ItemStack ingredient = slots.get(i);
            ingredient.decrement(1);
            if (ingredient.getItem().hasRecipeRemainder())
            {
                ItemStack remainder = new ItemStack(ingredient.getItem().getRecipeRemainder());
                if (remainder.isOf(Items.GLASS_BOTTLE))
                {
                    ItemStack bottles = slots.get(2);
                    bottles.increment(remainder.getCount());
                    slots.set(2, bottles);
                }
                else
                {
                    if (ingredient.isEmpty())
                    {
                        ingredient = remainder;
                    }
                    else
                    {
                        ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), remainder);
                    }
                }
            }

            slots.set(i, ingredient);
        }

        world.syncWorldEvent(1035, pos, 0);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack)
    {
        switch (slot)
        {
            case 0:
                return stack.isOf(Items.BLAZE_POWDER);
            case 1:
                return stack.isOf(Items.EMERALD);
            case 2:
                return stack.isOf(Items.GLASS_BOTTLE);
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                if (slotsLocked == 0)
                {
                    return BrewingRecipeRegistry.isValidIngredient(stack);
                }
                else
                {
                    return stack.isOf(inventory.get(slot).getItem());
                }

            default:
                return false;
        }
    }

    @Override
    public int[] getAvailableSlots(Direction side)
    {
        return ALL_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir)
    {
        return isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir)
    {
        return slot >= 8 && slot <= 10;
    }

    @Override
    public void readNbt(NbtCompound nbt)
    {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);

        brewTime = nbt.getShort("BrewTime");
        fuelBlaze = nbt.getByte("FuelBlaze");
        fuelEmerald = nbt.getByte("FuelEmerald");
        slotsLocked = nbt.getByte("SlotsLocked");
    }

    @Override
    protected void writeNbt(NbtCompound nbt)
    {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);

        nbt.putShort("BrewTime", (short)brewTime);
        nbt.putByte("FuelBlaze", (byte)fuelBlaze);
        nbt.putByte("FuelEmerald", (byte)fuelEmerald);
        nbt.putByte("SlotsLocked", (byte)slotsLocked);
    }
}
