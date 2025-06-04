FROM mcr.microsoft.com/mssql-tools

COPY docker/init.sql /init.sql
COPY docker/wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh
