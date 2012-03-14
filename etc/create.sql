-- -----------------------------
--  Table structure for "users"
-- -----------------------------
DROP TABLE IF EXISTS "users";
CREATE TABLE "users" (
	"id" int8 NOT NULL DEFAULT NULL,
	"version" int4 DEFAULT NULL,
	"password" varchar(255) DEFAULT NULL,
	"user_role" varchar(255) DEFAULT NULL,
	"username" varchar(255) DEFAULT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "users" OWNER TO "postgres";

-- ----------------------------
--  Records of "users"
-- ----------------------------
BEGIN;
INSERT INTO "users" VALUES ('1', '0', 'password', 'TESTER', 'tester1');
INSERT INTO "users" VALUES ('2', '0', 'password', 'DEVELOPER', 'developer1');
INSERT INTO "users" VALUES ('3', '0', 'password', 'MANAGER', 'manager1');
COMMIT;
