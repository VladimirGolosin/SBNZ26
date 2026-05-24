package com.ftn.sbnz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.sbnz.dto.CultureAnswerDTO;
import com.ftn.sbnz.dto.CultureQuestionRequestDTO;
import com.ftn.sbnz.dto.ErrorDTO;
import com.ftn.sbnz.exception.InvalidArgumentException;
import com.ftn.sbnz.service.CultureQuestionService;

@RestController
@RequestMapping("api/")
public class CultureQuestionController {

    @Autowired
    private CultureQuestionService questionService;

    @PostMapping("question")
    public ResponseEntity<?> processCultureQuestion(@RequestBody CultureQuestionRequestDTO requestDTO) {
        try {
            CultureAnswerDTO response = questionService.processCultureQuestion(requestDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidArgumentException e) {
            ErrorDTO dto = new ErrorDTO(e.getMessage());
            return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
        }
    }
}

