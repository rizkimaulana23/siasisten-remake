-- Schema and Relation

CREATE SCHEMA siasisten;

SET search_path TO siasisten;

CREATE TABLE mahasiswa (
                           npm VARCHAR(10) NOT NULL PRIMARY KEY,
                           nama TEXT NOT NULL,
                           email VARCHAR(50) UNIQUE NOT NULL,
                           status VARCHAR (30) NOT NULL,
                           no_hp VARCHAR(15) NOT NULL,
                           nama_bank VARCHAR(100) NOT NULL,
                           no_rek VARCHAR(50) NOT NULL,
                           nama_rekening TEXT NOT NULL,
                           username VARCHAR(30) UNIQUE NOT NULL,
                           password TEXT NOT NULL
);

CREATE TABLE dosen (
                       nid VARCHAR(10) NOT NULL PRIMARY KEY,
                       nama TEXT NOT NULL,
                       email VARCHAR(50) UNIQUE NOT NULL,
                       no_hp VARCHAR(15) NOT NULL,
                       username VARCHAR(30) UNIQUE NOT NULL,
                       password TEXT NOT NULL
);

CREATE TABLE matakuliah (
                            kode_mk VARCHAR(20) NOT NULL PRIMARY KEY,
                            nama VARCHAR(100) NOT NULL
);

CREATE TABLE periode (
                         tahun_ajaran VARCHAR(50) PRIMARY KEY
);

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

CREATE TABLE prasyarat (
                           id_prasyarat SERIAL PRIMARY KEY,
                           kode_lmk INTEGER NOT NULL,
                           nama_prasyarat VARCHAR(100) NOT NULL,
                           nilai VARCHAR(2) NOT NULL,
                           FOREIGN KEY (kode_lmk) REFERENCES lowonganmatakuliah(kode_lmk)
);

CREATE TABLE dosen_lmk (
                           id_dosen_lmk SERIAL PRIMARY KEY,
                           kode_lmk INTEGER NOT NULL,
                           nid VARCHAR(10) NOT NULL,
                           FOREIGN KEY (kode_lmk) REFERENCES lowonganmatakuliah(kode_lmk),
                           FOREIGN KEY (nid) REFERENCES dosen(nid)
);

CREATE TABLE log(
                    no_log SERIAL NOT NULL PRIMARY KEY,
                    kode_lmk INTEGER NOT NULL,
                    npm VARCHAR(10) NOT NULL,
                    timestamp_log TIMESTAMP NOT NULL,
                    durasi NUMERIC(21,0) NOT NULL,
                    kategori VARCHAR(100) NOT NULL,
                    deskripsi TEXT,
                    status VARCHAR(100) NOT NULL DEFAULT 'dilaporkan',
                    waktu_dibuat TIMESTAMP NOT NULL,
                    waktu_diubah TIMESTAMP NOT NULL,
                    FOREIGN KEY (kode_lmk) REFERENCES lowonganmatakuliah(kode_lmk),
                    FOREIGN KEY (npm) REFERENCES mahasiswa(npm)
);


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


-- Function and Trigger

CREATE OR REPLACE FUNCTION update_jumlah_pelamar_diterima()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE lowonganmatakuliah
    SET jumlah_diterima = (
        SELECT COUNT(*)
        FROM lmk_mahasiswa
        WHERE status = 'diterima' AND kode_lmk = OLD.kode_lmk
    )
    WHERE kode_lmk = OLD.kode_lmk;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_jumlah_diterima
    AFTER UPDATE OR DELETE ON lmk_mahasiswa
    FOR EACH ROW
EXECUTE FUNCTION update_jumlah_pelamar_diterima();

CREATE OR REPLACE FUNCTION update_jumlah_pelamar()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE lowonganmatakuliah
    SET jumlah_diterima = (
        SELECT COUNT(*)
        FROM lmk_mahasiswa
        WHERE kode_lmk = OLD.kode_lmk
    )
    WHERE kode_lmk = OLD.kode_lmk;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_jumlah_pelamar
    AFTER UPDATE OR DELETE ON lmk_mahasiswa
    FOR EACH ROW
EXECUTE FUNCTION update_jumlah_pelamar();
