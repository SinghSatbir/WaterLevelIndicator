package android.support.v4.util;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap(0, 0.75f, true);
    }

    public void resize(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        synchronized (this) {
            this.maxSize = maxSize;
        }
        trimToSize(maxSize);
    }

    /* JADX WARNING: Missing block: B:12:0x0023, code skipped:
            r0 = create(r5);
     */
    /* JADX WARNING: Missing block: B:13:0x0027, code skipped:
            if (r0 != null) goto L_0x002e;
     */
    /* JADX WARNING: Missing block: B:19:0x002e, code skipped:
            monitor-enter(r4);
     */
    /* JADX WARNING: Missing block: B:21:?, code skipped:
            r4.createCount++;
            r1 = r4.map.put(r5, r0);
     */
    /* JADX WARNING: Missing block: B:22:0x003b, code skipped:
            if (r1 == null) goto L_0x004b;
     */
    /* JADX WARNING: Missing block: B:23:0x003d, code skipped:
            r4.map.put(r5, r1);
     */
    /* JADX WARNING: Missing block: B:24:0x0042, code skipped:
            monitor-exit(r4);
     */
    /* JADX WARNING: Missing block: B:25:0x0043, code skipped:
            if (r1 == null) goto L_0x0058;
     */
    /* JADX WARNING: Missing block: B:26:0x0045, code skipped:
            entryRemoved(false, r5, r0, r1);
     */
    /* JADX WARNING: Missing block: B:28:?, code skipped:
            r4.size += safeSizeOf(r5, r0);
     */
    /* JADX WARNING: Missing block: B:32:0x0058, code skipped:
            trimToSize(r4.maxSize);
     */
    /* JADX WARNING: Missing block: B:37:?, code skipped:
            return null;
     */
    /* JADX WARNING: Missing block: B:38:?, code skipped:
            return r1;
     */
    /* JADX WARNING: Missing block: B:39:?, code skipped:
            return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            V mapValue = this.map.get(key);
            if (mapValue != null) {
                this.hitCount++;
                return mapValue;
            }
            this.missCount++;
        }
    }

    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        V previous;
        synchronized (this) {
            this.putCount++;
            this.size += safeSizeOf(key, value);
            previous = this.map.put(key, value);
            if (previous != null) {
                this.size -= safeSizeOf(key, previous);
            }
        }
        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }
        trimToSize(this.maxSize);
        return previous;
    }

    /* JADX WARNING: Missing block: B:9:0x0031, code skipped:
            throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void trimToSize(int maxSize) {
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (this.size >= 0 && (!this.map.isEmpty() || this.size == 0)) {
                    if (this.size <= maxSize || this.map.isEmpty()) {
                    } else {
                        Entry<K, V> toEvict = (Entry) this.map.entrySet().iterator().next();
                        key = toEvict.getKey();
                        value = toEvict.getValue();
                        this.map.remove(key);
                        this.size -= safeSizeOf(key, value);
                        this.evictionCount++;
                    }
                }
            }
            entryRemoved(true, key, value, null);
        }
    }

    public final V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        V previous;
        synchronized (this) {
            previous = this.map.remove(key);
            if (previous != null) {
                this.size -= safeSizeOf(key, previous);
            }
        }
        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }
        return previous;
    }

    /* Access modifiers changed, original: protected */
    public void entryRemoved(boolean evicted, K k, V v, V v2) {
    }

    /* Access modifiers changed, original: protected */
    public V create(K k) {
        return null;
    }

    private int safeSizeOf(K key, V value) {
        int result = sizeOf(key, value);
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("Negative size: " + key + "=" + value);
    }

    /* Access modifiers changed, original: protected */
    public int sizeOf(K k, V v) {
        return 1;
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final synchronized int size() {
        return this.size;
    }

    public final synchronized int maxSize() {
        return this.maxSize;
    }

    public final synchronized int hitCount() {
        return this.hitCount;
    }

    public final synchronized int missCount() {
        return this.missCount;
    }

    public final synchronized int createCount() {
        return this.createCount;
    }

    public final synchronized int putCount() {
        return this.putCount;
    }

    public final synchronized int evictionCount() {
        return this.evictionCount;
    }

    public final synchronized Map<K, V> snapshot() {
        return new LinkedHashMap(this.map);
    }

    public final synchronized String toString() {
        String format;
        int hitPercent = 0;
        synchronized (this) {
            int accesses = this.hitCount + this.missCount;
            if (accesses != 0) {
                hitPercent = (this.hitCount * 100) / accesses;
            }
            format = String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(hitPercent)});
        }
        return format;
    }
}
