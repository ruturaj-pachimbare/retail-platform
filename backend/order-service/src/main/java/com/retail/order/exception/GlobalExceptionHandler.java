package com.retail.order.exception;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*;
import java.time.Instant; import java.util.*; import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(OrderNotFoundException.class) public ResponseEntity<Map<String,Object>> notFound(Exception e){return body(HttpStatus.NOT_FOUND,e.getMessage());}
 @ExceptionHandler({OrderOperationException.class,IllegalStateException.class}) public ResponseEntity<Map<String,Object>> conflict(Exception e){return body(HttpStatus.CONFLICT,e.getMessage());}
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<Map<String,Object>> invalid(MethodArgumentNotValidException e){
  String m=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).collect(Collectors.joining("; "));return body(HttpStatus.BAD_REQUEST,m);
 }
 private ResponseEntity<Map<String,Object>> body(HttpStatus s,String m){Map<String,Object>b=new LinkedHashMap<>();b.put("timestamp",Instant.now());b.put("status",s.value());b.put("error",s.getReasonPhrase());b.put("message",m);return ResponseEntity.status(s).body(b);}
}
