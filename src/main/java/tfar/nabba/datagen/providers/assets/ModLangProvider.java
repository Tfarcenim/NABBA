package tfar.nabba.datagen.providers.assets;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;
import tfar.nabba.NABBA;
import tfar.nabba.api.Upgrade;
import tfar.nabba.block.AbstractBarrelBlock;
import tfar.nabba.blockentity.BarrelInterfaceBlockEntity;
import tfar.nabba.init.ModBlocks;
import tfar.nabba.init.ModItems;
import tfar.nabba.item.BarrelFrameUpgradeItem;
import tfar.nabba.item.keys.KeyItem;
import tfar.nabba.item.StorageUpgradeItem;
import tfar.nabba.item.UpgradeItem;
import tfar.nabba.util.Upgrades;
import tfar.nabba.util.Utils;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(DataGenerator gen) {
        super(gen, NABBA.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        //saves a couple of hundred lines of repetitive code
        for (Item item : Registry.ITEM) {
            if (item instanceof BarrelFrameUpgradeItem || item instanceof KeyItem) {
                defaultName(item);
            } else if (item instanceof StorageUpgradeItem storageUpgradeItem) {
                storageUpgradeName(item);
                addTooltip(item,"Adds %s "+storageUpgradeItem.getType().s+" of storage");
            }
        }

        for (Block block : Registry.BLOCK) {
            if (block instanceof AbstractBarrelBlock) {
                defaultName(block);
            }
        }


        addItem(() -> ModItems.INFINITE_VENDING_UPGRADE,"Infinite Vending Upgrade");
        addItem(() -> ModItems.VOID_UPGRADE,"Void Upgrade");
        addItem(() -> ModItems.PICKUP_1x1_UPGRADE,"Pickup 1x1 Upgrade");
        addItem(() -> ModItems.PICKUP_3x3_UPGRADE,"Pickup 3x3 Upgrade");
        addItem(()-> ModItems.PICKUP_9x9_UPGRADE,"Pickup 9x9 Upgrade");
        defaultName(ModBlocks.CONTROLLER);
        defaultName(ModBlocks.BARREL_INTERFACE);
        defaultName(ModBlocks.CONTROLLER_PROXY);
        defaultName(ModItems.BARREL_HAMMER);
        defaultName(ModItems.NETWORK_VISUALIZER);

        add(AbstractBarrelBlock.info,"Using %s upgrade slots");
        add(UpgradeItem.info,"Requires %s upgrade slots");
        add(UpgradeItem.info1,"Max of %s allowed per barrel");



        addUpgrade(Upgrades.INFINITE_VENDING,"Infinite Vending");
        addUpgrade(Upgrades.STORAGE,"Storage Units");
        addUpgrade(Upgrades.PICKUP,"Pickup Units");

        add("nabba.key_ring.selected_key","%s (%s)");
        add("itemGroup.nabba","Not (Just) Another Better Barrel Attempt");

        add("nabba.menu.vanity_key","Vanity Key");

        addItemTooltips();
        addSystemMessages();
    }

    public void addItemTooltips() {
        addTooltip(ModItems.INFINITE_VENDING_UPGRADE,"Items don't deplete when extracted");
        addTooltip(ModItems.VOID_UPGRADE,"Barrel voids excess items");
        addTooltip(ModItems.PICKUP_1x1_UPGRADE,"Picks up items in a 1x3x1 volume centered on the barrel");
        addTooltip(ModItems.PICKUP_3x3_UPGRADE,"Picks up items in a 3x3x3 volume centered on the barrel");
        addTooltip(ModItems.PICKUP_9x9_UPGRADE,"Picks up items in a 9x3x9 area centered on the barrel");
        addTooltip(ModBlocks.CONTROLLER, "Connects to all barrels and tanks from this mod within a "
                +(2 * Utils.RADIUS+1)+"x"+(2 * Utils.RADIUS+1)+"x"+(2 * Utils.RADIUS+1)+" volume");

        addTooltip(ModBlocks.CONTROLLER_PROXY,"Connects to nearby controller as an additional point for interaction, DOES NOT EXTEND CONTROLLER RANGE!");
        addTooltip(ModBlocks.BARREL_INTERFACE,"Holds up to "+ BarrelInterfaceBlockEntity.SIZE+" barrels and exposes capabilities");
        addTooltip(ModItems.NETWORK_VISUALIZER,"Shows proxies and barrels connected to a controller");
        addTooltip(ModItems.BARREL_HAMMER,"Used to downgrade barrel frames");
        addTooltip(ModItems.REMOTE_CONTROLLER_KEY,"Bound to (%s,%s,%s)");

        add("nabba.antibarrel.tooltip","Items Stored:");
        add("nabba.barrel.tooltip.upgrades","Upgrades");

        add("nabba.barrel.tooltip.discrete","Discrete:");
        add("nabba.barrel.tooltip.void","Void:");
        add("nabba.barrel.tooltip.locked","Locked:");
        add("nabba.barrel.tooltip.connected","Connected:");
        add("nabba.barrel.tooltip.infinite_vending","Infinite Vending:");
    }

    public void addSystemMessages() {
        add("nabba.barrel_hammer.message.no_downgrade","Barrel frame cannot be downgraded any further");
        add("nabba.barrel_hammer.message.remove_upgrade","Remove some upgrades first");
        add("nabba.remote_key.message.bind_success","Successfully bound %s to key");
    }

    public void storageUpgradeName(Item item) {
        addItem(()-> item,getNameFromItem(item).replace("X","x"));
    }

    public void defaultName(Item item) {
        addItem(() -> item,getNameFromItem(item));
    }

    public void defaultName(Block block) {
        addBlock(() -> block,getNameFromBlock(block));
    }

    public static String getNameFromItem(Item item) {
        return StringUtils.capitaliseAllWords(item.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public void addUpgrade(Upgrade upgrade,String text) {
        add(upgrade.getDescriptionId(),text);
    }

    public static String getNameFromBlock(Block block) {
        return StringUtils.capitaliseAllWords(block.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    protected void addTooltip(Block item, String s) {
        add(item.getDescriptionId()+".tooltip",s);
    }
    protected void addTooltip(Item item, String s) {
        add(item.getDescriptionId()+".tooltip",s);
    }
}
