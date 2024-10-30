package ru.otus.hw.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ExeptionController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException ex) {
        String message = ex.getMessage();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", message);
        modelAndView.setViewName("error/entitynotfound");
        return modelAndView;
    }
}
