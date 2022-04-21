package concurrent.cache;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CacheTest {

    private List<Base> bases;
    private Cache cache;

    @Before
    public void whenSetUp() {
        bases = List.of(
                new Base(1, 1),
                new Base(2, 1)
        );
        cache = new Cache();
    }

    @Test
    public void whenAddThanGet() {
        cache.add(bases.get(0));
        cache.add(bases.get(1));
        assertThat(cache.getBases(), is(List.of(bases.get(0), bases.get(1))));
    }

    @Test(expected = OptimisticException.class)
    public void whenUpdateAndNoSameVersionThanException() {
        cache.add(new Base(1, 1));
        Base modify = new Base(1, 2);
        cache.update(modify);
    }

    @Test
    public void whenUpdateThanGetBaseWithIncrementedVersion() {
        cache.add(new Base(1, 1));
        Base base = cache.getBases().get(0);
        base.setName("First Base");
        cache.update(base);
        assertThat(cache.getBases().get(0).getVersion(), is(2));
    }

    @Test
    public void whenDelete() {
        cache.add(bases.get(0));
        cache.add(bases.get(1));
        cache.delete(bases.get(0));
        assertThat(cache.getBases(), is(List.of(bases.get(1))));
    }
}