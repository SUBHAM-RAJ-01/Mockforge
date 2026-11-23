package com.mockforge.runtime.storage;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Query options for filtering, sorting, and pagination.
 */
@Data
@Builder
public class QueryOptions {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int limit = 20;

    private List<Filter> filters;

    private List<Sort> sorts;

    @Data
    @Builder
    public static class Filter {
        private String field;
        private FilterOperator operator;
        private Object value;
    }

    @Data
    @Builder
    public static class Sort {
        private String field;
        private SortDirection direction;
    }

    public enum FilterOperator {
        EQ,      // equals
        NE,      // not equals
        GT,      // greater than
        GTE,     // greater than or equal
        LT,      // less than
        LTE,     // less than or equal
        CONTAINS // string contains
    }

    public enum SortDirection {
        ASC,
        DESC
    }

    /**
     * Calculate offset from page and limit.
     */
    public int getOffset() {
        return (page - 1) * limit;
    }
}
