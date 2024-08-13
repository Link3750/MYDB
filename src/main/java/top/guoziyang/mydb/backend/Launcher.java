package top.guoziyang.mydb.backend;

import top.guoziyang.mydb.backend.dm.DataManager;
import top.guoziyang.mydb.backend.dm.pageCache.PageCacheImpl;
import top.guoziyang.mydb.backend.properties.PropertiesEntity;
import top.guoziyang.mydb.backend.server.Server;
import top.guoziyang.mydb.backend.tbm.TableManager;
import top.guoziyang.mydb.backend.tm.TransactionManager;
import top.guoziyang.mydb.backend.utils.Panic;
import top.guoziyang.mydb.backend.vm.VersionManager;
import top.guoziyang.mydb.backend.vm.VersionManagerImpl;
import top.guoziyang.mydb.common.Error;

import java.io.File;

public class Launcher {


    public static final long DEFAULT_MEM = (1 << 20) * 64;
    public static final long KB = 1 << 10;
    public static final long MB = 1 << 20;
    public static final long GB = 1 << 30;

    public static void main(String[] args) {
        PropertiesEntity propertiesEntity = new PropertiesEntity();

        openDB(propertiesEntity.getDBPath(), parseMem(propertiesEntity.getMem()), propertiesEntity.getPort());
        System.out.println("Usage: launcher (open|create) DBPath");
    }

    private static void createDB(String path) {
        TransactionManager tm = TransactionManager.create(path);
        DataManager dm = DataManager.create(path, DEFAULT_MEM, tm);
        VersionManager vm = new VersionManagerImpl(tm, dm);
        TableManager.create(path, vm, dm);
        tm.close();
        dm.close();
    }

    private static void openDB(String path, long mem, int port) {
        String dbPath = path + PageCacheImpl.DB_SUFFIX;
        File file = new File(dbPath);
        if (!file.exists()) {
            createDB(path);
            file = new File(dbPath);
            if (!file.exists()) {
                Panic.panic(Error.FileNotExistsException);
            }
        }
        TransactionManager tm = TransactionManager.open(path);
        DataManager dm = DataManager.open(path, mem, tm);
        VersionManager vm = new VersionManagerImpl(tm, dm);
        TableManager tbm = TableManager.open(path, vm, dm);
        new Server(port, tbm).start();
    }

    private static long parseMem(String memStr) {
        if (memStr == null || memStr.isEmpty()) {
            return DEFAULT_MEM;
        }
        if (memStr.length() < 2) {
            Panic.panic(Error.InvalidMemException);
        }
        String unit = memStr.substring(memStr.length() - 2);
        long memNum = Long.parseLong(memStr.substring(0, memStr.length() - 2));
        switch (unit) {
            case "KB":
                return memNum * KB;
            case "MB":
                return memNum * MB;
            case "GB":
                return memNum * GB;
            default:
                Panic.panic(Error.InvalidMemException);
        }
        return DEFAULT_MEM;
    }
}
