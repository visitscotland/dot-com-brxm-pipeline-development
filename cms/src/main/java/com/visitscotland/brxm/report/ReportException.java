package com.visitscotland.brxm.report;

public class ReportException extends RuntimeException{

        public ReportException() {

        }

        public ReportException(String message) {
            super(message);
        }

        public ReportException(String message, Throwable ex) {
            super(message, ex);
        }

        public ReportException(Throwable ex) {
            super(ex);
        }
}
