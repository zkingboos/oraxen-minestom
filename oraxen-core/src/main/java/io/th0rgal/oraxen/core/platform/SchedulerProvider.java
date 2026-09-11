package io.th0rgal.oraxen.core.platform;

/**
 * Provider for the scheduler implementation. Set by each platform at startup.
 */
public final class SchedulerProvider {

    private static volatile OraxenScheduler INSTANCE;

    private SchedulerProvider() {}

    public static void setScheduler(OraxenScheduler scheduler) {
        INSTANCE = scheduler;
    }

    public static OraxenScheduler getScheduler() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Scheduler not initialized. Call SchedulerProvider.setScheduler() at startup.");
        }
        return INSTANCE;
    }
}