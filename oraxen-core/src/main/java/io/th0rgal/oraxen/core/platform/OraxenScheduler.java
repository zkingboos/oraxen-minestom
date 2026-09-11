package io.th0rgal.oraxen.core.platform;

/**
 * Neutral scheduler contract. Implementations provided by each platform (Bukkit, Minestom).
 * All delay/period units are in ticks (1 tick = 50ms).
 */
public interface OraxenScheduler {

    // ==================== GLOBAL/SYNC TASKS ====================

    /**
     * Runs a task on the next server tick.
     */
    ScheduledTask runTask(Runnable runnable);

    /**
     * Runs a task on the next server tick with a specific plugin context.
     */
    ScheduledTask runTask(Object plugin, Runnable runnable);

    /**
     * Runs a task after the specified delay in ticks.
     */
    ScheduledTask runTaskLater(long delayTicks, Runnable runnable);

    /**
     * Runs a task after the specified delay in ticks with a specific plugin context.
     */
    ScheduledTask runTaskLater(Object plugin, long delayTicks, Runnable runnable);

    /**
     * Runs a task repeatedly with the specified delay and period in ticks.
     */
    ScheduledTask runTaskTimer(long delayTicks, long periodTicks, Runnable runnable);

    /**
     * Runs a task repeatedly with the specified delay and period in ticks with a specific plugin context.
     */
    ScheduledTask runTaskTimer(Object plugin, long delayTicks, long periodTicks, Runnable runnable);

    // ==================== ASYNC TASKS ====================

    /**
     * Runs a task asynchronously.
     */
    ScheduledTask runTaskAsync(Runnable runnable);

    /**
     * Runs a task asynchronously with a specific plugin context.
     */
    ScheduledTask runTaskAsync(Object plugin, Runnable runnable);

    /**
     * Runs a task asynchronously after the specified delay in ticks.
     */
    ScheduledTask runTaskLaterAsync(long delayTicks, Runnable runnable);

    /**
     * Runs a task asynchronously after the specified delay in ticks with a specific plugin context.
     */
    ScheduledTask runTaskLaterAsync(Object plugin, long delayTicks, Runnable runnable);

    /**
     * Runs a task asynchronously with the specified delay and period in ticks.
     */
    ScheduledTask runTaskTimerAsync(long delayTicks, long periodTicks, Runnable runnable);

    /**
     * Runs a task asynchronously with the specified delay and period in ticks with a specific plugin context.
     */
    ScheduledTask runTaskTimerAsync(Object plugin, long delayTicks, long periodTicks, Runnable runnable);

    // ==================== LOCATION-BASED TASKS ====================

    /**
     * Runs a task at a specific location.
     */
    ScheduledTask runAtLocation(Object location, Runnable runnable);

    /**
     * Runs a task at a specific location with a specific plugin context.
     */
    ScheduledTask runAtLocation(Object plugin, Object location, Runnable runnable);

    /**
     * Runs a task at a specific location after the specified delay in ticks.
     */
    ScheduledTask runAtLocationLater(Object location, long delayTicks, Runnable runnable);

    /**
     * Runs a task at a specific location after the specified delay in ticks with a specific plugin context.
     */
    ScheduledTask runAtLocationLater(Object plugin, Object location, long delayTicks, Runnable runnable);

    /**
     * Runs a task at a specific location repeatedly with the specified delay and period in ticks.
     */
    ScheduledTask runAtLocationTimer(Object location, long delayTicks, long periodTicks, Runnable runnable);

    /**
     * Runs a task at a specific location repeatedly with the specified delay and period in ticks with a specific plugin context.
     */
    ScheduledTask runAtLocationTimer(Object plugin, Object location, long delayTicks, long periodTicks, Runnable runnable);

    // ==================== ENTITY-BASED TASKS ====================

    /**
     * Runs a task for a specific entity.
     */
    ScheduledTask runForEntity(Object entity, Runnable runnable);

    /**
     * Runs a task for a specific entity with a retired callback.
     */
    ScheduledTask runForEntity(Object entity, Runnable runnable, Runnable retired);

    /**
     * Runs a task for a specific entity with a specific plugin context.
     */
    ScheduledTask runForEntity(Object plugin, Object entity, Runnable runnable, Runnable retired);

    /**
     * Runs a task for a specific entity after the specified delay in ticks.
     */
    ScheduledTask runForEntityLater(Object entity, long delayTicks, Runnable runnable, Runnable retired);

    /**
     * Runs a task for a specific entity after the specified delay in ticks with a specific plugin context.
     */
    ScheduledTask runForEntityLater(Object plugin, Object entity, long delayTicks, Runnable runnable, Runnable retired);

    /**
     * Runs a task for a specific entity repeatedly with the specified delay and period in ticks.
     */
    ScheduledTask runForEntityTimer(Object entity, long delayTicks, long periodTicks, Runnable runnable, Runnable retired);

    /**
     * Runs a task for a specific entity repeatedly with the specified delay and period in ticks with a specific plugin context.
     */
    ScheduledTask runForEntityTimer(Object plugin, Object entity, long delayTicks, long periodTicks, Runnable runnable, Runnable retired);

    // ==================== UTILITIES ====================

    /**
     * Cancels a task by its ID (Bukkit only, no-op on platforms without task IDs).
     */
    void cancelTask(int taskId);

    /**
     * Checks if running on the global/main thread.
     */
    boolean isGlobalThread();

    /**
     * Gets the singleton scheduler instance.
     */
    static OraxenScheduler get() {
        return SchedulerProvider.getScheduler();
    }
}