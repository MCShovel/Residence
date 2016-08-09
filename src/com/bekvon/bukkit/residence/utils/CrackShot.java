package com.bekvon.bukkit.residence.utils;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.containers.lm;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

public class CrackShot implements Listener {
	public CrackShot() {
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void AnimalKilling(EntityDamageByEntityEvent event) {

		if (!(event.getEntity() instanceof LivingEntity))
			return;
		LivingEntity victim = (LivingEntity) event.getEntity();
		
		// disabling event on world
		if (Residence.isDisabledWorldListener(victim.getWorld()))
			return;
		Entity damager = event.getDamager();

		if ((!(damager instanceof Arrow)) && (!(damager instanceof Player))) {
			return;
		}

		Player cause = null;
		if ((damager instanceof Arrow) && (!(((Arrow) damager).getShooter() instanceof Player))) {
			return;
		}

		if (damager instanceof Player) {
			cause = (Player) damager;
		} else {
			cause = (Player) ((Arrow) damager).getShooter();
		}

		if (cause == null)
			return;

		if (Residence.isResAdminOn(cause)) {
			return;
		}

		Entity entity = victim;
		ClaimedResidence res = Residence.getResidenceManager().getByLoc(entity.getLocation());

		if (Residence.getNms().isAnimal(entity)) {
			if (res != null && !res.getPermissions().playerHas(cause.getName(), Flags.animalkilling, true)) {
				cause.sendMessage(Residence.msg(lm.Residence_FlagDeny, Flags.animalkilling.getName(), res.getName()));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {

		if (!(event.getEntity() instanceof LivingEntity))
			return;
		LivingEntity victim = (LivingEntity) event.getEntity();

		// disabling event on world
		if (Residence.isDisabledWorldListener(victim.getWorld()))
			return;
		if (victim.getType() != EntityType.ITEM_FRAME && !Residence.getNms().isArmorStandEntity(victim.getType()))
			return;

		Entity dmgr = event.getDamager();
		Player player;
		if (event.getDamager() instanceof Player) {
			player = (Player) event.getDamager();
		} else {
			if (dmgr instanceof Projectile && ((Projectile) dmgr).getShooter() instanceof Player) {
				player = (Player) ((Projectile) dmgr).getShooter();
			} else
				return;
		}

		if (Residence.isResAdminOn(player))
			return;

		// Note: Location of entity, not player; otherwise player could stand
		// outside of res and still damage
		Location loc = victim.getLocation();
		ClaimedResidence res = Residence.getResidenceManager().getByLoc(loc);
		if (res != null && !res.getPermissions().playerHas(player.getName(), Flags.container, false)) {
			event.setCancelled(true);
			Residence.msg(player, lm.Residence_FlagDeny, Flags.container.getName(), res.getName());
		}

	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageByEntityEvent event) {

		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;

		Player victim = (Player) event.getEntity();
		Player damager = (Player) event.getDamager();

		// disabling event on world
		if (Residence.isDisabledWorldListener(victim.getWorld()))
			return;

		if (victim.hasMetadata("NPC")) {
			return;
		}

		ClaimedResidence area = Residence.getResidenceManager().getByLoc(victim.getLocation());
		/* Living Entities */
		ClaimedResidence srcarea = null;

		srcarea = Residence.getResidenceManager().getByLoc(damager.getLocation());

		boolean srcpvp = true;
		if (srcarea != null) {
			srcpvp = srcarea.getPermissions().has(Flags.pvp, true);
		}

		if (!srcpvp) {
			damager.sendMessage(Residence.msg(lm.General_NoPVPZone));
			event.setCancelled(true);
			return;
		}
		/* Check for Player vs Player */
		if (area == null) {
			/* World PvP */
			if (!Residence.getWorldFlags().getPerms(damager.getWorld().getName()).has(Flags.pvp, true)) {
				damager.sendMessage(Residence.msg(lm.General_WorldPVPDisabled));
				event.setCancelled(true);
			}
		} else {
			/* Normal PvP */
			if (!area.getPermissions().has(Flags.pvp, true)) {
				damager.sendMessage(Residence.msg(lm.General_NoPVPZone));
				event.setCancelled(true);
			}
		}
		return;

	}
}
