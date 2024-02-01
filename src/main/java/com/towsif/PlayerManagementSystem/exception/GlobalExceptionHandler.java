package com.towsif.PlayerManagementSystem.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler({
            EntityNotFoundException.class,
            PropertyReferenceException.class,
            ValidationException.class,
            ClassCastException.class
    })
    public Object handleBadRequests(RuntimeException exception, Model model)
    {
        model.addAttribute("errorMsg", exception.getMessage());

        return "error";
    }
}
