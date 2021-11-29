package interview_questions;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LRUCacheTest {

    private LRUCache<Integer, Integer> cache;

    @Test
    public void tesLruCache() {

        cache = new LRUCache<>(2);

        LRUCache.Node<Integer, Integer> lru;
        LRUCache.Node<Integer, Integer> mru;

        cache.put(1, 2);

        lru = cache.getLru();
        mru = cache.getMru();

        assertThat(lru.key).isEqualTo(1);
        assertThat(lru.value).isEqualTo(2);

        assertThat(mru.key).isEqualTo(1);
        assertThat(mru.value).isEqualTo(2);

        cache.put(2, 3);

        cache.get(2);
        cache.get(1);
        cache.get(2);

        lru = cache.getLru();
        mru = cache.getMru();

        assertThat(lru.key).isEqualTo(1);
        assertThat(lru.value).isEqualTo(2);

        assertThat(mru.key).isEqualTo(2);
        assertThat(mru.value).isEqualTo(3);

    }

    @Test
    public void tesLruCacheWithMoreEntries() {

        cache = new LRUCache<>(10);

        cache.put(1, 2);
        cache.put(2, 3);
        cache.put(5, 3);
        cache.put(7, 3);
        cache.put(9, 3);

        cache.get(2);
        cache.get(1);
        cache.get(2);
        cache.get(5);
        cache.get(2);

        cache.put(10, 3);

        cache.get(2);

        cache.put(10, 4);

        LRUCache.Node<Integer, Integer> lru = cache.getLru();
        LRUCache.Node<Integer, Integer> mru = cache.getMru();

        assertThat(lru.key).isEqualTo(7);
        assertThat(lru.value).isEqualTo(3);

        assertThat(mru.key).isEqualTo(10);
        assertThat(mru.value).isEqualTo(4);
        assertThat(mru.next).isNull();

    }

}
