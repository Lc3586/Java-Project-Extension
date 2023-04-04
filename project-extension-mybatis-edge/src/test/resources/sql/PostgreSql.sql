-- SCHEMA: test4java

-- DROP SCHEMA IF EXISTS test4java ;

CREATE SCHEMA IF NOT EXISTS test4java
    AUTHORIZATION test4java;

    -- Table: test4java.TestBlobEntity

    -- DROP TABLE IF EXISTS test4java."TestBlobEntity";

    CREATE TABLE IF NOT EXISTS test4java."TestBlobEntity"
    (
        "Id" character varying(36) COLLATE pg_catalog."default" NOT NULL,
        "Bytes" bytea,
        CONSTRAINT "TestBlobEntity_pkey" PRIMARY KEY ("Id")
            USING INDEX TABLESPACE test
    )

    TABLESPACE test;

    ALTER TABLE IF EXISTS test4java."TestBlobEntity"
        OWNER to test4java;

    COMMENT ON TABLE test4java."TestBlobEntity"
        IS '测试读写文件数据';

    COMMENT ON COLUMN test4java."TestBlobEntity"."Id"
        IS '主键';

    COMMENT ON COLUMN test4java."TestBlobEntity"."Bytes"
        IS '文件数据';

-- Table: test4java.TestClobEntity

-- DROP TABLE IF EXISTS test4java."TestClobEntity";

CREATE TABLE IF NOT EXISTS test4java."TestClobEntity"
(
    "Id" character varying(36) COLLATE pg_catalog."default" NOT NULL,
    "Text" text COLLATE pg_catalog."default",
    CONSTRAINT "TextClobEntity_pkey" PRIMARY KEY ("Id")
        USING INDEX TABLESPACE test
)

TABLESPACE test;

ALTER TABLE IF EXISTS test4java."TestClobEntity"
    OWNER to test4java;

COMMENT ON TABLE test4java."TestClobEntity"
    IS '测试读写长文本数据';

COMMENT ON COLUMN test4java."TestClobEntity"."Id"
    IS '主键';

COMMENT ON COLUMN test4java."TestClobEntity"."Text"
    IS '文本数据';


-- Table: test4java.TestGeneralEntity

-- DROP TABLE IF EXISTS test4java."TestGeneralEntity";

CREATE TABLE IF NOT EXISTS test4java."TestGeneralEntity"
(
    "Id" character varying(36) COLLATE pg_catalog."default" NOT NULL,
    "String" character varying(200) COLLATE pg_catalog."default",
    "Short" smallint,
    "Integer" integer,
    "Long" bigint,
    "Float" real,
    "Double" double precision,
    "Decimal" numeric(38,30),
    "Boolean" boolean,
    "Date" date,
    "Time" time with time zone,
    "Datetime" timestamp with time zone,
    "longIdentity" bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 0 MINVALUE 0 MAXVALUE 9223372036854775807 CACHE 1 ),
    "Char" character(1) COLLATE pg_catalog."default",
    "Byte" smallint,
    CONSTRAINT "TestGeneralEntity_pkey" PRIMARY KEY ("Id")
        USING INDEX TABLESPACE test
)

TABLESPACE test;

ALTER TABLE IF EXISTS test4java."TestGeneralEntity"
    OWNER to test4java;

COMMENT ON TABLE test4java."TestGeneralEntity"
    IS '测试读写常规数据';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Id"
    IS '主键';

COMMENT ON COLUMN test4java."TestGeneralEntity"."String"
    IS '字符串';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Short"
    IS '16位整数';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Integer"
    IS '32位整数';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Long"
    IS '64位整数';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Float"
    IS '单精度浮点数';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Double"
    IS '双精度浮点数';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Decimal"
    IS '高精度浮点数';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Boolean"
    IS '布尔';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Date"
    IS '日期';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Time"
    IS '时间';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Datetime"
    IS '日期时间';

COMMENT ON COLUMN test4java."TestGeneralEntity"."longIdentity"
    IS '64位整数自动增长';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Char"
    IS '字符';

COMMENT ON COLUMN test4java."TestGeneralEntity"."Byte"
    IS '8位整数';


-- Table: test4java.TestIdentityEntity

-- DROP TABLE IF EXISTS test4java."TestIdentityEntity";

CREATE TABLE IF NOT EXISTS test4java."TestIdentityEntity"
(
    "Id" bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    "No" character varying(36) COLLATE pg_catalog."default",
    CONSTRAINT "TestIdentityEntity_pkey" PRIMARY KEY ("Id")
        USING INDEX TABLESPACE test
)

TABLESPACE test;

ALTER TABLE IF EXISTS test4java."TestIdentityEntity"
    OWNER to test4java;

COMMENT ON TABLE test4java."TestIdentityEntity"
    IS '测试自增主键';

COMMENT ON COLUMN test4java."TestIdentityEntity"."Id"
    IS '自增主键';

COMMENT ON COLUMN test4java."TestIdentityEntity"."No"
    IS '编号';