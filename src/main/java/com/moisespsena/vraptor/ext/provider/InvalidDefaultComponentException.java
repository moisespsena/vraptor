/**
 * 
 */
package com.moisespsena.vraptor.ext.provider;

/**
 * 
 * @author Moises P. Sena &lt;moisespsena@gmail.com&gt;
 * @since 1.0 17/05/2011
 * 
 */
public class InvalidDefaultComponentException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public InvalidDefaultComponentException() {
	}

	/**
	 * @param message
	 */
	public InvalidDefaultComponentException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidDefaultComponentException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidDefaultComponentException(String message, Throwable cause) {
		super(message, cause);
	}

}
