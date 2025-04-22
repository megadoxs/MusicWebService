INSERT INTO playlists (playlist_id, `user`, name, duration)
VALUES ('200e8400-e29b-41d4-a716-446655440020', '57659e81-7c6b-4711-bb9e-bc73e1fa7824', 'Workout Hits', '00:08:01'),
       ('200e8400-e29b-41d4-a716-446655440021', '57659e81-7c6b-4711-bb9e-bc73e1fa7824', 'Chill Vibes', '00:07:49'),
       ('200e8400-e29b-41d4-a716-446655440022', '57659e81-7c6b-4711-bb9e-bc73e1fa7824', 'Hip Hop Bangers', '00:08:04'),
       ('200e8400-e29b-41d4-a716-446655440023', '3d2068eb-2cd5-42ac-8fee-fd022f88b604', 'Pop Anthems', '00:08:11'),
       ('200e8400-e29b-41d4-a716-446655440024', '3d2068eb-2cd5-42ac-8fee-fd022f88b604', 'RnB Grooves', '00:08:02'),
       ('200e8400-e29b-41d4-a716-446655440025', '41750448-a1d5-443d-84df-978be36ef333', 'Classic Rock', '00:04:41'),
       ('200e8400-e29b-41d4-a716-446655440026', '811a46fe-e4de-4790-ab84-1da7f4f2909f', 'Party Mix', '00:09:56'),
       ('200e8400-e29b-41d4-a716-446655440027', '811a46fe-e4de-4790-ab84-1da7f4f2909f', 'Throwback Jams', '00:08:47'),
       ('200e8400-e29b-41d4-a716-446655440028', '811a46fe-e4de-4790-ab84-1da7f4f2909f', 'Focus Mode', '00:08:03'),
       ('200e8400-e29b-41d4-a716-446655440029', '811a46fe-e4de-4790-ab84-1da7f4f2909f', 'Morning Motivation','00:08:03');

INSERT INTO playlist_songs (playlist_id, song_id)
VALUES (1, '100e8400-e29b-41d4-a716-446655440010'),  -- Workout Hits - Shallow
       (1, '100e8400-e29b-41d4-a716-446655440011'),  -- Workout Hits - Lose Yourself
       (2, '100e8400-e29b-41d4-a716-446655440013'),  -- Chill Vibes - Umbrella
       (2, '100e8400-e29b-41d4-a716-446655440017'),  -- Chill Vibes - No One
       (3, '100e8400-e29b-41d4-a716-446655440012'),  -- Hip Hop Bangers - God's Plan
       (3, '100e8400-e29b-41d4-a716-446655440014'),  -- Hip Hop Bangers - Empire State of Mind
       (4, '100e8400-e29b-41d4-a716-446655440015'),  -- Pop Anthems - Roar
       (4, '100e8400-e29b-41d4-a716-446655440010'),  -- Pop Anthems - Shallow
       (5, '100e8400-e29b-41d4-a716-446655440018'),  -- RnB Grooves - Blame It
       (5, '100e8400-e29b-41d4-a716-446655440017'),  -- RnB Grooves - No One
       (6, '100e8400-e29b-41d4-a716-446655440019'),  -- Classic Rock - Rocket Man
       (7, '100e8400-e29b-41d4-a716-446655440016'),  -- Party Mix - Drop It Like It's Hot
       (7, '100e8400-e29b-41d4-a716-446655440011'),  -- Party Mix - Lose Yourself
       (8, '100e8400-e29b-41d4-a716-446655440014'),  -- Throwback Jams - Empire State of Mind
       (8, '100e8400-e29b-41d4-a716-446655440016'),  -- Throwback Jams - Drop It Like It's Hot
       (9, '100e8400-e29b-41d4-a716-446655440017'),  -- Focus Mode - No One
       (9, '100e8400-e29b-41d4-a716-446655440019'),  -- Focus Mode - Rocket Man
       (10, '100e8400-e29b-41d4-a716-446655440015'), -- Morning Motivation - Roar
       (10, '100e8400-e29b-41d4-a716-446655440010'); -- Morning Motivation - Shallow