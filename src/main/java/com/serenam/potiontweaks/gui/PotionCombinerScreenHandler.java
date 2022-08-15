package com.serenam.potiontweaks.gui;

import com.serenam.potiontweaks.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class PotionCombinerScreenHandler extends ScreenHandler
{
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public PotionCombinerScreenHandler(int syncId, PlayerInventory playerInventory)
    {
        this(syncId, playerInventory, new SimpleInventory(6), new ArrayPropertyDelegate(1));
    }

    public PotionCombinerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate)
    {
        super(ModScreenHandlers.POTION_COMBINER, syncId);
        checkSize(inventory, 6);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;

        inventory.onOpen(playerInventory.player);

        addProperties(propertyDelegate);

        // Our inventory
        // Blaze powder and glowstone slots
        addSlot(new FuelSlot(inventory, 0, 37, 17, Items.BLAZE_POWDER));
        addSlot(new FuelSlot(inventory, 1, 37, 35, Items.GLOWSTONE_DUST));

        // Input potion slots
        addSlot(new PotionSlot(inventory, 2, 87, 17));
        addSlot(new PotionSlot(inventory, 3, 123, 17));

        // Output potion slots
        addSlot(new OutputSlot(inventory, 4, 96, 51));
        addSlot(new OutputSlot(inventory, 5, 114, 51));

        // The player inventory
        for (int m = 0; m < 3; m++)
        {
            for (int l = 0; l < 9; l++)
            {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

        // The player Hotbar
        for (int m = 0; m < 9; m++)
        {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    public int getBrewTime()
    {
        return propertyDelegate.get(0);
    }

    @Override
    public boolean canUse(PlayerEntity player)
    {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index)
    {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasStack())
        {
            ItemStack origStack = slot.getStack();
            newStack = origStack.copy();
            if (index < inventory.size())
            {
                if (!insertItem(origStack, inventory.size(), slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!insertItem(origStack, 0, inventory.size(), false))
            {
                return ItemStack.EMPTY;
            }

            if (origStack.isEmpty())
            {
                slot.setStack(ItemStack.EMPTY);
            }
            else
            {
                slot.markDirty();
            }
        }

        return newStack;
    }

    private static class FuelSlot extends Slot
    {
        private final Item FuelItem;

        public FuelSlot(Inventory inventory, int index, int x, int y, Item item)
        {
            super(inventory, index, x, y);
            FuelItem = item;
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return matches(stack);
        }

        @Override
        public int getMaxItemCount()
        {
            return FuelItem.getMaxCount();
        }

        public boolean matches(ItemStack stack)
        {
            return stack.isOf(FuelItem);
        }
    }

    private static class PotionSlot extends Slot
    {
        public PotionSlot(Inventory inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return matches(stack);
        }

        @Override
        public int getMaxItemCount()
        {
            return 1;
        }

        public boolean matches(ItemStack stack)
        {
            return stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION);
        }
    }

    private static class OutputSlot extends Slot
    {
        public OutputSlot(Inventory inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack)
        {
            return false;
        }

        @Override
        public int getMaxItemCount()
        {
            return 1;
        }
    }
}
