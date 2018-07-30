package org.sobotics.heatdetector.rest.security;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * This class is a very crude rate limiter (maybe replace with Kite)
 * The class accepts call at every <code>TIME_BETWEEN_REQEUST_SECONDS</code>, if calls are more often backoff will be sent 
 * and if this is not respected a <code>TIME_RATE_LIMIT_MINUTES</code> minute rate limit will be imposed.
 * Note: The time is reset if application continues to call.
 * @author Petter Friberg
 *
 */
@Service
public class ApiRateLimiter {
	
	private static final int TIME_BETWEEN_REQEUST_SECONDS= 30;
	private static final int TIME_RATE_LIMIT_MINUTES= 5;
	
	private LoadingCache<String, Integer> accessCache=CacheBuilder.newBuilder().expireAfterWrite(TIME_BETWEEN_REQEUST_SECONDS, TimeUnit.SECONDS).build(new CacheLoader<String, Integer>() {
        @Override
        public Integer load(final String key) {
            return 0;
        }
    });
	
	private LoadingCache<String, Integer> rateLimitCache=CacheBuilder.newBuilder().expireAfterWrite(TIME_RATE_LIMIT_MINUTES, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
        @Override
        public Integer load(final String key) {
            return 0;
        }
    });

	public void checkLimit(String key) throws RateLimitException {
		if (isRateLimited(key)){
			throw new RateLimitException("You have not respected backoff and will now we rate limited for " + TIME_RATE_LIMIT_MINUTES + " minutes and the time will reset if you continue to call api");
		}
		access(key);
	}
	
	public void access(final String key) {
		int cnt = accessCache.getUnchecked(key)+1;
        accessCache.put(key, cnt);
        if (cnt>2){
        	rateLimitCache.put(key, 1);
        }
        
    }

    public boolean isRateLimited(final String key) {
        return rateLimitCache.getUnchecked(key) > 0;
    }
    
    public int getBackOff(final HttpServletRequest request){
    	if (accessCache.getUnchecked(request.getRemoteAddr())>1){
    		return TIME_BETWEEN_REQEUST_SECONDS*1000;
    	}
    	return 0;
    }

	


}
