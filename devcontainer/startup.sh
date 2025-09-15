sed -i 's/codespaces/agnoster/' ~/.zshrc && \
sed -i'' -r -e "/prompt_hg/a\  prompt_newline" ~/.oh-my-zsh/themes/agnoster.zsh-theme && \
echo $(pwd) && \
./gradlew clean && \ 
./gradlew build --refresh-dependencies && \
echo DONE
