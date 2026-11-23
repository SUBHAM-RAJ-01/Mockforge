package com.mockforge.runtime.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockforge.common.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * SQLite-based implementation of DataStore using the mock_records table.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SQLiteDataStore implements DataStore {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public Map<String, Object> create(String resourceId, Map<String, Object> data) {
        String id = IdGenerator.generateRecordId();
        Map<String, Object> record = new HashMap<>(data);
        record.put("id", id);
        record.put("createdAt", LocalDateTime.now().format(ISO_FORMATTER));
        record.put("updatedAt", LocalDateTime.now().format(ISO_FORMATTER));

        try {
            String dataJson = objectMapper.writeValueAsString(record);
            String sql = "INSERT INTO mock_records (id, resource_id, data_json, version, created_at, updated_at) " +
                        "VALUES (?, ?, ?, 1, datetime('now'), datetime('now'))";
            jdbcTemplate.update(sql, id, resourceId, dataJson);
            log.debug("Created record in SQLite: resourceId={}, id={}", resourceId, id);
            return new HashMap<>(record);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize record data", e);
        }
    }

    @Override
    public Optional<Map<String, Object>> findById(String resourceId, String recordId) {
        String sql = "SELECT data_json FROM mock_records WHERE resource_id = ? AND id = ?";
        
        List<String> results = jdbcTemplate.query(sql, 
                (rs, rowNum) -> rs.getString("data_json"),
                resourceId, recordId);

        if (results.isEmpty()) {
            return Optional.empty();
        }

        try {
            Map<String, Object> record = objectMapper.readValue(
                    results.get(0), 
                    new TypeReference<Map<String, Object>>() {}
            );
            return Optional.of(record);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize record: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Map<String, Object>> findAll(String resourceId, QueryOptions options) {
        // For now, fetch all and filter in memory (can be optimized later)
        String sql = "SELECT data_json FROM mock_records WHERE resource_id = ? " +
                    "LIMIT ? OFFSET ?";

        List<String> jsonResults = jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getString("data_json"),
                resourceId, options.getLimit(), options.getOffset());

        List<Map<String, Object>> records = new ArrayList<>();
        for (String json : jsonResults) {
            try {
                Map<String, Object> record = objectMapper.readValue(
                        json, 
                        new TypeReference<Map<String, Object>>() {}
                );
                records.add(record);
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize record: {}", e.getMessage());
            }
        }

        return records;
    }

    @Override
    public Optional<Map<String, Object>> update(String resourceId, String recordId, Map<String, Object> data) {
        Optional<Map<String, Object>> existing = findById(resourceId, recordId);
        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Object> updated = new HashMap<>(existing.get());
        updated.putAll(data);
        updated.put("id", recordId);
        updated.put("updatedAt", LocalDateTime.now().format(ISO_FORMATTER));

        try {
            String dataJson = objectMapper.writeValueAsString(updated);
            String sql = "UPDATE mock_records SET data_json = ?, version = version + 1, " +
                        "updated_at = datetime('now') WHERE resource_id = ? AND id = ?";
            int rows = jdbcTemplate.update(sql, dataJson, resourceId, recordId);
            
            if (rows > 0) {
                log.debug("Updated record in SQLite: resourceId={}, id={}", resourceId, recordId);
                return Optional.of(updated);
            }
            return Optional.empty();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize record data", e);
        }
    }

    @Override
    public boolean delete(String resourceId, String recordId) {
        String sql = "DELETE FROM mock_records WHERE resource_id = ? AND id = ?";
        int rows = jdbcTemplate.update(sql, resourceId, recordId);
        
        if (rows > 0) {
            log.debug("Deleted record from SQLite: resourceId={}, id={}", resourceId, recordId);
            return true;
        }
        return false;
    }

    @Override
    public int deleteAll(String resourceId) {
        String sql = "DELETE FROM mock_records WHERE resource_id = ?";
        int count = jdbcTemplate.update(sql, resourceId);
        
        if (count > 0) {
            log.debug("Deleted all records from SQLite: resourceId={}, count={}", resourceId, count);
        }
        return count;
    }

    @Override
    public long count(String resourceId) {
        String sql = "SELECT COUNT(*) FROM mock_records WHERE resource_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, resourceId);
        return count != null ? count : 0;
    }

    @Override
    public boolean exists(String resourceId, String recordId) {
        String sql = "SELECT COUNT(*) FROM mock_records WHERE resource_id = ? AND id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, resourceId, recordId);
        return count != null && count > 0;
    }
}
