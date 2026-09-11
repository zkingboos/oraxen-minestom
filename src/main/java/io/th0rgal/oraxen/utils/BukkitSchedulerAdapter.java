package io.th0rgal.oraxen.utils;

import io.th0rgal.oraxen.core.platform.OraxenScheduler;
import io.th0rgal.oraxen.core.platform.ScheduledTask;
import io.th0rgal.oraxen.core.platform.SchedulerProvider;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * Bukkit implementation of the neutral OraxenScheduler contract.
 * Delegates to SchedulerUtil and wraps platform-specific types.
 */
public final class BukkitSchedulerAdapter implements OraxenScheduler {

    private BukkitSchedulerAdapter() {
        // Register self as the platform scheduler
        SchedulerProvider.setScheduler(this);
    }

    public static void register() {
        new BukkitSchedulerAdapter();
    }

    @Override
    public ScheduledTask runTask(Runnable runnable) {
        return wrap(SchedulerUtil.runTask(runnable));
    }

    @Override
    public ScheduledTask runTask(Object plugin, Runnable runnable) {
        return wrap(SchedulerUtil.runTask((Plugin) plugin, runnable));
    }

    @Override
    public ScheduledTask runTaskLater(long delayTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runTaskLater(delayTicks, runnable));
    }

    @Override
    public ScheduledTask runTaskLater(Object plugin, long delayTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runTaskLater((Plugin) plugin, delayTicks, runnable));
    }

    @Override
    public ScheduledTask runTaskTimer(long delayTicks, long periodTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runTaskTimer(delayTicks, periodTicks, runnable));
    }

    @Override
    public ScheduledTask runTaskTimer(Object plugin, long delayTicks, long periodTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runTaskTimer((Plugin) plugin, delayTicks, periodTicks, runnable));
    }

    @Override
    public ScheduledTask runTaskAsync(Runnable runnable) {
        return wrap(SchedulerUtil.runTaskAsync(runnable));
    }

    @Override
    public ScheduledTask runTaskAsync(Object plugin, Runnable runnable) {
        return wrap(SchedulerUtil.runTaskAsync((Plugin) plugin, runnable));
    }

    @Override
    public ScheduledTask runTaskLaterAsync(long delayTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runTaskLaterAsync(delayTicks, runnable));
    }

    @Override
    public ScheduledTask runTaskLaterAsync(Object plugin, long delayTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runTaskLaterAsync((Plugin) plugin, delayTicks, runnable));
    }

    @Override
    public ScheduledTask runTaskTimerAsync(long delayTicks, long periodTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runTaskTimerAsync(delayTicks, periodTicks, runnable));
    }

    @Override
    public ScheduledTask runTaskTimerAsync(Object plugin, long delayTicks, long periodTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runTaskTimerAsync((Plugin) plugin, delayTicks, periodTicks, runnable));
    }

    @Override
    public ScheduledTask runAtLocation(Object location, Runnable runnable) {
        return wrap(SchedulerUtil.runAtLocation((Location) location, runnable));
    }

    @Override
    public ScheduledTask runAtLocation(Object plugin, Object location, Runnable runnable) {
        return wrap(SchedulerUtil.runAtLocation((Plugin) plugin, (Location) location, runnable));
    }

    @Override
    public ScheduledTask runAtLocationLater(Object location, long delayTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runAtLocationLater((Location) location, delayTicks, runnable));
    }

    @Override
    public ScheduledTask runAtLocationLater(Object plugin, Object location, long delayTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runAtLocationLater((Plugin) plugin, (Location) location, delayTicks, runnable));
    }

    @Override
    public ScheduledTask runAtLocationTimer(Object location, long delayTicks, long periodTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runAtLocationTimer((Location) location, delayTicks, periodTicks, runnable));
    }

    @Override
    public ScheduledTask runAtLocationTimer(Object plugin, Object location, long delayTicks, long periodTicks, Runnable runnable) {
        return wrap(SchedulerUtil.runAtLocationTimer((Plugin) plugin, (Location) location, delayTicks, periodTicks, runnable));
    }

    @Override
    public ScheduledTask runForEntity(Object entity, Runnable runnable) {
        return wrap(SchedulerUtil.runForEntity((Entity) entity, runnable));
    }

    @Override
    public ScheduledTask runForEntity(Object entity, Runnable runnable, Runnable retired) {
        return wrap(SchedulerUtil.runForEntity((Entity) entity, runnable, retired));
    }

    @Override
    public ScheduledTask runForEntity(Object plugin, Object entity, Runnable runnable, Runnable retired) {
        return wrap(SchedulerUtil.runForEntity((Plugin) plugin, (Entity) entity, runnable, retired));
    }

    @Override
    public ScheduledTask runForEntityLater(Object entity, long delayTicks, Runnable runnable, Runnable retired) {
        return wrap(SchedulerUtil.runForEntityLater((Entity) entity, delayTicks, runnable, retired));
    }

    @Override
    public ScheduledTask runForEntityLater(Object plugin, Object entity, long delayTicks, Runnable runnable, Runnable retired) {
        return wrap(SchedulerUtil.runForEntityLater((Plugin) plugin, (Entity) entity, delayTicks, runnable, retired));
    }

    @Override
    public ScheduledTask runForEntityTimer(Object entity, long delayTicks, long periodTicks, Runnable runnable, Runnable retired) {
        return wrap(SchedulerUtil.runForEntityTimer((Entity) entity, delayTicks, periodTicks, runnable, retired));
    }

    @Override
    public ScheduledTask runForEntityTimer(Object plugin, Object entity, long delayTicks, long periodTicks, Runnable runnable, Runnable retired) {
        return wrap(SchedulerUtil.runForEntityTimer((Plugin) plugin, (Entity) entity, delayTicks, periodTicks, runnable, retired));
    }

    @Override
    public void cancelTask(int taskId) {
        SchedulerUtil.cancelTask(taskId);
    }

    @Override
    public boolean isGlobalThread() {
        return SchedulerUtil.isGlobalThread();
    }

    private static ScheduledTask wrap(io.th0rgal.oraxen.utils.SchedulerUtil.ScheduledTask task) {
        return new BukkitScheduledTask(task);
    }

    /**
     * Adapter wrapping SchedulerUtil.ScheduledTask to implement the core ScheduledTask interface.
     */
    private static final class BukkitScheduledTask implements ScheduledTask {
        private final io.th0rgal.oraxen.utils.SchedulerUtil.ScheduledTask delegate;

        BukkitScheduledTask(io.th0rgal.oraxen.utils.SchedulerUtil.ScheduledTask delegate) {
            this.delegate = delegate;
        }

        @Override
        public void cancel() {
            delegate.cancel();
        }

        @Override
        public int getTaskId() {
            return delegate.getTaskId();
        }

        @Override
        public boolean isCancelled() {
            return delegate.isCancelled();
        }

        @Override
        public Object getTask() {
            return delegate.getTask();
        }
    }
}