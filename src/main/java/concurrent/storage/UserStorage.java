package concurrent.storage;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.List;

@ThreadSafe
public class UserStorage {

    @GuardedBy("this")
    private final List<User> stoge;

    public UserStorage() {
        stoge = new ArrayList<>();
    }

    public synchronized boolean add(User user) {
        boolean rsl = false;
        if (!stoge.contains(user)) {
            stoge.add(user);
            rsl = true;
        }
        return rsl;
    }

    private synchronized int findIndexById(int id) {
        int rsl = -1;
        for (int index = 0; index < stoge.size(); index++) {
            if (stoge.get(index).getId() == id) {
                rsl = index;
            }
        }
        return rsl;
    }

    public synchronized boolean update(User user) {
        boolean rsl = false;
        int index = findIndexById(user.getId());
        if (index != -1) {
            stoge.set(index, user);
            rsl = true;
        }
        return rsl;
    }

    public synchronized boolean delete(User user) {
        return stoge.remove(user);
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean rsl = false;
        int indexFrom = findIndexById(fromId);
        int indexTo = findIndexById(toId);
            if (indexFrom != -1 && indexTo != -1) {
                User userFrom = stoge.get(indexFrom);
                int amountFrom = userFrom.getAmount();
                if (amountFrom >= amount) {
                    userFrom.setAmount(amountFrom - amount);
                    update(userFrom);
                    User userTo = stoge.get(indexTo);
                    userTo.setAmount(userTo.getAmount() + amount);
                    update(userTo);
                    rsl = true;
                }
            }
        return rsl;
    }
}
