package net.pl3x.bukkit.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import net.pl3x.bukkit.event.UpdateSignPacketEvent;
import net.pl3x.bukkit.util.SignUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TileEntityDataPacketAdapter extends PacketAdapter {
    public TileEntityDataPacketAdapter(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.TILE_ENTITY_DATA);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        if (packet.getIntegers().read(0) != 9) {
            return; // not a sign update packet
        }

        BlockPosition pos = packet.getBlockPositionModifier().read(0);
        NbtCompound nbt = (NbtCompound) packet.getNbtModifier().read(0).deepClone();

        if (pos == null || nbt == null) {
            return; // packet missing info; ignore it
        }

        if (nbt.containsKey("PurpurEditor")) {
            return; // this is a custom sign editor packet; ignore
        }

        Player player = event.getPlayer();
        Block block = player.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        if (!(block.getState() instanceof Sign)) {
            return; // position is not a sign
        }

        // call event
        UpdateSignPacketEvent updateSignPacketEvent = new UpdateSignPacketEvent(player, SignUtil.getLines(nbt), block.getLocation());
        Bukkit.getPluginManager().callEvent(updateSignPacketEvent);

        // update sign NBT from event
        SignUtil.setLines(nbt, updateSignPacketEvent.getLines());

        // write NBT back into packet
        packet.getNbtModifier().write(0, nbt);

        System.out.println(nbt);
    }
}
