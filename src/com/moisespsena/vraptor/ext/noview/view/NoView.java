/**
 * 
 */
package com.moisespsena.vraptor.ext.noview.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotatacao que identifica que o metodo anotado nao havera uma view 
 * (arquivo .jsp, por exemplo) correspondente.
 * 
 * Se uma classe de controle for anotada com {@link NoView}, significa que 
 * nenhum de seus metodos publicos possuira uma view correspondente.
 * 
 * @author Moises P. Sena
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface NoView {

}
