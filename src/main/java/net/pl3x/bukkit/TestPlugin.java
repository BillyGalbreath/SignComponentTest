package net.pl3x.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import net.pl3x.bukkit.adapter.MapChunkPacketAdapter;
import net.pl3x.bukkit.adapter.TileEntityDataPacketAdapter;
import net.pl3x.bukkit.listener.SignListener;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new MapChunkPacketAdapter(this));
        ProtocolLibrary.getProtocolManager().addPacketListener(new TileEntityDataPacketAdapter(this));

        getServer().getPluginManager().registerEvents(new SignListener(), this);
    }
}
