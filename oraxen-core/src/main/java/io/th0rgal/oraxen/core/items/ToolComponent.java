package io.th0rgal.oraxen.core.items;

import java.util.List;

/**
 * Tool component for items that can break blocks faster.
 */
public record ToolComponent(
        List<ToolRule> rules
) {}

record ToolRule(
        String blocks,
        float speed,
        boolean correctForDrops
) {}