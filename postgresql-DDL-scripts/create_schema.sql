create user baldina_a_s with password 'postgres';

create schema if not exists bas_file_store authorization baldina_a_s;

create table if not exists bas_file_store.file (
    file_id         varchar(64)         not null,
    file_name       varchar(256)        not null,
    file_dir        varchar             not null
);
create unique index if not exists file_primary_key on bas_file_store.file(file_id);

create schema if not exists bas_file_analysis authorization baldina_a_s;

create table if not exists bas_file_analysis.analysis (
    file_id             varchar(64)         not null,
    file_hash           varchar(512)        not null,
    word_count          numeric(32)         not null,
    text_length         numeric(32)         not null,
    paragraph_count     numeric(32)         not null,
    picture_path        varchar(512)
);
create unique index if not exists analysis_primary_key on bas_file_analysis.analysis(file_id);
create index if not exists analysis_file_hash_idx on bas_file_analysis.analysis(file_hash);

grant all on all tables in schema bas_file_store to baldina_a_s;
grant all on all tables in schema bas_file_analysis to baldina_a_s;