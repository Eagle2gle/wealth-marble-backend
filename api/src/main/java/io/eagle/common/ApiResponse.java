package io.eagle.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";

    private String status;
    private T data;
    private String message;

    public static <T> ApiResponse<T> createSuccess(T data) {
        return new ApiResponse<>(SUCCESS_STATUS, data, null);
    }

    public static ApiResponse<?> createSuccessWithNoContent() {
        return new ApiResponse<>(SUCCESS_STATUS, null, null);
    }

    public static ApiResponse<?> createError(String message) {
        return new ApiResponse<>(ERROR_STATUS, null, message);
    }

    public static <T> ApiResponse<T> createErrorWithContent(T data, String message) {
        return new ApiResponse<>(ERROR_STATUS, data, message);
    }

    private ApiResponse(String status, T data, String message) {
        this.status = status;
        if(data instanceof List){
            this.data = (T) new ListToClass(data);
        } else {
            this.data = data;
        }
        this.message = message;
    }

    @RequiredArgsConstructor
    class ListToClass{
        public final T result;
    }

}
