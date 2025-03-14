package org.protobeans.scheduler;

import java.time.LocalDateTime;

public interface ProcessableItem {
    void setErrorMessage(String msg);

    int getProcessTryCount();

    void setProcessTryCount(int tryCount);

    void setNextProcessedTime(LocalDateTime date);
}
