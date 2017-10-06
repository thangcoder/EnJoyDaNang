package node.com.enjoydanang.api.model;

/**
 * Created by chientruong on 1/6/17.
 */

public class MessageEvent {
    private  boolean isStopLoadMore;
    private String threadId;
    public boolean isStopLoadMore() {
        return isStopLoadMore;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public void setStopLoadMore(boolean stopLoadMore) {
        isStopLoadMore = stopLoadMore;
    }

    public MessageEvent(boolean isStopLoadMore, String threadId) {
        this.isStopLoadMore = isStopLoadMore;
        this.threadId = threadId;
    }

    public MessageEvent(boolean isStopLoadMore) {
        this.isStopLoadMore = isStopLoadMore;
    }
}