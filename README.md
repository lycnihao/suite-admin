版本
Java (1.8+)
Spring Boot (2.7.3)
Spring Security (5.7.3)

## 一、介绍Security
官方原话“Spring Security is a framework that provides authentication, authorization, and protection against common attacks”即"Spring Security 是一个提供身份验证、授权和防止常见攻击的框架"。它是Spring提供的一个安全框架，可以根据使用者需要定制相关验证授权操作，配合Spring Boot可以快速开发一套完善的权限系统。

## 二、快速上手

* 创建一个Spring Boot项目并导入如下依赖或 [点击下载示例代码](https://start.spring.io/starter.zip?type=maven-project&language=java&packaging=jar&jvmVersion=1.8&groupId=example&artifactId=hello-security&name=hello-security&description=Hello%20Security&packageName=example.hello-security&dependencies=web,security)
``` xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
```

* 运行 Spring Boot 应用程序

若是正确启动了，可以看到 Spring Security 生成了一段默认密码。
```
...
2022-09-13 23:56:07.841  WARN 19924 --- [           main] .s.s.UserDetailsServiceAutoConfiguration : 

Using generated security password: 70a36ac6-70c1-4f72-822c-71165988c56e
...
```
* 访问 http://localhost:8080/ 会跳转到`/login`登录页面，输入账号(user)密码(控制台自动生成的密码)以继续访问。

Srping Security主要解决的问题是安全访问控制，其实现原理是通过Filter对进入系统的请求进行拦截。当初始化Spring Security时，它创建了一个名为 springSecurityFilterChain的Servlet 过滤器，负责程序中的所以安全控制。

## 三、基本原理

### DelegatingFilterProxy
从必要知识里我们知道了Filter的工作原理，在Spring中使用自定义的Filter有个问题那就是Filter必须在Servlet容器启动前就注册好，但是Spring使用ContextLoaderListener来加载Spring Bean，于是设计了DelegatingFilterProxy。本质上来说DelegatingFilterProxy就是一个Filter，其间接实现了Filter接口，它嵌入在Servlet Filter Chain中，但是在doFilter中其实调用的从Spring 容器中获取到的代理Filter的实现类delegate。
![delegating-filter-proxy](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/filterchainproxy.png)

### FilterChainProxy和SecurityFilterChain

FilterChainProxy 是 Spring Security 提供的一个特殊 Filter，DelegatingFilterProxy并不是直接实例化和调用Spring Security Filter，而是构建了一个FilterChainProxy，当有请求进来就会去执行doFilter方法调用SecurityFilterChain所包含的各个Filter，同时 这些Filter作为Bean被Spring管理，它是Spring Security使用的核心。
![filter-chain-proxy](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/securityfilterchain.png)

此外，SecurityFilterChain 提供了更大的灵活性，Servlet容器中，仅根据URL调用过滤器。 但是，FilterChainProxy可以利用RequestMatcher接口，根据HttpServletRequest中的任何内容确定调用，比原生的Servlet更灵活，此外，FilterChainProxy可以构建多条SecurityFilterChain，你的应用程序可以为不同的情况提供完全独立的配置，如下图所示。![filter-chain-proxy](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/multi-securityfilterchain.png)

### 过滤器链中主要的几个过滤器及其作用
1. SecurityContextPersistenceFilter ：这个Filter是整个拦截过程的入口，会在请求开始时从配置好的 SecurityContextRepository 中获取 SecurityContext，然后把它设置给 SecurityContextHolder。在请求完成后将 SecurityContextHolder 持有的 SecurityContext 再保存到配置好的 SecurityContextRepository，同时清除 securityContextHolder 所持有的 SecurityContext。
2. UsernamePasswordAuthenticationFilter ：用于处理来自表单提交的认证。该表单必须提供对应的用户名和密码，其内部还有登录成功或失败后进行处理的 AuthenticationSuccessHandler 和 AuthenticationFailureHandler，这些都可以根据需求做相关改变；。
3. LogoutFilter：用来处理实现用户登出和清除认证信息工作，登出成功后执行LogoutSuccessHandler，这里可以自定义实现一些功能。
4. FilterSecurityInterceptor： 是用于保护web资源的，使用AccessDecisionManager对当前用户进行授权访问
5. ExceptionTranslationFilter： 能够捕获来自 FilterChain 所有的异常，并进行处理。但是它只会处理两类异常： AuthenticationException 和 AccessDeniedException，其它的异常它会继续抛出。

### 异常处理
![exception-translation-filter](https://segmentfault.com/img/bVbFsdo)

1. 首先，ExceptionTranslationFilter 调用 FilterChain.doFilter(request, response) 来调用应用程序的其余部分。
2. 如果用户未通过身份验证或者是 AuthenticationException，则启动身份验证。
	- SecurityContextHolder 被清除
	- HttpServletRequest 保存在 RequestCache 中。当用户成功认证后，RequestCache 用于重放原始请求。
	- AuthenticationEntryPoint 用于启动身份验证。例如，它可能重定向到登录页面或BASIC认证等。
	- 否则，如果是 AccessDeniedException，则拒绝访问。调用 AccessDeniedHandler 来处理拒绝访问。

### 表单登录


以上示例在未授权的情况下访问会经过以下安全过滤器：

```
Security filter chain: [
  DisableEncodeUrlFilter
  WebAsyncManagerIntegrationFilter
  SecurityContextPersistenceFilter
  HeaderWriterFilter
  CsrfFilter
  LogoutFilter
  UsernamePasswordAuthenticationFilter
  DefaultLoginPageGeneratingFilter
  DefaultLogoutPageGeneratingFilter
  BasicAuthenticationFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  FilterSecurityInterceptor
]
```
当没有登录的时候默认是anonymousUser匿名用户，经过一些列过滤器处理后，最后由FilterSecurityInterceptor进行权限校验[授权](https://docs.spring.io/spring-security/reference/servlet/authorization/architecture.html)，AccessDecisionManager进行授权投票，匿名用户不允许访问该接口，请求被拒绝重定向到登录页面，接着由DefaultLoginPageGeneratingFilter（自定义表单则不会初始化这个Filter）生成默认登录界面输出到浏览器。登录时经过UsernamePasswordAuthenticationFilter，只要用户请求满足该过滤器要求，则认证成功，接着是授权成功访问通过。

每个过滤器都有不同的功能，组织在一起形成了强大的安全体系，你可以在过滤链中自定义过滤器，里面的逻辑我就不一一细说了没啥好讲的，[官方文档](https://spring.io/projects/spring-security#learn)中都有介绍。下面讲讲我自己的一些实现吧。

## 四、我实现思路是什么，我是怎么实现的

背景：拓展Spring Security实现基于Token的API认证授权基础程序

> 采用的广为熟知的RBAC 模型，基于角色的访问控制(Role-Based Access Control)

拓展点：

- 禁用CSRF（有个过滤器校验会报错）、会话管理设置为无状态STATELESS（因为我们要自定义处理登录注销逻辑）
- 自定义**UserDetailsService** 重写loadUserByUsername方法，从数据库中读取账号信息
- 添加自定义Token认证过滤器
- 自定义登录成功和失败处理器successHandler与failureHandler
- 自定义注销处理器LogoutSuccessHandler
- 自定义异常处理器AuthenticationEntryPoint与AccessDeniedHandler
- 自定义AuthorizationManager

开发调试可以设置一下日志输出级别，这样能助于我们更快的分析和排查问题：

```yaml
logging:
  level:
    org.springframework.web: trace
    org.springframework.security: trace
```
另外 @EnableWebSecurity 这个注解debug属性设置为true也能看到更多的日志信息，这对我们很有帮助。

### SecurityConfiguration 核心配置类
```java
@EnableWebSecurity(debug = false)
public class SecurityConfiguration {

	private final AppUserDetailsService userDetailsService;
	private final AbstractStringCacheStore cacheStore;
	private final AuthenticationTokenFilter authenticationTokenFilter;
	private final PermissionAuthorizationManager<RequestAuthorizationContext> permissionAuthorizationManager;

	public SecurityConfiguration(AppUserDetailsService userDetailsService, AbstractStringCacheStore cacheStore, PermissionAuthorizationManager<RequestAuthorizationContext> permissionAuthorizationManager) {
		this.userDetailsService = userDetailsService;
		this.cacheStore = cacheStore;
		this.permissionAuthorizationManager = permissionAuthorizationManager;
		this.authenticationTokenFilter = new AuthenticationTokenFilter(cacheStore, userDetailsService);
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				// Spring Security should completely ignore URLs starting with /resources/
				.antMatchers("/resources/**");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/user/info").authenticated() // 需要认证
				.anyRequest().access(permissionAuthorizationManager) // 动态权限认证
				.and()
					.userDetailsService(userDetailsService)
					.formLogin()
					.permitAll()
					.successHandler(new CustomizeAuthenticationSuccessHandler(cacheStore))
					.failureHandler(new AuthenticationEntryPointFailureHandler(new CustomizeAuthenticationEntryPoint()))
				.and()
					.logout()
					.logoutSuccessHandler(new CustomizeLogoutSuccessHandler(cacheStore))
				.and()
					.exceptionHandling()
					.authenticationEntryPoint(new CustomizeAuthenticationEntryPoint())
					.accessDeniedHandler(new CustomizeAccessDeniedHandler())
				.and()
					.csrf().disable()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
					.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
					.addFilterBefore(authenticationTokenFilter, LogoutFilter.class);
		return http.build();
	}
```

释义:
AuthenticationTokenFilter-Token认证过滤器（除了自定义开放的接口外都会被调用）
PermissionAuthorizationManager-动态权限授权管理器（基于角色与资源权限表）
CustomizeAuthenticationSuccessHandler-登录处理器（登录成功后被调用用于生成Token）
CustomizeLogoutSuccessHandler-注销处理器（注销成功后被调用用于清除Toekn）
CustomizeAuthenticationEntryPoint-认证失败处理器（认证出现异常被调用）
CustomizeAccessDeniedHandler-授权失败处理器（授权出现异常被调用，如权限不足以访问某接口）
AbstractStringCacheStore-缓存类（用于缓存Token）

### CustomizeAuthenticationSuccessHandler 登录处理器

```java
@Slf4j
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final AbstractStringCacheStore cacheStore;

	/**
	 * Expired seconds.
	 */
	private static final int ACCESS_TOKEN_EXPIRED_SECONDS = 24 * 3600;

	private static final int REFRESH_TOKEN_EXPIRED_DAYS = 30;

	public CustomizeAuthenticationSuccessHandler(AbstractStringCacheStore cacheStore) {
		this.cacheStore = cacheStore;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// Generate new token
		AuthToken token = new AuthToken();
		token.setAccessToken(BottleUtils.randomUUIDWithoutDash());
		token.setExpiredIn(ACCESS_TOKEN_EXPIRED_SECONDS);
		token.setRefreshToken(BottleUtils.randomUUIDWithoutDash());
		// Cache those tokens, just for clearing
		cacheStore.putAny(SecurityUtils.buildAccessTokenKey(userDetails), token.getAccessToken(),
				ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
		cacheStore.putAny(SecurityUtils.buildRefreshTokenKey(userDetails), token.getRefreshToken(),
				REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

		// Cache those tokens with user id
		cacheStore.putAny(SecurityUtils.buildTokenAccessKey(token.getAccessToken()), userDetails.getUserId(),
				ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
		cacheStore.putAny(SecurityUtils.buildTokenRefreshKey(token.getRefreshToken()), userDetails.getUserId(),
				REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(BaseResponse.ok("登录成功！", token)));

	}
```

### LogoutSuccessHandler 注销处理器

```java
@Slf4j
public class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {

	private final AbstractStringCacheStore cacheStore;

	public CustomizeLogoutSuccessHandler(AbstractStringCacheStore cacheStore) {
		this.cacheStore = cacheStore;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		if (Objects.isNull(authentication)) {
			return;
		}

		AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

		// Clear access token
		cacheStore.getAny(SecurityUtils.buildAccessTokenKey(userDetails), String.class)
				.ifPresent(accessToken -> {
					// Delete token
					cacheStore.delete(SecurityUtils.buildTokenAccessKey(accessToken));
					cacheStore.delete(SecurityUtils.buildAccessTokenKey(userDetails));
				});

		// Clear refresh token
		cacheStore.getAny(SecurityUtils.buildRefreshTokenKey(userDetails), String.class)
				.ifPresent(refreshToken -> {
					cacheStore.delete(SecurityUtils.buildTokenRefreshKey(refreshToken));
					cacheStore.delete(SecurityUtils.buildRefreshTokenKey(userDetails));
				});

		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(BaseResponse.ok("登出成功！", null)));

		log.info("You have been logged out, looking forward to your next visit!");
	}
}
```

### AuthenticationTokenFilter Token认证过滤器

```java
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

	private static final String AUTHENTICATION_SCHEME_BEARER = "Bearer";

	private final AbstractStringCacheStore cacheStore;
	private final AppUserDetailsService appUserDetailsService;

	public AuthenticationTokenFilter(AbstractStringCacheStore cacheStore, AppUserDetailsService appUserDetailsService) {
		this.cacheStore = cacheStore;
		this.appUserDetailsService = appUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// Get token from request header
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (!StringUtils.hasText(accessToken)) {
			// Do filter
			filterChain.doFilter(request, response);
			return;
		}

		if (!StringUtils.startsWithIgnoreCase(accessToken, AUTHENTICATION_SCHEME_BEARER)) {
			throw new BadCredentialsException("Token 必须以bearer开头");
		}
		if (accessToken.equalsIgnoreCase(AUTHENTICATION_SCHEME_BEARER)) {
			throw new BadCredentialsException("Token 不能为空");
		}

		// Get token body
		accessToken = accessToken.substring(AUTHENTICATION_SCHEME_BEARER.length() + 1);

		Optional<Long> optionalUserId = cacheStore.getAny(SecurityUtils.buildTokenAccessKey(accessToken), Long.class);
		if (!optionalUserId.isPresent()) {
			log.debug("Token 已过期或不存在 [{}]", accessToken);
			filterChain.doFilter(request, response);
			return;
		}

		UserDetails userDetails = appUserDetailsService.loadUserById(optionalUserId.get());
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Do filter
		filterChain.doFilter(request, response);
	}

}
```

### CustomizeAuthenticationEntryPoint 认证异常处理器
```java
@Slf4j
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		BaseResponse<Object> errorDetail = handleBaseException(authException);
		errorDetail.setData(Collections.singletonMap("uri", request.getRequestURI()));
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(errorDetail));
	}

	private BaseResponse<Object> handleBaseException(Throwable t) {
		Assert.notNull(t, "Throwable must not be null");

		BaseResponse<Object> errorDetail = new BaseResponse<>();
		errorDetail.setStatus(HttpStatus.UNAUTHORIZED.value());

		if (log.isDebugEnabled()) {
			errorDetail.setDevMessage(ExceptionUtils.getStackTrace(t));
		}

		if (t instanceof AccountExpiredException){
			errorDetail.setMessage("账户过期");
		} else if (t instanceof DisabledException){
			errorDetail.setMessage("账号被禁用");
		} else if (t instanceof LockedException){
			errorDetail.setMessage("账户被锁定");
		} else if (t instanceof AuthenticationCredentialsNotFoundException){
			errorDetail.setMessage("用户身份凭证未找到");
		} else if (t instanceof AuthenticationServiceException){
			errorDetail.setMessage("用户身份认证服务异常");
		} else if (t instanceof BadCredentialsException){
			errorDetail.setMessage(t.getMessage());
		} else {
			errorDetail.setMessage("访问未授权");
		}

		return errorDetail;
	}
}
```

### CustomizeAccessDeniedHandler 授权异常
```java
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		BaseResponse<Object> errorDetail = new BaseResponse<>();
		errorDetail.setStatus(HttpStatus.FORBIDDEN.value());
		errorDetail.setMessage("禁止访问");
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(JsonUtils.objectToJson(errorDetail));
	}

}
```

### PermissionAuthorizationManager 动态权限授权管理
```java
@Slf4j
@Component
public class PermissionAuthorizationManager<T> implements AuthorizationManager<T> {

	private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	private final PermissionService permissionService;

	public PermissionAuthorizationManager(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
		// Determines if the current user is authorized by evaluating if the
		boolean granted = isGranted(authentication.get());
		if (!granted) {
			return new AuthorizationDecision(false);
		}

		Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
		Set<String> authority = authorities
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());

		log.debug("username [{}] hav roles:[{}]", authentication.get().getName(), authority);

		RequestAuthorizationContext requestAuthorizationContext = (RequestAuthorizationContext)object;
		String servletPath = requestAuthorizationContext.getRequest().getRequestURI();
		log.debug("access url:{}", servletPath);

		AppUserDetails userDetails = (AppUserDetails)authentication.get().getPrincipal();
		List<Long> roleIds = userDetails.getRoles().stream().map(Role::getId).collect(Collectors.toList());
		List<Permission> permissions = permissionService.listByRoleIds(roleIds);
		boolean agreeFlag = permissions.stream()
				.anyMatch(permission -> isRouter(permission) && permission.getUrl().equals(servletPath));
		log.debug("check result:{}", agreeFlag);
		return new AuthorizationDecision(agreeFlag);
	}

	private boolean isGranted(Authentication authentication) {
		return authentication != null && isNotAnonymous(authentication) && authentication.isAuthenticated();
	}

	private boolean isNotAnonymous(Authentication authentication) {
		return !this.trustResolver.isAnonymous(authentication);
	}

	private boolean isRouter(Permission permission) {
		return "1".equals(permission.getType());
	}

}
```

## 五、示例

> 登录成功

```
POST /login?username=user&password=123456
Host: localhost:8080

response:
{
    "status": 200,
    "message": "登录成功！",
    "devMessage": null,
    "data": {
        "access_token": "8430064e7d9b497c8b786a33b0524bc5",
        "expired_in": 86400,
        "refresh_token": "8d2c6fb3489b47389a65cbf79f732f9a"
    }
}
```

#### 登录失败

```
POST /login?username=user&password=123
Host: localhost:8080

response:
{
    "status": 401,
    "message": "用户名或密码错误",
    "devMessage": "org.springframework.security.authentication.BadCredentialsException: 用户名或密码错误...",
    "data": {
        "uri": "/login"
    }
}
```

#### 登录注销

```
POST /logout
Host: localhost:8080
Authorization: Bearer b6422e3462224126a67f876b5f1b3a1e

response:
{
    "status": 200,
    "message": "登出成功！",
    "devMessage": null,
    "data": null
}
```

#### 未登录或Token过期

```
POST /logout
Host: localhost:8080
Authorization: Bearer b6422e3462224126a67f876b5f1b3a1e

response:
{
    "status": 401,
    "message": "访问未授权",
    "devMessage": "org.springframework.security.authentication.InsufficientAuthenticationException: Full authentication is required to access this resource...",
    "data": {
        "uri": "/user/info"
    }
}
```

#### 权限不足

```json
GET /user/info
Host: localhost:8080
Authorization: Bearer f7a542c4899a4e6ea5039002a8f19110

response:
{
    "status": 403,
    "message": "禁止访问",
    "devMessage": null,
    "data": null
}
```

## 小结
好了，就分享到这里了，希望对大家有所帮助，另外如有理解错误的地方请多多指教。
Spring Security还有很多值得探索的功能，继续学习吧~

官方文档：[spring-security](https://spring.io/projects/spring-security#learn)
项目地址：[gitee](https://gitee.com/lycnihao/bottle-admin)
