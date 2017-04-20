package cn.edu.tsinghua.sicd.models;

/**
 * Created by douglaschan on 2016/3/22.
 */
public class CancerTumorSelectResult {
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPrimary() {
        return Primary;
    }

    public void setPrimary(String primary) {
        Primary = primary;
    }

    public String getSecondary() {
        return Secondary;
    }

    public void setSecondary(String secondary) {
        Secondary = secondary;
    }

    public String getInsitu() {
        return insitu;
    }

    public void setInsitu(String insitu) {
        this.insitu = insitu;
    }

    public String getBenign() {
        return benign;
    }

    public void setBenign(String benign) {
        this.benign = benign;
    }

    public String getTumor() {
        return tumor;
    }

    public void setTumor(String tumor) {
        this.tumor = tumor;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String keyword;
    public String Primary;
    public String Secondary;
    public String insitu;
    public String benign;
    public String tumor;
    public String Remark;
}
