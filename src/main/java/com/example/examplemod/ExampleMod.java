package com.example.examplemod;

import com.example.examplemod.command.CommandMetaItemHand;
import com.example.examplemod.common.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ExampleMod.MOD_ID, version = ExampleMod.VERSION)
public class ExampleMod {
    public static final String MOD_ID = "examplemod";
    public static final String VERSION = "1.0.0";

    @SidedProxy(
            clientSide = "com.example.examplemod.client.ClientProxy",
            serverSide = "com.example.examplemod.common.CommonProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandMetaItemHand());
    }
}
