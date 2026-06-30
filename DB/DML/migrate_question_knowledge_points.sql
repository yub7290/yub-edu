INSERT INTO edu_question_knowledge_point (question_id, knowledge_point_id, sort, create_time)
SELECT
  q.id AS question_id,
  CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(q.knowledge_points, ',', n.n), ',', -1) AS UNSIGNED) AS knowledge_point_id,
  n.n AS sort,
  NOW()
FROM edu_question q
JOIN (
  SELECT a.N + b.N * 10 + 1 n
  FROM
    (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a,
    (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b
  ORDER BY n
) n
  ON n.n <= 1 + LENGTH(q.knowledge_points) - LENGTH(REPLACE(q.knowledge_points, ',', ''))
WHERE q.deleted = 0
  AND q.knowledge_points IS NOT NULL
  AND q.knowledge_points != ''
  AND EXISTS (SELECT 1 FROM edu_knowledge_point kp WHERE kp.id = CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(q.knowledge_points, ',', n.n), ',', -1) AS UNSIGNED) AND kp.deleted = 0)
ORDER BY q.id, n.n;
