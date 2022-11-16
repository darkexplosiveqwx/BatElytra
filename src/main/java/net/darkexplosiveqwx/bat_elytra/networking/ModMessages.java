package net.darkexplosiveqwx.bat_elytra.networking;

import net.darkexplosiveqwx.bat_elytra.BatElytra;
import net.darkexplosiveqwx.bat_elytra.networking.packages.ElytraJumpC2SPacket;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.*;

public class ModMessages {
    private static SimpleChannel INSTANCE;


    private static int packedId = 0;
    private static int id(){
        return packedId++;
    }

    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(BatElytra.MODID,"messages"))
                .networkProtocolVersion(()->"1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ElytraJumpC2SPacket.class,id(),NetworkDirection.PLAY_TO_SERVER)
                .decoder(ElytraJumpC2SPacket::new)
                .encoder(ElytraJumpC2SPacket::toBytes)
                .consumerMainThread(ElytraJumpC2SPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()-> player), message);
    }
    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}