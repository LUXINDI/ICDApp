package cn.edu.tsinghua.sicd.models;

/**
 * Created by lxdbu on 2017/4/15.
 */

public class QuickSearchSelectResult {

    public String term;
    public String icd_code;
    public String Star_code;
    public String py;


    public String getICDcode() {
        return icd_code;
    }

    public String getStarCode() {
        return Star_code;
    }

    public String getKeyword() {
        return term;
    }
    public String getPy() {return  py;}
}
