package cat.udl.eps.softarch.mypadel.handler.exception;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ReservationControllerAdvice {

	@ResponseBody
	@ExceptionHandler(CompatibleCourtNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public VndErrors courtNotFoundExceptionHandler(CompatibleCourtNotFoundException ex) {
		return new VndErrors("error", ex.getMessage());
	}
}
