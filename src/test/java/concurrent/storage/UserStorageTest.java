package concurrent.storage;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UserStorageTest {
    private UserStorage userStorage;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void whenSetUp() {
        userStorage = new UserStorage();
        user1 = new User(1, 1000);
        user2 = new User(2, 2000);
        user3 = new User(3, 3000);
    }

    @Test
    public void whenAdd() throws InterruptedException {

        Thread first = new Thread(
                () -> userStorage.add(user1)
        );
        Thread second = new Thread(
                () -> userStorage.add(user2)
        );
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(userStorage.getUsers().get(0), is(user1));
        assertThat(userStorage.getUsers().get(1), is(user2));
    }

    @Test
    public void whenUpdate() throws InterruptedException {
        Thread first = new Thread(
                () -> {
                    user1.setAmount(0);
                    userStorage.update(user1);
                }
        );
        Thread second = new Thread(
                () -> {
                    user1.setAmount(100);
                    userStorage.update(user1);
                }
        );
        first.start();
        first.join();
        Thread.sleep(1000);
        second.start();
        second.join();
        assertThat(userStorage.getUsers().get(0).getAmount(), is(100));
    }

    @Test
    public void whenTransfer() throws InterruptedException {
        Thread first = new Thread(
                () -> {
                    userStorage.add(user1);
                    userStorage.add(user2);
                    userStorage.add(user3);
                }
        );
        Thread second = new Thread(
                () -> userStorage.transfer(2, 1, 2000)
        );
        Thread third = new Thread(
                () -> userStorage.transfer(1, 3, 4000)
        );

        first.start();
        first.join();
        Thread.sleep(1000);
        second.start();
        third.start();
        second.join();
        third.join();
        assertThat(userStorage.getUsers().get(0).getAmount(), is(3000));
        assertThat(userStorage.getUsers().get(1).getAmount(), is(0));
        assertThat(userStorage.getUsers().get(2).getAmount(), is(3000));
    }

    @Test
    public void whenDelete() throws InterruptedException {
        Thread first = new Thread(
                () -> {
                    userStorage.add(user1);
                    userStorage.add(user2);
                }
        );
        Thread second = new Thread(
                () -> userStorage.delete(user1)
        );
        Thread third = new Thread(
                () -> userStorage.delete(user1)
        );
        Thread fourth = new Thread(
                () -> userStorage.delete(user2)
        );
        first.start();
        first.join();
        Thread.sleep(1000);
        second.start();
        third.start();
        fourth.start();
        second.join();
        third.join();
        fourth.join();
        assertThat(userStorage.getUsers(), is(List.of()));
    }
}