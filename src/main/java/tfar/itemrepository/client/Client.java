package tfar.itemrepository.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfar.itemrepository.init.ModBlockEntityTypes;
import tfar.itemrepository.init.ModMenuTypes;

public class Client {

    public static void setup(FMLClientSetupEvent e) {
        MenuScreens.register(ModMenuTypes.REPOSITORY,RepositoryScreen::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.BETTER_BARREL,BetterBarrelRenderer::new);
    }
}
