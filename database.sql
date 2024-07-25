-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS siasisten;

-- Switch to the new schema
SET search_path TO siasisten;

-- Create tables only if they do not exist
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'siasisten' AND table_name = 'mahasiswa') THEN
        CREATE TABLE mahasiswa (
            npm VARCHAR(10) NOT NULL PRIMARY KEY,
            nama TEXT NOT NULL,
            email VARCHAR(50) UNIQUE NOT NULL,
            status VARCHAR(30) NOT NULL,
            no_hp VARCHAR(15) NOT NULL,
            nama_bank VARCHAR(100) NOT NULL,
            no_rek VARCHAR(50) NOT NULL,
            nama_rekening TEXT NOT NULL,
            username VARCHAR(30) UNIQUE NOT NULL,
            password TEXT NOT NULL
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'siasisten' AND table_name = 'dosen') THEN
        CREATE TABLE dosen (
            nid VARCHAR(10) NOT NULL PRIMARY KEY,
            nama TEXT NOT NULL,
            email VARCHAR(50) UNIQUE NOT NULL,
            no_hp VARCHAR(15) NOT NULL,
            username VARCHAR(30) UNIQUE NOT NULL,
            password TEXT NOT NULL
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'siasisten' AND table_name = 'matakuliah') THEN
        CREATE TABLE matakuliah (
            kode_mk VARCHAR(20) NOT NULL PRIMARY KEY,
            nama VARCHAR(100) NOT NULL
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'siasisten' AND table_name = 'periode') THEN
        CREATE TABLE periode (
            tahun_ajaran VARCHAR(50) PRIMARY KEY
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'siasisten' AND table_name = 'lowonganmatakuliah') THEN
        CREATE TABLE lowonganmatakuliah (
            kode_lmk SERIAL NOT NULL PRIMARY KEY,
            tahun_ajaran VARCHAR(50) NOT NULL,
            kode_mk VARCHAR(20) NOT NULL,
            status_lowongan VARCHAR(20) NOT NULL,
            jumlah_lowongan INTEGER NOT NULL DEFAULT 0,
            jumlah_pelamar INTEGER NOT NULL DEFAULT 0,
            jumlah_diterima INTEGER NOT NULL DEFAULT 0,
            FOREIGN KEY(tahun_ajaran) REFERENCES periode(tahun_ajaran),
            FOREIGN KEY(kode_mk) REFERENCES matakuliah(kode_mk)
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'siasisten' AND table_name = 'prasyarat') THEN
        CREATE TABLE prasyarat (
            id_prasyarat SERIAL PRIMARY KEY,
            kode_lmk INTEGER NOT NULL,
            nama_prasyarat VARCHAR(100) NOT NULL,
            nilai VARCHAR(2) NOT NULL,
            FOREIGN KEY (kode_lmk) REFERENCES lowonganmatakuliah(kode_lmk)
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'siasisten' AND table_name = 'dosen_lmk') THEN
        CREATE TABLE dosen_lmk (
            id_dosen_lmk SERIAL PRIMARY KEY,
            kode_lmk INTEGER NOT NULL,
            nid VARCHAR(10) NOT NULL,
            FOREIGN KEY (kode_lmk) REFERENCES lowonganmatakuliah(kode_lmk),
            FOREIGN KEY (nid) REFERENCES dosen(nid)
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'siasisten' AND table_name = 'log') THEN
        CREATE TABLE log(
            no_log SERIAL NOT NULL PRIMARY KEY,
            kode_lmk INTEGER NOT NULL,
            npm VARCHAR(10) NOT NULL,
            timestamp_log TIMESTAMP NOT NULL,
            durasi INTERVAL NOT NULL,
            kategori VARCHAR(100) NOT NULL,
            deskripsi TEXT,
            status VARCHAR(100) NOT NULL DEFAULT 'dilaporkan',
            waktu_dibuat TIMESTAMP NOT NULL,
            waktu_diubah TIMESTAMP NOT NULL,
            FOREIGN KEY (kode_lmk) REFERENCES lowonganmatakuliah(kode_lmk),
            FOREIGN KEY (npm) REFERENCES mahasiswa(npm)
        );
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'siasisten' AND table_name = 'lmk_mahasiswa') THEN
        CREATE TABLE lmk_mahasiswa (
            id_lmk_mahasiswa SERIAL PRIMARY KEY,
            kode_lmk INTEGER NOT NULL,
            npm VARCHAR(10) NOT NULL,
            status VARCHAR(100) NOT NULL DEFAULT 'melamar',
            ipk DECIMAL(3,2) NOT NULL,
            sks INTEGER NOT NULL,
            FOREIGN KEY (kode_lmk) REFERENCES lowonganmatakuliah(kode_lmk),
            FOREIGN KEY (npm) REFERENCES mahasiswa (npm)
        );
    END IF;
END $$;

-- Insert dummy data only if tables are empty
DO $$
BEGIN
    -- Check if mahasiswa table is empty
    IF NOT EXISTS (SELECT 1 FROM siasisten.mahasiswa LIMIT 1) THEN
        INSERT INTO siasisten.mahasiswa (npm, nama, email, status, no_hp, nama_bank, no_rek, nama_rekening, username, password) VALUES
        ('210001', 'Ahmad Budi', 'ahmad.budi@example.com', 'Aktif', '081234567890', 'Bank Mandiri', '1234567890', 'Ahmad Budi', 'ahmad123', 'password123'),
        ('210002', 'Siti Aminah', 'siti.aminah@example.com', 'Aktif', '081234567891', 'BNI', '2345678901', 'Siti Aminah', 'siti456', 'password456'),
        ('210003', 'Rudi Setiawan', 'rudi.setiawan@example.com', 'Non-Aktif', '081234567892', 'BCA', '3456789012', 'Rudi Setiawan', 'rudi789', 'password789');
    END IF;

    -- Check if dosen table is empty
    IF NOT EXISTS (SELECT 1 FROM siasisten.dosen LIMIT 1) THEN
        INSERT INTO siasisten.dosen (nid, nama, email, no_hp, username, password) VALUES
        ('D001', 'Dr. Arif Hidayat', 'arif.hidayat@example.com', '082345678901', 'arif', 'arif123'),
        ('D002', 'Prof. Lina Susanti', 'lina.susanti@example.com', '082345678902', 'lina', 'lina456');
    END IF;

    -- Check if matakuliah table is empty
    IF NOT EXISTS (SELECT 1 FROM siasisten.matakuliah LIMIT 1) THEN
        INSERT INTO siasisten.matakuliah (kode_mk, nama) VALUES
        ('MK001', 'Algoritma dan Struktur Data'),
        ('MK002', 'Basis Data'),
        ('MK003', 'Jaringan Komputer');
    END IF;

    -- Check if periode table is empty
    IF NOT EXISTS (SELECT 1 FROM siasisten.periode LIMIT 1) THEN
        INSERT INTO siasisten.periode (tahun_ajaran) VALUES
        ('2024/2025'),
        ('2025/2026');
    END IF;

    -- Check if lowonganmatakuliah table is empty
    IF NOT EXISTS (SELECT 1 FROM siasisten.lowonganmatakuliah LIMIT 1) THEN
        INSERT INTO siasisten.lowonganmatakuliah (tahun_ajaran, kode_mk, status_lowongan, jumlah_lowongan, jumlah_pelamar, jumlah_diterima) VALUES
        ('2024/2025', 'MK001', 'Aktif', 2, 10, 0),
        ('2024/2025', 'MK002', 'Aktif', 3, 8, 0);
    END IF;

    -- Check if prasyarat table is empty
    IF NOT EXISTS (SELECT 1 FROM siasisten.prasyarat LIMIT 1) THEN
        INSERT INTO siasisten.prasyarat (kode_lmk, nama_prasyarat, nilai) VALUES
        (1, 'Nilai C pada MK001', 'C'),
        (2, 'Nilai B pada MK002', 'B');
    END IF;

    -- Check if dosen_lmk table is empty
    IF NOT EXISTS (SELECT 1 FROM siasisten.dosen_lmk LIMIT 1) THEN
        INSERT INTO siasisten.dosen_lmk (kode_lmk, nid) VALUES
        (1, 'D001'),
        (2, 'D002');
    END IF;

    -- Check if log table is empty
    IF NOT EXISTS (SELECT 1 FROM siasisten.log LIMIT 1) THEN
        INSERT INTO siasisten.log (kode_lmk, npm, timestamp_log, durasi, kategori, deskripsi, status, waktu_dibuat, waktu_diubah) VALUES
        (1, '210001', '2024-07-25 10:00:00', '1 hour', 'Pengajuan', 'Pengajuan untuk lowongan MK001', 'dilaporkan', '2024-07-25 10:00:00', '2024-07-25 10:00:00'),
        (2, '210002', '2024-07-25 11:00:00', '30 minutes', 'Pengajuan', 'Pengajuan untuk lowongan MK002', 'dilaporkan', '2024-07-25 11:00:00', '2024-07-25 11:00:00');
    END IF;

    -- Check if lmk_mahasiswa table is empty
    IF NOT EXISTS (SELECT 1 FROM siasisten.lmk_mahasiswa LIMIT 1) THEN
        INSERT INTO siasisten.lmk_mahasiswa (kode_lmk, npm, status, ipk, sks) VALUES
        (1, '210001', 'Diterima', 3.50, 24),
        (2, '210002', 'Diterima', 3.80, 22);
    END IF;
END $$;
