package com.moisespsena.vraptor.ext.scan;
/**
 * Exceção disparada por {@link AnnotatedClassScanner}
 * 
 * @author Moises P. Sena &lt;moisespsena@gmail.com&gt;
 * @since 3.2
 */
public class ScannerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ScannerException() {
	}

	public ScannerException(String message) {
		super(message);
	}

	public ScannerException(Throwable cause) {
		super(cause);
	}

	public ScannerException(String message, Throwable cause) {
		super(message, cause);
	}

}
