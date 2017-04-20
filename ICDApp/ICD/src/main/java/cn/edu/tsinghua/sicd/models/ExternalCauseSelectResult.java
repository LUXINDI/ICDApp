package cn.edu.tsinghua.sicd.models;

/**
 * Created by douglaschan on 2016/3/22.
 */
public class ExternalCauseSelectResult {
    public String getCause() {
        return Cause;
    }

    public void setCause(String cause) {
        Cause = cause;
    }

    public String Cause;

    public String getICD10() {
        return ICD10;
    }

    public void setICD10(String ICD10) {
        this.ICD10 = ICD10;
    }

    public String ICD10;
}
