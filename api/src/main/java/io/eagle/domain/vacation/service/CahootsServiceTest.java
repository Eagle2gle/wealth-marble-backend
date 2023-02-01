//package io.eagle.domain.vacation.service;
//
//
//import io.eagle.domain.vacation.dto.CreateCahootsDto;
//import io.eagle.entity.type.ThemeBuildingType;
//import io.eagle.entity.type.ThemeLocationType;
//import io.eagle.domain.vacation.repository.VacationRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//public class CahootsServiceTest {
//
//    @InjectMocks
//    private CahootsService cahootsService;
//
//    @Mock
//    private VacationRepository vacationRepository;
//
//    public CreateCahootsDto createCahootsDto;
//
//    @Before
//    public void setup(){
//        // TODO : login
//
//    }
//    @Test
//    @DisplayName("공모 생성 확인")
//    public void createCahoots() {
//        createCahootsDto = CreateCahootsDto.builder()
//                .title("준호네 집")
//                .descritption("우리집 준호를 다같이 이용합니다. 준호는 공공재입니다.")
//                .expectedMonth(12)
//                .expectedTotalCost(100000L)
//                .location("경기도 성남시 분당구")
//                .shortDescription("우리집 그 준호입니다.")
//                .stockStart(LocalDate.of(2024,2,23))
//                .stockEnd(LocalDate.of(2024,4,23))
//                .stockNum(10)
//                .stockPrice(12000L)
//                .themeBuilding(ThemeBuildingType.GUEST_HOUSE)
//                .themeLocation(ThemeLocationType.DOWNTOWN)
//                .build();
//        cahootsService.create(createCahootsDto);
////        System.out.println(createCahootsDto);
////        System.out.println(cahootsRepository.findAll());
////        Optional<Cahoots> cahoots = cahootsRepository.findByTitle("준호네 집");
////        System.out.println(cahoots);
////        cahootsRepository.find
////        cahootsService.create(createCahootsDto);;
////        Assertions.assertThat(cahoots.get().getTitle(), is("준호네 집"));
//
//    }
//}
