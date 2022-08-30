package com.assignment.Assignment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class RequestBodyForTextSearch {
    public RequestBodyForTextSearch() {
        this.pageNumber = 0;
        this.pageSize = 100;
    }

    private String text;
    private int pageNumber;
    private int pageSize;
}
