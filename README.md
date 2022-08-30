# java-filmorate
**Filmorate Database ER-diagram**
![FilmorateDB_ERD](https://github.com/kazakov93msk/java-filmorate/blob/main/filmorate_erd.png)

## Примеры запросов к БД

1. Получить полную информацию о пользователе
```SQL
SELECT *
FROM users
WHERE id = { ID пользователя };
```

2. Вывести имя пользователя и имена всех его друзей
```SQL
SELECT 
  u.name AS user_name,
  f.name AS friend_name
FROM users u
  JOIN user_relationships ur ON u.id = ur.user_id
  JOIN users f ON f.id = ur.friend_id
WHERE u.id = { ID пользователя };
```

3. Вывысти полную информацию о фильме, его жанре и описании рейтинга MPA
```SQL
SELECT
  f.name AS film_name,
  f.description,
  f.release_date,
  f.duration ||' minutes' AS duration_in_minutes
  rating_mpa,
  r.description AS rating_description,
  g.description AS genre_name
FROM films f
  LEFT JOIN ratings_mpa r USING(rating_mpa)
  LEFT JOIN genres g USING(genre_id)
WHERE f.id = { ID фильма };
```

4. Вывести ТОП-5 фильмов по количеству лайков
```SQL
SELECT
  f.name AS film_name,
  COUNT(user_id) AS likes_cnt
FROM films f
  JOIN likes l ON f.id = l.film_id
GROUP BY film_name
ORDER BY likes_cnt DESC
LIMIT 5;
```

5. Вывести имена всех пользователей, поставивших лайк фильму, <br>содержащему в названии { часть названия } и в жанре { название жанра }
```SQL
SELECT
  u.name AS user_name
FROM films f
  JOIN genres g USING(genre_id)
  JOIN likes l ON l.film_id = f.id
  JOIN users u ON u.id = l.user_id
WHERE UPPER(f.name) LIKE '%{ часть названия }%'
  AND g.description = { название жанра };
```

---

## Комментарий P2P

*Привет!*

*Связь между конкретным фильмом и жанром описывается как "многое-к-многим", один и тот же фильм может относиться сразу к нескольким жанрам, и наоборот. Реализовать такую связь можно через добавление специальной таблицы-посредника.*

*Наше приложение не стоит на месте, и теперь добавление в друзья - не автоматическая процедура, пользователь еще должен подтвердить заявку. Есть несколько способов реализовать эту функциональность, например - добавить поле с булевым значением в таблицу user_relationships.*


---
 
