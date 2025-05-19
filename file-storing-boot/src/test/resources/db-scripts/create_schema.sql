create table if not exists file (
    file_id         varchar(64)         not null,
    file_name       varchar(256)        not null,
    file_dir        varchar             not null
);
create unique index if not exists file_primary_key on file(file_id);