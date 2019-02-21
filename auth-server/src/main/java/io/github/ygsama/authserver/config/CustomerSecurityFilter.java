package io.github.ygsama.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.annotation.PostConstruct;
import java.io.IOException;


@Component
public class CustomerSecurityFilter extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private CustomerInvocationSecurityMetadataSource securityMetadataSource;

    @Autowired
    private CustomerAccessDecisionManager customerAccessDecisionManager;


    @PostConstruct
    public void init(){
        super.setAccessDecisionManager(customerAccessDecisionManager);
    }


    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        FilterInvocation fi = new FilterInvocation( request, response, chain );
        System.out.println("filter..........................");
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try{
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }finally{
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("----------filter.init()");
    }

    @Override
    public void destroy() {
        System.out.println("----------filter.destroy()");
    }
}