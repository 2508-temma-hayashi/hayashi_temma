package com.example.hayashi_temma.controller;

import ch.qos.logback.core.model.Model;
import com.example.hayashi_temma.controller.form.TaskForm;
import com.example.hayashi_temma.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();

        List<TaskForm> TaskData = taskService.findTasks();
        mav.setViewName("/top");
        mav.addObject("tasks", TaskData);

        return mav;
    }

}
