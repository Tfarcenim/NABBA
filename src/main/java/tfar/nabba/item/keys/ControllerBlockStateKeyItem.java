package tfar.nabba.item.keys;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import tfar.nabba.api.InteractsWithBarrel;
import tfar.nabba.api.InteractsWithController;
import tfar.nabba.block.AbstractBarrelBlock;
import tfar.nabba.block.BetterBarrelBlock;
import tfar.nabba.blockentity.SingleSlotBarrelBlockEntity;

public class ControllerBlockStateKeyItem extends BlockStateKeyItem implements InteractsWithController {

    public ControllerBlockStateKeyItem(Properties pProperties, BooleanProperty property) {
        super(pProperties,property);
    }

    @Override
    public boolean handleController(BlockState state, ItemStack itemstack, Level level, BlockPos pos, Player player) {
        interactWithBarrels(state,itemstack,level,pos,player);
        return true;
    }
}
