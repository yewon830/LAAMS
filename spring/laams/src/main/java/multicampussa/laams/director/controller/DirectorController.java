package multicampussa.laams.director.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import multicampussa.laams.director.dto.*;
import multicampussa.laams.director.service.DirectorService;
import multicampussa.laams.manager.domain.examinee.ExamExaminee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/director")
@Api(tags = "감독관")
public class DirectorController {

    private final DirectorService directorService;

    // 시험 목록 조회
//    @GetMapping("/{directorNo}/exams")
//    public List<ExamListDto> getExams(@PathVariable Long directorNo){
//        return directorService.getExamList(directorNo);
//    }

    @ApiOperation(value = "시험 월별 조회 및 일별 조회")
    @GetMapping("/{directorNo}/exams")
    public ResponseEntity<Map<String, Object>> getExamMonthDayList(@PathVariable Long directorNo, @RequestParam int year, @RequestParam int month, @RequestParam(value = "day", defaultValue = "0") int day){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            resultMap.put("message","시험을 성공적으로 조회했습니다.");
            resultMap.put("data", directorService.getExamMonthDayList(directorNo, year, month, day));
            resultMap.put("code", HttpStatus.OK.value());
            resultMap.put("status", "success");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            resultMap.put("message", e.getMessage());
            resultMap.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "시험 상세정보 조회")
    @GetMapping("/exams/{examNo}")
    public ResponseEntity<Map<String, Object>> getExamInformation(@PathVariable Long examNo){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            ExamInformationDto examInformationDto = directorService.getExamInformation(examNo);

            resultMap.put("message","시험 상세정보를 성공적으로 조회했습니다.");
            resultMap.put("data", examInformationDto);
            resultMap.put("code", HttpStatus.OK.value());
            resultMap.put("status", "success");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            resultMap.put("message", e.getMessage());
            resultMap.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "시험 응시자 목록 조회")
    @GetMapping("/exams/{examNo}/examinees")
    public ResponseEntity<Map<String, Object>> getExamExamineeList(@PathVariable Long examNo) {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            resultMap.put("message","시험 응시자 목록을 성공적으로 조회했습니다.");
            resultMap.put("data", directorService.getExamExamineeList(examNo));
            resultMap.put("code", HttpStatus.OK.value());
            resultMap.put("status", "success");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            resultMap.put("message", e.getMessage());
            resultMap.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "시험 응시자 상세 조회")
    @GetMapping("/exams/{examNo}/examinees/{examineeNo}")
    public ResponseEntity<Map<String, Object>> getExamExaminee(@PathVariable Long examNo, @PathVariable Long examineeNo){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            ExamExamineeDto examExamineeDto = directorService.getExamExaminee(examNo, examineeNo);

            resultMap.put("message","시험 응시자의 상세 정보를 성공적으로 조회했습니다.");
            resultMap.put("data", examExamineeDto);
            resultMap.put("code", HttpStatus.OK.value());
            resultMap.put("status", "success");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            resultMap.put("message", e.getMessage());
            resultMap.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "시험 응시자 현황 조회")
    @GetMapping("/exams/{examNo}/status")
    public ResponseEntity<Map<String, Object>> getExamStatus(@PathVariable Long examNo){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ExamStatusDto examStatusDto = directorService.getExamStatus(examNo);

            resultMap.put("message","시험 응시자의 현황을 성공적으로 조회했습니다.");
            resultMap.put("data", examStatusDto);
            resultMap.put("code", HttpStatus.OK.value());
            resultMap.put("status", "success");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            resultMap.put("message", e.getMessage());
            resultMap.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
        }
    }

//    @ApiOperation(value = "응시자 출결 변경")
//    @PutMapping("/exams/{examNo}/examinees/{examineeNo}/attendance")
//    public ResponseEntity<Map<String, Object>> changeAttendance(@PathVariable Long examNo, @PathVariable Long examineeNo, @RequestBody ){
//        Map<String, Object> resultMap = new HashMap<>();
//        try {
//            resultMap.put("message","응시자의 출결 사항을 변경했습니다.");
//            resultMap.put("data", directorService.getExamExamineeList(examNo));
//            resultMap.put("code", HttpStatus.OK.value());
//            resultMap.put("status", "success");
//            return new ResponseEntity<>(resultMap, HttpStatus.OK);
//        } catch (IllegalArgumentException e){
//            resultMap.put("message", e.getMessage());
//            resultMap.put("status", HttpStatus.BAD_REQUEST.value());
//            return new ResponseEntity<>(resultMap, HttpStatus.BAD_REQUEST);
//        }
//    }
}
