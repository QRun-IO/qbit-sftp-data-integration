# qbit-sftp-data-integration

## Knowledge base

Reviewed dossiers for this repo and the wider QQQ ecosystem live in the second-brain vault:

- Hub / start here: `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/qqq-hub.md`
- This repo's dossier: `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/repos/qbit-sftp-data-integration.md`
  (reviewed at develop commit `fd326b256486`, 2026-07-04)
- QBit mechanics refresher: `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/architecture/metadata-model.md`
- Parent pom (`qbit-build-parent`, from QRun-IO/qbit-bom): `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/repos/qbit-bom.md`

Key cautions recorded in the dossier: develop and main have diverged (Apache-2.0 LICENSE,
Java 21, qqq 0.35.0, v0.3.0 are main-only; develop is AGPL / Java 17 / qqq 0.27.9); two
confirmed qqq-4.0 compile breaks (BREAK-04-18 in both config customizers, BREAK-04-11 in
BaseTest); pom `<licenses>` + all file headers + checkstyle header template still AGPL.
