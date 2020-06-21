package net.pl3x.bukkit.listener;

import net.pl3x.bukkit.event.LoadSignPacketEvent;
import net.pl3x.bukkit.event.UpdateSignPacketEvent;
import net.pl3x.bukkit.util.SignData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SignListener implements Listener {
    @EventHandler
    public void onSignUpdatePacket(UpdateSignPacketEvent event) {
        event.setLines(scanLines(event.getLines(), event.getPlayer()));
    }

    @EventHandler
    public void onSignLoadPacket(LoadSignPacketEvent event) {
        for (SignData signData : event.getSignDatas()) {
            signData.setLines(scanLines(signData.getLines(), event.getPlayer()));
        }
    }

    private String[] scanLines(String[] lines, Player player) {
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i]
                    .replace("{player}", player.getName())
                    .replace("{displayname}", player.getDisplayName());
        }
        return lines;
    }
}
