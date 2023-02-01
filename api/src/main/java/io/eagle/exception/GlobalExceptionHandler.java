package io.eagle.exception;

import io.eagle.common.ApiResponse;
import io.eagle.exception.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    /*
    * API 요청 runtime 에러가
    * */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(ApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn(errorCode.getMessage());
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ApiResponse<?>> handleS3UploadException(S3Exception e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn(makeStackTraceToString(e));
        return handleExceptionInternal(errorCode);
    }

    /*
     * Request parameter가 올바르지 못한 경우 에러
     */
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    protected ResponseEntity<Object> handleMissingRequestParamException(Exception e, HttpServletRequest request) {
//        ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
//        log.warn("[{}] Request Parameter 이상..", request.getRequestURI() );
//        return handleExceptionInternal(errorCode);
//    }
//    @Override
//    public ResponseEntity<Object> handleMissingServletRequestParameter(
//            MissingServletRequestParameterException e,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request){
//        ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
//        log.warn("Request Parameter 이상..");
//        return handleExceptionInternal(errorCode);
//    }

    /*
     * Request parameter DTO 검증 에러
     */
    @Override
    public ResponseEntity handleBindException(
            BindException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        String message = makeBindingErrorMessage(ex.getBindingResult());
        log.warn("Request Parameter 이상.. {}",  message );
        return handleExceptionInternal(errorCode, message);
    }
//
//    /*
//     * Request Body 검증 에러
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleRequestBodyValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
//        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;
//        String message = makeBindingErrorMessage(e.getBindingResult());
//        log.warn("[{}] Request Body 이상.. {}", request.getRequestURL(), message );
//        return handleExceptionInternal(errorCode, message);
//    }
//    @Override
//    public ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException e,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request) {
//        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;
//        String message = makeBindingErrorMessage(e.getBindingResult());
//        log.warn("Request Body 이상.. {}", message );
//        return handleExceptionInternal(errorCode, message);
//    }

    /*
     * 이외 에러
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnExpectedException(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error(makeStackTraceToString(e));
        return handleExceptionInternal(errorCode);
    }
    private ResponseEntity<ApiResponse<?>> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.createError(errorCode.getMessage()));
    }

    private ResponseEntity<ApiResponse<?>> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.createError(errorCode.getMessage()));
    }

    protected String makeBindingErrorMessage(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append("[");
            stringBuilder.append(fieldError.getField()).append("] ");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(" 입력 값 : ");
            stringBuilder.append(fieldError.getRejectedValue());
            stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    private String makeStackTraceToString(Exception e) {
        StringBuffer errorBuff = new StringBuffer();
        errorBuff.append(e.getMessage()).append("\n");
        for (StackTraceElement element : e.getStackTrace()){
            errorBuff.append("\tat ").append(element).append("\n");
        }
        return errorBuff.toString();
    }
}
