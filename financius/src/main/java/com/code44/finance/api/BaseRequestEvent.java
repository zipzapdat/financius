package com.code44.finance.api;

import android.text.TextUtils;

import de.greenrobot.event.EventBus;

public abstract class BaseRequestEvent<R, T extends BaseRequest<R>> {
    private final T request;
    private final R result;
    private final Exception error;
    private final State state;

    protected BaseRequestEvent(T request, R result, Exception error, State state) {
        this.request = request;
        this.result = result;
        this.error = error;
        this.state = state;
    }

    public static <P extends BaseRequestEvent<?, ?>> boolean isWorking(Class<P> eventClass, String uniqueId) {
        final P event = EventBus.getDefault().getStickyEvent(eventClass);
        final boolean eventExistsAndIsWorking = event != null && event.isWorking();
        final String workingUniqueId = eventExistsAndIsWorking ? event.getRequest().getUniqueId() : "";
        final boolean uniqueIdsAreEmpty = TextUtils.isEmpty(workingUniqueId) && TextUtils.isEmpty(uniqueId);
        final boolean uniqueIdsAreEqual = !TextUtils.isEmpty(workingUniqueId) && workingUniqueId.equals(uniqueId);

        return eventExistsAndIsWorking && (uniqueIdsAreEmpty || uniqueIdsAreEqual);
    }

    public T getRequest() {
        return request;
    }

    public R getResult() {
        return result;
    }

    public boolean isWorking() {
        return state == State.WORKING;
    }

    public static enum State {
        WORKING, FINISHED
    }
}
