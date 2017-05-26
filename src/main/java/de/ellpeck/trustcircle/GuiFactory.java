package de.ellpeck.trustcircle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GuiFactory implements IModGuiFactory{

    @Override
    public void initialize(Minecraft minecraftInstance){

    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass(){
        return ConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories(){
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element){
        return null;
    }

    public static class ConfigGui extends GuiConfig{

        public ConfigGui(GuiScreen parentScreen){
            super(parentScreen, getConfigElements(), TrustCircle.MOD_ID, false, false, getAbridgedConfigPath(TrustCircle.config.toString()));
        }

        private static List<IConfigElement> getConfigElements(){
            return Collections.<IConfigElement>singletonList(new ConfigElement(TrustCircle.config.getCategory(Configuration.CATEGORY_GENERAL)));
        }
    }
}
