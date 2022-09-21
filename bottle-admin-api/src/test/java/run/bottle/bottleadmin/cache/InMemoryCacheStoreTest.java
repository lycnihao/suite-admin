package run.bottle.bottleadmin.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * InMemoryCacheStoreTest.
 * <p>Part from halo project.</p>
 *
 * @author liyc
 * @date 2022-08-30
 */
public class InMemoryCacheStoreTest {

	InMemoryCacheStore cacheStore;

	@BeforeEach
	void setUp() {
		cacheStore = new InMemoryCacheStore();
	}

	@Test
	void putNullValueTest() {
		String key = "test_key";

		assertThrows(IllegalArgumentException.class, () -> cacheStore.put(key, null));
	}

	@Test
	void putNullKeyTest() {
		String value = "test_value";

		assertThrows(IllegalArgumentException.class, () -> cacheStore.put(null, value));
	}

	@Test
	void getByNullKeyTest() {
		assertThrows(IllegalArgumentException.class, () -> cacheStore.get(null));
	}

	@Test
	void getNullTest() {
		String key = "test_key";

		Optional<String> valueOptional = cacheStore.get(key);

		assertFalse(valueOptional.isPresent());
	}

	@Test
	void expirationTest() throws InterruptedException {
		String key = "test_key";
		String value = "test_value";
		cacheStore.put(key, value, 500, TimeUnit.MILLISECONDS);

		Optional<String> valueOptional = cacheStore.get(key);

		assertTrue(valueOptional.isPresent());
		assertEquals(value, valueOptional.get());

		TimeUnit.SECONDS.sleep(1L);

		valueOptional = cacheStore.get(key);

		assertFalse(valueOptional.isPresent());
	}

	@Test
	void toMapTest() {
		InMemoryCacheStore localCacheStore = new InMemoryCacheStore();
		localCacheStore.clear();
		String key1 = "test_key_1";
		String value1 = "test_value_1";

		// Put the cache
		localCacheStore.put(key1, value1);
		assertEquals("{test_key_1=test_value_1}", localCacheStore.toMap().toString());

		String key2 = "test_key_2";
		String value2 = "test_value_2";

		// Put the cache
		localCacheStore.put(key2, value2);
		assertEquals("{test_key_2=test_value_2, test_key_1=test_value_1}",
				localCacheStore.toMap().toString());
	}

}
