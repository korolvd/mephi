# Часть 1

Напишите конвейер, который будет использовать команды ps, grep и awk для мониторинга активных процессов в системе. 
Результаты должны быть отсортированы по использованию ресурсов, и информация должна быть записана 
в файл «system_monitoring.txt»

```shell
ps -aux --sort=-%cpu,-%mem | grep -v -e ps -e grep | awk '{ print $2, $3, $4, $11 }' > system_monitoring.txt
```


Напишите конвейер, который сравнивает содержимое двух директорий. Используйте команды find, sort, и diff для поиска и 
сравнения файлов в двух директориях. Результат сравнения сохраните в файл «directory_comparison.txt».

```shell
diff -y <(find dir1 -type f -printf '%f\n' | sort) <(find dir2 -type f -printf '%f\n' | sort) > directory_comparison.txt
```


Напишите конвейер для подсчета общего числа строк во всех текстовых файлах в указанной директории, используя find,
xargs и wc.

```shell
find dir1 -type f -name "*.txt" | xargs wc -l
```


Напишите конвейер, который использует команды df, sort и awk для мониторинга использования дискового пространства 
и записывает результаты в файл «disk_space_usage.txt».

```shell
cat /var/log/kern.log | grep ERROR | awk '{ printf "%s %s %s", $1, $2, $3; for (i = 8; i <= NF; i++) printf " %s", $i; print "" }' | sort -k1,1M -k2,2n -k3,3 > filtered_logs.txt && tar -cvzf filtered_logs.tar.gz filtered_logs.txt
```
