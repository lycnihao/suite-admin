//package run.bottle.admin.security.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * @author liyc
// * @date 2022-12-12
// */
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@WebFilter("/*")
//public class CorsFilter implements Filter {
//
//
//	public CorsFilter() {
//	}
//
//	@Override
//	public void init(FilterConfig fc) {
//	}
//
//	@Override
//	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
//		HttpServletResponse response = (HttpServletResponse) resp;
//		HttpServletRequest request = (HttpServletRequest) req;
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Credentials", "true");
//		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//		response.setHeader("Access-Control-Max-Age", "3600");
//		response
//				.setHeader("Access-Control-Allow-Headers", "Origin, origin, x-requested-with, authorization, " +
//						"Content-Type, Authorization, credential, X-XSRF-TOKEN");
//
//		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//			response.setStatus(HttpServletResponse.SC_OK);
//		} else {
//			chain.doFilter(req, resp);
//		}
//	}
//
//	@Override
//	public void destroy() {
//	}
//}
