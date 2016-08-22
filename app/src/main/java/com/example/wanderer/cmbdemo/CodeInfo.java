package com.example.wanderer.cmbdemo;

/**
 * Created by wanderer on 2016/8/16.
 */
public class CodeInfo {
    /**
     * result : true
     * msg : 执行成功
     * datas : ||b6242413d92dfde935b84665499e5720c7933580
     */

    private boolean result;
    private String msg;
    private int erorcode;
    private String datas;

    public int getErorcode() {
        return erorcode;
    }

    public void setErorcode(int erorcode) {
        this.erorcode = erorcode;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }
}
