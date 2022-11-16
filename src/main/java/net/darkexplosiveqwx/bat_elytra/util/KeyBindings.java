package net.darkexplosiveqwx.bat_elytra.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY= "key.category.bat_elytra.main";
    public static final String KEY_JUMP = "key.bat_elytra.elytra_jump";

    public static final KeyMapping ELYTRA_JUMP = new KeyMapping(KEY_JUMP, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V,KEY_CATEGORY);


}
