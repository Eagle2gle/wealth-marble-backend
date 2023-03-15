package io.eagle.domain.order.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {
    private String status;
    private String message;
}
