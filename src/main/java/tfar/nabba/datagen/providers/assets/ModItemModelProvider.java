package tfar.nabba.datagen.providers.assets;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.nabba.NABBA;
import tfar.nabba.init.ModBlocks;
import tfar.nabba.init.ModItems;
import tfar.nabba.item.BarrelFrameUpgradeItem;
import tfar.nabba.item.KeyItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator,  ExistingFileHelper existingFileHelper) {
        super(generator, NABBA.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        makeOneLayerItem(ModItems.STORAGE_UPGRADE);
        makeOneLayerItem(ModItems.x4_STORAGE_UPGRADE);
        makeOneLayerItem(ModItems.x16_STORAGE_UPGRADE);
        makeOneLayerItem(ModItems.x64_STORAGE_UPGRADE);
        makeOneLayerItem(ModItems.x256_STORAGE_UPGRADE);
        makeOneLayerItem(ModItems.x1024_STORAGE_UPGRADE);

        makeOneLayerItem(ModItems.INFINITE_STORAGE_UPGRADE);
        makeOneLayerItem(ModItems.INFINITE_VENDING_UPGRADE);

        makeOneLayerItem(ModItems.VOID_UPGRADE);

        makeOneLayerItem(ModItems.PICKUP_1x1_UPGRADE);
        makeOneLayerItem(ModItems.PICKUP_3x3_UPGRADE);
        makeOneLayerItem(ModItems.PICKUP_9x9_UPGRADE);

        makeOneLayerItem(ModItems.HIDE_KEY);
        makeOneLayerItem(ModItems.LOCK_KEY);
        makeOneLayerItem(ModItems.VANITY_KEY);
        makeOneLayerItem(ModItems.KEY_RING);

        makeSimpleBlockItem(ModBlocks.BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.COPPER_BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.STONE_BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.IRON_BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.LAPIS_BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.GOLD_BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.DIAMOND_BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.EMERALD_BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.NETHERITE_BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.CREATIVE_BETTER_BARREL.asItem());
        makeSimpleBlockItem(ModBlocks.CONTROLLER.asItem());

        for (Item item : Registry.ITEM) {
            if (item instanceof BarrelFrameUpgradeItem) {
                registerUpgrade(item);
            }
        }
    }


    protected void makeSimpleBlockItem(Item item, ResourceLocation loc) {
        getBuilder(Registry.ITEM.getKey(item).toString())
                .parent(getExistingFile(loc));
    }

    protected void makeSimpleBlockItem(Item item) {
        makeSimpleBlockItem(item, new ResourceLocation(NABBA.MODID, "block/" + Registry.ITEM.getKey(item).getPath()));
    }


    protected void makeOneLayerItem(Item item, ResourceLocation texture) {
        String path = Registry.ITEM.getKey(item).getPath();
        if (existingFileHelper.exists(new ResourceLocation(texture.getNamespace(), "item/" + texture.getPath())
                , PackType.CLIENT_RESOURCES, ".png", "textures")) {
            getBuilder(path).parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", new ResourceLocation(texture.getNamespace(), "item/" + texture.getPath()));
        } else {
            System.out.println("no texture for " + item + " found, skipping");
        }
    }

    protected void makeOneLayerItem(Item item) {
        ResourceLocation texture = Registry.ITEM.getKey(item);
        makeOneLayerItem(item, texture);
    }

    //wood_to_iron_frame_upgrade
    protected void registerUpgrade(Item item) {
        String name = Registry.ITEM.getKey(item).getPath();
        registerUpgrade(name);
    }
    protected void registerUpgrade(String name) {
        String[] strings = name.split("_");
        getBuilder(name)
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0","item/frame_upgrade/from_"+strings[0])
                .texture("layer1","item/frame_upgrade/to_"+strings[2]);
    }

}
