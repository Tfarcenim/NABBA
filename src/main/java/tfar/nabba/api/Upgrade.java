package tfar.nabba.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import tfar.nabba.blockentity.BetterBarrelBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface Upgrade {

    Map<ResourceLocation, Upgrade> REGISTRY = new HashMap<>();

    int getSlotRequirement();

    //todo, config stuff
    void setSlotsRequired(int slotsRequired);
    int getMaxStackSize();

    int getStorageBonus();
    void onUpgrade(BetterBarrelBlockEntity betterBarrelBlockEntity, UpgradeStack stack);
    Supplier<Item> getItem();
    BiConsumer<BetterBarrelBlockEntity, UpgradeStack> NOTHING = (b, u) -> {};
    void tick(BetterBarrelBlockEntity barrelBlockEntity, UpgradeStack upgradeStack);

    ResourceLocation getKey();

}