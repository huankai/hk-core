package com.hk.commons.poi.excel.write.handler;

import com.hk.commons.util.StringUtils;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;

/**
 * 写入单个工作表数据
 *
 * @author kevin
 */
public class SimpleWriteableHandler<T> extends AbstractWriteableHandler<T> {

//    private int maxRowData = 5000;

    @Override
    protected void writeWorkbook() {
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(params.getSheetName()));
        if (StringUtils.isNotEmpty(params.getPassword())) {
            sheet.protectSheet(params.getPassword());
        }
        sheet.setDisplayGridlines(params.isDisplayGridLines());
        writeSheet(sheet);
    }

//    public void setMaxRowData(int maxRowData) {
//        AssertUtils.isTrue(maxRowData > 0, "maxRowData must gt 0.");
//        this.maxRowData = maxRowData;
//    }

    protected void writeSheet(Sheet sheet) {
        createTitleRow(sheet);
        int size = params.getData().size();
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper creationHelper = workbook.getCreationHelper();
//        if (size > maxRowData) {
//            var count = (size / maxRowData) + (size % maxRowData > 0 ? 1 : 0);
//            var countDownLatch = new CountDownLatch(count);
//            for (var i = 0; i < count; i++) {
//                var startIndex = i * maxRowData;
//                new WriteSheetThread(countDownLatch, sheet, drawing, creationHelper, params.getDataStartRow() + startIndex,
//                        new ArrayList<>(params.getData().subList(startIndex + i, Math.min(startIndex + maxRowData, size))))
//                        .start();
//            }
//            try {
//                countDownLatch.await();
//            } catch (InterruptedException e) {
//                throw new WriteException("ThreadName: " + Thread.currentThread().getName() + ",,Message:" + e.getMessage(), e);
//            }
//        } else {
            createDataRows(sheet, drawing, creationHelper, params.getDataStartRow(), params.getData());
//        }
    }

//    @RequiredArgsConstructor
//    private class WriteSheetThread extends Thread {
//
//        private final CountDownLatch countDownLatch;
//
//        private final Sheet sheet;
//
//        private final Drawing<?> drawing;
//
//        private final CreationHelper creationHelper;
//
//        private final int startRow;
//
//        private final List<T> data;
//
//        @Override
//        public void run() {
//            try {
//                createDataRows(sheet, drawing, creationHelper, startRow, data);
//            } finally {
//                countDownLatch.countDown();
//            }
//        }
//    }

}
