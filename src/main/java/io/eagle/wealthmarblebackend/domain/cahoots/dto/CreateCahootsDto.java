package io.eagle.wealthmarblebackend.domain.cahoots.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateCahootsDto {

    @Length(min = 5, max = 15, message = "공모 제목을 5자 이상, 15자 미만으로 작성해주세요.")
    private String title;

    @NotBlank(message = "휴가지 테마를 확인해주세요.")
    private String themeLocation;

    @NotBlank(message = "휴가지 테마를 확인해주세요.")
    private String themeBuilding;

    @NotBlank(message = "휴가지 위치를 확인해주세요.")
    private String location;

    @NotNull(message = "휴가지 건설 예상 시간을 확인해주세요.")
    private Integer expectedMonth;

    @NotNull(message = "휴가지 건설 진행 예상 시간을 확인해주세요.")
//    @Max(1000000000000)
    private Long expectedTotalCost;

    @NotBlank
    @Size(min = 10, max = 50, message = "휴가지 아이디어 간략 소개를 10자이상 50자 이하로 작성해주세요.")
    private String shortDescription;

    @NotBlank
    @Size(min = 5, max = 4000, message = "휴가지 아이디어 상세 소개를 5자이상 4000자 이하로 작성해주세요.")
    private String descritption;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate stockStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate stockEnd;
    @NotNull(message = "1주당 공모 가격을 작성해주세요.")
    private Long stockPrice;
    @NotNull(message = "목표 공모 주식 수를 작성해주세요.")
    private Integer stockNum;

    // TODO : picture url
    private List<MultipartFile> images = new ArrayList<>();
}
