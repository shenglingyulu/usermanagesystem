package com.example.usermanageback.pojo.DTO;

import lombok.Data;

@Data
public class SearchDTO {
    private Long id;
    private String username;
    private String userAccount;
    private int pageSize;
    private int current;
}
