package concurrent.cache;

import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public class Cache {

    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) != null;
    }

    public boolean update(Base model) {
        Base computeBase = memory.computeIfPresent(
                model.getId(),
                (k, v) -> {
                    Base stored = memory.get(model.getId());
                    if (stored.getVersion() != model.getVersion()) {
                        throw new OptimisticException("Versions are not equal");
                    }
                    model.setVersion(model.getVersion() + 1);
                    return memory.put(k, v);
                }
        );
        return computeBase != null;
    }

    public void delete(Base model) {
        memory.remove(model.getId());
    }

    public List<Base> getBases() {
        return new ArrayList<>(memory.values());
    }
}
