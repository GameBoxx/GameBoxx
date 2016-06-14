/*
 The MIT License (MIT)

 Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 Copyright (c) 2016 contributors

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

package info.gameboxx.gameboxx.nms.worldloader;

import info.gameboxx.gameboxx.GameBoxx;
import net.minecraft.server.v1_10_R1.Chunk;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Level;

/**
 * @author Friwi (https://www.spigotmc.org/resources/lib-asyncworldloader.7370/)
 */
public class WorldLoader_V1_10_R1 implements info.gameboxx.gameboxx.nms.worldloader.WorldLoader {
    //TODO: Recreate this (It's a mess and needs to be updated to 1.9)

    private GameBoxx gb;
    private boolean alreadyLoading = false;

    private World ret = null;
    private boolean aborted = false;
    private Chunk wait = null;
    private ChunkGenerator generator = null;

    public WorldLoader_V1_10_R1(GameBoxx gb) {
        this.gb = gb;
    }

    @SuppressWarnings ("unchecked")
    public World createAsyncWorld(final WorldCreator creator) {
        /*
        //Only allow loading 1 world at a time.
        while (alreadyLoading) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        alreadyLoading = true;

        //If world is already loaded we're done.
        final String name = creator.name();
        World world = getCraftServer().getWorld(name);
        if (world != null) {
            return world;
        }

        //Initialize world loading.
        generator = creator.generator() == null ? getGenerator(name) : creator.generator();
        aborted = false;
        wait = null;
        ret = null;
        final net.minecraft.server.v1_10_R1.WorldType type = net.minecraft.server.v1_10_R1.WorldType.getType(creator.type().getName());

        File folder = new File(getWorldContainer(), name);
        if ((folder.exists()) && (!folder.isDirectory())) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' but it isn't a folder!");
        }

        //Try to convert the world to another version before loading it.
        Convertable converter = new WorldLoaderServer(getWorldContainer());
        if (converter.isConvertable(name)) {
            Bukkit.getLogger().info("Converting world '" + name + "'");
            converter.convert(name, new IProgressUpdate() {
                public void c(String s) {
                }

                public void a(String s) {
                }

                public void a(int i) {
                    if (System.currentTimeMillis() - this.b >= 1000L) {
                        this.b = System.currentTimeMillis();
                        MinecraftServer.LOGGER.info("Converting... " + i + "%");
                    }
                }

                private long b = System.currentTimeMillis();
            });
        }


        //Load the world in small bits.
        new BukkitRunnable() {
            public void run() {
                int dimension2 = 10 + getServer().worlds.size();
                boolean used = false;
                do {
                    for (WorldServer server : getServer().worlds) {
                        used = server.dimension == dimension2;
                        if (used) {
                            dimension2++;
                            break;
                        }
                    }
                } while (used);

                final boolean hardcore = false;
                final int dimension = dimension2;
                new Thread() {
                    public void run() {
                        Object sdm = new ServerNBTManager(getWorldContainer(), name, true);
                        WorldData worlddata = ((IDataManager)sdm).getWorldData();
                        if (worlddata == null) {
                            WorldSettings worldSettings =
                                    new WorldSettings(creator.seed(), WorldSettings.EnumGamemode.getById(getCraftServer().getDefaultGameMode().getValue()), creator.generateStructures(), hardcore,
                                            type);
                            worldSettings.setGeneratorSettings(creator
                                    .generatorSettings());
                            worlddata = new WorldData(worldSettings, name);
                        }
                        worlddata.checkName(name);
                        final WorldServer internal =
                                (WorldServer)new WorldServer(getServer(), (IDataManager)sdm, worlddata, dimension, getServer().methodProfiler, creator.environment(), generator).b();
                        new BukkitRunnable() {
                            public void run() {
                                try {
                                    Field w = CraftServer.class.getDeclaredField("worlds");
                                    w.setAccessible(true);
                                    if (!((Map<String, World>)w.get(getCraftServer())).containsKey(name.toLowerCase())) {
                                        aborted = true;
                                        return;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    aborted = true;
                                    return;
                                }
                                new Thread() {
                                    public void run() {
                                        internal.scoreboard = getCraftServer().getScoreboardManager().getMainScoreboard().getHandle();
                                        internal.tracker = new EntityTracker(internal);
                                        internal.addIWorldAccess(new WorldManager(getServer(), internal));
                                        internal.worldData.setDifficulty(EnumDifficulty.EASY);
                                        internal.setSpawnFlags(true, true);
                                        getServer().worlds.add(internal);

                                        if (generator != null) {
                                            internal.getWorld().getPopulators().addAll(generator.getDefaultPopulators(internal.getWorld()));
                                        }

                                        new BukkitRunnable() {
                                            public void run() {
                                                Bukkit.getPluginManager().callEvent(new WorldInitEvent(internal.getWorld()));
                                            }
                                        }.runTask(gb);

                                        System.out.print("Preparing start region for level " + (getServer().worlds.size() - 1) + " (Seed: " + internal.getSeed() + ")");
                                        if (internal.getWorld().getKeepSpawnInMemory()) {
                                            short short1 = 196;
                                            long i = System.currentTimeMillis();
                                            for (int j = -short1; j <= short1; j += 16) {
                                                for (int k = -short1; k <= short1; k += 16) {
                                                    long l = System.currentTimeMillis();

                                                    if (l < i) {
                                                        i = l;
                                                    }

                                                    if (l > i + 1000L) {
                                                        int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                                                        int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;

                                                        System.out.println("Preparing spawn area for " + name + ", " + j1 * 100 / i1 + "%");
                                                        i = l;
                                                    }

                                                    BlockPosition chunkcoordinates = internal.getSpawn();
                                                    getChunkAt(internal.chunkProviderServer, chunkcoordinates.getX() + j >> 4, chunkcoordinates.getZ() + k >> 4);
                                                }
                                            }
                                        }

                                        new BukkitRunnable() {
                                            public void run() {
                                                Bukkit.getPluginManager().callEvent(new WorldLoadEvent(internal.getWorld()));
                                            }
                                        }.runTask(gb);

                                        ret = internal.getWorld();
                                    }

                                    private Chunk getChunkAt(final ChunkProviderServer cps, final int i, final int j) {
                                        cps.unloadQueue.remove(i, j);
                                        Chunk chunk = cps.chunks.get(LongHash.toLong(i, j));
                                        ChunkRegionLoader loader = null;
                                        try {
                                            Field f = ChunkProviderServer.class.getDeclaredField("chunkLoader");
                                            f.setAccessible(true);
                                            if ((f.get(cps) instanceof ChunkRegionLoader)) {
                                                loader = (ChunkRegionLoader)f.get(cps);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if ((chunk == null) && (loader != null) && (loader.chunkExists(cps.world, i, j))) {
                                            final ChunkRegionLoader loader1 = loader;
                                            wait = null;
                                            new BukkitRunnable() {
                                                public void run() {
                                                    wait = ChunkIOExecutor.syncChunkLoad(cps.world, loader1, cps, i, j);
                                                }
                                            }.runTask(gb);

                                            while (wait == null) {
                                                try {
                                                    Thread.sleep(10);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            chunk = wait;
                                        } else if (chunk == null) {
                                            chunk = originalGetChunkAt(cps, i, j);
                                        }

                                        return chunk;
                                    }

                                    public Chunk originalGetChunkAt(final ChunkProviderServer cps, final int i, final int j) {
                                        cps.unloadQueue.remove(i, j);
                                        Chunk chunk = cps.chunks.get(LongHash.toLong(i, j));
                                        boolean newChunk = false;

                                        if (chunk == null) {
                                            //cps.world.timings.syncChunkLoadTimer.startTiming();
                                            chunk = cps.loadChunk(i, j);
                                            if (chunk == null) {
                                                if (cps.chunkProvider == null) {
                                                    chunk = cps.emptyChunk;
                                                } else {
                                                    try {
                                                        chunk = cps.chunkProvider.getOrCreateChunk(i, j);
                                                    } catch (Throwable throwable) {
                                                        CrashReport crashreport = CrashReport.a(throwable, "Exception generating new chunk");
                                                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Chunk to be generated");

                                                        crashreportsystemdetails.a("Location", String.format("%d,%d", new Object[] {Integer.valueOf(i), Integer.valueOf(j)}));
                                                        crashreportsystemdetails.a("Position hash", LongHash.toLong(i, j));
                                                        crashreportsystemdetails.a("Generator", cps.chunkProvider.getName());
                                                        throw new ReportedException(crashreport);
                                                    }
                                                }
                                                newChunk = true;
                                            }

                                            cps.chunks.put(LongHash.toLong(i, j), chunk);
                                            final Chunk chunki = chunk;
                                            final boolean newChunki = newChunk;
                                            new BukkitRunnable() {
                                                public void run() {
                                                    chunki.addEntities();

                                                    Server server = cps.world.getServer();
                                                    if (server != null) {
                                                        server.getPluginManager().callEvent(new ChunkLoadEvent(chunki.bukkitChunk, newChunki));
                                                    }
                                                }
                                            }.runTask(gb);

                                            for (int x = -2; x < 3; x++) {
                                                for (int z = -2; z < 3; z++) {
                                                    if ((x == 0) && (z == 0)) {
                                                        continue;
                                                    }
                                                    Chunk neighbor = cps.getChunkIfLoaded(chunk.locX + x, chunk.locZ + z);
                                                    if (neighbor != null) {
                                                        neighbor.setNeighborLoaded(-x, -z);
                                                        chunk.setNeighborLoaded(x, z);
                                                    }
                                                }
                                            }

                                            loadNearby(chunk, cps, cps, i, j);
                                            //cps.world.timings.syncChunkLoadTimer.stopTiming();
                                        }

                                        return chunk;
                                    }

                                    public void loadNearby(Chunk c, IChunkProvider ichunkprovider, IChunkProvider ichunkprovider1, int i, int j) {
                                        //c.world.timings.syncChunkLoadPostTimer.startTiming();
                                        boolean flag = ichunkprovider.isChunkLoaded(i, j - 1);
                                        boolean flag1 = ichunkprovider.isChunkLoaded(i + 1, j);
                                        boolean flag2 = ichunkprovider.isChunkLoaded(i, j + 1);
                                        boolean flag3 = ichunkprovider.isChunkLoaded(i - 1, j);
                                        boolean flag4 = ichunkprovider.isChunkLoaded(i - 1, j - 1);
                                        boolean flag5 = ichunkprovider.isChunkLoaded(i + 1, j + 1);
                                        boolean flag6 = ichunkprovider.isChunkLoaded(i - 1, j + 1);
                                        boolean flag7 = ichunkprovider.isChunkLoaded(i + 1, j - 1);

                                        if ((flag1) && (flag2) && (flag5)) {
                                            if (!c.isDone()) {
                                                getChunkAt((ChunkProviderServer)ichunkprovider1, i, j);
                                            } else {
                                                ichunkprovider.a(ichunkprovider1, c, i, j);
                                            }

                                        }

                                        if ((flag3) && (flag2) && (flag6)) {
                                            Chunk chunk = getOrCreateChunk((ChunkProviderServer)ichunkprovider, i - 1, j);
                                            if (!chunk.isDone()) {
                                                getChunkAt((ChunkProviderServer)ichunkprovider1, i - 1, j);
                                            } else {
                                                ichunkprovider.a(ichunkprovider1, chunk, i - 1, j);
                                            }
                                        }

                                        if ((flag) && (flag1) && (flag7)) {
                                            Chunk chunk = getOrCreateChunk((ChunkProviderServer)ichunkprovider, i, j - 1);
                                            if (!chunk.isDone()) {
                                                getChunkAt((ChunkProviderServer)ichunkprovider1, i, j - 1);
                                            } else {
                                                ichunkprovider.a(ichunkprovider1, chunk, i, j - 1);
                                            }
                                        }

                                        if ((flag4) && (flag) && (flag3)) {
                                            Chunk chunk = getOrCreateChunk((ChunkProviderServer)ichunkprovider, i - 1, j - 1);
                                            if (!chunk.isDone()) {
                                                getChunkAt((ChunkProviderServer)ichunkprovider1, i - 1, j - 1);
                                            } else {
                                                ichunkprovider.a(ichunkprovider1, chunk, i - 1, j - 1);
                                            }
                                        }

                                        //c.world.timings.syncChunkLoadPostTimer.stopTiming();
                                    }

                                    public boolean a(IChunkProvider ichunkprovider, Chunk chunk, int i, int j) {
                                        if ((ichunkprovider != null) && (ichunkprovider.a(ichunkprovider, chunk, i, j))) {
                                            Chunk chunk1 = getOrCreateChunk((ChunkProviderServer)ichunkprovider, i, j);

                                            chunk1.e();
                                            return true;
                                        }
                                        return false;
                                    }

                                    private Chunk getOrCreateChunk(ChunkProviderServer ip, int i, int j) {
                                        Chunk chunk = ip.chunks.get(LongHash.toLong(i, j));
                                        chunk = chunk == null ? getChunkAt(ip, i, j) : (!ip.world.ad()) && (!ip.forceChunkLoad) ? ip.emptyChunk : chunk;

                                        if (chunk == ip.emptyChunk) {
                                            return chunk;
                                        }
                                        if ((i != chunk.locX) || (j != chunk.locZ)) {
                                            System.err.println("Chunk (" + chunk.locX + ", " + chunk.locZ + ") stored at  (" + i + ", " + j + ") in world '" + ip.world.getWorld().getName() + "'");
                                            System.err.println(chunk.getClass().getName());
                                            Throwable ex = new Throwable();
                                            ex.fillInStackTrace();
                                            ex.printStackTrace();
                                        }

                                        return chunk;

                                    }

                                }.start();
                            }
                        }.runTask(gb);
                    }
                }.start();
            }
        }.runTask(gb);

        //Wait for the world to load before returning it.
        while (ret == null && !aborted) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Wait a extra second to make sure everything is loaded.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //DONE!
        alreadyLoading = false;
        */
        return ret;
    }

    private static File getWorldContainer() {
        if (getServer().universe != null) {
            return getServer().universe;
        }
        try {
            Field container = CraftServer.class.getDeclaredField("container");
            container.setAccessible(true);
            Field settings = CraftServer.class.getDeclaredField("configuration");
            settings.setAccessible(true);
            File co = (File)container.get(getCraftServer());
            if (co == null) {
                container.set(getCraftServer(), new File(((YamlConfiguration)settings.get(getCraftServer())).getString("settings.world-container", ".")));
            }

            return (File)container.get(getCraftServer());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static MinecraftServer getServer() {
        return getCraftServer().getServer();
    }

    private static CraftServer getCraftServer() {
        return ((CraftServer)Bukkit.getServer());
    }

    private static ChunkGenerator getGenerator(String world) {
        try {
            Field settings = CraftServer.class.getDeclaredField("configuration");
            settings.setAccessible(true);
            ConfigurationSection section = ((YamlConfiguration)settings.get(getCraftServer())).getConfigurationSection("worlds");
            ChunkGenerator result = null;

            if (section == null) {
                return null;
            }
            section = section.getConfigurationSection(world);
            if (section == null) {
                return null;
            }

            String name = section.getString("generator");
            if (name == null || name.isEmpty()) {
                return null;
            }

            String[] split = name.split(":", 2);
            String id = split.length > 1 ? split[1] : null;
            Plugin plugin = Bukkit.getPluginManager().getPlugin(split[0]);

            if (plugin == null) {
                Bukkit.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
            } else if (!plugin.isEnabled()) {
                Bukkit.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled yet (is it load:STARTUP?)");
            } else {
                try {
                    result = plugin.getDefaultWorldGenerator(world, id);
                    if (result == null) {
                        Bukkit.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' lacks a default world generator");
                    }
                } catch (Throwable t) {
                    plugin.getLogger().log(Level.SEVERE, "Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName(), t);
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}