package com.serenam.potiontweaks.blocks;

import com.serenam.potiontweaks.gui.PotionCombinerScreenHandler;
import com.serenam.potiontweaks.registry.ModBlockEntities;
import com.serenam.potiontweaks.util.CustomPotionUtil;
import com.serenam.potiontweaks.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PotionCombinerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory
{
    private static final int[] ALL_SLOTS = new int[] { 0, 1, 2, 3, 4, 5 };
    private static final int[] LEFT_SLOTS = new int[] { 0, 1, 2, 4, 5 };
    private static final int[] RIGHT_SLOTS = new int[] { 0, 1, 3, 4, 5 };

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);

    private int brewTime;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate()
    {
        @Override
        public int get(int index)
        {
            return brewTime;
        }

        @Override
        public void set(int index, int value)
        {
            brewTime = value;
        }

        @Override
        public int size()
        {
            return 1;
        }
    };

    public PotionCombinerBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.POTION_COMBINER, pos, state);
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
        return new PotionCombinerScreenHandler(syncId, inv, this, propertyDelegate);
    }

    @Override
    public Text getDisplayName()
    {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    public static void tick(World world, BlockPos pos, BlockState state, PotionCombinerBlockEntity be)
    {
        // Inputs
        ItemStack blazePowder = be.inventory.get(0);
        ItemStack glowstone = be.inventory.get(1);
        ItemStack potion1 = be.inventory.get(2);
        ItemStack potion2 = be.inventory.get(3);

        // Outputs
        ItemStack potionOut = be.inventory.get(4);
        ItemStack bottleOut = be.inventory.get(5);

        boolean craftable = !blazePowder.isEmpty() && !glowstone.isEmpty() && !potion1.isEmpty()
                && !potion2.isEmpty() && potionOut.isEmpty() && bottleOut.getCount() < 64;
        boolean brewing = be.brewTime > 0;
        if (brewing)
        {
            be.brewTime--;
            boolean doneBrewing = be.brewTime <= 0;
            if (craftable && doneBrewing)
            {
                // Set outputs
                ItemStack comboPotion = CustomPotionUtil.CombinePotions(potion1, potion2);
                be.inventory.set(4, comboPotion);

                if (bottleOut.isEmpty())
                {
                    bottleOut = new ItemStack(Items.GLASS_BOTTLE);
                }
                else
                {
                    bottleOut.increment(1);
                }

                be.inventory.set(5, bottleOut);

                // Decrement inputs
                blazePowder.decrement(1);
                glowstone.decrement(1);
                potion1.decrement(1);
                potion2.decrement(1);

                be.inventory.set(0, blazePowder);
                be.inventory.set(1, glowstone);
                be.inventory.set(2, potion1);
                be.inventory.set(3, potion2);

                markDirty(world, pos, state);
            }
            else if (!craftable)
            {
                be.brewTime = 0;
                markDirty(world, pos, state);
            }
        }
        else if (craftable)
        {
            be.brewTime = 200;
            markDirty(world, pos, state);
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack)
    {
        return switch (slot)
        {
            case 0 -> stack.isOf(Items.BLAZE_POWDER);
            case 1 -> stack.isOf(Items.GLOWSTONE_DUST);
            case 2, 3 -> CustomPotionUtil.IsNormalPotion(stack);
            default -> false;
        };
    }

    @Override
    public int[] getAvailableSlots(Direction side)
    {
        if (side == Direction.UP || side == Direction.DOWN)
        {
            return ALL_SLOTS;
        }

        Direction facing = getCachedState().get(PotionCombinerBlock.FACING);
        Direction left = facing.rotateClockwise(Direction.Axis.Y);
        Direction right = facing.rotateCounterclockwise(Direction.Axis.Y);

        if (side == left)
        {
            return LEFT_SLOTS;
        }
        else if (side == right)
        {
            return RIGHT_SLOTS;
        }

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
        return slot == 4 || slot == 5;
    }

    @Override
    public void readNbt(NbtCompound nbt)
    {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);

        brewTime = nbt.getShort("BrewTime");
    }

    @Override
    protected void writeNbt(NbtCompound nbt)
    {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);

        nbt.putShort("BrewTime", (short)brewTime);
    }
}
