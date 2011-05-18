package com.moisespsena.vraptor.ext.scan;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.scannotation.AnnotationDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.scan.ClasspathResolver;

/**
 * A Scannotation based Component Scanner
 * 
 * @author Moises P. Sena &lt;moisespsena@gmail.com&gt;
 * @since 3.2
 */
public class AnnotatedClassScanner {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory
			.getLogger(AnnotatedClassScanner.class);

	public Map<String, Set<String>> scan(ClasspathResolver resolver) {
		final URL webInfClasses = resolver.findWebInfClassesLocation();
		final List<String> basePackages = resolver.findBasePackages();

		Map<String, Set<String>> webInfClassesAnnotationMap = scanWebInfClasses(webInfClasses);
		Map<String, Set<String>> basePackagesAnnotationMap = scanBasePackages(basePackages);

		Map<String, Set<String>> results = new HashMap<String, Set<String>>();
		merge(results, basePackagesAnnotationMap);
		merge(results, webInfClassesAnnotationMap);

		return results;
	}

	private void merge(Map<String, Set<String>> results,
			Map<String, Set<String>> map) {
		Set<String> keys = map.keySet();

		for (String key : keys) {
			if (results.containsKey(key)) {
				Set<String> valueSet = results.get(key);
				valueSet.addAll(map.get(key));
			} else {
				results.put(key, map.get(key));
			}
		}
	}

	private Map<String, Set<String>> scanWebInfClasses(URL webInfClasses) {
		try {
			AnnotationDB db = createAnnotationDB();
			db.scanArchives(webInfClasses);
			return db.getAnnotationIndex();
		} catch (IOException e) {
			throw new ScannerException("Could not scan WEB-INF/classes", e);
		}
	}

	private Map<String, Set<String>> scanBasePackages(List<String> basePackages) {
		try {
			AnnotationDB db = createAnnotationDB();

			for (String basePackage : basePackages) {
				scanPackage(basePackage, db);
			}

			return db.getAnnotationIndex();
		} catch (IOException e) {
			throw new ScannerException("Could not scan base packages", e);
		}
	}

	private void scanPackage(String basePackage, AnnotationDB db)
			throws IOException {
		String resource = basePackage.replace('.', '/');
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
				.getResources(resource);
		if (!urls.hasMoreElements()) {
			logger.error("There's no occurence of package {} in classpath",
					basePackage);
			return;
		}
		do {
			URL url = urls.nextElement();

			String file = toFileName(resource, url);

			db.scanArchives(new URL(file));
		} while (urls.hasMoreElements());
	}

	private String toFileName(String resource, URL url) {
		String file = url.getFile();
		file = file.substring(0, file.length() - resource.length() - 1);
		if (file.charAt(file.length() - 1) == '!') {
			file = file.substring(0, file.length() - 1);
		}
		if (!file.startsWith("file:")) {
			file = "file:" + file;
		}
		return file;
	}

	private AnnotationDB createAnnotationDB() {
		AnnotationDB db = new AnnotationDB();
		db.setScanClassAnnotations(true);
		db.setScanFieldAnnotations(false);
		db.setScanMethodAnnotations(false);
		db.setScanParameterAnnotations(false);
		return db;
	}
}