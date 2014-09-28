package org.netcook.android.tools;

public interface CrashCatchable {
    String TRACE_INFO = "TRACE_INFO";
    String HAS_CRASHED = "HAS_CRASHED";
    String RESULT_EXTRA_TEXT = "RESULT_EXTRA_TEXT";

    void onSendLog();

    void onReportReady();
}
