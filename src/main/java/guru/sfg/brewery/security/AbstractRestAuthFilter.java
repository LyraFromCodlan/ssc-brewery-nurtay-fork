package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AbstractRestAuthFilter extends AbstractAuthenticationProcessingFilter {
    public AbstractRestAuthFilter(RequestMatcher requiresAuthMatcher){
        super(requiresAuthMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String username = getParameter("Api-Key",request);
        String pwd = getParameter("Api-Secret",request);

        log.info(username);
        log.info(pwd);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, pwd);

        if(!StringUtils.isEmpty(username))
            return this.getAuthenticationManager().authenticate(token);
        else
            return null;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Request is to process authentication");
        }

        try{
            Authentication authResult = this.attemptAuthentication(request, response);
            if (authResult != null)
                successfulAuthentication(request, response, chain, authResult);
            else
                chain.doFilter(request, response);
        }catch (AuthenticationException e){
            log.info("Authentication failed. User hadn't passed auth.", e);
            unsuccessfulAuthentication(request,response,e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        if (log.isDebugEnabled()) {
            log.debug("Authentication request failed: " + failed.toString(), failed);
            log.debug("Updated SecurityContextHolder to contain null Authentication");
        }

        response.sendError(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    public String getParameter(String parameterName, HttpServletRequest request){
        String value = request.getHeader(parameterName);
        return value == null ? "" : value;
    }
}
