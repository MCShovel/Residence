package com.bekvon.bukkit.residence.permissions;

import com.bekvon.bukkit.residence.Residence;
import java.util.List;
import org.bukkit.entity.Player;

public class PermissionsBukkitAdapter implements PermissionsInterface {

    public PermissionsBukkitAdapter() {
    }

    @Override
    public String getPlayerGroup(Player player) {
	return this.getPlayerGroup(player.getName(), player.getWorld().getName());
    }

    @Override
    public String getPlayerGroup(String player, String world) {
    	return null;
    }

}
