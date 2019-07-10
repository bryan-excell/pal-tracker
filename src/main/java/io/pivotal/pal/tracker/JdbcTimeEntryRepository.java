package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final RowMapper<TimeEntry> rowMapper = new RowMapper<TimeEntry>() {
        @Override
        public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TimeEntry(
                    rs.getLong(1),
                    rs.getLong(2),
                    rs.getLong(3),
                    rs.getDate(4).toLocalDate(),
                    rs.getInt(5)
            );
        }
    };

    JdbcTemplate jdbcTemplate;
    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        String sql = "insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();


        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                    ps.setLong(4, timeEntry.getHours());
                    return ps;
                }, keyHolder);

        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        List<TimeEntry> entry = jdbcTemplate.query("select * from time_entries where id = ?", rowMapper, timeEntryId);
        if (entry.size() == 0) return null;
        return entry.get(0);
    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> entry = jdbcTemplate.query("select * from time_entries", rowMapper);
        return entry;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String sql = "update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?";
        jdbcTemplate.update(sql, timeEntry.getProjectId(), timeEntry.getUserId(), Date.valueOf(timeEntry.getDate()), timeEntry.getHours(), id);

        return find(id);
    }

    @Override
    public void delete(long timeEntryId) {
        jdbcTemplate.update("delete from time_entries where id = ?", timeEntryId);
    }

}
