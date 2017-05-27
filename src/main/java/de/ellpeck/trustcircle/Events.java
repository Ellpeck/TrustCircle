package de.ellpeck.trustcircle;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events{

    @SubscribeEvent
    public void onPlayerUpdate(LivingUpdateEvent event){
        EntityLivingBase entity = event.getEntityLiving();
        if(entity != null && !entity.worldObj.isRemote && entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)entity;
            if(player.worldObj.getTotalWorldTime()%TrustCircle.updateInterval == 0){
                double modifier = 0;

                for(EntityPlayer other : player.worldObj.playerEntities){
                    if(other != player && !other.isSpectator()){
                        if(doesTeamWork(player.getTeam(), other.getTeam())){
                            double dist = other.getDistanceSq(player.posX, player.posY, player.posZ);
                            if(dist <= TrustCircle.maxRange*TrustCircle.maxRange){
                                double mod = dist <= 0 ? 1 : (1/Math.sqrt(dist));
                                modifier += mod*TrustCircle.baseCalcModifier;

                                if(!TrustCircle.allowMultiplePlayers){
                                    break;
                                }
                            }
                        }
                    }
                }

                if(modifier > 0){
                    int amplifier = Math.min(3, MathHelper.ceiling_double_int(modifier*TrustCircle.amplifierModifier)-1);
                    int duration = Math.max(TrustCircle.updateInterval+1, MathHelper.ceiling_double_int(modifier*TrustCircle.durationModifier));

                    PotionEffect active = player.getActivePotionEffect(TrustCircle.potionTrust);
                    boolean ampChange = active != null && active.getAmplifier() != amplifier;
                    if(active == null || ampChange || active.getDuration() <= TrustCircle.updateInterval){
                        if(ampChange){
                            player.removePotionEffect(TrustCircle.potionTrust);
                        }

                        player.addPotionEffect(new PotionEffect(TrustCircle.potionTrust, duration, amplifier, true, true));
                    }
                }
            }
        }
    }

    private static boolean doesTeamWork(Team myTeam, Team otherTeam){
        if(TrustCircle.isTeamDependent){
            if(myTeam != null){
                return myTeam.isSameTeam(otherTeam);
            }
            else{
                return TrustCircle.trustWithoutTeam && otherTeam == null;
            }
        }
        else{
            return true;
        }
    }

    @SubscribeEvent
    public void onConfigChange(OnConfigChangedEvent event){
        if(TrustCircle.MOD_ID.equals(event.getModID())){
            TrustCircle.defineConfigValues();
        }
    }
}