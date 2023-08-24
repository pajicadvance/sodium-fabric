package me.jellysquid.mods.sodium.client.util.workarounds;

import me.jellysquid.mods.sodium.client.gui.console.Console;
import me.jellysquid.mods.sodium.client.gui.console.message.MessageLevel;
import net.minecraft.resource.Resource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InGameChecks {

    private static final Logger LOGGER = LoggerFactory.getLogger("Sodium-InGameChecks");
    public static final List<String> fileBlacklist = new ArrayList<>(Arrays.asList("rendertype_solid", "rendertype_cutout_mipped", "rendertype_cutout", "rendertype_translucent", "rendertype_tripwire"));
    public static List<String> coreShaderResourcePacks = new ArrayList<>();

    public static void checkIfCoreShaderLoaded(Resource resource, String filename) {
        if (!resource.getResourcePackName().equals("vanilla") && !resource.getResourcePackName().equals("fabric") && fileBlacklist.contains(filename)) {
            String resourcePackName = resource.getResourcePackName().substring(5);

            if (coreShaderResourcePacks.isEmpty()) {
                showConsoleMessage(Text.translatable("sodium.console.core_shaders"));
            }
            if (!coreShaderResourcePacks.contains(resourcePackName)) {
                coreShaderResourcePacks.add(resourcePackName);
                showConsoleMessage(Text.literal(resourcePackName));
            }

            logMessage("Resource pack '" + resourcePackName + "' replaces core shader '" + filename + "'");
        }
        else if (!resource.getResourcePackName().equals("vanilla") && !resource.getResourcePackName().equals("fabric")) {
            String line;

            try (BufferedReader br = resource.getReader()) {
                while ((line = br.readLine()) != null) {
                    if (line.contains("<light.glsl>") || line.contains("<fog.glsl>")) {
                        String resourcePackName = resource.getResourcePackName().substring(5);

                        if (coreShaderResourcePacks.isEmpty())
                            showConsoleMessage(Text.translatable("sodium.console.core_shaders"));
                        if (!coreShaderResourcePacks.contains(resourcePackName)) {
                            coreShaderResourcePacks.add(resourcePackName);
                            showConsoleMessage(Text.literal(resourcePackName));
                        }

                        String importName = line.substring(0, line.length() - 1).split("<", 2)[1];
                        logMessage("Core shader '" + filename + "' from resource pack '" + resourcePackName + "' has import '" + importName + "'");
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Could not read shader file " + filename);
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
        LOGGER.warn(message, args);
    }

}
