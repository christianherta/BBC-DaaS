package de.bbcdaas.disambiguationweb.filter;

import java.io.IOException;
import javax.servlet.*;
/**
 * 
 * @author Robert Illers
 */
public class UTF8Filter implements Filter {
	
	/**
	 * 
	 */
	@Override
    public void destroy() {}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException 
	 */
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
		request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
        chain.doFilter(request, response);
    }

	/**
	 * 
	 * @param filterConfig
	 * @throws ServletException 
	 */
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {}
}
