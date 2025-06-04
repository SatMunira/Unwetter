FROM mcr.microsoft.com/mssql-tools

COPY init.sql /init.sql
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
