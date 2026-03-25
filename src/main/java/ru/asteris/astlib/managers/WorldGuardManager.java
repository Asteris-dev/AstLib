package ru.asteris.astlib.managers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorldGuardManager {

    private boolean isEnabled = false;

    public void setup() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            isEnabled = true;
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    private RegionContainer getContainer() {
        return WorldGuard.getInstance().getPlatform().getRegionContainer();
    }

    public List<String> getRegionNames(org.bukkit.Location location) {
        List<String> regions = new ArrayList<>();
        if (!isEnabled) return regions;

        Location weLoc = BukkitAdapter.adapt(location);
        RegionQuery query = getContainer().createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(weLoc);

        if (set != null) {
            for (ProtectedRegion region : set) {
                regions.add(region.getId());
            }
        }
        return regions;
    }

    public boolean isInRegion(org.bukkit.Location location, String regionName) {
        if (!isEnabled) return false;

        List<String> regions = getRegionNames(location);
        for (String id : regions) {
            if (id.equalsIgnoreCase(regionName)) {
                return true;
            }
        }
        return false;
    }

    public boolean testStateFlag(Player player, org.bukkit.Location location, String flagName) {
        if (!isEnabled) return true;

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        Flag<?> flag = registry.get(flagName);

        if (!(flag instanceof StateFlag)) {
            return true;
        }

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        Location weLoc = BukkitAdapter.adapt(location);
        RegionQuery query = getContainer().createQuery();

        return query.testState(weLoc, localPlayer, (StateFlag) flag);
    }
}