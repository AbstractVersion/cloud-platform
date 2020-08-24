package com.gateway.api.conf.filters;

import com.gateway.api.conf.jwt.JwtTokenUtil;
import com.gateway.api.conf.jwt.JwtUserDetailsService;
import com.gateway.api.service.repository.template.SessionInfo;
import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
