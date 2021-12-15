package com.oracle.springboot.lxt.po;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop implements Serializable {
    private Long id;

    private String name;

    private Integer price;

    private String url;

    private String status;

    private Integer num;

    private static final long serialVersionUID = 1L;
}