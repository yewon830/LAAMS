package multicampussa.laams.manager.dto.dashboard.response;

import lombok.Getter;
import multicampussa.laams.director.domain.errorReport.ErrorReport;

@Getter
public class DashboardErrorReport {
    Long directorNo;
    String directorName;
    String title;
    String errorType;

    public DashboardErrorReport(ErrorReport errorReport) {
        this.directorNo = errorReport.getDirector().getNo();
        this.directorName = errorReport.getDirector().getName();
        this.title = errorReport.getTitle();
        this.errorType = errorReport.getType();
    }
}
