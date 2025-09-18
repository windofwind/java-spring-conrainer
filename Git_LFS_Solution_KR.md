# Git LFS 문제 해결 방법

## 문제

저장소가 Git LFS로 구성되어 있지만 시스템 경로에서 'git-lfs' 명령을 찾을 수 없습니다. 이로 인해 푸시를 시도할 때 오류가 발생하며, Git LFS를 설치하거나 더 이상 필요하지 않으면 pre-push 훅을 제거하라는 메시지가 표시됩니다.

오류 메시지:

```
This repository is configured for Git LFS but 'git-lfs' was not found on your path. If you no longer wish to use Git LFS, remove this hook by deleting the 'pre-push' file in the hooks directory (set by 'core.hookspath'; usually '.git/hooks').
```

## 해결 방법

이 문제를 해결하려면 Git LFS를 설치하고 Git 훅을 업데이트하세요. 이렇게 하면 Git LFS가 제대로 구성되고 pre-push 훅이 올바르게 작동합니다.

### 수정 단계

1. **Git LFS 설치**:

   - 패키지 목록을 업데이트하고 apt를 사용하여 Git LFS를 설치하세요 (Ubuntu와 같은 Debian 기반 시스템의 경우):
     ```
     sudo apt update && sudo apt install git-lfs
     ```
   - 이렇게 하면 Git LFS 바이너리가 설치되고 `git lfs` 명령을 사용할 수 있습니다.

2. **Git 훅 업데이트**:
   - Git LFS용 Git 훅을 업데이트하려면 다음 명령을 실행하세요:
     ```
     git lfs update --force
     ```
   - 이렇게 하면 기존 pre-push 훅을 올바른 Git LFS 훅으로 덮어쓰고 오류를 해결합니다.

### 확인

- 단계를 완료한 후 Git LFS가 작동하는지 확인하려면 다음을 실행하세요:
  ```
  git lfs version
  ```
- 오류 없이 설치된 Git LFS 버전이 표시되어야 합니다.

### 대안 해결 방법 (Git LFS를 사용하지 않는 경우)

이 저장소에서 더 이상 Git LFS를 사용하지 않으려면:

1. 훅 디렉터리를 찾으세요 (일반적으로 `.git/hooks`).
2. `pre-push` 파일을 삭제하세요:
   ```
   rm .git/hooks/pre-push
   ```
3. 필요하지 않으면 Git LFS를 제거하세요:
   ```
   sudo apt remove git-lfs
   ```

## 참고 사항

- Git LFS는 Git 저장소에서 대용량 파일을 효율적으로 관리하는 데 사용됩니다.
- 패키지를 설치하려면 sudo 권한이 있는지 확인하세요.
- 다른 패키지 관리자(예: CentOS의 yum)를 사용하는 경우 설치 명령을 적절히 조정하세요.
