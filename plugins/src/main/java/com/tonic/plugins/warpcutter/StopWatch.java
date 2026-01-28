package com.tonic.plugins.warpcutter;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class StopWatch
{
    private Instant start;
    private Instant end;
    private Instant pauseStart;
    private Duration pausedTime = Duration.ZERO;

    public StopWatch(Supplier<Instant> supplier, Duration duration)
    {
        start = supplier.get();
        if (duration != null)
        {
            end = start.plus(duration);
        }
    }

    public static StopWatch start(Supplier<Instant> supplier)
    {
        return new StopWatch(supplier, null);
    }

    public static StopWatch start()
    {
        return start(Instant::now);
    }

    public boolean exceeds(Duration duration)
    {
        return getElapsed().getSeconds() > duration.getSeconds();
    }

    public void setEndIn(Duration duration)
    {
        end = Instant.now().plus(duration);
    }

    public boolean isRunning()
    {
        return end == null || Instant.now().isBefore(end);
    }

    public Duration getElapsed()
    {
        Instant now = Instant.now();
        if (pauseStart != null)
        {
            return Duration.between(start, pauseStart).plus(pausedTime);
        }
        return Duration.between(start, now).minus(pausedTime);
    }

    public Duration getRemaining()
    {
        return end != null ? Duration.between(end, Instant.now()) : Duration.ZERO;
    }

    public String toElapsedString()
    {
        return format(getElapsed());
    }

    public String toRemainingString()
    {
        return format(getRemaining());
    }

    public void reset()
    {
        Instant prevStart = start;
        start = Instant.now();
        if (end != null)
        {
            Duration duration = Duration.between(prevStart, end);
            setEndIn(duration);
        }
    }

    public double getRate(long value, Duration rate)
    {
        long elapsed = getElapsed().toMillis();
        if (elapsed == 0)
        {
            return 0;
        }

        return (double) (value * rate.toMillis() / getElapsed().toMillis());
    }

    public double getHourlyRate(long value)
    {
        return getRate(value, Duration.ofHours(1));
    }

    public void pause()
    {
        if (pauseStart == null)
        {
            pauseStart = Instant.now();
        }
    }
    
    public void resume()
    {
        if (pauseStart != null)
        {
            pausedTime = pausedTime.plus(Duration.between(pauseStart, Instant.now()));
            pauseStart = null;
        }
    }
    
    public boolean isPaused()
    {
        return pauseStart != null;
    }

    public static String format(Duration duration)
    {
        long secs = Math.abs(duration.getSeconds());
        return String.format("%02d:%02d:%02d", secs / 3600L, secs % 3600L / 60L, secs % 60L);
    }
}