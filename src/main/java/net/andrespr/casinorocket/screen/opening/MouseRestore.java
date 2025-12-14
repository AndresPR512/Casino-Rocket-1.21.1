package net.andrespr.casinorocket.screen.opening;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;

public final class MouseRestore {

    private static boolean pending = false;
    private static double x = 0;
    private static double y = 0;

    private static int applyAttemptsRemaining = 0;

    private MouseRestore() {}

    public static void capture(MinecraftClient client) {
        if (client == null) return;
        x = client.mouse.getX();
        y = client.mouse.getY();
        pending = true;

        applyAttemptsRemaining = 3;
    }

    public static void capture() {
        capture(MinecraftClient.getInstance());
    }

    public static boolean applyIfPending(MinecraftClient client) {
        if (!pending || client == null) return false;

        if (applyAttemptsRemaining <= 0) {
            clear();
            return false;
        }

        applyAttemptsRemaining--;

        Window w = client.getWindow();
        long handle = w.getHandle();

        if (handle == 0L) return false;

        GLFW.glfwSetCursorPos(handle, x, y);

        clear();
        return true;
    }

    public static void clear() {
        pending = false;
        applyAttemptsRemaining = 0;
    }

}