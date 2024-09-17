package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RestHeaderAuthFilter extends AbstractRestAuthFilter {
    public RestHeaderAuthFilter(RequestMatcher requiresAuthMatcher){
        super(requiresAuthMatcher);
    }

    public String getParameter(String parameterName, HttpServletRequest request){
        String value = request.getHeader(parameterName);
        return value == null ? "" : value;
    }
}
