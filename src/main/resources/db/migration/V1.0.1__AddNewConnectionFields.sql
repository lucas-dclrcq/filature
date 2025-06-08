ALTER TABLE connections
    ADD COLUMN status text,
    ADD COLUMN lastDocumentDownloadedDate timestamp(6);