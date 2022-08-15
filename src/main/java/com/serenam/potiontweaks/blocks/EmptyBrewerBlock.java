package com.serenam.potiontweaks.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class EmptyBrewerBlock extends HorizontalFacingBlock
{
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public EmptyBrewerBlock()
    {
        super(FabricBlockSettings
                .of(Material.WOOD)
                .strength(2.0F, 3.0F)
                .sounds(BlockSoundGroup.WOOD).nonOpaque());

        setDefaultState(stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }
}
