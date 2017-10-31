package org.protobeans.mvc.controller.advice;

import java.util.ArrayList;
import java.util.List;

import org.protobeans.mvc.util.GlobalModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ModelControllerAdvice {
    @Autowired(required = false)
    private List<GlobalModelAttribute> attributes = new ArrayList<>();
    
    @ModelAttribute
    public void prepareModel(Model model) {
        for (GlobalModelAttribute attr : attributes) {
            model.addAttribute(attr.getKey(), attr.getValue());
        }
    }
}
