# qbit-sftp-data-integration

## Knowledge base

Reviewed dossiers for this repo and the wider QQQ ecosystem live in the second-brain vault:

- Hub / start here: `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/qqq-hub.md`
- This repo's dossier: `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/repos/qbit-sftp-data-integration.md`
  (reviewed at develop commit `fd326b256486`, 2026-07-04)
- QBit mechanics refresher: `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/architecture/metadata-model.md`
- Parent pom (`qbit-build-parent`, from QRun-IO/qbit-bom): `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/repos/qbit-bom.md`

Since the dossier review, main was back-merged into develop and the build moved to
`qbit-build-parent` 2.0.0 (qqq 4.0.0, Java 21), with the BREAK-04-18 and BREAK-04-11 compile
breaks migrated (QRun-IO/qqq#770). The qqq version comes only from the parent; check the next
line with `mvn -B verify -Pqqq-snapshot` (qqq `4.1.0-SNAPSHOT`, override with
`-Dqqq.snapshot.version`). Current first-party license declarations use Apache-2.0
consistently across LICENSE/NOTICE, the pom, source headers, Checkstyle template and README.
