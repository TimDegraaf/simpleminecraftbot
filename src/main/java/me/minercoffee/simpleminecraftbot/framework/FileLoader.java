package me.minercoffee.simpleminecraftbot.framework;

import com.google.common.io.ByteStreams;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileLoader {
    public static File loadFile(@NotNull JavaPlugin plugin, @NotNull String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }

        File resourceFile = new File(folder, resource);

        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                InputStream in = plugin.getResource(resource);
                Throwable var5 = null;

                try {
                    OutputStream out = new FileOutputStream(resourceFile);
                    Throwable var7 = null;

                    try {
                        ByteStreams.copy(in, out);
                    } catch (Throwable var32) {
                        var7 = var32;
                        throw var32;
                    } finally {
                        if (out != null) {
                            if (var7 != null) {
                                try {
                                    out.close();
                                } catch (Throwable var31) {
                                    var7.addSuppressed(var31);
                                }
                            } else {
                                out.close();
                            }
                        }

                    }
                } catch (Throwable var34) {
                    var5 = var34;
                    throw var34;
                } finally {
                    if (in != null) {
                        if (var5 != null) {
                            try {
                                in.close();
                            } catch (Throwable var30) {
                                var5.addSuppressed(var30);
                            }
                        } else {
                            in.close();
                        }
                    }

                }
            }
        } catch (Exception var36) {
            var36.printStackTrace();
        }

        return resourceFile;
    }

    private FileLoader() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
