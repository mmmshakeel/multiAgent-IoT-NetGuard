#!/bin/bash
set -e

# Run the user-provided SQL file
mysql -u root -ppassword jade_db < /docker-entrypoint-initdb.d/db.sql
