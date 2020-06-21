package net.pl3x.bukkit.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import net.pl3x.bukkit.event.LoadSignPacketEvent;
import net.pl3x.bukkit.util.SignData;
import net.pl3x.bukkit.util.SignUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MapChunkPacketAdapter extends PacketAdapter {
    public MapChunkPacketAdapter(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();

        List<NbtCompound> signNBTList = new ArrayList<>();
        List<NbtBase<?>> rawPacketNBTList = packet.getListNbtModifier().read(0);

        // pull out all sign NBT from the packet
        Iterator<NbtBase<?>> iter = rawPacketNBTList.iterator();
        while (iter.hasNext()) {
            NbtBase<?> obj = iter.next();
            if (obj instanceof NbtCompound) {
                NbtCompound nbt = (NbtCompound) obj;
                if (nbt.containsKey("id")) {
                    String id = nbt.getString("id").toLowerCase();
                    if (id.startsWith("minecraft:") && id.endsWith("sign")) {
                        signNBTList.add(nbt);
                        iter.remove();
                    }
                }
            }
        }

        // create some SignData from the sign NBT
        List<SignData> signDatas = new ArrayList<>();
        for (NbtCompound nbt : signNBTList) {
            String[] lines = SignUtil.getLines(nbt);
            SignData signData = new SignData(Arrays.copyOf(lines, lines.length), nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
            signDatas.add(signData);
        }

        // call the event
        Player player = event.getPlayer();
        LoadSignPacketEvent loadSignPacketEvent = new LoadSignPacketEvent(player, signDatas);
        Bukkit.getPluginManager().callEvent(loadSignPacketEvent);

        // update NBT from event data
        signDatas = loadSignPacketEvent.getSignDatas();
        for (int i = 0; i < signDatas.size(); i++) {
            SignUtil.setLines(signNBTList.get(i), signDatas.get(i).getLines());
        }

        // put sign NBT back into packet
        rawPacketNBTList.addAll(signNBTList);
        packet.getListNbtModifier().write(0, rawPacketNBTList);
    }
}
