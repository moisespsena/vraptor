package com.moisespsena.vraptor.ext.noview.view;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.DefaultPageResult;
import br.com.caelum.vraptor.view.PathResolver;

/**
 * Sobrescreve o {@link DefaultPageResult} para suportar metodos 
 * anotados com {@link NoView}
 * 
 * Veja:
 * 
 * <pre><code>
 * public class MyController {
 * 
 *  \@NoView
 *  \@Delete("/user/{user.id}")
 * 	public void removeUser(User user) {
 *		// ..
 *  	// userDao.remove(user)
 *   
 *  }
 * 
 * }
 * </code></pre>
 * 
 * Neste exemplo, ao ser processado pelo vraptor, o metodo MyController.removeUser
 * nao exijira que haja uma view (um arquivo .jsp, por exemplo) que
 * corresponda a este metodo.
 * 
 * A anotacao na classe poderia ser utilizada para marcar todos os metodos 
 * do Controller:
 * 
 * <pre><code>
 * \@NoView
 * public class MyController {
 * 
 *  \@Delete("/user/{user.id}")
 * 	public void removeUser(User user) {
 *		// ..
 *  	// userDao.remove(user)
 *  }
 * 
 *  \@Post("/user")
 * 	public void addUser(User user) {
 *		// ..
 *  	// userDao.insert(user)
 *		// ..
 *  }
 * 
 * }
 * </code></pre>
 * 
 * @author Moises P. Sena
 */
@Component
@RequestScoped
public class PageResult extends DefaultPageResult {
	private static final Logger logger = LoggerFactory
			.getLogger(PageResult.class);
	private final ResourceMethod method;

	public PageResult(MutableRequest req, HttpServletResponse res,
			MethodInfo requestInfo, PathResolver resolver, Proxifier proxifier) {
		super(req, res, requestInfo, resolver, proxifier);
		this.method = requestInfo.getResourceMethod();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.caelum.vraptor.view.DefaultPageResult#defaultView()
	 */
	@Override
	public void defaultView() {
		/**
		 * Se havera processamento da View
		 */
		boolean noView = false;
		if (method.getMethod().isAnnotationPresent(NoView.class)) {
			noView = true;
		} else if (method.getResource().getType()
				.isAnnotationPresent(NoView.class)) {
			noView = true;
		}

		if (noView) {
			logger.debug("The method {} does not reder any view", method);
		}
		else {
			super.defaultView();
		}
	}

}
