package me.jellysquid.mods.sodium.client.util.workarounds;

import me.jellysquid.mods.sodium.client.gui.console.Console;
import me.jellysquid.mods.sodium.client.gui.console.message.MessageLevel;
import net.minecraft.resource.Resource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InGameChecks {

    private static final Logger LOGGER = LoggerFactory.getLogger("Sodium-InGameChecks");
    public static List<String> coreShaderResourcePacks = new ArrayList<>();

    public static void checkIfCoreShaderLoaded(Resource resource) {
        if (!resource.getResourcePackName().equals("vanilla") && !resource.getResourcePackName().equals("fabric")) {
            if (coreShaderResourcePacks.isEmpty())
                showConsoleMessage(Text.translatable("sodium.console.core_shaders"));
            String resourcePackName = resource.getResourcePackName().substring(5);
            if (!coreShaderResourcePacks.contains(resourcePackName)) {
                coreShaderResourcePacks.add(resourcePackName);
                showConsoleMessage(Text.literal(resourcePackName));
                logMessage("Resource pack contains core shaders: " + resourcePackName);
            }
        }
    }

    public static void clearCoreShaderResourcePackList() {
        coreShaderResourcePacks.clear();
    }

    private static void showConsoleMessage(MutableText message) {
        Console.instance().logMessage(MessageLevel.SEVERE, message, 30.0);
    }

    private static void logMessage(String message, Object... args) {
        LOGGER.error(message, args);
    }

}
