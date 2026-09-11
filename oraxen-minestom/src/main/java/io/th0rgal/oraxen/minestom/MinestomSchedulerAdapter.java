package io.th0rgal.oraxen.minestom;

import io.th0rgal.oraxen.core.platform.OraxenScheduler;
import io.th0rgal.oraxen.core.platform.ScheduledTask;
import io.th0rgal.oraxen.core.platform.SchedulerProvider;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Minestom implementation of the neutral OraxenScheduler contract.
 */
public final class MinestomSchedulerAdapter implements OraxenScheduler {

    private final SchedulerManager scheduler = MinecraftServer.getSchedulerManager();

    public MinestomSchedulerAdapter() {
        SchedulerProvider.setScheduler(this);
    }

    public static void register() {
        new MinestomSchedulerAdapter();
    }

    @Override
    public ScheduledTask runTask(Runnable runnable) {
        return wrap(scheduler.buildTask(runnable).schedule());
    }

    @Override
    public ScheduledTask runTask(Object plugin, Runnable runnable) {
        return runTask(runnable);
    }

    @Override
    public ScheduledTask runTaskLater(long delayTicks, Runnable runnable) {
        return wrap(scheduler.buildTask(runnable).delay(Duration.ofMillis(Math.max(1, delayTicks) * 50)).schedule());
    }

    @Override
    public ScheduledTask runTaskLater(Object plugin, long delayTicks, Runnable runnable) {
        return runTaskLater(delayTicks, runnable);
    }

    @Override
    public ScheduledTask runTaskTimer(long delayTicks, long periodTicks, Runnable runnable) {
        return wrap(scheduler.buildTask(runnable)
            .delay(Duration.ofMillis(Math.max(1, delayTicks) * 50))
            .repeat(Duration.ofMillis(Math.max(1, periodTicks) * 50))
            .schedule());
    }

    @Override
    public ScheduledTask runTaskTimer(Object plugin, long delayTicks, long periodTicks, Runnable runnable) {
        return runTaskTimer(delayTicks, periodTicks, runnable);
    }

    @Override
    public ScheduledTask runTaskAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
        return new MinestomScheduledTask(null);
    }

    @Override
    public ScheduledTask runTaskAsync(Object plugin, Runnable runnable) {
        return runTaskAsync(runnable);
    }

    @Override
    public ScheduledTask runTaskLaterAsync(long delayTicks, Runnable runnable) {
        long delayMs = delayTicks * 50;
        CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS).execute(runnable);
        return new MinestomScheduledTask(null);
    }

    @Override
    public ScheduledTask runTaskLaterAsync(Object plugin, long delayTicks, Runnable runnable) {
        return runTaskLaterAsync(delayTicks, runnable);
    }

    @Override
    public ScheduledTask runTaskTimerAsync(long delayTicks, long periodTicks, Runnable runnable) {
        return runTaskTimer(delayTicks, periodTicks, runnable);
    }

    @Override
    public ScheduledTask runTaskTimerAsync(Object plugin, long delayTicks, long periodTicks, Runnable runnable) {
        return runTaskTimerAsync(delayTicks, periodTicks, runnable);
    }

    @Override
    public ScheduledTask runAtLocation(Object location, Runnable runnable) {
        return runTask(runnable);
    }

    @Override
    public ScheduledTask runAtLocation(Object plugin, Object location, Runnable runnable) {
        return runAtLocation(location, runnable);
    }

    @Override
    public ScheduledTask runAtLocationLater(Object location, long delayTicks, Runnable runnable) {
        return runTaskLater(delayTicks, runnable);
    }

    @Override
    public ScheduledTask runAtLocationLater(Object plugin, Object location, long delayTicks, Runnable runnable) {
        return runAtLocationLater(location, delayTicks, runnable);
    }

    @Override
    public ScheduledTask runAtLocationTimer(Object location, long delayTicks, long periodTicks, Runnable runnable) {
        return runTaskTimer(delayTicks, periodTicks, runnable);
    }

    @Override
    public ScheduledTask runAtLocationTimer(Object plugin, Object location, long delayTicks, long periodTicks, Runnable runnable) {
        return runAtLocationTimer(location, delayTicks, periodTicks, runnable);
    }

    @Override
    public ScheduledTask runForEntity(Object entity, Runnable runnable) {
        return runTask(runnable);
    }

    @Override
    public ScheduledTask runForEntity(Object entity, Runnable runnable, Runnable retired) {
        return runForEntity(entity, runnable);
    }

    @Override
    public ScheduledTask runForEntity(Object plugin, Object entity, Runnable runnable, Runnable retired) {
        return runForEntity(entity, runnable, retired);
    }

    @Override
    public ScheduledTask runForEntityLater(Object entity, long delayTicks, Runnable runnable, Runnable retired) {
        return runTaskLater(delayTicks, runnable);
    }

    @Override
    public ScheduledTask runForEntityLater(Object plugin, Object entity, long delayTicks, Runnable runnable, Runnable retired) {
        return runForEntityLater(entity, delayTicks, runnable, retired);
    }

    @Override
    public ScheduledTask runForEntityTimer(Object entity, long delayTicks, long periodTicks, Runnable runnable, Runnable retired) {
        return runTaskTimer(delayTicks, periodTicks, runnable);
    }

    @Override
    public ScheduledTask runForEntityTimer(Object plugin, Object entity, long delayTicks, long periodTicks, Runnable runnable, Runnable retired) {
        return runForEntityTimer(entity, delayTicks, periodTicks, runnable, retired);
    }

    @Override
    public void cancelTask(int taskId) {
    }

    @Override
    public boolean isGlobalThread() {
        return true;
    }

    private static ScheduledTask wrap(Task task) {
        return new MinestomScheduledTask(task);
    }

    /**
     * Adapter wrapping Minestom Task to implement the core ScheduledTask interface.
     */
    private static final class MinestomScheduledTask implements ScheduledTask {
        private final Task task;

        MinestomScheduledTask(Task task) {
            this.task = task;
        }

        @Override
        public void cancel() {
            if (task != null) {
                task.cancel();
            }
        }

        @Override
        public int getTaskId() {
            return task != null ? task.id() : -1;
        }

        @Override
        public boolean isCancelled() {
            return task == null || !task.isAlive();
        }

        @Override
        public Object getTask() {
            return task;
        }
    }
}