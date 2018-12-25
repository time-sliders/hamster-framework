package com.noob.storage.component;

public class DefaultFileHandler extends AbstractFileLineHandler {

    public static void main(String[] args) {
        new DefaultFileHandler().handle();
    }

    @Override
    protected String getInputFileAbsolutePath() {
        return "/Users/zhangwei/Downloads/1";
    }

    @Override
    protected String getOutputFileAbsolutePath() {
        return "/Users/zhangwei/Downloads/2";
    }

    @Override
    protected String handleLineData(String line) {
        return line;
    }
}
