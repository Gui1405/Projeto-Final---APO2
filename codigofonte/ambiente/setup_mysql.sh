#!/bin/bash
echo "=========================================="
echo "Configurando Banco de Dados Cinema APO2..."
echo "=========================================="
echo ""
echo "Por favor, certifique-se de que o MySQL Server esta rodando na porta 3306."
echo "Insira sua senha do usuario 'root' se solicitado."
echo ""

mysql -u root -p < ../database/DDL.sql
mysql -u root -p < ../database/DML.sql

echo ""
echo "=========================================="
echo "Banco de dados configurado com sucesso!"
echo "=========================================="
