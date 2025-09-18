# Git LFS Issue Solution

## Problem

The repository is configured for Git LFS, but the 'git-lfs' command was not found on the system path. This causes an error when attempting to push, with the message suggesting to either install Git LFS or remove the pre-push hook if Git LFS is no longer needed.

Error message:

```
This repository is configured for Git LFS but 'git-lfs' was not found on your path. If you no longer wish to use Git LFS, remove this hook by deleting the 'pre-push' file in the hooks directory (set by 'core.hookspath'; usually '.git/hooks').
```

## Solution

To resolve this issue, install Git LFS and update the Git hooks. This ensures Git LFS is properly configured and the pre-push hook functions correctly.

### Steps to Fix

1. **Install Git LFS**:

   - Update the package list and install Git LFS using apt (for Debian-based systems like Ubuntu):
     ```
     sudo apt update && sudo apt install git-lfs
     ```
   - This installs the Git LFS binary and makes the `git lfs` command available.

2. **Update Git Hooks**:
   - Run the following command to update the Git hooks for Git LFS:
     ```
     git lfs update --force
     ```
   - This overwrites the existing pre-push hook with the proper Git LFS hook, resolving the error.

### Verification

- After completing the steps, verify Git LFS is working by running:
  ```
  git lfs version
  ```
- This should display the installed version of Git LFS without errors.

### Alternative Solution (If Not Using Git LFS)

If you no longer wish to use Git LFS in this repository:

1. Locate the hooks directory (usually `.git/hooks`).
2. Delete the `pre-push` file:
   ```
   rm .git/hooks/pre-push
   ```
3. Optionally, uninstall Git LFS if it's no longer needed:
   ```
   sudo apt remove git-lfs
   ```

## Notes

- Git LFS is used for managing large files in Git repositories efficiently.
- Ensure you have sudo privileges to install packages.
- If using a different package manager (e.g., yum on CentOS), adjust the installation command accordingly.
