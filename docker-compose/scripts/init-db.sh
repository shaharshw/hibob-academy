set -e
set -u

source ../postgres-env.properties

create_database() {
  local database=$1
  echo "  Creating database '$database'"
  local dbAlreadyExists
  dbAlreadyExists="$(
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --tuples-only <<-EOSQL
			SELECT 1 FROM pg_database WHERE datname = '$database' ;
		EOSQL
  )"

  if [ -z "$dbAlreadyExists" ]; then
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE DATABASE "$database";
	    GRANT ALL PRIVILEGES ON DATABASE "$database" TO $POSTGRES_USER;
EOSQL
  echo "$database successfully created"
  else
    echo " $database already exists and not created"
  fi
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
  echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
  for db in $(echo "$POSTGRES_MULTIPLE_DATABASES" | tr ',' ' '); do
    create_database $db
  done
  echo "Multiple databases created"
fi
