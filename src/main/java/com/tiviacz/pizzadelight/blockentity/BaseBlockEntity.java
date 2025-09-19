package com.tiviacz.pizzadelight.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.items.IItemHandlerModifiable;

public class BaseBlockEntity extends BlockEntity {
    protected final String INVENTORY = "Inventory";

    public BaseBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    public boolean isEmpty(IItemHandlerModifiable inventory) {
        for(int i = 0; i < inventory.getSlots(); i++) {
            if(!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        notifyBlockUpdate();
    }

    protected void notifyBlockUpdate() {
        if(getLevel() == null || getLevel().isClientSide) {
            return;
        }
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        getLevel().gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(getBlockState()));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.handleUpdateTag(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata().copy();
    }
}