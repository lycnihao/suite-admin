package run.bottle.bottleadmin.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import run.bottle.bottleadmin.exception.ServiceException;
import run.bottle.bottleadmin.util.JsonUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * AbstractStringCacheStore.
 * <p>Part from halo project.</p>
 *
 * @author liyc
 * @date 2022-08-29
 * @see "run.bottle.bottleadmin.cache.AbstractStringCacheStore"
 */
@Slf4j
public abstract class AbstractStringCacheStore extends AbstractCacheStore<String, String> {

	protected Optional<CacheWrapper<String>> jsonToCacheWrapper(String json) {
		if (!StringUtils.hasText(json)) {
			return Optional.empty();
		}
		CacheWrapper<String> cacheWrapper = null;
		try {
			cacheWrapper = JsonUtils.jsonToObject(json, new TypeReference<CacheWrapper<String>>() {});
		} catch (Exception e) {
			log.debug("Failed to convert json to wrapper value bytes: [{}]", json, e);
		}
		return Optional.ofNullable(cacheWrapper);
	}

	public <T> void putAny(String key, T value) {
		try {
			put(key, JsonUtils.objectToJson(value));
		} catch (JsonProcessingException e) {
			throw new ServiceException("Failed to convert " + value + " to json", e);
		}
	}

	public <T> void putAny(@NonNull String key, @NonNull T value, long timeout,
						   @NonNull TimeUnit timeUnit) {
		try {
			put(key, JsonUtils.objectToJson(value), timeout, timeUnit);
		} catch (JsonProcessingException e) {
			throw new ServiceException("Failed to convert " + value + " to json", e);
		}
	}

	public <T> Optional<T> getAny(String key, Class<T> type) {
		Assert.notNull(type, "Type must not be null");

		return get(key).map(value -> {
			try {
				return JsonUtils.jsonToObject(value, type);
			} catch (IOException e) {
				log.error("Failed to convert json to type: " + type.getName(), e);
				return null;
			}
		});
	}

}
