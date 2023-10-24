package multicampussa.laams.manager.controller.examinee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import multicampussa.laams.manager.domain.examinee.Examinee;
import multicampussa.laams.manager.dto.examinee.request.ExamineeCreateRequest;
import multicampussa.laams.manager.dto.examinee.request.ExamineeUpdateRequest;
import multicampussa.laams.manager.dto.examinee.response.ExamineeCompensationDetailResponse;
import multicampussa.laams.manager.dto.examinee.response.ExamineeCompensationListResponse;
import multicampussa.laams.manager.dto.examinee.response.ExamineeResponse;
import multicampussa.laams.manager.service.examinee.ExamineeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "응시자 관련")
@RestController
public class ExamineeController {

    private final ExamineeService examineeService;

    public ExamineeController(ExamineeService examineeService) {
        this.examineeService = examineeService;
    }

    // 응시자 생성
    @ApiOperation("응시자 생성")
    @PostMapping("/manager/examinee")
    public void saveExaminee(@RequestBody ExamineeCreateRequest request) {
        examineeService.saveExaminee(request);
    }

    // 응시자 목록 조회
    @ApiOperation("응시자 목록 조회")
    @GetMapping("/manager/examinees")
    public List<ExamineeResponse> getExaminees() {
        return examineeService.getExaminees();
    }

    // 보상대상 응시자 조회
    @ApiOperation("보상 대상 응시자 조회")
    @GetMapping("/manager/examinees/compensation")
    public List<ExamineeCompensationListResponse> getCompensationList() {
        return examineeService.getCompensationList();
    }

    // 보상대상자 상세 조회
    @ApiOperation("보상 대상 응시자 상세 조회")
    @GetMapping("manager/examinees/compensation/{examineeNo}")
    public ExamineeCompensationDetailResponse getCompensationDetail(@PathVariable Long examineeNo){
        return examineeService.getCompensationDetail(examineeNo);
    }


    // 응시자 수정
    @ApiOperation("응시자 수정")
    @PutMapping("/manager/examinee")
    public void updateExaminee(@RequestBody ExamineeUpdateRequest request) {
        examineeService.updateExaminee(request);
    }

    // 응시자 삭제
    @ApiOperation("응시자 삭제")
    @DeleteMapping("/manager/examinee/{no}")
    public void deleteExaminee(@PathVariable Long no) {
        examineeService.deleteExaminee(no);
    }

}
