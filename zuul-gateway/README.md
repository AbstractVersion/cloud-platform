# Zuul-Reverse-Proxy

As mentioned [Zuul](https://github.com/Netflix/zuul) is an opensource framework provided by [Netfilix OSS](https://netflix.github.io/). Zuul is an L7 application gateway that provides capabilities for dynamic routing, monitoring, resiliency, security, and more.  Zuul works with or without Eureka discovery service. For static routing you can raise zuul instances and root the traffic to you microservices based on static network locations. In this case though we use the combination of Zuul, Eureka & Ribbon.

## Eureka Server.

Eureka is a [discovery service](https://www.getambassador.io/resources/service-discovery-microservices/) provided by [Netfilix OSS](https://netflix.github.io/). Once you have an Eureka instance up & running, you can register you microservices with it. The Eureka service keeps track of every service and all its instances. It provides periodical helthchecks and many more functionalities that make the system robast  fault tolerant. With Eureka you have a list of every service and all its instances in real-time.

## Zuul & Eureka.
Zuul service if registered with Eureka will periodically request this service-list. That way Zuul have knowledge over every component of the system. Zuul framework provides rooting functionalities, request filters etc. It is basically a naked proxy waiting for someone to add functionality over it. By defaut zuul redirects the traffic based on the name of the service (as it's registered on Eureka).

## Ribbon & Zuul

On top of [Zuul](https://github.com/Netflix/zuul) framework we have the Ribbon package. Ribbon is a dedicated client side load balancer provided by [Netfilix OSS](https://netflix.github.io/). Since zuul has a list of all the current services & instances ribbon can decide based on an algorithm (by default [Round Robin](https://en.wikipedia.org/wiki/Round-robin_scheduling)), in which service the request should be redirect to. 


![service-discovery][service-discovery]

[service-discovery]: service-discovery.jpg "service-discovery"


## JWT Validation

As previusely mentioned the system authenticates & authorizes requests using [JWT](https://jwt.io/introduction/) tokens. The JWT generation does not take place on the `zuul-gateway-service`, but zuul is the infastracture that verifies & validates the jwt token uppon each request. If the request comes in without a valid (expired, diff in signature etc.) jwt token the zuul will reject the request and it will return a `401 Forbitten` HTTP answer. As stated a client must follow the [authentication documentation](https://gitlab.akka.eu/Georgios.FIOTAKIS/3djuump-x-teams/-/blob/master/3djuump-x-teams-authentication.md) in order to retrieve a self-signed system token. 

### Zuul JWT validation.

Now lets take a look on how we extract and validate the JWT tokens from each request. Spring provides concepts such as request filters & Web security. We use a compination of those so as to filter each request that comes through zuul-proxy. In this case we use a filter called `OncePerRequestFilter`. This filter allows as to act before the redirection of the request to a different service. 

#### Token extraction & validation
```java
    @Slf4j
    @RequiredArgsConstructor
    @Component
    public class JwtRequestFilter extends OncePerRequestFilter {

        @Autowired
        private JwtUserDetailsService jwtUserDetailsService;
        @Autowired
        private JwtTokenUtil jwtTokenUtil;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws ServletException, IOException {
            final String requestTokenHeader = request.getHeader("Authorization");
            String username = null;
            String jwtToken = null;
            // JWT Token is in the form "Bearer token". Remove Bearer word and get
            // only the Token
            log.info("Decoding jwt token && extracting information");
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                    
    //                response.setHeader("user-session-id", username);
                } catch (IllegalArgumentException e) {
                    log.warn("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    log.warn("JWT Token has expired");
                    throw new IOException("JWT Token has expired");
                }
            } else {
                log.warn("JWT Token does not begin with Bearer String or Token is not present");
            }
            // Once we get the token validate it.
            log.info("Validating token information");
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("Request information validation from Session Service");
                SessionInfo userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                // if token is valid configure Spring Security to manually set
                // authentication
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            chain.doFilter(request, response);
        }
    }

```

> As we can see from the above code, we extract the JWT token from the Bearer Header, next we extract from the token the username & we verify that is not expired. 

> Class `JwtTokenUtil` models the functionalities of a Json Web Token. In this class you can finde the `@Autowired` field that holds the token signature. This signature is distrubuted by cloud-config-server. Apart from that on this class you can find basic functionalities such as username-extraction `getUsernameFromToken`, expiration date validation `getExpirationDateFromToken` etc.

#### Token information cross-validation

```java
    @Service
    public class JwtUserDetailsService implements UserDetailsService {

        @Autowired
        private SessionServiceRemoteRepository sessionRepo;
        private ObjectMapper mapper;

        public JwtUserDetailsService() {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        @Override
        public SessionInfo loadUserByUsername(String username) throws UsernameNotFoundException {
            try {
                System.out.println(username);
                ResponseEntity obj = sessionRepo.findSessionById(username);
                if (obj.getStatusCode() == HttpStatus.OK) {
                    return mapper.convertValue(obj.getBody(), SessionInfo.class);
                }
            } catch (Exception ex) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

```

> Final step of the validation is to retrive the corresponding session from the session-service. That way we corss validate the information. 

> If all the steps above passed succesfully, the request is redirected to the appropriate service through ribbon and the procedure we discussed earlier.

#### Filter declaration & Auth Witelist.

```java
    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        @Autowired
        private UserDetailsService jwtUserDetailsService;
        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            // configure AuthenticationManager so that it knows from where to load
            // user for matching credentials
            // Use BCryptPasswordEncoder
            auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            // We don't need CSRF for this example
            httpSecurity.csrf().disable()
                    // dont authenticate this particular request
                    .authorizeRequests()
                    .antMatchers(AUTH_WHITELIST).permitAll().
                    // all other requests need to be authenticated
                    anyRequest().authenticated().and().
                    // make sure we use stateless session; session won't be used to
                    // store user's state.
                    exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            // Add a filter to validate the tokens with every request
            httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        }

        private static final String[] AUTH_WHITELIST = {
            "/session-service/session/register"
        };
    }

```

> As you can see from the aboce code block, we register the `JwtRequestFilter` we previus demonstrate as main web-securtity configuration. This happens on the last line of the function `configure`.

> By default the security filter "cuts" every request on every subdomain of zuul-proxy. This is a problem because sometimes we need to expose some functionalities without authentication. To achive this we have register an `AUTH_WHITELIST`. **In this whitelist we include all the endpoints that we don't want to be filtered by the `JwtRequestFilter` and therefore they will be open for the public**. A good scenario as example is the registration endpoint, in which we don't expect the user to provide a jwt token.

## Other request filters
Apart from the Authentication filter we have creted 2 more filters which are essential for the system functionality. The `PostConstractRequestFilter` and the `PreRequestFilter`.

### PostConstractRequestFilter

This filter is responsible for the injection to the request headder of the parameter `request-trace-id`. Each request that is served by the system can be tracked through `Elastic Stack` infastracture. Once a request reaches zuul and gets redirected to the appropriate service, a chain reaction between different service takes place. We reffair to this chain reaction as **request lifecycle**. To monitor the **request lifecycle** of a specific request we use the [`Spring Cloud Sleuth`] (https://docs.spring.io/spring-cloud-sleuth/docs/current-SNAPSHOT/reference/html/) framwork. This framework was originally developed by spring cloud, but it provides packages accross different paltforms such as python Flask etc. Spring Cloud Sleuth does inject a request trace with which we can monitor the request logs accross its jurney within the microservice ecosystem. The `PostConstractRequestFilter` injects this request-trace-id after before we return the result of the request to the client. That way we can trace the request lifecycle.

```java
    @Slf4j
    @RequiredArgsConstructor
    public class PostConstractRequestFilter extends ZuulFilter {

        @Autowired
        private Tracer tracer;

        @Override
        public String filterType() {
            return "post";
        }

        @Override
        public int filterOrder() {
            return 1;
        }

        @Override
        public boolean shouldFilter() {
            return true;
        }

        @Override
        public Object run() {
            log.info("Injeacting request trace-id : {} on header", tracer.currentSpan().context().traceIdString());
            RequestContext ctx = RequestContext.getCurrentContext();
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setHeader("request-trace-id", tracer.currentSpan().context().traceIdString());
            return null;
        }

    }
```

> As we can see in the above code block the `filter-type` is defined as `post` which means that the filter will be triggered after the request finished its execution and before the request returns to the client.

> The function `run` states the functionality of the filter. In this function we inject the `request-trace-id` parameter to the response headder.

### PreRequestFilter

This filter is responsible for the injection to the request headder of the parameters `user-session-id`, `user-display-name` & `graph-token`. This filter improves the performance of the system since we don't need to find this information once the request leaves zuul with direction to a different microservice. For excample there are some services such as `clone-service` that require information such as the user corresponding `Microsoft Graph Token` in order to act on behalf of the user. If we had not include the graph-token on the request headder, the clone-service should have made 3 different microservice calls to retrieve it. Which would have major impact on the performance of the system. 

```java
    @Slf4j
    @RequiredArgsConstructor
    @Component
    public class PreRequestFilter extends ZuulFilter {

        @Autowired
        private JwtUserDetailsService jwtUserDetailsService;
        @Autowired
        private JwtTokenUtil jwtTokenUtil;

        @Override
        public String filterType() {
            return "pre";
        }

        @Override
        public int filterOrder() {
            return 1;
        }

        @Override
        public boolean shouldFilter() {
            return true;
        }

        @Override
        public Object run() {
            RequestContext ctx = RequestContext.getCurrentContext();
            HttpServletRequest request = ctx.getRequest();
            final String requestTokenHeader = request.getHeader("Authorization");
            log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

            String username = null;
            String jwtToken = null;
    //        // JWT Token is in the form "Bearer token". Remove Bearer word and get
    //        // only the Token
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                    SessionInfo userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                    ctx.addZuulRequestHeader("user-session-id", username);
                    ctx.addZuulRequestHeader("user-display-name", userDetails.getUsername());
                    log.info("injecting user trace on request headder : {}", username);
                    log.info("injecting microsoft token on request headers.");
                    ctx.addZuulRequestHeader("graph-token", userDetails.getAccessToken());

                } catch (IllegalArgumentException e) {
                    log.warn("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    log.warn("JWT Token has expired");
                }
            } else {
                log.warn("Skipping regisration request.");
            }
            //        log.info("injecting user session id : {} on request headers.", username);
            //        ctx.setSendZuulResponse(false);
            //        ctx.getResponse().setHeader("user-session-id", username);
            return null;
        }

    }
```
> The `filter-type` is defined as `pre` which means that the filter will be triggered before the request gets redirected on the appropriate microservice.

> The `zuulRequestHeadder` is a consept provided by Zuul framework. The functionality that provides is that the headder *leaves* as lon as the request is under execution. Once the request is executed the headders that are tagged as `zuulRequestHeadders` will be automatically removed by `zuul-gateway-service`.

> This filter is triggered after the request has verified & validate the self-signed-token (if the token is expired or whatever the pre-filter will not be triggered, isntead a `401 Forbitten` response will be returned). Thats the reasson we feel confortable exporting the information directely from the self-signed-token.

## Author & Licence

Original Author         :   [George Fiotakis](https://www.linkedin.com/in/george-fiotakis-320967159/). <br>
Contact Information     :   georgios.FIOTAKIS@akka.eu <br>
Contact Information     :   g.fiotakis@hotmail.com <br>

*This document and it's sub components & documents are Confidential INFORMATION which belongs to the group of AKKA Technologies & REAL FUSIO. Any
attempt of replication and distribution of this document without AKKA Technology's and REAL FUSIO's consent, will be persecuted legally.*

