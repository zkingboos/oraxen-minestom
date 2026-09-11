package io.th0rgal.oraxen.core.platform;

/**
 * Neutral scheduled task handle. Implementations wrap platform-specific task objects.
 */
public interface ScheduledTask {

    /**
     * Cancels this scheduled task.
     */
    void cancel();

    /**
     * Gets the task ID (Bukkit only, returns -1 on platforms without task IDs).
     */
    int getTaskId();

    /**
     * Checks if the task is cancelled.
     */
    boolean isCancelled();

    /**
     * Gets the underlying platform-specific task object.
     */
    Object getTask();
}