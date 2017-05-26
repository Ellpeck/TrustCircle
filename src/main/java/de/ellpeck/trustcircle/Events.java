package de.ellpeck.trustcircle;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
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
                    if(other != player){
                        double dist = other.getDistanceSq(player.posX, player.posY, player.posZ);
                        if(dist <= TrustCircle.maxRange){
                            double mod = dist <= 0 ? 1 : (1/dist);
                            modifier += mod*TrustCircle.baseCalcModifier;

                            if(!TrustCircle.allowMultiplePlayers){
                                break;
                            }
                        }
                    }
                }

                if(modifier > 0){
                    int amplifier = MathHelper.ceiling_double_int(modifier*TrustCircle.amplifierModifier)-1;
                    int duration = MathHelper.ceiling_double_int(modifier*TrustCircle.durationModifier);

                    PotionEffect active = player.getActivePotionEffect(TrustCircle.potionTrust);
                    if(active == null || active.getAmplifier() != amplifier || active.getDuration() != duration){
                        player.addPotionEffect(new PotionEffect(TrustCircle.potionTrust, duration, amplifier));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onConfigChange(OnConfigChangedEvent event){
        if(TrustCircle.MOD_ID.equals(event.getModID())){
            TrustCircle.defineConfigValues();
        }
    }
}
