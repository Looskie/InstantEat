package com.looskie.InstantEat;

import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class InstantEatEvents implements Listener {
    private final InstantEat pl;

    public InstantEatEvents(InstantEat pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onEat(PlayerInteractEvent e){
        if(e.getAction()== Action.RIGHT_CLICK_AIR||e.getAction()==Action.RIGHT_CLICK_BLOCK){
            if(e.getClickedBlock() instanceof Container){
                return;
            }
            if(e.getItem()==null){
                return;
            }
            if(e.getPlayer().getFoodLevel()>=20){
                return;
            }
            if(pl.foods.containsKey(e.getItem().getType())){
                e.setCancelled(true);
                eat(e.getPlayer(),e.getItem(),pl.foods.get(e.getItem().getType()));
            }
        }
    }
    private void eat(Player p, ItemStack item, Integer time){
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(p.getInventory().getItemInMainHand()==null){
                        return;
                    }
                    if(p.getInventory().getItemInMainHand().equals(item)) {
                        if(item.getType().toString() == "MILK_BUCKET") {
                            for(PotionEffect effect:p.getActivePotionEffects()){
                                p.removePotionEffect(effect.getType());
                            }
                        } else {
                            Integer food[] = Foods.valueOf(item.getType().toString()).getValue();
                            setItem(p, item);
                            p.setFoodLevel(p.getFoodLevel() + food[0]);
                            p.setSaturation(p.getSaturation() + food[1]);
                        }
                        item.setAmount(item.getAmount() - 1);
                    }
                }
            }.runTaskLater(pl,time);

    }
    private void setItem(Player p,ItemStack item){
        p.getInventory().setItemInMainHand(null);
        new BukkitRunnable(){

            @Override
            public void run() {
                p.getInventory().setItemInMainHand(item);
                p.updateInventory();
            }
        }.runTaskLater(pl,1);
    }
    public enum Foods{
        APPLE(4, 2),
        BREAD(5, 6),
        PORKCHOP(1, 2),
        COOKED_PORKCHOP(8, 12),
        GOLDEN_APPLE(4, 9),
        ENCHANTED_GOLDEN_APPLE(4, 9),
        COD(2, 1),
        COOKED_COD(5, 6),
        SALMON(2, 1),
        COOKED_SALMON(5, 9),
        TROPICAL_FISH(1, 1),
        COOKIE(2, 0),
        MELON_SLICE(2, 1),
        DRIED_KELP(1, 0),
        BEEF(3, 2),
        COOKED_BEEF(8, 12),
        CHICKEN(2, 1),
        COOKED_CHICKEN(6, 7),
        CARROT(3, 3),
        GOLDEN_CARROT(6, 14),
        POTATO(1, 0),
        BAKED_POTATO(5, 6),
        PUMPKIN_PIE(8, 5),
        RABBIT(3, 2),
        COOKED_RABBIT(5, 6),
        RABBIT_STEW(10,12),
        MUTTON(2, 1),
        COOKED_MUTTON(6, 9),
        BEETROOT(1, 2),
        BEETROOT_SOUP(6,7),
        SWEET_BERRIES(2, 1),
        HONEY_BOTTLE(6, 1),
        MUSHROOM_STEW(6,7),
        MILK_BUCKET(0,0);

        private Integer[] value;
        private Foods(Integer value,Integer saturation){
            this.value= new Integer[]{value,saturation};
        }
        public Integer[] getValue(){
            return value;
        }
        public Foods returnByName(String name){
            return Foods.valueOf(name);
        }

    }
}
