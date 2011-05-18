package com.moisespsena.vraptor.ext.scan;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import br.com.caelum.vraptor.scan.StandaloneClasspathResolver;

/**
 * A Annotated class Mapper
 * 
 * @author Moises P. Sena &lt;moisespsena@gmail.com&gt;
 * @since 1.0 18/05/2011
 * 
 */
public class AnnotatedClassMapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private final AnnotatedClassScanner scanner;
	private Map<String, Set<String>> cache;

	public AnnotatedClassMapper() {
		scanner = new AnnotatedClassScanner();
		StandaloneClasspathResolver resolver = new StandaloneClasspathResolver();
		cache = scanner.scan(resolver);
	}

	public Set<String> getClassSet(String annotatedClassName) {
		if (cache.containsKey(annotatedClassName)) {
			return cache.get(annotatedClassName);
		} else {
			return null;
		}
	}

	public Set<String> getClassSet(Class<? extends Annotation> annotatedClass) {
		return getClassSet(annotatedClass.getName());
	}
}
