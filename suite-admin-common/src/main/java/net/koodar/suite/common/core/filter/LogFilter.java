package net.koodar.suite.common.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.koodar.suite.common.util.ServletUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 日志记录过滤器
 *
 * @author liyc
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		final String remoteAddr = ServletUtils.getClientIP(request);

		log.debug("Starting url: [{}], method: [{}], ip: [{}]",
				request.getRequestURL(),
				request.getMethod(),
				remoteAddr);

		// Set start time
		final long startTime = System.currentTimeMillis();

		// Do filter
		filterChain.doFilter(request, response);

		log.debug("Ending url: [{}], method: [{}], ip: [{}], status: [{}], usage: [{}] ms",
				request.getRequestURL(),
				request.getMethod(),
				remoteAddr,
				response.getStatus(),
				System.currentTimeMillis() - startTime);

	}
}
