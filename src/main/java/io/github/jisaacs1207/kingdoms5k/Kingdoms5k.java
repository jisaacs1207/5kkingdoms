package io.github.jisaacs1207.kingdoms5k;

import java.util.Iterator;







import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;








import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public final class Kingdoms5k extends JavaPlugin implements Listener{
	@Override
	public void onEnable() {
		getLogger().info("5K Kingdoms has been invoked!");
		saveDefaultConfig();
		getWorldGuard();
		getServer().getPluginManager().registerEvents(this, this);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	for(Player player : Bukkit.getOnlinePlayers()){
            		Location loc = player.getLocation(); 
            		RegionManager regionManager = getWorldGuard().getRegionManager(loc.getWorld());
            		ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
            		for (ProtectedRegion region : set){
            			String name = region.getId();
            			if(name.equalsIgnoreCase(getConfig().getString("name1"))){
            				// perk1
            			}
            			else if(name.equalsIgnoreCase(getConfig().getString("name2"))){
            				// perk2
            			}
            			else if(name.equalsIgnoreCase(getConfig().getString("name3"))){
            				// perk3
            			}
            			else if(name.equalsIgnoreCase(getConfig().getString("name4"))){
            				// perk4
            				
            			}
            			else if(name.equalsIgnoreCase(getConfig().getString("name5"))){
            				// perk5
            			}
            		}
            	}
            }
        }, 0L, 20L);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void playerMove(PlayerMoveEvent event){
		Player player = (Player) event.getPlayer();
		Location loc = event.getPlayer().getLocation(); 
		RegionManager regionManager = getWorldGuard().getRegionManager(loc.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
		boolean techSpeed=false;
		boolean lightLight=false;
		boolean darkDark=false;
		boolean magicVis=false;
		for (ProtectedRegion region : set){
			String name = region.getId();
			if(name.equalsIgnoreCase(getConfig().getString("name1"))){
				lightLight=true;
			}
			else if(name.equalsIgnoreCase(getConfig().getString("name2"))){
				darkDark=true;
			}
			else if(name.equalsIgnoreCase(getConfig().getString("name3"))){
				magicVis=true;
			}
			else if(name.equalsIgnoreCase(getConfig().getString("name4"))){
				techSpeed=true;
			}
			else if(name.equalsIgnoreCase(getConfig().getString("name5"))){
				// perk5
			}
		}
		// Tech
		if(techSpeed==true)player.setWalkSpeed(0.4F);
		if(techSpeed==false)player.setWalkSpeed(0.2F);
		
		// Light
		if(lightLight==true){
			player.setMaxHealth(30);
			if (Math.random() * 100 >= 99.3){
				player.sendMessage("light");
				player.setHealth(player.getHealth()+1);
				
			}
			if (Math.random() * 100 >= 99.5){
				player.setExhaustion(player.getExhaustion()+1);
			}
		}
		if(lightLight==false)player.resetMaxHealth();
		
		// Dark
		if(darkDark==true){
			if(player.getPlayerTime()!=0000)player.setPlayerTime(0000, false);
		}
		if(darkDark==false){
			if(!player.isPlayerTimeRelative())player.resetPlayerTime();	
		}
		
		// Magic
		if(magicVis==true){
			if(player.isSprinting()){
				for (Player victims : Bukkit.getOnlinePlayers()){
					if(victims.canSee(player)){
						if(!victims.isOp()&&!victims.hasPermission("5k.admin")){
							victims.hidePlayer(player);
						}
					}
				}
			}
			else{
				for (Player victims : Bukkit.getOnlinePlayers()){
					if(!victims.canSee(player)){
						victims.showPlayer(player);
					}
				}
			}
		}
	}
	 
	//nether
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerFire( EntityDamageEvent event) {
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			Location loc = player.getLocation(); 
			RegionManager regionManager = getWorldGuard().getRegionManager(loc.getWorld());
			ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
			for (ProtectedRegion region : set){
				String name = region.getId();
				if(name.equalsIgnoreCase(getConfig().getString("name5"))){
					if(event.getCause()==DamageCause.FIRE_TICK||event.getCause()==DamageCause.FIRE||event.getCause()==DamageCause.LAVA){
						event.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerCombust( EntityCombustEvent event) {
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			Location loc = player.getLocation(); 
			RegionManager regionManager = getWorldGuard().getRegionManager(loc.getWorld());
			ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
			for (ProtectedRegion region : set){
				String name = region.getId();
				player.sendMessage(name);
				if(name.equalsIgnoreCase(getConfig().getString("name5"))){
					event.setCancelled(true);
				}
			}
		}
	}
	
	
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        //May disable Plugin
	        return null; // Maybe you want throw an exception instead
	    }
	    return (WorldGuardPlugin) plugin;
	}
}