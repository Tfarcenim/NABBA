package tfar.nabba.util;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import tfar.nabba.NABBA;
import tfar.nabba.api.Upgrade;
import tfar.nabba.api.UpgradeStack;
import tfar.nabba.blockentity.AbstractBarrelBlockEntity;
import tfar.nabba.init.ModItems;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static tfar.nabba.util.Utils.*;

public enum Upgrades implements Upgrade {
    DUMMY(0,NOTHING,() -> Items.AIR),
    STORAGE(1, Utils.add_to_internal_upgrades,BASE_STORAGE,() -> ModItems.BETTER_BARREL_STORAGE_UPGRADE,64000,NOTHING),
    VOID(1,Utils.apply_void,() -> ModItems.VOID_UPGRADE,1),
    PICKUP(8,Utils.add_to_internal_upgrades,() -> ModItems.PICKUP_1x1_UPGRADE,9, PICKUP_TICK),
    INFINITE_VENDING(2000000000, apply_infinite_vending,() -> ModItems.INFINITE_VENDING_UPGRADE,1);

    private int slotsRequired;
    private final Map<BarrelType,Integer> additionalStorage;
    private final BiConsumer<AbstractBarrelBlockEntity, UpgradeStack> onUpgrade;
    private final BiConsumer<AbstractBarrelBlockEntity, UpgradeStack> onTick;
    private final Supplier<Item> itemSupplier;
    private final int maxAllowed;

    Upgrades(int slotsRequired, BiConsumer<AbstractBarrelBlockEntity, UpgradeStack> onUpgrade, Supplier<Item> itemSupplier) {
        this(slotsRequired,onUpgrade,itemSupplier,64000);
    }

    Upgrades(int slotsRequired,  BiConsumer<AbstractBarrelBlockEntity, UpgradeStack> onUpgrade, Supplier<Item> itemSupplier, int maxAllowed) {
        this(slotsRequired,onUpgrade,itemSupplier,maxAllowed,NOTHING);
    }
    Upgrades(int slotsRequired, BiConsumer<AbstractBarrelBlockEntity, UpgradeStack> onUpgrade, Supplier<Item> itemSupplier, int maxAllowed,BiConsumer<AbstractBarrelBlockEntity, UpgradeStack> onTick) {
        this(slotsRequired,onUpgrade,NO_STORAGE,itemSupplier,maxAllowed,onTick);
    }

    Upgrades(int slotsRequired, BiConsumer<AbstractBarrelBlockEntity, UpgradeStack> onUpgrade, Map<BarrelType,Integer> additionalStorage,
             Supplier<Item> itemSupplier, int maxAllowed, BiConsumer<AbstractBarrelBlockEntity, UpgradeStack> onTick) {
        this.slotsRequired = slotsRequired;
        this.additionalStorage = additionalStorage;
        this.onUpgrade = onUpgrade;

        this.itemSupplier = itemSupplier;
        this.maxAllowed = maxAllowed;
        this.onTick = onTick;
        REGISTRY.put(this.getKey(),this);
    }

    public void setSlotsRequired(int slotsRequired) {
        this.slotsRequired = slotsRequired;
    }

    @Override
    public int getMaxStackSize() {
        return maxAllowed;
    }

    @Override
    public int getSlotRequirement() {
        return slotsRequired;
    }

    @Override
    public int getStorageBonus(BarrelType type) {
        return additionalStorage.get(type);
    }

    @Override
    public void onUpgrade(AbstractBarrelBlockEntity betterBarrelBlockEntity, UpgradeStack stack) {
        onUpgrade.accept(betterBarrelBlockEntity,stack);
    }

    @Override
    public Supplier<Item> getItem() {
        return itemSupplier;
    }

    @Override
    public void tick(AbstractBarrelBlockEntity barrelBlockEntity, UpgradeStack upgradeStack) {
        onTick.accept(barrelBlockEntity, upgradeStack);
    }

    @Override
    public ResourceLocation getKey() {
        return new ResourceLocation(NABBA.MODID,name().toLowerCase(Locale.ROOT));
    }

    private String descriptionId;

    public String getDescriptionId() {
        return getOrCreateDescriptionId();
    }

    private String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("nabba.upgrade",getKey());
        }
        return this.descriptionId;
    }
}
