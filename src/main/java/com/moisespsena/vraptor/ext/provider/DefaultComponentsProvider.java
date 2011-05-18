package com.moisespsena.vraptor.ext.provider;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.ComponentRegistry;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.spring.SpringProvider;

import com.moisespsena.vraptor.ext.scan.AnnotatedClassMapper;

/**
 * Provedor de componentes defaults
 * 
 * @author Moises P. Sena &lt;moisespsena@gmail.com&gt;
 * @since 1.0
 */
public class DefaultComponentsProvider extends SpringProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultComponentsProvider.class);

	// mapa de classes anotadas
	private AnnotatedClassMapper annotatedClassMapper = new AnnotatedClassMapper();

	public DefaultComponentsProvider() {
		super();
	}

	@Override
	protected void registerCustomComponents(ComponentRegistry registry) {
		// obtem a lista de componentes customizados
		Set<String> customComponents = annotatedClassMapper
				.getClassSet(Component.class);
		// obtem a lista de componentes defaults
		Set<String> defaultComponents = annotatedClassMapper
				.getClassSet(DefaultComponent.class);

		if (logger.isDebugEnabled()) {
			logger.debug("Register default components");
		}

		Set<Class<?>> overrides = new HashSet<Class<?>>();

		try {
			// itera pelos componenes defaults
			for (String defaultComponent : defaultComponents) {
				Class<?> defaultComponentClass = Class
						.forName(defaultComponent);

				// itera pelos componentes customizados
				for (String customComponent : customComponents) {
					Class<?> customComponentClass = Class
							.forName(customComponent);

					// retorna a interface caso o componente customizado
					// implemente a interface do componente default
					Class<?> interfaceClass = getOverrideClass(
							defaultComponentClass, customComponentClass);

					// casa implemente
					if (interfaceClass != null) {
						// registra que o customComponent sobrescreve o default
						overrides.add(defaultComponentClass);

						if (logger.isDebugEnabled()) {
							logger.debug(
									"Register custom component class '{}' for '{}' interface",
									interfaceClass.getName(), customComponent);
						}

						// bloqueia a iteracao
						break;
					}
				}
			}

			// itera pelos componenes defaults novamente
			for (String defaultComponent : defaultComponents) {
				Class<?> defaultComponentClass = Class
						.forName(defaultComponent);

				// se este component default nao foi sobrecrito
				if (!overrides.contains(defaultComponentClass)) {
					// identifica a interface do custom component
					Class<?> interfaceClass = defaultComponentClass
							.getAnnotation(DefaultComponent.class).value();

					// registra o default componente como a implementacao da
					// interface definida no valor da anotacao @DefaultComponent
					registry.register(interfaceClass, defaultComponentClass);
				}
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private Class<?> getOverrideClass(Class<?> defaultComponentClass,
			Class<?> componentClass) {
		Class<?> interfaceClass = defaultComponentClass.getAnnotation(
				DefaultComponent.class).value();

		// o devault component nao implementa a interface definida no valor da
		// anotacao @DefaultComponent
		if (!interfaceClass.isAssignableFrom(defaultComponentClass)) {
			throw new InvalidDefaultComponentException(String.format(
					"The class '%s' has not be implements the '%s' interface",
					defaultComponentClass.getName(), interfaceClass.getName()));
		}

		// o componente customizado implementa a interface do componente default
		if (interfaceClass.isAssignableFrom(componentClass)) {
			// retora a interface
			return interfaceClass;
		}

		return null;
	}
}
