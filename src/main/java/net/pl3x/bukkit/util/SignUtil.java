package net.pl3x.bukkit.util;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;

public class SignUtil {
    public static String[] getLines(NbtCompound nbt) {
        String[] lines = new String[4];
        for (int i = 0; i < 4; i++) {
            if (nbt.containsKey("Text" + (i + 1))) {
                lines[i] = fromJsonAPI(nbt.getString("Text" + (i + 1)));
            } else {
                lines[i] = ""; // clients crash if all lines are null, so fill with empty string instead
            }
        }
        return lines;
    }

    public static void setLines(NbtCompound nbt, String[] lines) {
        for (int i = 0; i < 4; i++) {
            nbt.put("Text" + (i + 1), toJsonAPI(lines[i]));
        }
    }

    // these NMS methods work brilliantly
    private static String fromJsonNMS(String json) {
        IChatBaseComponent component = IChatBaseComponent.ChatSerializer.jsonToComponent(json);
        return CraftChatMessage.fromComponent(component);
    }

    private static String toJsonNMS(String str) {
        IChatBaseComponent[] component = CraftChatMessage.fromString(str);
        return CraftChatMessage.toJSON(component[0]);
    }

    // these API methods inject default colors and turn sign text white :(
    private static String fromJsonAPI(String json) {
        BaseComponent[] components = ComponentSerializer.parse(json);
        return TextComponent.toLegacyText(components);
    }

    private static String toJsonAPI(String str) {
        BaseComponent[] components = TextComponent.fromLegacyText(str);
        return ComponentSerializer.toString(components);
    }
}
