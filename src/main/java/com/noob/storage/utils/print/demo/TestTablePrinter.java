package com.noob.storage.utils.print.demo;

import com.noob.storage.utils.print.TableLocation;
import com.noob.storage.utils.print.TablePrinter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author luyun
 * @version 1.0
 * @since 2017.11.04
 */
public class TestTablePrinter {

    public static void main(String[] args) {
        BatchTakeDownResult r = new BatchTakeDownResult();
        r.addExceptionMsg("系统异常，服务器出错了");
        r.addExceptionMsg("系统异常，服务器出错了");
        r.addExceptionMsg("系统异常，服务器出错了");
        r.addExceptionMsg("系统异常，服务器出错了");
        r.addExceptionMsg("系统异常，服务器出错了，系统异常，服务器出错了，系统异常，服务器出错了，系统异常，服务器出错了，系统异常，服务器出错了，系统异常，服务器出错了");
        r.finish();
        System.out.println(TablePrinter.printHtml(r));
    }

    static class BatchTakeDownResult extends JobResult {

        @TableLocation(row = 1, col = 1, propertyName = "处理总量", fieldTitle = "数据汇总")
        private int totalCount = 0;

        @TableLocation(row = 2, col = 1, propertyName = "成功总量")
        private int succCount = 0;

        @TableLocation(row = 3, col = 1, propertyName = "普通转让总量")
        private int commonCount = 0;

        @TableLocation(row = 3, col = 2, propertyName = "普通转让成功总量")
        private int commonSuccCount = 0;

        @TableLocation(row = 4, col = 1, propertyName = "投资户总量")
        private int investorCount = 0;

        @TableLocation(row = 4, col = 2, propertyName = "投资户成功总量")
        private int investorSuccCount = 0;

        @TableLocation(row = 5, col = 1, propertyName = "理财计划总量")
        private int planCount = 0;

        @TableLocation(row = 5, col = 2, propertyName = "理财计划总额")
        private BigDecimal totalPlanAmount = BigDecimal.ZERO;

        @TableLocation(row = 6, col = 1, fieldTitle = "异常信息列表(最多显示100条)")
        private List<String> exceptionMsgList = new LinkedList<String>();

        public void addExceptionMsg(String errorMsg) {
            exceptionMsgList.add(errorMsg);
        }
    }

}
