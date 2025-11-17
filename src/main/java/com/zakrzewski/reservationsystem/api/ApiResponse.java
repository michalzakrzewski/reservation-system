package com.zakrzewski.reservationsystem.api;

public record ApiResponse<T>(
        boolean success,
        int statusCode,
        T data,
        String errorDetails
) {
    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    public static class ApiResponseBuilder<T> {
        private boolean success;
        private int statusCode;
        private T data;
        private String errorDetails;

        public ApiResponseBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public ApiResponseBuilder<T> statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder<T> errorDetails(String errorDetails) {
            this.errorDetails = errorDetails;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(success, statusCode, data, errorDetails);
        }
    }
}
