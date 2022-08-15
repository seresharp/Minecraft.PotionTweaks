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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PotionCombinerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory
{
    private static final int[] ALL_SLOTS = new int[] { 0, 1, 2 };

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

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
        return new PotionCombinerScreenHandler(syncId, inv, this);
    }

    @Override
    public Text getDisplayName()
    {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    public static void tick(World world, BlockPos pos, BlockState state, PotionCombinerBlockEntity be)
    {
        ItemStack combined = CustomPotionUtil.CombinePotions(be.inventory.get(0), be.inventory.get(1));
        be.inventory.set(2, combined);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack)
    {
        return true;
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
        return true;
    }

    @Override
    public void readNbt(NbtCompound nbt)
    {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt)
    {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }
}
