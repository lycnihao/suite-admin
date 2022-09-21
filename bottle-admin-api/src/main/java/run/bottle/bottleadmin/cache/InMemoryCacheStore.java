package run.bottle.bottleadmin.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In-memory cache store.
 * <p>Part from halo project.</p>
 *
 * @author liyc
 * @date 2022-08-30
 * @see ”run.bottle.bottleadmin.cache.InMemoryCacheStore“
 */
@Slf4j
public class InMemoryCacheStore extends AbstractStringCacheStore {

	/**
	 * Cleaner schedule period. (ms)
	 */
	private static final long PERIOD = 60 * 1000;

	/**
	 * Cache container.
	 */
	private static final ConcurrentHashMap<String, CacheWrapper<String>> CACHE_CONTAINER =
			new ConcurrentHashMap<>();

	private final ScheduledExecutorService executors;

	/**
	 * Lock.
	 */
	private final Lock lock = new ReentrantLock();

	public InMemoryCacheStore() {
		ThreadFactory threadFactory = new CustomizableThreadFactory("clear-cache-pool-");
		executors = new ScheduledThreadPoolExecutor(1, threadFactory);
		executors.scheduleAtFixedRate(new CacheExpiryCleaner(), 0, PERIOD, TimeUnit.MILLISECONDS);
	}

	@Override
	@NonNull
	Optional<CacheWrapper<String>> getInternal(@NonNull String key) {
		Assert.hasText(key, "Cache key must not be blank");

		return Optional.ofNullable(CACHE_CONTAINER.get(key));
	}

	@Override
	void putInternal(@NonNull String key, @NonNull CacheWrapper<String> cacheWrapper) {
		Assert.hasText(key, "Cache key must not be blank");
		Assert.notNull(cacheWrapper, "Cache wrapper must not be null");

		// Put the cache wrapper
		CacheWrapper<String> putCacheWrapper = CACHE_CONTAINER.put(key, cacheWrapper);
		log.debug("Put [{}] cache result: [{}], original cache wrapper: [{}]", key, putCacheWrapper,
				cacheWrapper);
	}

	@Override
	Boolean putInternalIfAbsent(@NonNull String key, @NonNull CacheWrapper<String> cacheWrapper) {
		Assert.hasText(key, "Cache key must not be blank");
		Assert.notNull(cacheWrapper, "Cache wrapper must not be null");

		log.debug("Preparing to put key: [{}], value: [{}]", key, cacheWrapper);

		lock.lock();
		try {
			// Get the value before
			Optional<String> valueOptional = get(key);

			if (valueOptional.isPresent()) {
				log.warn("Failed to put the cache, because the key: [{}] has been present already",
						key);
				return false;
			}

			// Put the cache wrapper
			putInternal(key, cacheWrapper);
			log.debug("Put successfully");
			return true;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void delete(@NonNull String key) {
		Assert.hasText(key, "Cache key must not be blank");

		CACHE_CONTAINER.remove(key);
		log.debug("Removed key: [{}]", key);
	}

	@Override
	public LinkedHashMap<String, String> toMap() {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		CACHE_CONTAINER.forEach((key, value) -> map.put(key, value.getData()));
		return map;
	}

	@PreDestroy
	public void preDestroy() {
		log.debug("Cancelling all timer tasks");
		executors.shutdown();
		clear();
	}

	public void clear() {
		CACHE_CONTAINER.clear();
	}

	/**
	 * Cache cleaner.
	 */
	private class CacheExpiryCleaner extends TimerTask {

		@Override
		public void run() {
			CACHE_CONTAINER.keySet().forEach(key -> {
				if (!InMemoryCacheStore.this.get(key).isPresent()) {
					log.debug("Deleted the cache: [{}] for expiration", key);
				}
			});
		}
	}
}
