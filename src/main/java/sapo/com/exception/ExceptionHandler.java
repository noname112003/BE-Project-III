package sapo.com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sapo.com.model.dto.response.ResponseObject;
import sapo.com.model.entity.Order;

import java.util.HashMap;
import java.util.Map;

// inh lớp naày  ngoại lệ chung
@RestControllerAdvice
public class ExceptionHandler extends RuntimeException {
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> handleGeneralException(Exception exception){
        return ResponseEntity.internalServerError().body(
                ResponseObject.builder()
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(exception.getMessage())
                        .build()
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError error = ex.getBindingResult().getFieldErrors().get(0);

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put(error.getField(), error.getDefaultMessage());

        ResponseObject response = new ResponseObject(ex.getMessage(), errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ResponseObject> handleDataConflictException(DataConflictException ex) {
        ResponseObject response = new ResponseObject(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseObject> handleResourceNotFound(ResourceNotFoundException ex) {
        ResponseObject response = new ResponseObject(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Xử lý ngoại lệ CustomerNotFoundException
    @org.springframework.web.bind.annotation.ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<?> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        // Tạo thông báo lỗi
        String errorMessage = ex.getMessage();

        // Trả về thông báo lỗi và mã trạng thái 404 NOT FOUND
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleCustomerNotFoundException(OrderNotFoundException ex) {
        // Tạo thông báo lỗi
        String errorMessage = ex.getMessage();

        // Trả về thông báo lỗi và mã trạng thái 404 NOT FOUND
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

}
