-- Inserting sample data for ArticleKind = HOW_TO_USE (with targets)
INSERT INTO article (title, kind, article_content, target, display_image)
VALUES
('Drone Flying 101', 'HOW_TO_USE', 'Introduction to drone flying...', 'ALL', NULL),
('Basic Drone Maintenance', 'HOW_TO_USE', 'Essential care for your drone...', 'USER', NULL),
('Advanced Aerial Photography', 'HOW_TO_USE', 'Pro tips for stunning aerial shots...', 'EXPERT', NULL),
('Drone Safety Guidelines', 'HOW_TO_USE', 'Important safety rules for all drone pilots...', 'ALL', NULL),
('Choosing Your First Drone', 'HOW_TO_USE', 'Guide for new drone enthusiasts...', 'USER', NULL),
('Choosing Your First Drone1', 'HOW_TO_USE', 'Guide for new drone enthusiasts...', 'USER', NULL),
('Choosing Your First Drone2', 'HOW_TO_USE', 'Guide for new drone enthusiasts...', 'USER', NULL),
('Choosing Your First Drone3', 'HOW_TO_USE', 'Guide for new drone enthusiasts...', 'USER', NULL),
('Choosing Your First Drone4', 'HOW_TO_USE', 'Guide for new drone enthusiasts...', 'USER', NULL),
('Mastering FPV Flying', 'HOW_TO_USE', 'Advanced techniques for FPV drone pilots...', 'EXPERT', NULL),
('Understanding Drone Regulations', 'HOW_TO_USE', 'Overview of drone laws and regulations...', 'ALL', NULL),
('Drone Racing for Beginners', 'HOW_TO_USE', 'Getting started with drone racing...', 'USER', NULL),
('Professional Drone Surveying', 'HOW_TO_USE', 'Using drones for professional land surveys...', 'EXPERT', NULL);

-- Inserting sample data for ArticleKind = DRONE_CONTENT (without target)
INSERT INTO article (title, kind, article_content, display_image)
VALUES
('Top 10 Drone Photographs of 2023', 'DRONE_CONTENT', 'Showcasing the best aerial photography...', NULL),
('Drone Racing World Championship Highlights', 'DRONE_CONTENT', 'Exciting moments from the recent championship...', NULL),
('Drones in Agriculture: A Revolution', 'DRONE_CONTENT', 'How drones are changing farming practices...', NULL),
('Urban Exploration: Cities from Above', 'DRONE_CONTENT', 'Stunning aerial views of major cities...', NULL),
('Wildlife Conservation with Drones', 'DRONE_CONTENT', 'Using drones to protect endangered species...', NULL),
('Drones in Search and Rescue Operations', 'DRONE_CONTENT', 'Real-life stories of drones saving lives...', NULL),
('The Art of Drone Light Shows', 'DRONE_CONTENT', 'Behind the scenes of spectacular drone performances...', NULL);