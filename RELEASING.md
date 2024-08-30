# Releasing

1. Update the `CHANGELOG.md`:
  1. Change the `Unreleased` header to the release version.
  2. Add a link URL to ensure the header link works.
  3. Add a new `Unreleased` section to the top.

2. Update the `docs/setup.md`:
   1. Change the snapshot section to reflect the next "SNAPSHOT" version, in case it's changing.

3. Run release script:

   ```
   $ scripts/release.sh
   ```
If it successfully finishes running it will trigger a `GitHub Action` workflow which will create a
GitHub release and push to Maven Central.
