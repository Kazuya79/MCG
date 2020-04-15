package amerebagatelle.github.io.simplecoordinates.commands;

import amerebagatelle.github.io.simplecoordinates.coordinates.CoordinatesManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.TranslatableText;

import java.io.IOException;

import static io.github.cottonmc.clientcommands.ArgumentBuilders.*;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.string;

public class WriteCoordinateCommand implements ClientCommandPlugin {
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> commandDispatcher) {
        LiteralArgumentBuilder<CottonClientCommandSource> writecoordinates = literal("writecoordinate").
                then(argument("name", string()).
                    then(argument("x", integer()).
                        then(argument("y", integer()).
                            then(argument("z", integer()).
                                    then(argument("details", string())).
                                        executes(WriteCoordinateCommand::run)))));
        commandDispatcher.register(writecoordinates);
    }

    private static int run(CommandContext<CottonClientCommandSource> ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        try {
            CoordinatesManager.writeToCoordinates(StringArgumentType.getString(ctx, "name"), IntegerArgumentType.getInteger(ctx, "x"), IntegerArgumentType.getInteger(ctx, "y"), IntegerArgumentType.getInteger(ctx, "z"), StringArgumentType.getString(ctx, "details"));
            mc.inGameHud.addChatMessage(MessageType.SYSTEM, new TranslatableText("return.simplecoordinates.coordinatewritesuccess"));
            return 1;
        } catch (IOException e) {
            mc.inGameHud.addChatMessage(MessageType.SYSTEM, new TranslatableText("return.simplecoordinates.coordinatewritefail"));
            return 0;
        }
    }
}
