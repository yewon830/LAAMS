package multicampussa.laams.manager.controller.dashboard;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import multicampussa.laams.centerManager.dto.ConfirmDirectorRequest;
import multicampussa.laams.global.ApiResponse;
import multicampussa.laams.global.CustomExceptions;
import multicampussa.laams.home.member.jwt.JwtTokenProvider;
import multicampussa.laams.manager.dto.dashboard.response.DashboardResponse;
import multicampussa.laams.manager.service.dashboard.ManagerDashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "운영자의 대시보드 관련 기능")
@Controller("managerDashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ManagerDashboardService managerDashboardService;
    private final JwtTokenProvider jwtTokenProvider;

    // 운영자 대시보드 조회
    @ApiOperation(value = "운영자 대시보드 조회")
    @GetMapping("/api/v1/manager/dashboard")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "대쉬보드 조회에 성공했습니다.", response = DashboardResponse.class),
            @io.swagger.annotations.ApiResponse(code = 400, message = "잘못된 요청"),
    })
    public ResponseEntity<ApiResponse<List<DashboardResponse>>> getDashboard(
            @ApiIgnore @RequestHeader String authorization,
            @RequestParam int year, @RequestParam int month
    ) {
        String token = authorization.replace("Bearer", "");
        String authority = jwtTokenProvider.getAuthority(token);
        if (authority.equals("ROLE_MANAGER")) {
            return new ResponseEntity<>(
                    new ApiResponse<>(
                            "success",
                            HttpStatus.OK.value(),
                            managerDashboardService.generateMonthlyDashboard(year, month)
                    ),
                    HttpStatus.OK);
        } else {
            throw new CustomExceptions.UnauthorizedException("접근 권한이 없습니다.");
        }
    }

}
