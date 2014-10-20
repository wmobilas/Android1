package com.agcy.eatwithme.Api;

/**
 * Created by kiolt_000 on 20/08/2014.
 */
public interface TaskCallback<T> {
    void onSuccess(T response);

    void onError(Exception exp);
}
