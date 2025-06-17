-- Insert sample books
INSERT INTO books (title, author, genre, isbn, year_published, available_copies, total_copies) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 'Fiction', '978-0-7432-7356-5', 1925, 3, 5),
('To Kill a Mockingbird', 'Harper Lee', 'Fiction', '978-0-06-112008-4', 1960, 2, 3),
('1984', 'George Orwell', 'Dystopian Fiction', '978-0-452-28423-4', 1949, 4, 4),
('Pride and Prejudice', 'Jane Austen', 'Romance', '978-0-14-143951-8', 1813, 2, 2),
('The Catcher in the Rye', 'J.D. Salinger', 'Fiction', '978-0-316-76948-0', 1951, 1, 3),
('Lord of the Flies', 'William Golding', 'Fiction', '978-0-571-05686-2', 1954, 3, 3),
('The Hobbit', 'J.R.R. Tolkien', 'Fantasy', '978-0-547-92822-7', 1937, 5, 6),
('Harry Potter and the Philosopher''s Stone', 'J.K. Rowling', 'Fantasy', '978-0-7475-3269-9', 1997, 4, 5),
('The Da Vinci Code', 'Dan Brown', 'Mystery', '978-0-307-47572-5', 2003, 2, 4),
('The Alchemist', 'Paulo Coelho', 'Fiction', '978-0-06-231500-7', 1988, 3, 3);

-- Insert sample members
INSERT INTO members (name, email, phone, address, membership_status, registration_date) VALUES
('John Smith', 'john.smith@email.com', '+1-555-0101', '123 Main St, Anytown, USA', 'ACTIVE', '2024-01-15'),
('Emily Johnson', 'emily.johnson@email.com', '+1-555-0102', '456 Oak Ave, Somewhere, USA', 'ACTIVE', '2024-02-20'),
('Michael Brown', 'michael.brown@email.com', '+1-555-0103', '789 Pine Rd, Elsewhere, USA', 'ACTIVE', '2024-03-10'),
('Sarah Davis', 'sarah.davis@email.com', '+1-555-0104', '321 Elm St, Nowhere, USA', 'SUSPENDED', '2024-01-05'),
('David Wilson', 'david.wilson@email.com', '+1-555-0105', '654 Maple Dr, Anywhere, USA', 'ACTIVE', '2024-04-12'),
('Lisa Anderson', 'lisa.anderson@email.com', '+1-555-0106', '987 Cedar Ln, Someplace, USA', 'ACTIVE', '2024-02-28'),
('Robert Taylor', 'robert.taylor@email.com', '+1-555-0107', '147 Birch St, Anyplace, USA', 'EXPIRED', '2023-12-01'),
('Jennifer Martinez', 'jennifer.martinez@email.com', '+1-555-0108', '258 Spruce Ave, Everytown, USA', 'ACTIVE', '2024-03-25');

-- Insert sample borrowing transactions
INSERT INTO borrowing_transactions (book_id, member_id, borrow_date, due_date, return_date, status) VALUES
(1, 1, '2024-11-01', '2024-11-15', '2024-11-14', 'RETURNED'),
(2, 2, '2024-11-05', '2024-11-19', NULL, 'BORROWED'),
(3, 3, '2024-10-20', '2024-11-03', '2024-11-02', 'RETURNED'),
(4, 1, '2024-11-10', '2024-11-24', NULL, 'BORROWED'),
(5, 4, '2024-10-15', '2024-10-29', NULL, 'OVERDUE'),
(6, 5, '2024-11-08', '2024-11-22', NULL, 'BORROWED'),
(7, 6, '2024-11-12', '2024-11-26', NULL, 'BORROWED'),
(8, 2, '2024-10-25', '2024-11-08', '2024-11-07', 'RETURNED');

-- Insert sample notifications
INSERT INTO notifications (member_id, message, date_sent, type, is_read) VALUES
(1, 'You have successfully borrowed ''The Great Gatsby''. Due date: 2024-11-15', '2024-11-01 10:30:00', 'GENERAL', true),
(2, 'You have successfully borrowed ''To Kill a Mockingbird''. Due date: 2024-11-19', '2024-11-05 14:15:00', 'GENERAL', false),
(4, 'Your book ''The Catcher in the Rye'' is 5 days overdue. Please return it immediately to avoid additional fines.', '2024-11-03 09:00:00', 'OVERDUE_NOTICE', false),
(1, 'You have successfully returned ''The Great Gatsby''. Thank you!', '2024-11-14 16:45:00', 'GENERAL', true),
(5, 'You have successfully borrowed ''Lord of the Flies''. Due date: 2024-11-22', '2024-11-08 11:20:00', 'GENERAL', false);

-- Insert sample fines
INSERT INTO fines (member_id, transaction_id, amount, status, transaction_date, reason) VALUES
(4, 5, 5.00, 'PENDING', '2024-11-03 09:00:00', 'Overdue book: The Catcher in the Rye (5 days overdue)');
