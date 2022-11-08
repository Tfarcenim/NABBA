package tfar.itemrepository.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import tfar.itemrepository.ItemRepository;
import tfar.itemrepository.RepositoryMenu;
import tfar.itemrepository.util.Utils;
import tfar.itemrepository.init.ModBlockEntityTypes;
import tfar.itemrepository.inventory.RepositoryInventoryInputWrapper;
import tfar.itemrepository.inventory.RepositoryInventoryOutputWrapper;
import tfar.itemrepository.world.RepositoryInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RepositoryBlockEntity extends BlockEntity implements MenuProvider {

    public CompoundTag settings = new CompoundTag();
    private Component customName;
    public LazyOptional<IItemHandler> fullOptional = LazyOptional.of(this::getInventory);
    public LazyOptional<IItemHandler> inputOptional = LazyOptional.of(() -> new RepositoryInventoryInputWrapper(getInventory()));
    public LazyOptional<IItemHandler> outputOptional = LazyOptional.of(() -> new RepositoryInventoryOutputWrapper(getInventory()));

    public String search = "";

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int pIndex) {
            switch (pIndex) {
                case 0:
                    return getInventory().getSlots();
                case 1:
                    return getInventory().getFullSlots(search);
                default:
                    return 0;
            }
        }

        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0:
            }
        }

        public int getCount() {
            return 2;
        }
    };

    protected final int[] syncSlots = new int[54];

    private static void defaultDisplaySlots(int[] ints) {
        for (int i = 0; i <  ints.length;i++) {
            ints[i] = i;
        }
    }
    protected final ContainerData syncSlotsAccess = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return syncSlots[pIndex];
        }

        @Override
        public void set(int pIndex, int pValue) {
            syncSlots[pIndex] = pValue;
        }

        @Override
        public int getCount() {
            return syncSlots.length;
        }
    };

    public RepositoryInventory getInventory() {
        return ItemRepository.instance.data.getOrCreateInventory(settings.getInt(Utils.ID));
    }

    public RepositoryBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        defaultDisplaySlots(syncSlots);
    }

    public RepositoryBlockEntity(BlockPos pos,BlockState state) {
        this(ModBlockEntityTypes.REPOSITORY,pos,state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("repository");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new RepositoryMenu(pContainerId,pPlayerInventory, ContainerLevelAccess.create(level,getBlockPos()), getInventory(), dataAccess, syncSlotsAccess);
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) {
                return inputOptional.cast();
            }
            if (side == Direction.DOWN) {
                return outputOptional.cast();
            }
            return fullOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        fullOptional.invalidate();
    }

    public void saveAdditional(CompoundTag tag) {
        tag.put("settings",settings);
        if (this.customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
        super.saveAdditional(tag);
    }
    @Override//read
    public void load(CompoundTag tag) {
        settings = tag.getCompound("settings");
       // handler.deserializeNBT(settings);
        if (tag.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
        super.load(tag);
    }

    public void setCustomName(Component hoverName) {
        customName = hoverName;
    }

    public void initialize(ItemStack stack) {
        settings.putInt(Utils.ID,0);
    }
}