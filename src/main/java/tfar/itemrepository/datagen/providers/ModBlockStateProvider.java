package tfar.itemrepository.datagen.providers;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.itemrepository.ItemRepository;
import tfar.itemrepository.init.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ItemRepository.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeBarrel(ModBlocks.BETTER_BARREL);
        makeBarrel(ModBlocks.COPPER_BETTER_BARREL);
        makeBarrel(ModBlocks.STONE_BETTER_BARREL);
        makeBarrel(ModBlocks.IRON_BETTER_BARREL);
    }

    protected void makeBarrel(Block block) {
        String name = Registry.BLOCK.getKey(block).getPath();
        BlockModelBuilder modelBuilder = models().withExistingParent(name,modLoc("block/better_barrel_block"));
        simpleBlock(block,modelBuilder.texture("frame_side",modLoc("block/"+name+"_frame_side")));
    }
}
