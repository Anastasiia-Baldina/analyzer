create table if not exists analysis (
    file_id             varchar(64)         not null,
    file_hash           varchar(512)        not null,
    word_count          numeric(32)         not null,
    text_length         numeric(32)         not null,
    paragraph_count     numeric(32)         not null,
    picture_path        varchar(512)
);
create unique index if not exists analysis_primary_key on analysis(file_id);
create index if not exists analysis_file_hash_idx on analysis(file_hash);
