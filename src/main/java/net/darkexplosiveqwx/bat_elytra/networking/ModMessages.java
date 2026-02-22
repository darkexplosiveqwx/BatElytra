package net.darkexplosiveqwx.bat_elytra.networking;

import net.darkexplosiveqwx.bat_elytra.BatElytra;
import net.darkexplosiveqwx.bat_elytra.networking.packages.ElytraJumpC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(BatElytra.MODID, "messages"))
                .networkProtocolVersion(1)
                .clientAcceptedVersions((status, version) -> true)
                .serverAcceptedVersions((status, version) -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ElytraJumpC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ElytraJumpC2SPacket::new)
                .encoder(ElytraJumpC2SPacket::toBytes)
                .consumerMainThread(ElytraJumpC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with(player));
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(message, PacketDistributor.ALL.noArg());
    }
}
