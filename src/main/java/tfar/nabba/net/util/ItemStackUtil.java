package tfar.nabba.net.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.units.qual.C;
import tfar.nabba.util.NBTKeys;

public class ItemStackUtil {
    //this is needed because servers, and the itemstack read/write methods will truncate item count to a byte
    public static void writeExtendedItemStack(FriendlyByteBuf buf, ItemStack stack) {
        buf.writeItem(stack);
        buf.writeInt(stack.getCount());
    }

    public static ItemStack readExtendedItemStack(FriendlyByteBuf buf) {
        ItemStack itemstack = buf.readItem();
        int realCount = buf.readInt();
        itemstack.setCount(realCount);
        return itemstack;
    }

    public static CompoundTag writeExtendedStack(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        tag.put(NBTKeys.Stack.name(),stack.save(new CompoundTag()));
        tag.putInt(NBTKeys.RealCount.name(), stack.getCount());
        return tag;
    }

    public static ItemStack readExtendedItemStack(CompoundTag tag) {
        ItemStack stack = ItemStack.of(tag.getCompound(NBTKeys.Stack.name()));
        int count = tag.getInt(NBTKeys.RealCount.name());
        stack.setCount(count);
        return stack;
    }

    public static void writeList(FriendlyByteBuf buf, NonNullList<ItemStack> stacks) {
        buf.writeInt(stacks.size());
        for (int i = 0; i < stacks.size(); i++) {
            writeExtendedItemStack(buf, stacks.get(i));
        }
    }

    public static NonNullList<ItemStack> readList(FriendlyByteBuf buf) {
        int size = buf.readInt();
        NonNullList<ItemStack> stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        for (int i = 0; i < size; i++) {
            ItemStack stack = readExtendedItemStack(buf);
            stacks.set(i, stack);
        }
        return stacks;
    }
}
