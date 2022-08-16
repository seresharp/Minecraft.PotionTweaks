package com.serenam.potiontweaks.blocks;

import com.serenam.potiontweaks.gui.SingularityCrafterScreenHandler;
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

public class SingularityCrafterBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory
{
    private static final int[] ALL_SLOTS = new int[] { 0, 1 };

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public SingularityCrafterBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.SINGULARITY_CRAFTER, pos, state);
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
        return new SingularityCrafterScreenHandler(syncId, inv, this);
    }

    @Override
    public Text getDisplayName()
    {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    public static void tick(World world, BlockPos pos, BlockState state, SingularityCrafterBlockEntity be)
    {
        ItemStack in = be.inventory.get(0);
        ItemStack out = be.inventory.get(1);

        if (out.isEmpty() && CustomPotionUtil.IsNormalPotion(in))
        {
            be.inventory.set(1, CustomPotionUtil.ToSingularity(be.inventory.get(0)));
            be.inventory.set(0, ItemStack.EMPTY);
        }
        else if (CustomPotionUtil.IsSingularity(out) && out.getDamage() > 0 && CustomPotionUtil.IsNormalPotion(in))
        {
            ItemStack match = CustomPotionUtil.ToSingularity(in);
            match.setDamage(out.getDamage());
            if (ItemStack.areEqual(out, match))
            {
                out.setDamage(out.getDamage() - 1);
                be.inventory.set(0, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack)
    {
        return slot == 0 && CustomPotionUtil.IsNormalPotion(stack);
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
        return slot == 1 && stack.getDamage() == 0;
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
