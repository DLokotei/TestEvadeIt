package com.example.testevadeit.logic

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * base class for periodic tasks
 * @param period       -- time interval to perform task, is seconds
 * @param autoStart    -- start task from constructor, otherwise object has to be started manually
 * @param initialDelay -- delay before performing first task, used if auto-start spcified
 */
abstract class PeriodicalTask(period: Long, autoStart: Boolean = false, initialDelay: Long = period) {
    private var period = period
        private set
    private var disposable: Disposable? = null
    private var lastScheduleTS = 0L  // time stamp (seconds) last task was called

    init {
        // start task if auto start is required
        if (autoStart) {
            start(initialDelay)
        }
    }

    fun isRunning(): Boolean = disposable != null

    @Synchronized
    fun start(delay: Long = period) {
        stop()
        lastScheduleTS = System.currentTimeMillis()
        disposable = Observable.interval(delay, period, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    lastScheduleTS = System.currentTimeMillis()
                    doTask()
                }
    }

    @Synchronized
    private fun stop() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
            disposable = null
        }
    }

    fun forceTask() {
        start(0)
    }

    enum class RescheduleMode {
        /**
         * will immediately start task and schedule next to specified period
         */
        IMMEDIATE,

        /**
         * if current task is scheduled, it will re-schedule it to have make sure
         * it run in specified period from last event (if more time already passed than
         * new period -- will start it immediately
         */
        ADJUST,

        /**
         * already scheduled task will wait for old period, next one will be scheduled to new interval
         */
        NEXT
    }

    @Synchronized
    fun reschedule(newPeriod: Long, rescheduleMode: RescheduleMode = RescheduleMode.NEXT) {
        val restartDelay = when (rescheduleMode) {
            RescheduleMode.IMMEDIATE -> 0L
            RescheduleMode.ADJUST, RescheduleMode.NEXT -> {
                // going to stop and re-schedule to new period, start delay has to be adjusted
                // up to specified mode
                val waitedTime = System.currentTimeMillis()  - lastScheduleTS
                val timeLeft = if (rescheduleMode == RescheduleMode.ADJUST) {
                    // number of seconds required to wait till new period reached, or 0 to immediate
                    max(0L, newPeriod - waitedTime)
                } else {
                    // just number of seconds left in old period
                    max(0L, this.period - waitedTime)
                }
                timeLeft
            }
        }
        this.period = newPeriod
        // re-start with new delay
        start(restartDelay)
    }

    @Synchronized
    fun pause() {
        if (isRunning())
            stop()
    }

    @Synchronized
    fun resume() {
        if (!isRunning())
            reschedule(period, RescheduleMode.ADJUST)
    }

    /**
     * this method is called periodically
     */
    protected abstract fun doTask()
}