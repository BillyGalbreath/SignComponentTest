package net.pl3x.bukkit.event;

import net.pl3x.bukkit.util.SignData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public class LoadSignPacketEvent extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList();
    private final List<SignData> signDatas;

    public LoadSignPacketEvent(Player player, List<SignData> signDatas) {
        super(player);
        this.signDatas = signDatas;
    }

    public List<SignData> getSignDatas() {
        return signDatas;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
