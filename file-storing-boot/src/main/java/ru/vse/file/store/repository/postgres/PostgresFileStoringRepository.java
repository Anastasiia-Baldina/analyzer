package ru.vse.file.store.repository.postgres;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.vse.file.store.dao.FileDao;
import ru.vse.file.store.repository.FileStoringRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresFileStoringRepository implements FileStoringRepository {
    private static final String sqlFindById =
            "select " +
                    "   *" +
                    " from" +
                    "   file" +
                    " where" +
                    "   file_id = :file_id";
    private static final String sqlInsert =
            "insert into file" +
                    "(" +
                    "   file_id," +
                    "   file_name," +
                    "   file_dir" +
                    ")" +
                    " values" +
                    "(" +
                    "   :file_id," +
                    "   :file_name," +
                    "   :file_dir" +
                    ")";
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresFileStoringRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Nullable
    @Override
    public FileDao findById(String id) {
        var pSrc = new MapSqlParameterSource("file_id", id);
        var resList = jdbc.query(sqlFindById, pSrc, FileRowMapper.instance);

        return resList.isEmpty() ? null : resList.getFirst();
    }

    @Override
    public void insert(@NotNull FileDao fileDao) {
        var pSrc = new MapSqlParameterSource()
                .addValue("file_id", fileDao.id())
                .addValue("file_name", fileDao.name())
                .addValue("file_dir", fileDao.dir());
        jdbc.update(sqlInsert, pSrc);
    }

    private static class FileRowMapper implements RowMapper<FileDao> {
        static final FileRowMapper instance = new FileRowMapper();

        @Override
        public FileDao mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            return FileDao.builder()
                    .setId(rs.getString("file_id"))
                    .setName(rs.getString("file_name"))
                    .setDir(rs.getString("file_dir"))
                    .build();
        }
    }
}
