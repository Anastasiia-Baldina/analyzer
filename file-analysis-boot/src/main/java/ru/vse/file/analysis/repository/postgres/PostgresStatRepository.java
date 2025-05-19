package ru.vse.file.analysis.repository.postgres;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.vse.file.analysis.dao.StatDao;
import ru.vse.file.analysis.repository.StatRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PostgresStatRepository implements StatRepository {
    private static final String sqlFindById =
            "select " +
                    "   *" +
                    " from" +
                    "   analysis" +
                    " where" +
                    "   file_id = :file_id";
    private static final String sqlFindByHash =
            "select " +
                    "   file_id" +
                    " from" +
                    "   analysis" +
                    " where" +
                    "   file_hash = :file_hash";
    private static final String sqlInsert =
            "insert into analysis" +
                    "(" +
                    "   file_id," +
                    "   file_hash," +
                    "   word_count," +
                    "   text_length," +
                    "   paragraph_count," +
                    "   picture_path" +
                    ")" +
                    " values" +
                    "(" +
                    "   :file_id," +
                    "   :file_hash," +
                    "   :word_count," +
                    "   :text_length," +
                    "   :paragraph_count," +
                    "   :picture_path" +
                    ")";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresStatRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insert(@NotNull StatDao statDao) {
        var pSrc = new MapSqlParameterSource()
                .addValue("file_id", statDao.fileId())
                .addValue("file_hash", statDao.fileHash())
                .addValue("word_count", statDao.wordCount())
                .addValue("text_length", statDao.textLength())
                .addValue("paragraph_count", statDao.paragraphCount())
                .addValue("picture_path", statDao.picturePath());
        jdbc.update(sqlInsert, pSrc);
    }

    @Nullable
    @Override
    public StatDao findById(String fileId) {
        var pSrc = new MapSqlParameterSource("file_id", fileId);
        var resList = jdbc.query(sqlFindById, pSrc, StatRowMapper.instance);

        return resList.isEmpty() ? null : resList.getFirst();
    }

    @Override
    @NotNull
    public List<String> findByHash(String fileHash) {
        var pSrc = new MapSqlParameterSource("file_hash", fileHash);
        return jdbc.queryForList(sqlFindByHash, pSrc, String.class);
    }

    private static class StatRowMapper implements RowMapper<StatDao> {
        static final StatRowMapper instance = new StatRowMapper();

        @Override
        public StatDao mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return StatDao.builder()
                    .setFileId(rs.getString("file_id"))
                    .setFileHash(rs.getString("file_hash"))
                    .setWordCount(rs.getInt("word_count"))
                    .setTextLength(rs.getInt("text_length"))
                    .setParagraphCount(rs.getInt("paragraph_count"))
                    .setPicturePath(rs.getString("picture_path"))
                    .build();
        }
    }
}
