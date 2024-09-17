package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RestUrlAuthFilter extends AbstractRestAuthFilter {
    public RestUrlAuthFilter(RequestMatcher requiresAuthMatcher){
        super(requiresAuthMatcher);
    }

    @Override
    public String getParameter(String parameterName, HttpServletRequest request){
        String value = request.getParameter(parameterName);
        return value == null ? "" : value;
    }
}
