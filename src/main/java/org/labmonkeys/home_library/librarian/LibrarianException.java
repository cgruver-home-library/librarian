package org.labmonkeys.home_library.librarian;

public class LibrarianException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -1135393627086893480L;

    public LibrarianException() {
    }

    public LibrarianException(String message) {
        super(message);
    }

    public LibrarianException(Throwable cause) {
        super(cause);
    }

    public LibrarianException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibrarianException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
