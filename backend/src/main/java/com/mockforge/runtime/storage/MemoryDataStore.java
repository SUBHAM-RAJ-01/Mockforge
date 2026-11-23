package com.mockforge.runtime.storage;

import com.mockforge.common.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of DataStore.
 * Data is lost when the application restarts.
 */
@Component
@Slf4j
public class MemoryDataStore implements DataStore {

    private final Map<String, Map<String, Map<String, Object>>> storage = new ConcurrentHashMap<>();
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public Map<String, Object> create(String resourceId, Map<String, Object> data) {
        Map<String, Map<String, Object>> resourceData = storage.computeIfAbsent(resourceId, k -> new ConcurrentHashMap<>());
        
        String id = IdGenerator.generateRecordId();
        Map<String, Object> record = new HashMap<>(data);
        record.put("id", id);
        record.put("createdAt", LocalDateTime.now().format(ISO_FORMATTER));
        record.put("updatedAt", LocalDateTime.now().format(ISO_FORMATTER));
        
        resourceData.put(id, record);
        log.debug("Created record: resourceId={}, id={}", resourceId, id);
        return new HashMap<>(record);
    }

    @Override
    public Optional<Map<String, Object>> findById(String resourceId, String recordId) {
        Map<String, Map<String, Object>> resourceData = storage.get(resourceId);
        if (resourceData == null) {
            return Optional.empty();
        }
        
        Map<String, Object> record = resourceData.get(recordId);
        return Optional.ofNullable(record).map(HashMap::new);
    }

    @Override
    public List<Map<String, Object>> findAll(String resourceId, QueryOptions options) {
        Map<String, Map<String, Object>> resourceData = storage.get(resourceId);
        if (resourceData == null) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> records = new ArrayList<>(resourceData.values());

        // Apply filters
        if (options.getFilters() != null) {
            for (QueryOptions.Filter filter : options.getFilters()) {
                records = applyFilter(records, filter);
            }
        }

        // Apply sorting
        if (options.getSorts() != null && !options.getSorts().isEmpty()) {
            records = applySort(records, options.getSorts());
        }

        // Apply pagination
        int offset = options.getOffset();
        int limit = options.getLimit();
        int total = records.size();
        
        if (offset >= total) {
            return Collections.emptyList();
        }
        
        int end = Math.min(offset + limit, total);
        return records.subList(offset, end).stream()
                .map(HashMap::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Map<String, Object>> update(String resourceId, String recordId, Map<String, Object> data) {
        Map<String, Map<String, Object>> resourceData = storage.get(resourceId);
        if (resourceData == null) {
            return Optional.empty();
        }

        Map<String, Object> existing = resourceData.get(recordId);
        if (existing == null) {
            return Optional.empty();
        }

        Map<String, Object> updated = new HashMap<>(existing);
        updated.putAll(data);
        updated.put("id", recordId); // Ensure ID is not changed
        updated.put("updatedAt", LocalDateTime.now().format(ISO_FORMATTER));

        resourceData.put(recordId, updated);
        log.debug("Updated record: resourceId={}, id={}", resourceId, recordId);
        return Optional.of(new HashMap<>(updated));
    }

    @Override
    public boolean delete(String resourceId, String recordId) {
        Map<String, Map<String, Object>> resourceData = storage.get(resourceId);
        if (resourceData == null) {
            return false;
        }

        boolean removed = resourceData.remove(recordId) != null;
        if (removed) {
            log.debug("Deleted record: resourceId={}, id={}", resourceId, recordId);
        }
        return removed;
    }

    @Override
    public int deleteAll(String resourceId) {
        Map<String, Map<String, Object>> resourceData = storage.remove(resourceId);
        int count = resourceData != null ? resourceData.size() : 0;
        if (count > 0) {
            log.debug("Deleted all records: resourceId={}, count={}", resourceId, count);
        }
        return count;
    }

    @Override
    public long count(String resourceId) {
        Map<String, Map<String, Object>> resourceData = storage.get(resourceId);
        return resourceData != null ? resourceData.size() : 0;
    }

    @Override
    public boolean exists(String resourceId, String recordId) {
        Map<String, Map<String, Object>> resourceData = storage.get(resourceId);
        return resourceData != null && resourceData.containsKey(recordId);
    }

    /**
     * Apply a filter to a list of records.
     */
    private List<Map<String, Object>> applyFilter(List<Map<String, Object>> records, QueryOptions.Filter filter) {
        return records.stream()
                .filter(record -> matchesFilter(record, filter))
                .collect(Collectors.toList());
    }

    /**
     * Check if a record matches a filter.
     */
    private boolean matchesFilter(Map<String, Object> record, QueryOptions.Filter filter) {
        Object fieldValue = record.get(filter.getField());
        Object filterValue = filter.getValue();

        if (fieldValue == null) {
            return filter.getOperator() == QueryOptions.FilterOperator.NE;
        }

        switch (filter.getOperator()) {
            case EQ:
                return fieldValue.equals(filterValue);
            case NE:
                return !fieldValue.equals(filterValue);
            case GT:
                return compareValues(fieldValue, filterValue) > 0;
            case GTE:
                return compareValues(fieldValue, filterValue) >= 0;
            case LT:
                return compareValues(fieldValue, filterValue) < 0;
            case LTE:
                return compareValues(fieldValue, filterValue) <= 0;
            case CONTAINS:
                return fieldValue.toString().contains(filterValue.toString());
            default:
                return false;
        }
    }

    /**
     * Compare two values.
     */
    @SuppressWarnings("unchecked")
    private int compareValues(Object a, Object b) {
        if (a instanceof Comparable && b instanceof Comparable) {
            return ((Comparable) a).compareTo(b);
        }
        return 0;
    }

    /**
     * Apply sorting to records.
     */
    private List<Map<String, Object>> applySort(List<Map<String, Object>> records, List<QueryOptions.Sort> sorts) {
        return records.stream()
                .sorted((a, b) -> {
                    for (QueryOptions.Sort sort : sorts) {
                        Object aVal = a.get(sort.getField());
                        Object bVal = b.get(sort.getField());
                        
                        int cmp = compareValues(aVal, bVal);
                        if (cmp != 0) {
                            return sort.getDirection() == QueryOptions.SortDirection.ASC ? cmp : -cmp;
                        }
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }
}
