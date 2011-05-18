/**
 * 
 */
package com.moisespsena.vraptor.ext.provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.caelum.vraptor.ComponentRegistry;
import br.com.caelum.vraptor.ioc.Component;

/**
 * Marca um componente como Default
 * 
 * <p>
 * Quando uma classe é anotada com @{@link DefaultComponent}, esta classe será
 * registrada como uma implementação da interface @{@link DefaultComponent#value()} no {@link ComponentRegistry} caso nao exista
 * nenhuma outra classe que também implemente esta mesma interface e seja
 * anotada com um @{@link Component}.
 * </p>
 * 
 * @author Moises P. Sena &lt;moisespsena@gmail.com&gt;
 * @since 1.0
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface DefaultComponent {
	Class<?> value();
}
