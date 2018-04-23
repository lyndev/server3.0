/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.notice;

import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author Administrator
 */
public class NoticeTimerBean
{
    private final NoticeBean noticeBean;
    private final ScheduledFuture timerFuture;

    public NoticeTimerBean(NoticeBean noticeBean, ScheduledFuture timerFuture)
    {
        this.noticeBean = noticeBean;
        this.timerFuture = timerFuture;
    }

    public int getNoticeId()
    {
        return noticeBean.getId();
    }

    public NoticeBean getNoticeBean()
    {
        return noticeBean;
    }

    public ScheduledFuture getTimerFuture()
    {
        return timerFuture;
    }
}
