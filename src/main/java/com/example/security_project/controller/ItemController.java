package com.example.security_project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security_project.dto.ItemDto;
import com.example.security_project.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    public ResponseEntity<List<ItemDto>> getItems(){
        List<ItemDto> items = itemService.receiveItems();
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }
    
}
