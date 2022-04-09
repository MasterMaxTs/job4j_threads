package concurrent.storage;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.*;

@ThreadSafe
public class UserStorage {

    @GuardedBy("this")
    private final Map<Integer, User> stoge;

    public UserStorage() {
        stoge = new TreeMap<>();
    }

    public synchronized boolean add(User user) {
        return stoge.putIfAbsent(user.getId(), user) != null;
    }

    public synchronized boolean update(User user) {
        return stoge.put(user.getId(), user) != null;
    }

    public synchronized boolean delete(User user) {
        return stoge.remove(user.getId()) != null;
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean rsl = false;
            if (stoge.containsKey(fromId) && stoge.containsKey(toId)) {
                User userFrom = stoge.get(fromId);
                int amountFrom = userFrom.getAmount();
                if (amountFrom >= amount) {
                    userFrom.setAmount(amountFrom - amount);
                    update(userFrom);
                    User userTo = stoge.get(toId);
                    userTo.setAmount(userTo.getAmount() + amount);
                    update(userTo);
                    rsl = true;
                }
            }
        return rsl;
    }

    public synchronized List<User> getUsers() {
        return new ArrayList<>(stoge.values());
    }
}
