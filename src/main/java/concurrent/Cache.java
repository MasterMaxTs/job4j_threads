package concurrent;

public final class Cache {

    private static Cache cache;

    public static synchronized Cache instOf() {
        if (cache == null) {
            cache = new Cache();
        }
        return cache;
    }

    public static void main(String[] args) {
        Cache cache = Cache.instOf();
    }
}
