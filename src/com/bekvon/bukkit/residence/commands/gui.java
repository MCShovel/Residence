package com.bekvon.bukkit.residence.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.CommandAnnotation;
import com.bekvon.bukkit.residence.containers.ConfigReader;
import com.bekvon.bukkit.residence.containers.cmd;

public class gui implements cmd {

    @Override
    @CommandAnnotation(simple = true, priority = 4600)
    public boolean perform(String[] args, boolean resadmin, Command command, CommandSender sender) {
	if (!(sender instanceof Player))
	    return false;

    return true;
    }

    @Override
    public void getLocale(ConfigReader c, String path) {
	c.get(path + "Description", "Opens gui (Spout only)");
	c.get(path + "Info", Arrays.asList("&eUsage: &6/res gui <residence>"));
    }
}
