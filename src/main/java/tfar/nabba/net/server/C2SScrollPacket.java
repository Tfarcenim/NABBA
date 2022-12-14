package tfar.nabba.net.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import tfar.nabba.menu.AntiBarrelMenu;
import tfar.nabba.menu.ControllerKeyItemMenu;
import tfar.nabba.menu.SearchableFluidMenu;
import tfar.nabba.menu.SearchableMenu;
import tfar.nabba.net.util.C2SPacketHelper;


public class C2SScrollPacket implements C2SPacketHelper {

  int scroll_amount;

  public C2SScrollPacket(){}

  public C2SScrollPacket(int amount){ this.scroll_amount = amount;}

  //decode
  public C2SScrollPacket(FriendlyByteBuf buf) {
    this.scroll_amount = buf.readInt();
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeInt(scroll_amount);
  }
    public void handleServer(ServerPlayer player) {
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof SearchableMenu antiBarrelMenu) {
          antiBarrelMenu.handleScroll(player,scroll_amount);
        }
    }
}

