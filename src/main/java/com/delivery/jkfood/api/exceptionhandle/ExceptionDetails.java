package com.delivery.jkfood.api.exceptionhandle;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ExceptionDetails {

    private Integer status;
    private String type;
    private String title;
    private String detail;
    private LocalDateTime timestamp;
}
