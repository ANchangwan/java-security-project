package com.example.security_project.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.security_project.domain.Item;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemRepository {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Rollback(false)
    void testSave(){

        List<Item> item = new ArrayList<>();
        item.add(Item.builder().name("냉장고").build());
        item.add(Item.builder().name("에어컨").build());
        item.add(Item.builder().name("세탁기").build());
        item.add(Item.builder().name("공기 청정기").build());




    }
    
}
