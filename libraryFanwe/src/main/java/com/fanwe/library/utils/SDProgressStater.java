package com.fanwe.library.utils;

/**
 * Created by Administrator on 2016/7/14.
 */
public class SDProgressStater
{
    private boolean isSuccess;
    private boolean isInProgress;


    public SDProgressStater()
    {
        reset();
    }

    private void reset()
    {
        isSuccess = false;
        isInProgress = false;
    }

    /**
     * 是否成功
     *
     * @return
     */
    public boolean isSuccess()
    {
        return isSuccess;
    }

    /**
     * 设置成功
     */
    public void setSuccess()
    {
        setSuccess(true);
    }

    /**
     * 设置失败
     */
    public void setFailure()
    {
        setSuccess(false);
    }

    private void setSuccess(boolean success)
    {
        isSuccess = success;
        setInProgress(false);
    }

    /**
     * 是否正在执行中
     *
     * @return
     */
    public boolean isInProgress()
    {
        return isInProgress;
    }

    /**
     * 开始执行
     */
    public void startProgress()
    {
        setInProgress(true);
    }

    private void setInProgress(boolean inProgress)
    {
        isInProgress = inProgress;
    }
}
