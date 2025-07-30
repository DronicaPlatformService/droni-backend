INSERT INTO users (user_id, phone_number, email, name, profile_image, nickname, zone_code, address, address_detail,
                   refresh_token, app_push_notification_enabled, kakao_talk_notification_enabled, chat_notification_enabled, estimate_bid_notification_enabled, created_at, updated_at, deleted_at, provider,
                   oauth_id)
VALUES (4, 'EMPTY_NUMBER', 'Taeyuntest@kakao.com', NULL,
        'http://k.kakaocdn.net/dn/oKUIq/btsGw7dsH0J/8u10RQbQHWk1mM46du7Cfk/test.jpg', 'Taeyun', 'Asia/Seoul',
        NULL, NULL,
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzYWRmYTdkNS02YWE0LTRkZjEtYTYwYi1iYTFmY2Y4OTE2NzkiLCJleHAiOjE3MTcyMjkyNzN9.rLdzDQujvVIudU7VbCdNCWUlkrUcaM8kpuiSRDqimfQ',
        TRUE, TRUE, TRUE, TRUE, '2024-05-25 17:08:28.201082', '2024-05-25 17:08:28.201082', NULL, 'KAKAO', '3497839913'),
       (3, 'EMPTY_NUMBER', 'TaeyunTest@naver.com', '김태윤', NULL, NULL, 'Asia/Seoul', NULL, NULL,
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNmQ5ZjU2Zi0wZjIyLTRiY2QtOWZmZS0zN2VhZTQyOWU4NmIiLCJleHAiOjE3MTg3MDk4Mzl9.GeTk0Dm1PicgHS5SXsGuGtnFOIOxMJGEZD5gTmBArqA',
        TRUE, TRUE, TRUE, TRUE, '2024-05-11 19:07:16.276813', '2024-05-11 19:07:16.276813', NULL, 'NAVER',
        'Ai6kJjz8PehhWY3sVTYbIZomTTxkcghQSAtYThbfujY');
