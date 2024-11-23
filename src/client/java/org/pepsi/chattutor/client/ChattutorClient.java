package org.pepsi.chattutor.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Identifier;

import java.util.List;

import static net.minecraft.command.argument.BlockStateArgumentType.getBlockState;

public class ChattutorClient implements ClientModInitializer {
    static Chat chat = new Chat();
    @Override
    public void onInitializeClient() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("ask")
                    .then(CommandManager.argument("question", StringArgumentType.string())
                            .executes(ChattutorClient::askQuestion)));
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("blueprint")
                    .then(CommandManager.argument("building", StringArgumentType.string())
                            .executes(ChattutorClient::getBlueprint)));
        });
    }
    private static int askQuestion(CommandContext<ServerCommandSource> context) {
        String value = StringArgumentType.getString(context, "question");
        String responce = chat.askGpt(value);
        context.getSource().sendFeedback(() -> Text.literal(responce), false);
        return 1;
    }

    private static int getBlueprint(CommandContext<ServerCommandSource> context) {
        new Thread(() -> {
            String build = StringArgumentType.getString(context, "building");
            String responce = chat.generateBuild(build);
            List<List<List<String>>> coords = Generator.getCoords(responce);
            final ServerCommandSource source = context.getSource();
            final PlayerEntity self = source.getPlayer();
            final ServerWorld world = source.getWorld();
            Identifier id;
            Block blockMC;
            BlockPos pos = new BlockPos(self.getBlockPos());
            BlockState blo;
            pos = pos.add(5, 0, 0); //x y z
            for (int i = 0; i < coords.size(); i++) {
                for (int j = 0; j < coords.get(i).size(); j++) {
                    for (int k = 0; k < coords.get(i).get(j).size(); k++) {
                        id = Identifier.tryParse(coords.get(i).get(j).get(k));
                        blockMC = Registries.BLOCK.get(id);
                        blo = blockMC.getDefaultState();
                        world.setBlockState(pos, blo);
                        pos = pos.add(1, 0, 0); //x y z
                    }
                    pos = pos.add(-(coords.get(i).get(j).size()), 0, 1); //x y z
                }
                pos = pos.add(0, 1, -(coords.get(i).size())); //x y z
            }
            context.getSource().sendFeedback(() -> Text.literal(responce), false);
        }).start();
        return 1;
    }
}
