package it.polito.ai.lab2;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;

import org.hibernate.*;

@WebFilter("/*")
public class HibernateSessionRequestFilter implements Filter {

	private SessionFactory sf = HibernateUtil.getSessionFactory();

	// Need a filter in order to make the beginTransaction the first operation
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Start a new session every time a request arrives, crea una sessione nuova se non ce ne è un'altra.
		Session s = sf.getCurrentSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			request.setAttribute("session", s);
			chain.doFilter(request, response);
			tx.commit();
		} catch (Throwable ex) {
			if (tx != null)
				tx.rollback();
			throw new ServletException(ex);
		} finally {
			if (s != null && s.isOpen()){
				// Close sessione if open
				s.close();
			}
			s = null;
		}

	}

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
