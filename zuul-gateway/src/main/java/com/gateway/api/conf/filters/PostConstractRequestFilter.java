package com.gateway.api.conf.filters;

import brave.Tracer;
import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

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
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        request.getHttpServletMapping();
//        ctx.
//        ctx.addZuulRequestHeader("trace-id", tracer.currentSpan().context().traceIdString());
        log.info("Injeacting request trace-id : {} on header", tracer.currentSpan().context().traceIdString());
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setSendZuulResponse(false);
        ctx.getResponse().setHeader("request-trace-id", tracer.currentSpan().context().traceIdString());
//        ctx.getResponse().setCharacterEncoding(CharsetConstants.CHARSET_UTF8);
        return null;
    }

}
