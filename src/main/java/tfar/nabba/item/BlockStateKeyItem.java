package tfar.nabba.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import tfar.nabba.block.AbstractBarrelBlock;
import tfar.nabba.block.BetterBarrelBlock;
import tfar.nabba.blockentity.BetterBarrelBlockEntity;

public class BlockStateKeyItem extends KeyItem implements InteractsWithBarrel,InteractsWithController {
    private final BooleanProperty property;

    public BlockStateKeyItem(Properties pProperties, BooleanProperty property) {
        super(pProperties);
        this.property = property;
    }

    @Override
    public boolean handleBarrel(BlockState state, ItemStack itemstack, Level level, BlockPos pos, Player pPlayer) {

        if (!(state.getBlock() instanceof AbstractBarrelBlock) || !state.hasProperty(property)) return false;

        BlockState newState = state.setValue(property,!state.getValue(property));

        if (property == BetterBarrelBlock.LOCKED) {
            level.setBlock(pos,newState,3);
            if (!newState.getValue(property)) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof BetterBarrelBlockEntity barrelBlockEntity) {
                    barrelBlockEntity.clearGhost();
                }
            }
        } else if (property == BetterBarrelBlock.DISCRETE) {
            loadAndReplace(newState,level,pos);
        }

       // level.sendBlockUpdated(pos,state,newState,3);
        return true;
    }

    private static void loadAndReplace(BlockState newState, Level level, BlockPos pos) {
        BlockEntity oldBarrelEntity = level.getBlockEntity(pos);
        //saves the old barrels contents
        CompoundTag tag = oldBarrelEntity.saveWithoutMetadata();



        level.setBlockAndUpdate(pos, newState);

        //get the blockentity that now exists
        BlockEntity newBlockEntity = level.getBlockEntity(pos);
        newBlockEntity.load(tag);
        //need to make sure the game saves it!
        newBlockEntity.setChanged();
    }

    @Override
    public boolean handleController(BlockState state, ItemStack itemstack, Level level, BlockPos pos, Player player) {
        interactWithBarrels(state,itemstack,level,pos,player);
        return true;
    }
}
