package top.guoziyang.mydb.backend.utils;

public class Types {
    public static long addressToUid(int pgno, short offset) {
        return (long)pgno << 32 | (long)offset;
    }
}
