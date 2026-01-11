#!/bin/bash

# Конфигурация
version=5.0.47527-A
repository_url="https://maven.pkg.github.com/man4j/crypto"
repository_id="crypto"

# Функция для нормализации имени артефакта
function normalize_artifact_id {
  local original_name=$1
  # Преобразуем в нижний регистр и заменяем запрещенные символы на дефисы
  echo "$original_name" | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9.-]/-/g' | sed 's/--*/-/g' | sed 's/^-\|-$//g'
}

# Функция для деплоя артефакта
function deploy {
  local jar_name=$1
  local original_artifact_id=$jar_name
  local normalized_artifact_id=$(normalize_artifact_id "$original_artifact_id")

  echo "Деплой файла: ${jar_name}.jar"
  echo "  Оригинальное имя артефакта: ${original_artifact_id}"
  echo "  Нормализованное имя артефакта: ${normalized_artifact_id}"

  # Проверяем существование файла
  if [ ! -f "./${jar_name}.jar" ]; then
    echo "✗ Файл не найден: ${jar_name}.jar"
    return 1
  fi

  mvn deploy:deploy-file \
    -DgroupId=ru.crypto \
    -DartifactId=${normalized_artifact_id} \
    -Dversion=${version} \
    -Dpackaging=jar \
    -Dfile=./${jar_name}.jar \
    -DrepositoryId=${repository_id} \
    -Durl=${repository_url} \
    -DgeneratePom=true \
    -DupdateReleaseInfo=true

  exit_code=$?
  if [ $exit_code -eq 0 ]; then
    echo "✓ Успешно задеплоен: ${normalized_artifact_id}"
  else
    echo "✗ Ошибка при деплое: ${normalized_artifact_id} (код: ${exit_code})"
  fi
  echo ""
}

# Проверка наличия jar файлов
if ! ls *.jar 1> /dev/null 2>&1; then
  echo "Ошибка: не найдено ни одного .jar файла в текущей директории"
  exit 1
fi

echo "Начинаем деплой артефактов в репозиторий: ${repository_url}"
echo "Версия: ${version}"
echo ""

# Обработка всех jar файлов
IFS=$'\n'
for i in $(ls | grep .jar | sed -e 's/\.jar$//'); do
  deploy $i
done

echo ""
echo "Деплой завершен!"