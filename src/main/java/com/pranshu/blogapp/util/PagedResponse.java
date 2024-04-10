package com.pranshu.blogapp.util;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private int currentPage;
    private int totalPages;
    private List<T> posts;

    
}
