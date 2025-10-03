package com.example.hayashi_temma.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskForm {

    private Integer id;
    private String content;
    private Integer status;
    private LocalDateTime limitDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
