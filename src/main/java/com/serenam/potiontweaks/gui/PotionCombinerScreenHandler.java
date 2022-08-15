package com.serenam.potiontweaks.gui;

import com.serenam.potiontweaks.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class PotionCombinerScreenHandler extends ScreenHandler
{
    private final Inventory inventory;

    public PotionCombinerScreenHandler(int syncId, PlayerInventory playerInventory)
    {
        this(syncId, playerInventory, new SimpleInventory(3));
    }

    public PotionCombinerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory)
    {
        super(ModScreenHandlers.POTION_COMBINER, syncId);
        checkSize(inventory, 3);
        this.inventory = inventory;

        inventory.onOpen(playerInventory.player);

        // Our inventory
        addSlot(new Slot(inventory, 0, 17, 17));
        addSlot(new Slot(inventory, 1, 17, 35));
        addSlot(new Slot(inventory, 2, 17, 53));

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
}
