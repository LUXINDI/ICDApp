package cn.edu.tsinghua.sicd.models;

import java.util.List;

/**
 * Created by douglaschan on 2016/11/2.
 */
public class LandPage {

    private int RecordCount;
    private int PageCount;

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public int getPageCount() {
        return PageCount;
    }

    public List<LandTransportSelectResult> getDataTable() {
        return DataTable;
    }

    public void setDataTable(List<LandTransportSelectResult> dataTable) {
        DataTable = dataTable;
    }

    public void setPageCount(int pageCount) {
        PageCount = pageCount;
    }



    private List<LandTransportSelectResult> DataTable;

}
